package com.data.log.dao;

import com.base.database.IBaseDao;
import java.util.List;

import com.data.log.LoginLog;


/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public interface ILoginLogDao extends IBaseDao<LoginLog>
{
	List<LoginLog> getLoginLogByUserID(int userID);

}
