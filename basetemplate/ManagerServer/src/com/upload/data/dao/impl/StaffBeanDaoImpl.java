package com.upload.data.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.upload.data.dao.IStaffBeanDao;
import com.upload.data.data.StaffBean;
import com.upload.database.BaseDao;
import com.upload.database.DBParamWrapper;
import com.upload.database.DataExecutor;
import com.upload.database.pool.DBHelper;

public class StaffBeanDaoImpl extends BaseDao<StaffBean> implements IStaffBeanDao
{
	public StaffBeanDaoImpl(DBHelper helper)
	{
		super(helper);
	}

	@Override
	public boolean add(StaffBean staffBean)
	{
		boolean result = false;
		String sql = "insert into t_s_staff(`AccountName`, `Password`, `NickName`, `Remark`, `CreateTime`, `Status`, `CheckCount`, `CheckTime`) values(?, ?, ?, ?, ?, ?, ?, ?);";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR,staffBean.getAccountName());
		params.put(Types.VARCHAR,staffBean.getPassword());
		params.put(Types.VARCHAR,staffBean.getNickName());
		params.put(Types.VARCHAR,staffBean.getRemark());
		params.put(Types.TIMESTAMP,staffBean.getCreateTime());
		params.put(Types.INTEGER,staffBean.getStatus());
		params.put(Types.INTEGER,staffBean.getCheckCount());
		params.put(Types.TIMESTAMP,staffBean.getCheckTime());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean update(StaffBean staffBean)
	{
		boolean result = false;
		String sql = "update t_s_staff set `AccountName`=?, `Password`=?, `NickName`=?, `Remark`=?, `CreateTime`=?, `Status`=?, `CheckCount`=?, `CheckTime`=? where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR,staffBean.getAccountName());
		params.put(Types.VARCHAR,staffBean.getPassword());
		params.put(Types.VARCHAR,staffBean.getNickName());
		params.put(Types.VARCHAR,staffBean.getRemark());
		params.put(Types.TIMESTAMP,staffBean.getCreateTime());
		params.put(Types.INTEGER,staffBean.getStatus());
		params.put(Types.INTEGER,staffBean.getCheckCount());
		params.put(Types.TIMESTAMP,staffBean.getCheckTime());
		params.put(Types.INTEGER,staffBean.getUserID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean delete(StaffBean staffBean)
	{
		boolean result = false;
		String sql = "delete from t_s_staff where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,staffBean.getUserID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean addOrUpdate(StaffBean staffBean)
	{
		boolean result = false;
		String sql = "insert into t_s_staff(`UserID`, `AccountName`, `Password`, `NickName`, `Remark`, `CreateTime`, `Status`, `CheckCount`, `CheckTime`) values(?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `AccountName`=?,`Password`=?,`NickName`=?,`Remark`=?,`CreateTime`=?,`Status`=?,`CheckCount`=?,`CheckTime`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,staffBean.getUserID());
		params.put(Types.VARCHAR,staffBean.getAccountName());
		params.put(Types.VARCHAR,staffBean.getPassword());
		params.put(Types.VARCHAR,staffBean.getNickName());
		params.put(Types.VARCHAR,staffBean.getRemark());
		params.put(Types.TIMESTAMP,staffBean.getCreateTime());
		params.put(Types.INTEGER,staffBean.getStatus());
		params.put(Types.INTEGER,staffBean.getCheckCount());
		params.put(Types.TIMESTAMP,staffBean.getCheckTime());
		params.put(Types.VARCHAR,staffBean.getAccountName());
		params.put(Types.VARCHAR,staffBean.getPassword());
		params.put(Types.VARCHAR,staffBean.getNickName());
		params.put(Types.VARCHAR,staffBean.getRemark());
		params.put(Types.TIMESTAMP,staffBean.getCreateTime());
		params.put(Types.INTEGER,staffBean.getStatus());
		params.put(Types.INTEGER,staffBean.getCheckCount());
		params.put(Types.TIMESTAMP,staffBean.getCheckTime());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean deleteByKey(Object... ids)
	{
		boolean result = false;
		String sql = "delete from t_s_staff where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public StaffBean getByKey(Object... ids)
	{
		String sql = "select * from t_s_staff where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		StaffBean staffBean = query(sql, params);		return staffBean;
	}


	@Override
	public List<StaffBean> listAll()
	{
		String sql = "select * from t_s_staff;";
		List<StaffBean> staffBeans = queryList(sql);		return staffBeans;
	}

	@Override
	public int[] addOrUpdateBatch(List<StaffBean> staffBeans)
	{
		String sql = "insert into t_s_staff(`UserID`, `AccountName`, `Password`, `NickName`, `Remark`, `CreateTime`, `Status`, `CheckCount`, `CheckTime`) values(?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `AccountName`=?,`Password`=?,`NickName`=?,`Remark`=?,`CreateTime`=?,`Status`=?,`CheckCount`=?,`CheckTime`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, staffBeans, new DataExecutor<int[]>()
			{
				@Override
				public int[] execute(PreparedStatement statement, Object... objects) throws Exception
				{
					@SuppressWarnings("unchecked")
					List<StaffBean>staffBeans = (List<StaffBean>)objects[0];
					for (StaffBean staffBean : staffBeans)
					{
						DBParamWrapper params = new DBParamWrapper();
						params.put(Types.INTEGER,staffBean.getUserID());
						params.put(Types.VARCHAR,staffBean.getAccountName());
						params.put(Types.VARCHAR,staffBean.getPassword());
						params.put(Types.VARCHAR,staffBean.getNickName());
						params.put(Types.VARCHAR,staffBean.getRemark());
						params.put(Types.TIMESTAMP,staffBean.getCreateTime());
						params.put(Types.INTEGER,staffBean.getStatus());
						params.put(Types.INTEGER,staffBean.getCheckCount());
						params.put(Types.TIMESTAMP,staffBean.getCheckTime());
						params.put(Types.VARCHAR,staffBean.getAccountName());
						params.put(Types.VARCHAR,staffBean.getPassword());
						params.put(Types.VARCHAR,staffBean.getNickName());
						params.put(Types.VARCHAR,staffBean.getRemark());
						params.put(Types.TIMESTAMP,staffBean.getCreateTime());
						params.put(Types.INTEGER,staffBean.getStatus());
						params.put(Types.INTEGER,staffBean.getCheckCount());
						params.put(Types.TIMESTAMP,staffBean.getCheckTime());
						statement = getDBHelper().prepareCommand(statement,params.getParams());
						statement.addBatch();
					}
					return statement.executeBatch();
				}
			});
		return effectedRows;
	}

	@Override
	public int[] deleteBatch(List<StaffBean> staffBeans)
	{
		String sql = "delete from t_s_staff where `UserID`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, staffBeans, new DataExecutor<int[]>()
		{
			@Override
		public int[] execute(PreparedStatement statement, Object... objects) throws Exception
		{
			
			@SuppressWarnings("unchecked")
			List<StaffBean>staffBeans = (List<StaffBean>)objects[0];
			for (StaffBean staffBean : staffBeans)
			{
					DBParamWrapper params = new DBParamWrapper();
					params.put(Types.INTEGER,staffBean.getUserID());
					statement = getDBHelper().prepareCommand(statement,params.getParams());
					statement.addBatch();
				}
				return statement.executeBatch();
			}
		});
		return effectedRows;
	}
	@Override
	public StaffBean rsToEntity(ResultSet rs) throws SQLException
	{
		StaffBean staffBean = new StaffBean();
		staffBean.setUserID(rs.getInt("UserID"));
		staffBean.setAccountName(rs.getString("AccountName"));
		staffBean.setPassword(rs.getString("Password"));
		staffBean.setNickName(rs.getString("NickName"));
		staffBean.setRemark(rs.getString("Remark"));
		staffBean.setCreateTime(rs.getTimestamp("CreateTime"));
		staffBean.setStatus(rs.getInt("Status"));
		staffBean.setCheckCount(rs.getInt("CheckCount"));
		staffBean.setCheckTime(rs.getTimestamp("CheckTime"));
		return staffBean;
	}

	@Override
	public StaffBean getStaffBeanByUserID(int userID)
	{
		String sql = "select * from t_s_staff where `userID` = ?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userID);
		StaffBean staffBean = query(sql,params);
		return staffBean;
	}

}