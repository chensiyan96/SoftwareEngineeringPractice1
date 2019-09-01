create table `User` (
    `userId` int not null auto_increment primary key,
    `email` varchar(255) not null unique,
    `username` varchar(255) not null unique,
    `password` char(64) not null,
    `salt` char(32) not null,
    `avatar` varchar(255) null,
    `info` text null
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table `article`
(
    articleId int not null auto_increment primary key,
    `userId` int not null,
    `title` varchar(255) not null,
    `description` varchar(255) not null,
    `content` longtext not null,
    `contentFormat` longtext not null,
    `readNum` int not null,
    `likeNum` int not null,
    `cover` varchar(255) null,
    `appendix` varchar(255) null,
    `createTime` long not null,
    `updateTime` long not null,
    `categoryId` varchar(255) null,
    `isPublic` boolean not null,
    `isTop` boolean not null,
    constraint `FK_article_user` foreign key(`userId`) references `User`(`userId`) on delete cascade
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table `comment` (
	`commentId` int not null auto_increment primary key,
	`userId` int not null,
	`articleId` int not null,
	`parentId` int null,
	`createTime` long not null,
	`content` text not null,
	constraint `FK_comment_user` foreign key(`userId`) references `User`(`userId`) on delete cascade,
	constraint `FK_comment_article` foreign key(`articleId`) references `article`(`articleId`) on delete cascade,
	constraint `FK_comment_comment` foreign key(`parentId`) references `comment`(`commentId`) on delete cascade
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table `Love` (
    `userId` int not null,
	`articleId` int not null,
	constraint `FK_like_user` foreign key(`userId`) references `User`(`userId`) on delete cascade,
    constraint `FK_like_article` foreign key(`articleId`) references `article`(`articleId`) on delete cascade
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table `Follow` (
    `hostId` int not null,
	`fansId` int not null,
	constraint `FK_follow_host` foreign key(`hostId`) references `User`(`userId`) on delete cascade,
    constraint `FK_follow_fans` foreign key(`fansId`) references `User`(`userId`) on delete cascade
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table `tag`
(
  id int not null auto_increment primary key,
  `name` varchar(50) not null unique,
  `type` int null,
  `userId` int not null,
  constraint `FK_tag_user` foreign key(`userId`) references `User`(`userId`) on delete cascade
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table tag_link
(
  id int not null auto_increment primary key,
  tagId int null comment '标签Id',
  linkId int null comment '关联Id',
  type int null comment '所属类别：0文章，1阅读',
  constraint `FK_tag_id` foreign key(`tagId`) references `tag`(`id`) on delete cascade
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table `category`
(
  id int not null auto_increment primary key,
  `name` varchar(255) not null unique,
  `type` int null,
  `rank` int null,
  parent_id int default '0' null,
  constraint operation_category_id_uindex
  unique (id)
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;

create table oss_resource
(
  id int not null auto_increment primary key,
  name varchar(255) null,
  url varchar(255) not null
) ENGINE = InnoDB auto_increment = 1 default charset = utf8mb4;
