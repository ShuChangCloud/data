# proxy_pass和rewrite的区别



**1.反向代理也是跳转的一种**

比如这个就是将当前匹配的地址转发到http://localhost:8080/demo1/demo2/indexPage.html

```conf
proxy_pass http://localhost:8080/demo1/demo2/indexPage.html;
```

**2.proxy_pass跳转一般用于匹配跨域的ip和端口，rewrite匹配同域**



3.示例，将首页http://localhost:8080/demo1/demo2/indexPage.html的网页配置用域名www.test.com就能访问到首页

```conf
	upstream tomcat { 
		server    localhost:8080;
	}	
    server {
        listen       80;
        #这里配置域名
        server_name  www.test.com;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

		#匹配根路径 =号是精确匹配
        location = / {
            proxy_set_header Host $http_host;
			proxy_set_header X-Real-IP $remote_addr;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			#配置首页
			proxy_pass http://localhost:8080/demo1/demo2/indexPage.html;
        }
		#匹配其它路径
		 location /{
            proxy_set_header Host $http_host;
			proxy_set_header X-Real-IP $remote_addr;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			#方向代理8080端口,注意Tomcat后面不要加斜杠/
			proxy_pass http://tomcat;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }
```

