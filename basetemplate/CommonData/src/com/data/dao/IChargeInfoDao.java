package com.data.dao;

import com.base.database.IBaseDao;
import java.util.List;

import com.data.info.ChargeInfo;


/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public interface IChargeInfoDao extends IBaseDao<ChargeInfo>
{
	List<ChargeInfo> getChargeInfoByUserID(long userID);

}
