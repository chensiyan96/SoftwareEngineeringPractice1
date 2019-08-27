create table `user` (
    `userId` int not null auto_increment primary key,
    `email` varchar(255) not null,
    `username` varchar(255) not null,
    `password` char(64) not null,
    `salt` char(32) not null
) ENGINE = InnoDB auto_increment = 1 default charset = utf8;

create table `article` (
	`articleId` int not null auto_increment primary key
) ENGINE = InnoDB auto_increment = 1 default charset = utf8;

create table `comment` (
	`commentId` int not null auto_increment primary key,
	`userId` int not null,
	`articleId` int not null,
	`parentId` int null,
	`content` text not null,
	constraint `FK_comment_user` foreign key(`userId`) references `user`(`userId`),
	constraint `FK_comment_article` foreign key(`articleId`) references `article`(`articleId`),
	constraint `FK_comment_comment` foreign key(`parentId`) references `comment`(`commentId`)
) ENGINE = InnoDB auto_increment = 1 default charset = utf8;