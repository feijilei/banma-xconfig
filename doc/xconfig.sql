CREATE TABLE `wisdom` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

CREATE TABLE `x_kv` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project` varchar(50) NOT NULL,
  `profile` varchar(50) NOT NULL DEFAULT '',
  `xKey` varchar(50) NOT NULL DEFAULT '' COMMENT 'key',
  `xValue` varchar(200) NOT NULL DEFAULT '',
  `security` char(11) NOT NULL DEFAULT 'N' COMMENT 'Y，N，是否高密字段，高密字段只有master可以查看',
  `description` varchar(500) NOT NULL DEFAULT '' COMMENT '描述信息',
  `createTime` timestamp NOT NULL DEFAULT '2016-07-01 00:00:00',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_project_profile_xKey` (`project`,`profile`,`xKey`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

CREATE TABLE `x_profile` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project` varchar(50) NOT NULL,
  `profile` varchar(50) NOT NULL DEFAULT '',
  `md5` varchar(200) NOT NULL DEFAULT '' COMMENT '当前profile的MD5码',
  `profileKey` varchar(50) DEFAULT NULL COMMENT '安全秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_project_profile` (`project`,`profile`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

CREATE TABLE `x_project` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_project` (`project`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

CREATE TABLE `x_project_dependency` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project` varchar(50) NOT NULL DEFAULT '',
  `depProject` varchar(50) NOT NULL DEFAULT '' COMMENT '依赖的project',
  PRIMARY KEY (`id`),
  KEY `udx_project_dep` (`project`,`depProject`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

CREATE TABLE `x_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) NOT NULL DEFAULT '',
  `userNike` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(100) NOT NULL DEFAULT '',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role` int(11) NOT NULL COMMENT 'guest 10，owner 20 ,master 30',
  PRIMARY KEY (`id`),
  KEY `udx_userName` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `wisdom` (`id`, `content`)
VALUES
	(1, 'UNIX很简单。但需要有一定天赋的人才能理解这种简单。'),
	(2, '软件在能够复用前必须先能用。'),
	(3, '优秀的判断力来自经验，但经验来自于错误的判断。'),
	(4, '‘理论’是你知道是这样，但它却不好用。‘实践’是它很好用，但你不知道是为什么。程序员将理论和实践结合到一起：既不好用，也不知道是为什么。'),
	(5, '当你想在你的代码中找到一个错误时，这很难；当你认为你的代码是不会有错误时，这就更难了。'),
	(6, '如果建筑工人盖房子的方式跟程序员写程序一样，那第一只飞来的啄木鸟就将毁掉人类文明。'),
	(7, '项目开发的六个阶段：1.充满热情。2.醒悟。3.痛苦。4.找出罪魁祸首。5.惩罚无辜。6.褒奖闲人。'),
	(8, '优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释？”'),
	(9, '我们这个世界的一个问题是，蠢人信誓旦旦，智人满腹狐疑。'),
	(10, '无论在排练中演示是如何的顺利(高效)，当面对真正的现场观众时，出现错误的可能性跟在场观看的人数成正比。'),
	(11, '罗马帝国崩溃的一个主要原因是，没有0，他们没有有效的方法表示他们的C程序成功的终止。'),
	(12, '如果debugging是一种消灭bug的过程，那编程就一定是把bug放进去的过程。'),
	(13, '你要么要软件质量，要么要指针算法；两者不可兼得。'),
	(14, '有两种方法能写出没有错误的程序；但只有第三种好用。'),
	(15, '用代码行数来测评软件开发进度，就相对于用重量来计算飞机建造进度。'),
	(16, '最初的90%的代码用去了最初90%的开发时间。余下的10%的代码用掉另外90%的开发时间。'),
	(17, '程序员和上帝打赌要开发出更大更好——傻瓜都会用的软件。而上帝却总能创造出更大更傻的傻瓜。所以，上帝总能赢。');
