package com.upload.data.dao;


import java.util.List;

import com.upload.data.data.RedisBean;
import com.upload.database.IBaseDao;


public interface IRedisBeanDao extends IBaseDao<RedisBean>
{
	List<RedisBean> getRedisBeanByUserID(int userID);

}
