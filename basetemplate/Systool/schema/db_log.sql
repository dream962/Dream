CREATE TABLE t_log_server(
	RecordDate DATETIME NOT NULL COMMENT '时间',
	Online INT NOT NULL COMMENT '在线人数',
	PRIMARY KEY(RecordDate)
) COMMENT '在线人数';

CREATE TABLE t_log_user_upgrade(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	RecordDate DATETIME NOT NULL COMMENT '升级时间',
	UserID INT NOT NULL COMMENT '用户id',
	OldLevel SMALLINT NOT NULL COMMENT '原等级',
	NewLevel SMALLINT NOT NULL COMMENT '新等级',
	PRIMARY KEY(ID),
	KEY ix_RecordDate(RecordDate),
	KEY ix_userId(UserID)
) COMMENT '角色升级记录';

CREATE TABLE t_log_login(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	LoginDate DATETIME NOT NULL COMMENT '登录时间',
	LogoutDate DATETIME NOT NULL COMMENT '登出时间',
	UserID INT NOT NULL COMMENT '用户id',
	PRIMARY KEY(ID),
	KEY ix_logoutdate(LogoutDate),
	KEY ix_userId(UserID)
) COMMENT '登录记录';

CREATE TABLE t_log_wealth(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	RecordDate DATETIME NOT NULL COMMENT '消费时间',
	UserID INT NOT NULL COMMENT '用户id',
	Level INT NOT NULL COMMENT '等级',
	MainType SMALLINT NOT NULL COMMENT '消费主类型',
	SubType INT NOT NULL COMMENT '消费子类型',
	Money INT NOT NULL COMMENT '使用财富,正数为增加,负数为消耗',
	RemainMoney INT NOT NULL COMMENT '此次消耗剩余的财富',
	PRIMARY KEY(ID),
	KEY ix_RecordDate(RecordDate),
	KEY ix_userId(UserID)
) COMMENT '财富记录';

CREATE TABLE t_log_shop(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	RecordDate DATETIME NOT NULL COMMENT '消费时间',
	UserID INT NOT NULL COMMENT '付费人用户id',
	Level INT NOT NULL COMMENT '付费人等级',
	ReceiverUserID INT NOT NULL COMMENT '接收人用户id,用于赠送物品时,其它为0',
	ShopType TINYINT(4) NOT NULL COMMENT '商城类型',
	MainType SMALLINT NOT NULL COMMENT '消费主类型',
	SubType INT NOT NULL COMMENT '消费子类型',
	MoneyType SMALLINT NOT NULL COMMENT '货币类型',
	Money INT NOT NULL COMMENT '货币金额',
	TemplateId INT NOT NULL COMMENT '物品模板id',
	ItemId INT NOT NULL COMMENT '物品id',
	ItemCount INT NOT NULL COMMENT '物品数量',
	ValidPeriod INT NOT NULL COMMENT '物品有限期',
	PRIMARY KEY(ID),
	KEY ix_RecordDate(RecordDate),
	KEY ix_userId(UserID)
) COMMENT '商城消费记录';


CREATE TABLE t_log_drop_item(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	RecordDate DATETIME NOT NULL COMMENT '掉落时间',
	UserID INT NOT NULL COMMENT '用户id',
	DropType SMALLINT NOT NULL COMMENT '掉落类型',
	TemplateId INT NOT NULL COMMENT '物品模板id',
	ItemId INT NOT NULL COMMENT '物品id',
	ItemCount INT NOT NULL COMMENT '物品数量', 
	IsBind TINYINT(1) NOT NULL COMMENT '是否绑定',
	PRIMARY KEY(ID),
	KEY ix_RecordDate(RecordDate),
	KEY ix_userId(UserID)
) COMMENT '物品掉落记录';


CREATE TABLE t_log_fight(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	StartDate DATETIME NOT NULL COMMENT '开始时间',
	EndDate DATETIME NOT NULL COMMENT '结束时间',
	FightType SMALLINT NOT NULL COMMENT '战斗类型',
	MapId INT NOT NULL COMMENT '地图id',
	TeamA VARCHAR(2000) NOT NULL COMMENT '组A,逗号拼接的用户id',
	TeamB VARCHAR(2000) NOT NULL COMMENT '组B,逗号拼接的用户id',
	TeamALevel VARCHAR(2000) NOT NULL COMMENT '组A等级,逗号拼接的角色等级',
	TeamBLevel VARCHAR(2000) NOT NULL COMMENT '组B等级,逗号拼接的角色等级',
	Result TINYINT(4) NOT NULL COMMENT '战斗结果',
	PRIMARY KEY(ID),
	KEY ix_startDate(startDate)
) COMMENT '战斗记录';

CREATE TABLE t_log_item(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	RecordDate DATETIME NOT NULL COMMENT '操作时间',
	UserID INT NOT NULL COMMENT '用户id',
	Operation SMALLINT NOT NULL COMMENT '操作类型',
	TemplateId INT NOT NULL COMMENT '物品模板id',
	ItemId INT NOT NULL COMMENT '物品id', 
	StartProperty VARCHAR(1000) NOT NULL COMMENT '操作前属性',
	EndProperty VARCHAR(1000) NOT NULL COMMENT '操作后属性',
	IsSucceed TINYINT(1) NOT NULL COMMENT '是否成功,1为成功',
	PRIMARY KEY(ID),
	KEY ix_RecordDate(RecordDate),
	KEY ix_userId(UserID)
) COMMENT '物品操作记录'; 

CREATE TABLE t_log_vip(
	ID INT NOT NULL AUTO_INCREMENT COMMENT '自增id,保持唯一',
	RecordDate DATETIME NOT NULL COMMENT '充值时间',
	UserID INT NOT NULL COMMENT '付款人id',
	RenewUserID INT NOT NULL COMMENT '受益人id',
	VipType TINYINT(4) NOT NULL COMMENT 'VIP类型',
	ValidPeriod SMALLINT NOT NULL COMMENT '续费有效期',
	StartDate DATETIME NOT NULL COMMENT '有效期开始时间',
	EndDate DATETIME NOT NULL COMMENT '有效期结束时间',
	IsFirst TINYINT(1) NOT NULL COMMENT '是否首充,1为首充',
	PRIMARY KEY(ID),
	KEY ix_RecordDate(RecordDate),
	KEY ix_userId(UserID)
) COMMENT 'VIP充值记录'; 
