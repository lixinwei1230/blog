/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.6.10 : Database - blog
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`blog` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `blog`;

/*Table structure for table `blog` */

DROP TABLE IF EXISTS `blog`;

CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` mediumtext NOT NULL,
  `space_id` varchar(20) NOT NULL,
  `write_date` datetime NOT NULL,
  `category` int(11) NOT NULL,
  `scope` int(11) NOT NULL DEFAULT '0',
  `hits` int(11) NOT NULL DEFAULT '0',
  `blog_from` int(11) NOT NULL DEFAULT '0',
  `summary` varchar(1000) NOT NULL,
  `blog_status` int(11) NOT NULL DEFAULT '0',
  `lastModifyDate` datetime DEFAULT NULL,
  `comment_scope` int(11) NOT NULL DEFAULT '0',
  `blog_level` int(11) NOT NULL,
  `recommend` tinyint(1) NOT NULL DEFAULT '0',
  `comments` int(11) NOT NULL DEFAULT '0',
  `del` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `catalogue` (`category`),
  KEY `user_id` (`space_id`),
  CONSTRAINT `blog_ibfk_2` FOREIGN KEY (`space_id`) REFERENCES `user_space` (`id`),
  CONSTRAINT `blog_ibfk_3` FOREIGN KEY (`category`) REFERENCES `blog_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;

/*Data for the table `blog` */

LOCK TABLES `blog` WRITE;

UNLOCK TABLES;

/*Table structure for table `blog_category` */

DROP TABLE IF EXISTS `blog_category`;

CREATE TABLE `blog_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(30) NOT NULL,
  `category_order` int(11) NOT NULL DEFAULT '1',
  `space_id` varchar(20) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `space_id` (`space_id`),
  CONSTRAINT `blog_category_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `user_space` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;

/*Data for the table `blog_category` */

LOCK TABLES `blog_category` WRITE;

UNLOCK TABLES;

/*Table structure for table `blog_comment` */

DROP TABLE IF EXISTS `blog_comment`;

