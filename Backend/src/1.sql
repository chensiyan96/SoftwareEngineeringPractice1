create table `user`
(
    `userId` int not null auto_increment primary key,
    `email` varchar(255),
    `username` varchar(255),
    `password` varchar(255),
    `salt` int
) auto_increment = 1 default charset = utf8;