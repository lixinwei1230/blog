# blog
钱宇豪的个人博客

博客地址:<a href="http://www.qyh.me" target="_blank">http://www.qyh.me</a><br/> 

环境:<br/>
eclipse&maven<br/> 
spring 4&spring security 3&mybatis 3<br/>
jdk7+&mysql 5+&tomcat7+<br/>
数据库配置信息在resources/mybatis/dbconn.properties中。<br/>

邮件服务:<br/>
注册，找回密码需要邮件的支持，需要在resources/mail.properties配置邮件服务信息<br/>

泛域名支持:<br/>
需要泛域名解析的支持<br/>
用户激活space的时候会自动为配置一个子域名(eg:test.qyh.me)<br/>
需要在webConfig.properties中配置enableSpaceDomain为true<br/>
会自动开启SpaceDomainFilter<br/>

文件存储:<br/>
头像存储固定为avatarStore<br/>
不能在FileServer中配置avatarStore<br/>
配置了LocalFileStorage并且配置了SimulationDomainFileMapping会自动开启SimulationDomainFileMappingFilter<br/>

图片裁剪:<br/>
图片的裁剪使用Im4java + GraphicsMagick。需要安装GraphicsMagick，下载地址：http://ftp.icm.edu.pl/pub/unix/graphics/GraphicsMagick/windows/ ，im4java在maven中可以找到
ps:在windows系统下，需要配置config.properties文件中的config.magick.path，该值必须指向GraphicsMagick的安装目录，unix下可以为空<br/>

html过滤:<br/>
在resouces/spring/htmlClean.xml文件中可以配置或者新增htmlClean过滤(注意xss)，通过实现me.qyh.helper.htmlclean.HtmlContentHandler可以实现更多的效果。<br/>

其他：<br/>
resources/config.properties 可以配置一些分页每页记录数、字段验证已经一些服务的配置。
在resources/webConfig中可以开启二级域名(需要泛域名的支持，通过enableSpaceDomain来开启，本地可以修改hosts文件)，https支持<br/>

初始化用户名和密码：test,123456</br>

支持rss订阅</br>
支持qq、sina微博的oauth登录<br/>

<br/><strong>不支持也不会支持metaweblog</strong><br/>
<strong>bug</strong>信息可以在http://www.qyh.me/bug  看到或提出(需要登录)
