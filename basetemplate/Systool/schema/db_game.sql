CREATE TABLE `t_s_activity` (
  `ActivityID` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `UUID` char(36) NOT NULL COMMENT '活动唯一标识',
  `Title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `ActivityType` smallint(6) NOT NULL DEFAULT '0' COMMENT '活动类型',
  `StartDate` datetime NOT NULL COMMENT '开始时间',
  `EndDate` datetime NOT NULL COMMENT '结束时间',
  `IsActive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否激活',
  `ActivityContent` varchar(4000) NOT NULL DEFAULT '' COMMENT '活动内容',
  `RewardContent` varchar(4000) NOT NULL DEFAULT '' COMMENT '奖励内容',
  `Operator` varchar(100) NOT NULL DEFAULT '' COMMENT '操作人',
  `CreateDate` datetime NOT NULL COMMENT '创建时间',
  `LastUpdateDate` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`ActivityID`),
  KEY `ix_uuid` (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动设置';

CREATE TABLE `t_s_activity_reward` (
  `RewardID` int(11) NOT NULL AUTO_INCREMENT COMMENT '奖励ID',
  `ActivityID` int(11) NOT NULL COMMENT '活动ID',
  `RewardContent` varchar(8000) NOT NULL DEFAULT '' COMMENT '奖励内容',
  PRIMARY KEY (`RewardID`),
  KEY `ix_activityid` (`ActivityID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动奖励设置';

CREATE TABLE `t_s_activity_reward_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `RewardDate` datetime NOT NULL COMMENT '奖励时间',
  `ActivityID` int(11) NOT NULL COMMENT '活动ID',
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `RewardID` int(11) NOT NULL COMMENT '奖励ID',
  `Remark` varchar(1000) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`ID`),
  KEY `ix_activityid` (`ActivityID`),
  KEY `ix_userid` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动奖励记录';

CREATE TABLE `t_u_account` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `Username` varchar(50) NOT NULL COMMENT '用户名',
  `Site` varchar(50) NOT NULL DEFAULT '' COMMENT '站点',
  `IsOnline` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否在线',
  `CreateDate` datetime NOT NULL COMMENT '创建时间',
  `ForbidDate` datetime NOT NULL COMMENT '封号时间，小于当前时间表示未被封号或已解封',
  `LastPayDate` datetime DEFAULT NULL COMMENT '最后充值时间，NULL为未充值',
  `LastLoginIP` varchar(50) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `LastLoginDate` datetime NOT NULL COMMENT '最后登录时间',
  `LastLogin2Date` datetime NOT NULL COMMENT '上一天最后登录时间',
  `LastLogin3Date` datetime NOT NULL COMMENT '上上一天最后登录时间',
  `LastLogoutDate` datetime NOT NULL COMMENT '最后登出时间',
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `ix_username_site` (`Username`,`Site`),
  KEY `ix_createdate` (`CreateDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

CREATE TABLE `t_u_achievement_data` (
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `AchievementID` int(11) NOT NULL COMMENT '成就ID',  
  `CompletedDate` datetime NULL COMMENT '完成时间，NULL为未完成',
  PRIMARY KEY (`UserID`,`AchievementID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成就完成记录';

CREATE TABLE `t_u_auction` (
  `AuctionID` int(11) NOT NULL AUTO_INCREMENT,
  `AuctionType` tinyint(4) NOT NULL COMMENT '拍卖类型',
  `ItemID` int(11) NOT NULL COMMENT '物品ID',
  `ItemCount` smallint(6) NOT NULL DEFAULT '0' COMMENT '拍卖物品个数',
  `ItemName` varchar(200) NOT NULL DEFAULT '' COMMENT '物品名称',
  `TemplateID` int(11) NOT NULL COMMENT '物品模板ID',
  `StartPrice` int(11) NOT NULL COMMENT '起拍价格',
  `EndPrice` int(11) NOT NULL DEFAULT '0' COMMENT '成交价格',
  `SellerUserID` int(11) NOT NULL COMMENT '卖家用户ID',
  `SellerNickname` varchar(50) NOT NULL DEFAULT '' COMMENT '专家昵称',
  `BuyerUserID` int(11) NOT NULL DEFAULT '0' COMMENT '买家用户ID',
  `BuyerNickname` varchar(50) NOT NULL DEFAULT '' COMMENT '买家昵称',
  `StartDate` datetime NOT NULL COMMENT '开始时间',
  `ValidPeriod` smallint(6) NOT NULL COMMENT '有效期，以小时为单位',
  `DealDate` datetime DEFAULT NULL COMMENT '成交时间，NULL为未成交',
  `IsExist` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`AuctionID`),
  KEY `ix_selleruserid` (`SellerUserID`),
  KEY `ix_buyeruserid` (`BuyerUserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='拍卖场';

CREATE TABLE `t_u_forbid_log` (
  `OperateDate` datetime NOT NULL COMMENT '操作时间',
  `UserID` int(11) NOT NULL COMMENT '解/封号用户ID',
  `Grade` smallint(6) NOT NULL COMMENT '用户等级',
  `OperateType` tinyint(4) NOT NULL COMMENT '操作类型，1为封号，2为解封',
  `Operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人',
  `Reason` varchar(200) NOT NULL DEFAULT '' COMMENT '原因',
  `OldForbidDate` datetime NOT NULL COMMENT '原封号时间',
  `NewForbidDate` datetime NOT NULL COMMENT '现封号时间',
  PRIMARY KEY (`OperateDate`,`UserID`),
  KEY `ix_userid` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='解/封号记录';

CREATE TABLE `t_u_friend` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `FriendUserID` int(11) NOT NULL COMMENT '好友用户ID',
  `FriendType` varchar(20) NOT NULL DEFAULT '' COMMENT '好友分类',
  PRIMARY KEY (`ID`),
  KEY `ix_userid` (`UserID`),
  KEY `ix_frienduserid` (`FriendUserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户好友信息';

CREATE TABLE `t_u_guild` (
  `GuildID` int(11) NOT NULL AUTO_INCREMENT COMMENT '公会ID',
  `GuildName` varchar(50) NOT NULL COMMENT '公会名称',
  `ChairmanUserID` int(11) NOT NULL COMMENT '会长用户ID',
  `ChairmanNickname` char(10) NOT NULL COMMENT '会长昵称',
  `CreateDate` datetime NOT NULL COMMENT '创建时间',
  `Level` smallint(6) NOT NULL DEFAULT '1' COMMENT '公会等级',
  `MembershipCount` int(11) NOT NULL COMMENT '会员数',
  `MaxMembershipCount` int(11) NOT NULL COMMENT '会员数上限',
  `Ranking` int(11) NOT NULL DEFAULT '0' COMMENT '排名',
  `Introduce` varchar(1000) NOT NULL DEFAULT '' COMMENT '公会简介',
  `Notice` varchar(1000) NOT NULL DEFAULT '' COMMENT '公告',
  `Riches` int(11) NOT NULL DEFAULT '0' COMMENT '公会财富',
  `IsExist` tinyint(1) NOT NULL DEFAULT 1 COMMENT '',
  PRIMARY KEY (`GuildID`),
  KEY `ix_guildname` (`GuildName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会';

CREATE TABLE `t_u_guild_event` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `RecordDate` datetime NOT NULL COMMENT '记录时间',
  `EventType` smallint(6) NOT NULL COMMENT '事件类型',
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `GuildID` int(11) NOT NULL COMMENT '公会ID',
  `Content` varchar(1000) NOT NULL DEFAULT '' COMMENT '内容',
  PRIMARY KEY (`ID`),
  KEY `ix_recorddate` (`RecordDate`),
  KEY `ix_guildid` (`GuildID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会事件，记录公会各种操作日志';

CREATE TABLE `t_u_guild_user` (
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `GuildID` int(11) NOT NULL COMMENT '公会ID',
  `JoinDate` datetime NOT NULL COMMENT '加入时间',
  `Duty` varchar(100) NOT NULL DEFAULT '' COMMENT '职责',
  PRIMARY KEY (`UserID`),
  KEY `ix_guildid` (`GuildID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会成员';

CREATE TABLE `t_u_guild_user_apply` (
  `ApplyDate` datetime NOT NULL COMMENT '申请时间',
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `GuildID` int(11) NOT NULL COMMENT '公会ID',
  `ManagerUserID` int(11) NOT NULL DEFAULT '0' COMMENT '管理人用户ID',
  `ApprovalState` tinyint(4) NOT NULL DEFAULT '0' COMMENT '审批情况，0未审批，1批准，2拒绝，其它。。。',
  PRIMARY KEY (`ApplyDate`,`UserID`),
  KEY `ix_userid` (`UserID`),
  KEY `ix_guildid` (`GuildID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会申请';

CREATE TABLE `t_u_item` (
  `ItemID` int(11) NOT NULL AUTO_INCREMENT COMMENT '物品ID',
  `TemplateID` int(11) NOT NULL COMMENT '物品模板ID',
  `Place` smallint(6) NOT NULL COMMENT '物品位置',
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `Count` smallint(6) NOT NULL DEFAULT '0' COMMENT '数量',
  `IsBind` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否绑定',
  `IsExist` tinyint(1) NOT NULL DEFAULT '0',
  `AddDate` datetime NOT NULL COMMENT '添加时间',
  `AddType` smallint(6) NOT NULL DEFAULT '0' COMMENT '添加类型',
  `RemoveDate` datetime DEFAULT NULL COMMENT '移除时间',
  `RemoveType` smallint(6) NOT NULL DEFAULT '0' COMMENT '移除类型',
  `BagType` smallint(6) DEFAULT NULL COMMENT '存放状态(1-背包,2-身上)',
  PRIMARY KEY (`ItemID`),
  KEY `ix_templateid` (`TemplateID`),
  KEY `ix_userid` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户物品信息';

CREATE TABLE `t_u_mail` (
  `MailID` int(11) NOT NULL AUTO_INCREMENT COMMENT '邮件ID',
  `SendDate` datetime NOT NULL COMMENT '发送时间',
  `ReadDate` datetime DEFAULT NULL COMMENT '阅读时间，NULL为未读',
  `SenderUserID` int(11) NOT NULL COMMENT '发送人用户ID',
  `SenderNickname` varchar(50) NOT NULL DEFAULT '' COMMENT '发送人昵称',
  `ReceiverUserID` int(11) NOT NULL COMMENT '接收人用户ID',
  `ReceiverNickname` varchar(50) NOT NULL DEFAULT '' COMMENT '接收人昵称',
  `Title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `Content` varchar(2000) NOT NULL DEFAULT '' COMMENT '内容',
  `IsExist` tinyint(1) NOT NULL DEFAULT 1 COMMENT '',
  `ValidPeriod` smallint(6) NOT NULL DEFAULT '72' COMMENT '邮件有效期，小时为单位',
  `Gold` int(11) NOT NULL DEFAULT '0' COMMENT '金币',
  `IsGoldGet` tinyint(1) DEFAULT NULL COMMENT '是否提取金币',
  `Money` int(11) NOT NULL DEFAULT '0' COMMENT '点卷',
  `IsMoneyGet` tinyint(1) DEFAULT NULL COMMENT '是否提取点卷',
  `Annex1` int(11) DEFAULT NULL COMMENT '附件1编号',
  `Annex1Name` varchar(50) DEFAULT NULL COMMENT '附件一名称',
  `IsAnnex1Get` tinyint(1) DEFAULT NULL COMMENT '是否提取附件',
  `Annex2` int(11) DEFAULT NULL COMMENT '附件2编号',
  `Annex2Name` varchar(50) DEFAULT NULL COMMENT '附件二名称',
  `IsAnnex2Get` tinyint(1) DEFAULT NULL COMMENT '是否提取附件',
  `Annex3` int(11) DEFAULT NULL COMMENT '附件三编号',
  `Annex3Name` varchar(50) DEFAULT NULL COMMENT '附件三名称',
  `IsAnnex3Get` tinyint(1) DEFAULT NULL COMMENT '是否提取附件',
  `Annex4` int(11) DEFAULT NULL COMMENT '附件四编号',
  `Annex4Name` varchar(50) DEFAULT NULL COMMENT '附件四名称',
  `IsAnnex4Get` tinyint(1) DEFAULT NULL COMMENT '是否提取附件',
  `Annex5` int(11) DEFAULT NULL COMMENT '附件五编号',
  `Annex5Name` varchar(50) DEFAULT NULL COMMENT '附件五名称',
  `IsAnnex5Get` tinyint(1) DEFAULT NULL COMMENT '是否提取附件',
  `AnnexRemark` varchar(2000) DEFAULT NULL COMMENT '附件汇总说明',
  PRIMARY KEY (`MailID`),
  KEY `ix_senderuserid` (`SenderUserID`),
  KEY `ix_receiveruserid` (`ReceiverUserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户邮件';

CREATE TABLE `t_u_pay_order` (
  `PayID` int(11) NOT NULL AUTO_INCREMENT COMMENT '充值流水号',
  `OrderNo` varchar(36) NOT NULL COMMENT '充值订单号',
  `PayDate` datetime NOT NULL COMMENT '充值时间',
  `UserID` int(11) NOT NULL COMMENT '充值用户ID',
  `Username` varchar(50) NOT NULL COMMENT '用户名',
  `Money` int(11) NOT NULL COMMENT '获得<虚拟货币>金额',
  `CurrencyAmt` decimal(10,2) NOT NULL COMMENT '充值金额，实际的钱，美元人民币',
  `CurrencyType` varchar(20) NOT NULL DEFAULT '' COMMENT '币种，美元、人民币、其它',
  `GetDate` datetime DEFAULT NULL COMMENT '领取时间，NULL为未领取',
  `Site` varchar(30) NOT NULL DEFAULT '' COMMENT '站点',
  `Level` int(11) NOT NULL DEFAULT '0' COMMENT '充值时用户等级',
  `PayWay` varchar(30) NOT NULL DEFAULT '' COMMENT '充值途径',
  `PayIP` varchar(40) NOT NULL DEFAULT '' COMMENT '充值IP',
  PRIMARY KEY (`PayID`),
  KEY `ix_orderno` (`OrderNo`),
  KEY `ix_paydate` (`PayDate`),
  KEY `ix_userid` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值订单';

CREATE TABLE `t_u_player` (
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `Nickname` varchar(50) NOT NULL COMMENT '昵称',
  `Sex` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1为男，0为女，其它性别',  
  `Career` tinyint(4) NOT NULL DEFAULT '0' COMMENT '职业',
  `Level` smallint(6) NOT NULL DEFAULT '1' COMMENT '等级',
  `Gold` bigint(20) NOT NULL DEFAULT '0' COMMENT '金币',
  `Exp` int(11) NOT NULL DEFAULT '0' COMMENT '经验',
  `Money` int(11) NOT NULL COMMENT '金钱(使用真实货币充值兑换的币种)',
  `GuildID` int(11) NOT NULL DEFAULT 0 COMMENT '公会ID，未加入公会则为0',  
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `ix_nickname` (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

CREATE TABLE `t_u_player_vip_info` (
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `VipGP` int(11) NOT NULL COMMENT 'VIP经验',
  `VipLevel` smallint(6) NOT NULL COMMENT 'VIP等级',
  `VipType` smallint(6) NOT NULL COMMENT 'VIP类型',
  `IsVip` tinyint(1) NOT NULL COMMENT '是否VIP，0不是，1是',
  `FirstPayDate` datetime NOT NULL COMMENT '首次时间',
  `LastPayDate` datetime DEFAULT NULL COMMENT '最后充值时间',
  `ExpireDate` datetime NOT NULL COMMENT '到期时间',
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='VIP信息表';

CREATE TABLE `t_u_rename_guild` (
  `RenameDate` datetime NOT NULL COMMENT '改名时间',
  `GuildID` int(11) NOT NULL COMMENT '公会ID',
  `OperatorUserID` int(11) NOT NULL DEFAULT '0' COMMENT '操作人用户ID',
  `RenameType` tinyint(4) NOT NULL DEFAULT '1' COMMENT '改名类型，1改名卡，2合区，其它。。。',
  `OldGuildName` varchar(50) NOT NULL DEFAULT '' COMMENT '原昵称',
  `NewGuildName` varchar(50) NOT NULL DEFAULT '' COMMENT '新昵称',
  `EffectiveDate` datetime DEFAULT NULL COMMENT '生效时间，NULL为未生效',
  PRIMARY KEY (`RenameDate`,`GuildID`),
  KEY `ix_newguildname` (`NewGuildName`),
  KEY `ix_guildid` (`OperatorUserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会改名记录';

CREATE TABLE `t_u_rename_user` (
  `RenameDate` datetime NOT NULL COMMENT '改名时间',
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `RenameType` tinyint(4) NOT NULL DEFAULT '1' COMMENT '改名类型，1改名卡，2合区，其它。。。',
  `OldNickname` varchar(50) NOT NULL DEFAULT '' COMMENT '原昵称',
  `NewNickname` varchar(50) NOT NULL DEFAULT '' COMMENT '新昵称',
  `EffectiveDate` datetime DEFAULT NULL COMMENT '生效时间，NULL为未生效',
  PRIMARY KEY (`RenameDate`,`UserID`),
  KEY `ix_newnickname` (`NewNickname`),
  KEY `ix_userid` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户改名记录';

CREATE TABLE `t_u_task_data` (
  `UserID` int(11) NOT NULL COMMENT '用户ID',
  `TaskID` int(11) NOT NULL COMMENT '任务ID',
  `CompletedDate` datetime DEFAULT NULL COMMENT '完成时间，NULL为未完成',
  PRIMARY KEY (`userID`,`taskID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户任务信息';