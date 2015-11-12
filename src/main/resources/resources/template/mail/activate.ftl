<html>  
   <head>  
      <meta http-equiv="content-type" content="text/html;charset=utf8">  
   </head>  
   <body>  
     	<div>
     		欢迎${user.username}注册我网站,请点击下面的链接激活您的帐户，如果链接不能被点击，请将链接拷贝至浏览器地址栏进行激活
     	</div>
     	<div>
     		http://${urlHelper.getUrl()}/activate?activateCode=${activateCode}&userid=${user.id}
     	<div>
   </body>  
</html>  
