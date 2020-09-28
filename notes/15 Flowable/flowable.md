我们要构建的流程是一个非常简单的请假流程。 在Flowable术语中，我们将其称为一个**流程定义(process definition)**。一个*流程定义*可以启动多个**流程实例(process instance)**。

*流程定义*可以看做是重复执行流程的蓝图。 在这个例子中，*流程定义*定义了请假的各个步骤，而一个*流程实例*对应某个雇员提出的一个请假申请。



![](assets/getting.started.bpmn.process.png)

- 左侧的圆圈叫做**启动事件(start event)**。这是一个流程实例的起点。
- 第一个矩形是一个**用户任务(user task)**。这是流程中人类用户操作的步骤。在这个例子中，经理需要批准或驳回申请。
- 取决于经理的决定，**排他网关(exclusive gateway)** (带叉的菱形)会将流程实例路由至批准或驳回路径。
- 如果批准，则需要将申请注册至某个外部系统，并跟着另一个用户任务，将经理的决定通知给申请人。当然也可以改为发送邮件。
- 如果驳回，则为雇员发送一封邮件通知他。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:xsd="http://www.w3.org/2001/XMLSchema"
             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
             xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
             xmlns:flowable="http://flowable.org/bpmn"
             typeLanguage="http://www.w3.org/2001/XMLSchema"
             expressionLanguage="http://www.w3.org/1999/XPath"
             targetNamespace="http://www.flowable.org/processdef">

    <process id="holidayRequest" name="Holiday Request" isExecutable="true">

        <startEvent id="startEvent"/>
        <sequenceFlow sourceRef="startEvent" targetRef="approveTask"/>

        <userTask id="approveTask" name="Approve or reject request"/>
        <sequenceFlow sourceRef="approveTask" targetRef="decision"/>

        <exclusiveGateway id="decision"/>
        <sequenceFlow sourceRef="decision" targetRef="externalSystemCall">
            <conditionExpression xsi:type="tFormalExpression">
                <![CDATA[
          ${approved}
        ]]>
            </conditionExpression>
        </sequenceFlow>
        <sequenceFlow sourceRef="decision" targetRef="sendRejectionMail">
            <conditionExpression xsi:type="tFormalExpression">
                <![CDATA[
          ${!approved}
        ]]>
            </conditionExpression>
        </sequenceFlow>

        <serviceTask id="externalSystemCall" name="Enter holidays in external system"
                     flowable:class="org.flowable.CallExternalSystemDelegate"/>
        <sequenceFlow sourceRef="externalSystemCall" targetRef="holidayApprovedTask"/>

        <userTask id="holidayApprovedTask" name="Holiday approved"/>
        <sequenceFlow sourceRef="holidayApprovedTask" targetRef="approveEnd"/>

        <serviceTask id="sendRejectionMail" name="Send out rejection email"
                     flowable:class="org.flowable.SendRejectionMail"/>
        <sequenceFlow sourceRef="sendRejectionMail" targetRef="rejectEnd"/>

        <endEvent id="approveEnd"/>

        <endEvent id="rejectEnd"/>

    </process>

