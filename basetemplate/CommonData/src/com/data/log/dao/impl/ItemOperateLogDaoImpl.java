package com.data.log.dao.impl;

import com.base.database.DataReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.base.database.BaseDao;
import com.base.database.DBParamWrapper;
import com.base.database.DataExecutor;
import com.base.database.pool.DBHelper;


import com.data.log.dao.IItemOperateLogDao;
import com.data.log.ItemOperateLog;



/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public class ItemOperateLogDaoImpl extends BaseDao<ItemOperateLog> implements IItemOperateLogDao
{
	public ItemOperateLogDaoImpl(DBHelper helper)
	{
		super(helper);
	}


	@Override
	public boolean add(ItemOperateLog itemOperateLog)
	{
		boolean result = false;
		String sql = "insert into t_l_item_operate(`UserID`, `NickName`, `ItemID`, `ItemName`, `OpCount`, `OpType`, `OpTime`, `IsProfit`) values(?, ?, ?, ?, ?, ?, ?, ?);";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.BIGINT,itemOperateLog.getUserID());
		params.put(Types.VARCHAR,itemOperateLog.getNickName());
		params.put(Types.INTEGER,itemOperateLog.getItemID());
		params.put(Types.VARCHAR,itemOperateLog.getItemName());
		params.put(Types.INTEGER,itemOperateLog.getOpCount());
		params.put(Types.INTEGER,itemOperateLog.getOpType());
		params.put(Types.TIMESTAMP,itemOperateLog.getOpTime());
		params.put(Types.TINYINT,itemOperateLog.getIsProfit());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean update(ItemOperateLog itemOperateLog)
	{
		boolean result = false;
		String sql = "update t_l_item_operate set `UserID`=?, `NickName`=?, `ItemID`=?, `ItemName`=?, `OpCount`=?, `OpType`=?, `OpTime`=?, `IsProfit`=? where `ItemOperateID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.BIGINT,itemOperateLog.getUserID());
		params.put(Types.VARCHAR,itemOperateLog.getNickName());
		params.put(Types.INTEGER,itemOperateLog.getItemID());
		params.put(Types.VARCHAR,itemOperateLog.getItemName());
		params.put(Types.INTEGER,itemOperateLog.getOpCount());
		params.put(Types.INTEGER,itemOperateLog.getOpType());
		params.put(Types.TIMESTAMP,itemOperateLog.getOpTime());
		params.put(Types.TINYINT,itemOperateLog.getIsProfit());
		params.put(Types.INTEGER,itemOperateLog.getItemOperateID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean delete(ItemOperateLog itemOperateLog)
	{
		boolean result = false;
		String sql = "delete from t_l_item_operate where `ItemOperateID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,itemOperateLog.getItemOperateID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean addOrUpdate(ItemOperateLog itemOperateLog)
	{
		boolean result = false;
		String sql = "insert into t_l_item_operate(`ItemOperateID`, `UserID`, `NickName`, `ItemID`, `ItemName`, `OpCount`, `OpType`, `OpTime`, `IsProfit`) values(?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `UserID`=?,`NickName`=?,`ItemID`=?,`ItemName`=?,`OpCount`=?,`OpType`=?,`OpTime`=?,`IsProfit`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,itemOperateLog.getItemOperateID());
		params.put(Types.BIGINT,itemOperateLog.getUserID());
		params.put(Types.VARCHAR,itemOperateLog.getNickName());
		params.put(Types.INTEGER,itemOperateLog.getItemID());
		params.put(Types.VARCHAR,itemOperateLog.getItemName());
		params.put(Types.INTEGER,itemOperateLog.getOpCount());
		params.put(Types.INTEGER,itemOperateLog.getOpType());
		params.put(Types.TIMESTAMP,itemOperateLog.getOpTime());
		params.put(Types.TINYINT,itemOperateLog.getIsProfit());
		params.put(Types.BIGINT,itemOperateLog.getUserID());
		params.put(Types.VARCHAR,itemOperateLog.getNickName());
		params.put(Types.INTEGER,itemOperateLog.getItemID());
		params.put(Types.VARCHAR,itemOperateLog.getItemName());
		params.put(Types.INTEGER,itemOperateLog.getOpCount());
		params.put(Types.INTEGER,itemOperateLog.getOpType());
		params.put(Types.TIMESTAMP,itemOperateLog.getOpTime());
		params.put(Types.TINYINT,itemOperateLog.getIsProfit());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean deleteByKey(Object... ids)
	{
		boolean result = false;
		String sql = "delete from t_l_item_operate where `ItemOperateID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public ItemOperateLog getByKey(Object... ids)
	{
		String sql = "select * from t_l_item_operate where `ItemOperateID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		ItemOperateLog itemOperateLog = query(sql, params);		return itemOperateLog;
	}


	@Override
	public List<ItemOperateLog> listAll()
	{
		String sql = "select * from t_l_item_operate;";
		List<ItemOperateLog> itemOperateLogs = queryList(sql);		return itemOperateLogs;
	}

	@Override
	public int[] addOrUpdateBatch(List<ItemOperateLog> itemOperateLogs)
	{
		if (itemOperateLogs == null || itemOperateLogs.isEmpty())
			return new int[1];
		String sql = "insert into t_l_item_operate(`ItemOperateID`, `UserID`, `NickName`, `ItemID`, `ItemName`, `OpCount`, `OpType`, `OpTime`, `IsProfit`) values(?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `UserID`=?,`NickName`=?,`ItemID`=?,`ItemName`=?,`OpCount`=?,`OpType`=?,`OpTime`=?,`IsProfit`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, itemOperateLogs, new DataExecutor<int[]>()
			{
				@Override
				public int[] execute(PreparedStatement statement, Object... objects) throws Exception
				{
					@SuppressWarnings("unchecked")
					List<ItemOperateLog>itemOperateLogs = (List<ItemOperateLog>)objects[0];
					for (ItemOperateLog itemOperateLog : itemOperateLogs)
					{
						DBParamWrapper params = new DBParamWrapper();
						params.put(Types.INTEGER,itemOperateLog.getItemOperateID());
						params.put(Types.BIGINT,itemOperateLog.getUserID());
						params.put(Types.VARCHAR,itemOperateLog.getNickName());
						params.put(Types.INTEGER,itemOperateLog.getItemID());
						params.put(Types.VARCHAR,itemOperateLog.getItemName());
						params.put(Types.INTEGER,itemOperateLog.getOpCount());
						params.put(Types.INTEGER,itemOperateLog.getOpType());
						params.put(Types.TIMESTAMP,itemOperateLog.getOpTime());
						params.put(Types.TINYINT,itemOperateLog.getIsProfit());
						params.put(Types.BIGINT,itemOperateLog.getUserID());
						params.put(Types.VARCHAR,itemOperateLog.getNickName());
						params.put(Types.INTEGER,itemOperateLog.getItemID());
						params.put(Types.VARCHAR,itemOperateLog.getItemName());
						params.put(Types.INTEGER,itemOperateLog.getOpCount());
						params.put(Types.INTEGER,itemOperateLog.getOpType());
						params.put(Types.TIMESTAMP,itemOperateLog.getOpTime());
						params.put(Types.TINYINT,itemOperateLog.getIsProfit());
						statement = getDBHelper().prepareCommand(statement,params.getParams());
						statement.addBatch();
					}
					return statement.executeBatch();
				}
			});
		return effectedRows;
	}

	@Override
	public int[] deleteBatch(List<ItemOperateLog> itemOperateLogs)
	{
		String sql = "delete from t_l_item_operate where `ItemOperateID`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, itemOperateLogs, new DataExecutor<int[]>()
		{
			@Override
		public int[] execute(PreparedStatement statement, Object... objects) throws Exception
		{
			
			@SuppressWarnings("unchecked")
			List<ItemOperateLog>itemOperateLogs = (List<ItemOperateLog>)objects[0];
			for (ItemOperateLog itemOperateLog : itemOperateLogs)
			{
					DBParamWrapper params = new DBParamWrapper();
					params.put(Types.INTEGER,itemOperateLog.getItemOperateID());
					statement = getDBHelper().prepareCommand(statement,params.getParams());
					statement.addBatch();
				}
				return statement.executeBatch();
			}
		});
		return effectedRows;
	}
	@Override
	public ItemOperateLog rsToEntity(ResultSet rs) throws SQLException
	{
		ItemOperateLog itemOperateLog = new ItemOperateLog();
		itemOperateLog.setItemOperateID(rs.getInt("ItemOperateID"));
		itemOperateLog.setUserID(rs.getLong("UserID"));
		itemOperateLog.setNickName(rs.getString("NickName"));
		itemOperateLog.setItemID(rs.getInt("ItemID"));
		itemOperateLog.setItemName(rs.getString("ItemName"));
		itemOperateLog.setOpCount(rs.getInt("OpCount"));
		itemOperateLog.setOpType(rs.getInt("OpType"));
		itemOperateLog.setOpTime(rs.getTimestamp("OpTime"));
		itemOperateLog.setIsProfit(rs.getBoolean("IsProfit"));
		return itemOperateLog;
	}

	@Override
	public List<ItemOperateLog> getItemOperateLogByUserID(long userID)
	{
		String sql = "select * from t_l_item_operate where `userID` = ?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(userID);
		List<ItemOperateLog> itemOperateLog = queryList(sql,params);
		return itemOperateLog;
	}

}