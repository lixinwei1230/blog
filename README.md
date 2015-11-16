# blog
钱宇豪的个人博客

# http://www.qyh.me

eclipse maven项目。 
基于spring 4&spring security 3&mybatis 3。

数据库：
采用mysql，配置信息在resources/mybatis/dbconn.properties中。

邮件服务
注册，找回密码需要邮件的支持，可在resources/mail.properties配置邮件服务信息

文件存储
重写了文件存储，现在只有一个InnerFileStore,而且FileServer只需配置一个InnserFileStore
图片的裁剪使用Im4java + GraphicsMagick。需要安装GraphicsMagick，下载地址：http://ftp.icm.edu.pl/pub/unix/graphics/GraphicsMagick/windows/ ，im4java在maven中可以找到
ps:在windows系统下，需要配置config.properties文件中的config.magick.path必须指向GraphicsMagick的安装目录，unix下可以为空

html过滤
在resouces/spring/htmlClean.xml文件中可以配置或者新增htmlClean过滤(注意xss)，通过me.qyh.helper.htmlclean.HtmlContentHandler可以实现更多的效果。

其他：
resources/config.properties 可以配置一些分页每页记录数、字段验证已经一些服务的配置。
在resources/appConfig中可以开启二级域名(需要泛域名的支持，通过enableSpaceDomain来开启，本地可以修改hosts文件)

bug信息可以在http://www.qyh.me/bug看到或提出(需要登录)

3.3====
1.增加了rss订阅
2.增加了qq的oauth登录
3.删除了相册、附件组、附件和投票，只保留了博客、站内信和挂件。
4.新增了文件管理，用来统一管理上传的文件
5.初始化用户名和密码：test,123456
如果密码错误，请执行这条sql：UPDATE blog_user SET user_password = '$2a$10$eJ8pM6Zl9tltoO/aHu9FFuUI6RIBoJmC8q740jajSj1X.Wt0JDowq' WHERE user_name = 'test'

ps:
程序无法启动，提示EmptySet<>是因为resource/mail.properties没有加密所致，利用me.qyh.helper.encrypt.SimpleAESPropertyDecoder的main方法随意加密一个字符串然后替换mail.password即可，例如
mail.password=NAo4DWI9Nj00Z2lSaW9kNE5JbFJ1akRhNWl5ZXIwbFhjWG1aWEJlYUV5Z0I9Mj1DDWsKTA==