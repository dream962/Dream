package com.data.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.base.database.BaseDao;
import com.base.database.DBParamWrapper;
import com.base.database.DataExecutor;
import com.base.database.DataReader;
import com.base.database.pool.DBHelper;
import com.data.dao.ITablesInfoDao;
import com.data.info.TablesInfo;


public class TablesInfoDaoImpl extends BaseDao<TablesInfo> implements ITablesInfoDao
{
	public TablesInfoDaoImpl(DBHelper helper)
	{
		super(helper);
	}

	@Override
	public boolean add(TablesInfo tablesInfo)
	{
		boolean result = false;
		String sql = "insert into t_u_tables(`TableID`, `Value`) values(?, ?);";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,tablesInfo.getTableID());
		params.put(Types.INTEGER,tablesInfo.getValue());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean update(TablesInfo tablesInfo)
	{
		boolean result = false;
		String sql = "update t_u_tables set `Value`=? where `TableID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,tablesInfo.getValue());
		params.put(Types.INTEGER,tablesInfo.getTableID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean delete(TablesInfo tablesInfo)
	{
		boolean result = false;
		String sql = "delete from t_u_tables where `TableID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,tablesInfo.getTableID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean addOrUpdate(TablesInfo tablesInfo)
	{
		boolean result = false;
		String sql = "insert into t_u_tables(`TableID`, `Value`) values(?, ?) on DUPLICATE KEY update `Value`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,tablesInfo.getTableID());
		params.put(Types.INTEGER,tablesInfo.getValue());
		params.put(Types.INTEGER,tablesInfo.getValue());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean deleteByKey(Object... ids)
	{
		boolean result = false;
		String sql = "delete from t_u_tables where `TableID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public TablesInfo getByKey(Object... ids)
	{
		String sql = "select * from t_u_tables where `TableID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		TablesInfo tablesInfo = query(sql, params);		return tablesInfo;
	}


	@Override
	public List<TablesInfo> listAll()
	{
		String sql = "select * from t_u_tables;";
		List<TablesInfo> tablesInfos = queryList(sql);		return tablesInfos;
	}

	@Override
	public int[] addOrUpdateBatch(List<TablesInfo> tablesInfos)
	{
		String sql = "insert into t_u_tables(`TableID`, `Value`) values(?, ?) on DUPLICATE KEY update `Value`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, tablesInfos, new DataExecutor<int[]>()
			{
				@Override
				public int[] execute(PreparedStatement statement, Object... objects) throws Exception
				{
					@SuppressWarnings("unchecked")
					List<TablesInfo>tablesInfos = (List<TablesInfo>)objects[0];
					for (TablesInfo tablesInfo : tablesInfos)
					{
						DBParamWrapper params = new DBParamWrapper();
						params.put(Types.INTEGER,tablesInfo.getTableID());
						params.put(Types.INTEGER,tablesInfo.getValue());
						params.put(Types.INTEGER,tablesInfo.getValue());
						statement = getDBHelper().prepareCommand(statement,params.getParams());
						statement.addBatch();
					}
					return statement.executeBatch();
				}
			});
		return effectedRows;
	}

	@Override
	public int getMaxId()
	{
		String sql = "select max(`id`) from t_u_tables ;";
		Integer val = getDBHelper().executeQuery(sql, new DataReader<Integer>()
		{
			@Override
			public Integer readData(ResultSet rs, Object... objects) throws Exception{
				if(rs.next()){
					return rs.getInt(1);
				}
				return 0;
			}
		});
		return val;
	}

	@Override
	public int[] deleteBatch(List<TablesInfo> tablesInfos)
	{
		String sql = "delete from t_u_tables where `TableID`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, tablesInfos, new DataExecutor<int[]>()
		{
			@Override
		public int[] execute(PreparedStatement statement, Object... objects) throws Exception
		{
			
			@SuppressWarnings("unchecked")
			List<TablesInfo>tablesInfos = (List<TablesInfo>)objects[0];
			for (TablesInfo tablesInfo : tablesInfos)
			{
					DBParamWrapper params = new DBParamWrapper();
					params.put(Types.INTEGER,tablesInfo.getTableID());
					statement = getDBHelper().prepareCommand(statement,params.getParams());
					statement.addBatch();
				}
				return statement.executeBatch();
			}
		});
		return effectedRows;
	}
	@Override
	public TablesInfo rsToEntity(ResultSet rs) throws SQLException
	{
		TablesInfo tablesInfo = new TablesInfo();
		tablesInfo.setTableID(rs.getInt("TableID"));
		tablesInfo.setValue(rs.getInt("Value"));
		return tablesInfo;
	}

}