</definitions>
```

每一个步骤（在BPMN 2.0术语中称作***活动(activity)\***）都有一个*id*属性，为其提供一个在XML文件中唯一的标识符。所有的*活动*都可以设置一个名字，以提高流程图的可读性。

*活动*之间通过**顺序流(sequence flow)**连接，在流程图中是一个有向箭头。在执行流程实例时，执行(execution)会从*启动事件*沿着*顺序流*流向下一个*活动*。

离开*排他网关(带有X的菱形)*的*顺序流*很特别：都以*表达式(expression)*的形式定义了*条件(condition)* 当流程实例的执行到达这个*网关*时，会计算*条件*，并使用第一个计算为*true*的顺序流。这就是*排他*的含义：只选择一个。当然如果需要不同的路由策略，可以使用其他类型的网关。



这里用作条件的*表达式*为*${approved}*，这是*${approved == true}*的简写。变量’approved’被称作**流程变量(process variable)**。*流程变量*是持久化的数据，与流程实例存储在一起，并可以在流程实例的生命周期中使用。在这个例子里，我们需要在特定的地方（当经理用户任务提交时，或者以Flowable的术语来说，*完成(complete)*时）设置这个*流程变量*，因为这不是流程实例启动时就能获取的数据。

在流程实例启动后，会创建一个**执行(execution)**，并将其放在启动事件上。从这里开始，这个*执行*沿着顺序流移动到经理审批的用户任务，并执行用户任务行为。这个行为将在数据库中创建一个任务，该任务可以之后使用查询找到。用户任务是一个*等待状态(wait state)*，引擎会停止执行，返回API调用处。











**RepositoryService**很可能是使用Flowable引擎要用的第一个服务。这个服务提供了管理与控制`部署(deployments)`与`流程定义(process definitions)`的操作。在这里简单说明一下，流程定义是BPMN 2.0流程对应的Java对象，体现流程中每一步的结构与行为。此外，这个服务还可以：

- 查询引擎现有的部署与流程定义。
- 暂停或激活部署中的某些流程，或整个部署。暂停意味着不能再对它进行操作，激活刚好相反，重新使它可以操作。
- 获取各种资源，比如部署中保存的文件，或者引擎自动生成的流程图。
- 获取POJO版本的流程定义。它可以用Java而不是XML的方式查看流程。

与提供静态信息（也就是不会改变，至少不会经常改变的信息）的`RepositoryService`相反，**RuntimeService**用于启动流程定义的新流程实例。前面介绍过，`流程定义`中定义了流程中不同步骤的结构与行为。流程实例则是流程定义的实际执行过程。同一时刻，一个流程定义通常有多个运行中的实例。`RuntimeService`也用于读取与存储`流程变量`。流程变量是流程实例中的数据，可以在流程的许多地方使用（例如排他网关经常使用流程变量判断流程下一步要走的路径）。`RuntimeService`还可以用于查询流程实例与执行(Execution)。执行也就是BPMN 2.0中 `'token'` 的概念。通常执行是指向流程实例当前位置的指针。最后，还可以在流程实例等待外部触发时使用`RuntimeService`，使流程可以继续运行。流程有许多`等待状态(wait states)`，`RuntimeService`服务提供了许多操作用于“通知”流程实例：已经接收到外部触发，流程实例可以继续运行。



对于像Flowable这样的BPM引擎来说，核心是需要人类用户操作的任务。所有任务相关的东西都组织在**TaskService**中，例如：

- 查询分派给用户或组的任务
- 创建*独立运行(standalone)*任务。这是一种没有关联到流程实例的任务。
- 决定任务的执行用户(assignee)，或者将用户通过某种方式与任务关联。
- 认领(claim)与完成(complete)任务。认领是指某人决定成为任务的执行用户，也即他将会完成这个任务。完成任务是指“做这个任务要求的工作”，通常是填写某个表单。

**IdentityService**很简单。它用于管理（创建，更新，删除，查询……）组与用户。请注意，Flowable实际上在运行时并不做任何用户检查。例如任务可以分派给任何用户，而引擎并不会验证系统中是否存在该用户。这是因为Flowable有时要与LDAP、Active Directory等服务结合使用。

**FormService**是可选服务。也就是说Flowable没有它也能很好地运行，而不必牺牲任何功能。这个服务引入了*开始表单(start form)*与*任务表单(task form)*的概念。 *开始表单*是在流程实例启动前显示的表单，而*任务表单*是用户完成任务时显示的表单。Flowable可以在BPMN 2.0流程定义中定义这些表单。表单服务通过简单的方式暴露这些数据。再次重申，表单不一定要嵌入流程定义，因此这个服务是可选的。

**HistoryService**暴露Flowable引擎收集的所有历史数据。当执行流程时，引擎会保存许多数据（可配置），例如流程实例启动时间、谁在执行哪个任务、完成任务花费的事件、每个流程实例的执行路径，等等。这个服务主要提供查询这些数据的能力。

**ManagementService**通常在用Flowable编写用户应用时不需要使用。它可以读取数据库表与表原始数据的信息，也提供了对作业(job)的查询与管理操作。Flowable中很多地方都使用作业，例如定时器(timer)，异步操作(asynchronous continuation)，延时暂停/激活(delayed suspension/activation)等等。后续会详细介绍这些内容。

**DynamicBpmnService**可用于修改流程定义中的部分内容，而不需要重新部署它。例如可以修改流程定义中一个用户任务的办理人设置，或者修改一个服务任务中的类名。