CREATE TABLE `blog_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_title` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `blog_id` int(11) NOT NULL,
  `content` varchar(5000) NOT NULL,
  `anonymous` tinyint(1) NOT NULL DEFAULT '0',
  `comment_date` datetime NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `reply_to` int(11) DEFAULT NULL,
  `reply_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `scope_id` (`blog_id`),
  KEY `comment__ibfk_3` (`reply_to`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `blog_comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`),
  CONSTRAINT `blog_comment_ibfk_2` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`id`),
  CONSTRAINT `blog_comment_ibfk_3` FOREIGN KEY (`reply_to`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=264 DEFAULT CHARSET=utf8;

/*Data for the table `blog_comment` */

LOCK TABLES `blog_comment` WRITE;

UNLOCK TABLES;

/*Table structure for table `blog_file` */

DROP TABLE IF EXISTS `blog_file`;

CREATE TABLE `blog_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) NOT NULL,
  `file_status` int(11) NOT NULL DEFAULT '0',
  `file_size` int(11) NOT NULL DEFAULT '0',
  `extension` varchar(20) NOT NULL,
  `uploadDate` datetime NOT NULL,
  `file_store` int(11) NOT NULL,
  `relativePath` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `original_name` varchar(1000) NOT NULL,
  `cover` int(11) DEFAULT NULL,
  `is_cover` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `cover` (`cover`),
  CONSTRAINT `blog_file_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=659 DEFAULT CHARSET=utf8;

/*Data for the table `blog_file` */

LOCK TABLES `blog_file` WRITE;

UNLOCK TABLES;

/*Table structure for table `blog_tag` */

DROP TABLE IF EXISTS `blog_tag`;

CREATE TABLE `blog_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tag_id` (`tag_id`),
  KEY `blog` (`blog`),
  CONSTRAINT `blog_tag_ibfk_1` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),
  CONSTRAINT `blog_tag_ibfk_2` FOREIGN KEY (`blog`) REFERENCES `blog` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=469 DEFAULT CHARSET=utf8;

/*Data for the table `blog_tag` */

LOCK TABLES `blog_tag` WRITE;

UNLOCK TABLES;

/*Table structure for table `blog_user` */

DROP TABLE IF EXISTS `blog_user`;

CREATE TABLE `blog_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(40) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_email` varchar(100) NOT NULL,
  `registerDate` datetime NOT NULL,
  `activateDate` datetime DEFAULT NULL,
  `activate` tinyint(1) NOT NULL DEFAULT '0',
  `user_enable` tinyint(1) NOT NULL DEFAULT '1',
  `accountNonExpired` tinyint(1) NOT NULL DEFAULT '1',
  `credentialsNonExpired` tinyint(1) NOT NULL DEFAULT '1',
  `accountNonLocked` tinyint(1) NOT NULL DEFAULT '1',
  `nickname` varchar(40) NOT NULL DEFAULT '',
  `avatar` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `user_email` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

/*Data for the table `blog_user` */

LOCK TABLES `blog_user` WRITE;

insert  into `blog_user`(`id`,`user_name`,`user_password`,`user_email`,`registerDate`,`activateDate`,`activate`,`user_enable`,`accountNonExpired`,`credentialsNonExpired`,`accountNonLocked`,`nickname`,`avatar`) values (25,'admin','admin','admin@admin.com','2015-08-01 13:39:01','2015-08-01 13:39:03',1,1,1,1,1,'admin',NULL),(26,'messager','messager','messager@admin.com','2015-08-01 13:39:32','2015-08-01 13:39:33',1,1,1,1,1,'messager',NULL),(53,'test','$2a$10$eJ8pM6Zl9tltoO/aHu9FFuUI6RIBoJmC8q740jajSj1X.Wt0JDowq','test@tst.com','2016-01-28 21:03:49','2016-01-28 21:04:42',1,1,1,1,1,'test',NULL);

UNLOCK TABLES;

/*Table structure for table `login_info` */

DROP TABLE IF EXISTS `login_info`;

CREATE TABLE `login_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `login_date` datetime NOT NULL,
  `remote_address` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `login_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8;

/*Data for the table `login_info` */

LOCK TABLES `login_info` WRITE;

insert  into `login_info`(`id`,`user_id`,`login_date`,`remote_address`) values (380,53,'2016-01-28 21:05:09','0:0:0:0:0:0:0:1');

UNLOCK TABLES;

/*Table structure for table `message_detail` */

DROP TABLE IF EXISTS `message_detail`;

CREATE TABLE `message_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` varchar(8000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8;

/*Data for the table `message_detail` */

LOCK TABLES `message_detail` WRITE;

UNLOCK TABLES;

/*Table structure for table `message_receive` */

DROP TABLE IF EXISTS `message_receive`;

CREATE TABLE `message_receive` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `receiver` int(11) NOT NULL,
  `isRead` tinyint(1) NOT NULL DEFAULT '0',
  `message_status` int(11) NOT NULL DEFAULT '0',
  `message` int(11) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `receiver` (`receiver`),
  KEY `message` (`message`),
  CONSTRAINT `message_receive_ibfk_1` FOREIGN KEY (`receiver`) REFERENCES `blog_user` (`id`),
  CONSTRAINT `message_receive_ibfk_2` FOREIGN KEY (`message`) REFERENCES `message_send` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

/*Data for the table `message_receive` */

LOCK TABLES `message_receive` WRITE;

UNLOCK TABLES;

/*Table structure for table `message_send` */

DROP TABLE IF EXISTS `message_send`;

CREATE TABLE `message_send` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) NOT NULL,
  `sendDate` datetime NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT '0',
  `detail` int(11) NOT NULL,
  `message_type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sender` (`sender`),
  KEY `detail` (`detail`),
  CONSTRAINT `message_send_ibfk_1` FOREIGN KEY (`sender`) REFERENCES `blog_user` (`id`),
  CONSTRAINT `message_send_ibfk_2` FOREIGN KEY (`detail`) REFERENCES `message_detail` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;

/*Data for the table `message_send` */

LOCK TABLES `message_send` WRITE;

UNLOCK TABLES;

/*Table structure for table `oauth_user` */

DROP TABLE IF EXISTS `oauth_user`;

CREATE TABLE `oauth_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `oauth_type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `oauth_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

/*Data for the table `oauth_user` */

LOCK TABLES `oauth_user` WRITE;

UNLOCK TABLES;

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

LOCK TABLES `role` WRITE;

insert  into `role`(`id`,`role_name`) values (1,'ROLE_SUPERVISOR'),(2,'ROLE_USER'),(3,'ROLE_SPACE'),(4,'ROLE_MESSAGER'),(5,'ROLE_OAUTH');

UNLOCK TABLES;

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(20) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;

/*Data for the table `tag` */

LOCK TABLES `tag` WRITE;

UNLOCK TABLES;

/*Table structure for table `temporary_blog` */

DROP TABLE IF EXISTS `temporary_blog`;

