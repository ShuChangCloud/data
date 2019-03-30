> AOP部分       
     
- 基于schema-base实现的aop，无论是什么类型的通知，都必须实现接口     
- 基于aspectj的方式，对通知类的要求没有那么严格，也就是说，在一个切面类中可以写任何的方法，而且不必继承任何接口<br>但是aspect对配置文件的要求非常高，必须指定通知在哪个切面类中，还必须指定是类中的哪个方法
- 出了异常就不会执行后置通知。
- aop一般应用于service层中，service中的方法出现异常不应该处理，必须抛出，否则spring aop就无调用异常通知，因为异常已经被service中的方法try catch过了   
- aspectj中的after与after-returning区别： 
       
		//无论是否发生异常 这个方法都会被执行
		public void  after(){
		    System.out.println("执行后置通知");
		}
	
	    //发生异常时，该方法不会执行
	    public void afterReturning(){``
	        System.out.println("执行afterRunning");
	    }

**注意：使用注解来配置切面只适合个别方法，当大面积的方法都需要被代理时，切面类中的方法上的注解就无法复用了**