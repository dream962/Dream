package com.upload.data.dao;

import com.upload.data.data.StaffBean;
import com.upload.database.IBaseDao;

public interface IStaffBeanDao extends IBaseDao<StaffBean>
{
	StaffBean getStaffBeanByUserID(int userID);
}