CREATE TABLE `temporary_blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` mediumtext NOT NULL,
  `space_id` varchar(20) NOT NULL,
  `write_date` datetime DEFAULT NULL,
  `save_date` datetime NOT NULL,
  `scope` int(11) NOT NULL DEFAULT '0',
  `blog_from` int(11) NOT NULL DEFAULT '0',
  `blog_status` int(11) NOT NULL DEFAULT '0',
  `comment_scope` int(11) NOT NULL DEFAULT '0',
  `blog_level` int(11) NOT NULL DEFAULT '0',
  `comments` int(11) NOT NULL DEFAULT '0',
  `blog` int(11) DEFAULT NULL,
  `tagStr` varchar(500) DEFAULT NULL,
  `category` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `space_id` (`space_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `temporary_blog` */

LOCK TABLES `temporary_blog` WRITE;

UNLOCK TABLES;

/*Table structure for table `test` */

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `test` */

LOCK TABLES `test` WRITE;

UNLOCK TABLES;

/*Table structure for table `user_code` */

DROP TABLE IF EXISTS `user_code`;

CREATE TABLE `user_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_code` varchar(64) NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `code_type` int(11) NOT NULL DEFAULT '0',
  `alive` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_code_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8;

/*Data for the table `user_code` */

LOCK TABLES `user_code` WRITE;

insert  into `user_code`(`id`,`user_code`,`user_id`,`create_date`,`code_type`,`alive`) values (118,'2498e47c-7aef-4ad2-8b94-ef246cf7ee03',53,'2016-01-28 21:03:50',0,0);

UNLOCK TABLES;

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`),
  CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

LOCK TABLES `user_role` WRITE;

insert  into `user_role`(`id`,`user_id`,`role_id`) values (22,25,1),(23,26,4),(66,53,2),(67,53,1),(68,53,3);

UNLOCK TABLES;

/*Table structure for table `user_space` */

DROP TABLE IF EXISTS `user_space`;

CREATE TABLE `user_space` (
  `id` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `space_status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_id` (`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_space_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_space` */

LOCK TABLES `user_space` WRITE;

insert  into `user_space`(`id`,`user_id`,`create_date`,`space_status`) values ('test',53,'2016-01-28 21:05:24',0);

UNLOCK TABLES;

/*Table structure for table `user_tag` */

DROP TABLE IF EXISTS `user_tag`;

CREATE TABLE `user_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `user_tag_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`),
  CONSTRAINT `user_tag_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `user_tag` */

LOCK TABLES `user_tag` WRITE;

UNLOCK TABLES;

/*Table structure for table `widget_blogwidgetconfig` */

DROP TABLE IF EXISTS `widget_blogwidgetconfig`;

CREATE TABLE `widget_blogwidgetconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hidden` tinyint(1) NOT NULL DEFAULT '0',
  `widget_id` int(11) NOT NULL,
  `space_id` varchar(20) DEFAULT NULL,
  `display_mode` int(11) NOT NULL DEFAULT '0',
  `scope` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `space_id` (`space_id`),
  KEY `widget_id` (`widget_id`),
  CONSTRAINT `widget_blogwidgetconfig_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `user_space` (`id`),
  CONSTRAINT `widget_blogwidgetconfig_ibfk_2` FOREIGN KEY (`widget_id`) REFERENCES `widget_locationwidget` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

/*Data for the table `widget_blogwidgetconfig` */

LOCK TABLES `widget_blogwidgetconfig` WRITE;

UNLOCK TABLES;

/*Table structure for table `widget_config` */

DROP TABLE IF EXISTS `widget_config`;

CREATE TABLE `widget_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hidden` tinyint(1) NOT NULL DEFAULT '0',
  `widget_id` int(11) NOT NULL,
  `scope` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `widget_id` (`widget_id`),
  CONSTRAINT `widget_config_ibfk_1` FOREIGN KEY (`widget_id`) REFERENCES `widget_locationwidget` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8;

/*Data for the table `widget_config` */

LOCK TABLES `widget_config` WRITE;

UNLOCK TABLES;

/*Table structure for table `widget_locationwidget` */

DROP TABLE IF EXISTS `widget_locationwidget`;

CREATE TABLE `widget_locationwidget` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `row_index` int(11) NOT NULL DEFAULT '0',
  `column_index` int(11) NOT NULL DEFAULT '0',
  `widget_index` int(11) NOT NULL DEFAULT '0',
  `widget_type` int(11) NOT NULL DEFAULT '0',
  `page_id` int(11) NOT NULL,
  `widget_id` int(11) NOT NULL,
  `width` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `page_id` (`page_id`),
  CONSTRAINT `widget_locationwidget_ibfk_1` FOREIGN KEY (`page_id`) REFERENCES `widget_page` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;

/*Data for the table `widget_locationwidget` */

LOCK TABLES `widget_locationwidget` WRITE;

UNLOCK TABLES;

/*Table structure for table `widget_page` */

DROP TABLE IF EXISTS `widget_page`;

CREATE TABLE `widget_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_type` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `widget_page_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

/*Data for the table `widget_page` */

LOCK TABLES `widget_page` WRITE;

insert  into `widget_page`(`id`,`page_type`,`user_id`) values (65,0,53),(66,1,53);

UNLOCK TABLES;

/*Table structure for table `widget_user` */

DROP TABLE IF EXISTS `widget_user`;

CREATE TABLE `widget_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `widget_html` varchar(5000) NOT NULL,
  `widget_name` varchar(20) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `widget_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `blog_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

/*Data for the table `widget_user` */

LOCK TABLES `widget_user` WRITE;

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
