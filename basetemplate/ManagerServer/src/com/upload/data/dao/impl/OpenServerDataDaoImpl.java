package com.upload.data.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.upload.data.dao.IOpenServerDataDao;
import com.upload.data.data.OpenServerData;
import com.upload.database.BaseDao;
import com.upload.database.DBParamWrapper;
import com.upload.database.DataExecutor;
import com.upload.database.pool.DBHelper;


public class OpenServerDataDaoImpl extends BaseDao<OpenServerData> implements IOpenServerDataDao
{
	public OpenServerDataDaoImpl(DBHelper helper)
	{
		super(helper);
	}


	@Override
	public boolean add(OpenServerData openServerData)
	{
		boolean result = false;
		String sql = "insert into t_p_open_server(`ServerID`, `OpenTime`, `MonitorServerID`, `MonitorCondition1`, `MonitorCondition2`, `BeginTime`, `EndTime`, `Operator`, `CreateTime`, `OpenState`, `Message`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,openServerData.getServerID());
		params.put(Types.TIMESTAMP,openServerData.getOpenTime());
		params.put(Types.INTEGER,openServerData.getMonitorServerID());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition1());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition2());
		params.put(Types.VARCHAR,openServerData.getBeginTime());
		params.put(Types.VARCHAR,openServerData.getEndTime());
		params.put(Types.VARCHAR,openServerData.getOperator());
		params.put(Types.TIMESTAMP,openServerData.getCreateTime());
		params.put(Types.INTEGER,openServerData.getOpenState());
		params.put(Types.VARCHAR,openServerData.getMessage());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean update(OpenServerData openServerData)
	{
		boolean result = false;
		String sql = "update t_p_open_server set `OpenTime`=?, `MonitorServerID`=?, `MonitorCondition1`=?, `MonitorCondition2`=?, `BeginTime`=?, `EndTime`=?, `Operator`=?, `CreateTime`=?, `OpenState`=?, `Message`=? where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.TIMESTAMP,openServerData.getOpenTime());
		params.put(Types.INTEGER,openServerData.getMonitorServerID());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition1());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition2());
		params.put(Types.VARCHAR,openServerData.getBeginTime());
		params.put(Types.VARCHAR,openServerData.getEndTime());
		params.put(Types.VARCHAR,openServerData.getOperator());
		params.put(Types.TIMESTAMP,openServerData.getCreateTime());
		params.put(Types.INTEGER,openServerData.getOpenState());
		params.put(Types.VARCHAR,openServerData.getMessage());
		params.put(Types.INTEGER,openServerData.getServerID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean delete(OpenServerData openServerData)
	{
		boolean result = false;
		String sql = "delete from t_p_open_server where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,openServerData.getServerID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean addOrUpdate(OpenServerData openServerData)
	{
		boolean result = false;
		String sql = "insert into t_p_open_server(`ServerID`, `OpenTime`, `MonitorServerID`, `MonitorCondition1`, `MonitorCondition2`, `BeginTime`, `EndTime`, `Operator`, `CreateTime`, `OpenState`, `Message`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `OpenTime`=?,`MonitorServerID`=?,`MonitorCondition1`=?,`MonitorCondition2`=?,`BeginTime`=?,`EndTime`=?,`Operator`=?,`CreateTime`=?,`OpenState`=?,`Message`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,openServerData.getServerID());
		params.put(Types.TIMESTAMP,openServerData.getOpenTime());
		params.put(Types.INTEGER,openServerData.getMonitorServerID());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition1());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition2());
		params.put(Types.VARCHAR,openServerData.getBeginTime());
		params.put(Types.VARCHAR,openServerData.getEndTime());
		params.put(Types.VARCHAR,openServerData.getOperator());
		params.put(Types.TIMESTAMP,openServerData.getCreateTime());
		params.put(Types.INTEGER,openServerData.getOpenState());
		params.put(Types.VARCHAR,openServerData.getMessage());
		params.put(Types.TIMESTAMP,openServerData.getOpenTime());
		params.put(Types.INTEGER,openServerData.getMonitorServerID());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition1());
		params.put(Types.VARCHAR,openServerData.getMonitorCondition2());
		params.put(Types.VARCHAR,openServerData.getBeginTime());
		params.put(Types.VARCHAR,openServerData.getEndTime());
		params.put(Types.VARCHAR,openServerData.getOperator());
		params.put(Types.TIMESTAMP,openServerData.getCreateTime());
		params.put(Types.INTEGER,openServerData.getOpenState());
		params.put(Types.VARCHAR,openServerData.getMessage());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean deleteByKey(Object... ids)
	{
		boolean result = false;
		String sql = "delete from t_p_open_server where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public OpenServerData getByKey(Object... ids)
	{
		String sql = "select * from t_p_open_server where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		OpenServerData openServerData = query(sql, params);		return openServerData;
	}


	@Override
	public List<OpenServerData> listAll()
	{
		String sql = "select * from t_p_open_server;";
		List<OpenServerData> openServerDatas = queryList(sql);		return openServerDatas;
	}

	@Override
	public int[] addOrUpdateBatch(List<OpenServerData> openServerDatas)
	{
		String sql = "insert into t_p_open_server(`ServerID`, `OpenTime`, `MonitorServerID`, `MonitorCondition1`, `MonitorCondition2`, `BeginTime`, `EndTime`, `Operator`, `CreateTime`, `OpenState`, `Message`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `OpenTime`=?,`MonitorServerID`=?,`MonitorCondition1`=?,`MonitorCondition2`=?,`BeginTime`=?,`EndTime`=?,`Operator`=?,`CreateTime`=?,`OpenState`=?,`Message`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, openServerDatas, new DataExecutor<int[]>()
			{
				@Override
				public int[] execute(PreparedStatement statement, Object... objects) throws Exception
				{
					@SuppressWarnings("unchecked")
					List<OpenServerData>openServerDatas = (List<OpenServerData>)objects[0];
					for (OpenServerData openServerData : openServerDatas)
					{
						DBParamWrapper params = new DBParamWrapper();
						params.put(Types.INTEGER,openServerData.getServerID());
						params.put(Types.TIMESTAMP,openServerData.getOpenTime());
						params.put(Types.INTEGER,openServerData.getMonitorServerID());
						params.put(Types.VARCHAR,openServerData.getMonitorCondition1());
						params.put(Types.VARCHAR,openServerData.getMonitorCondition2());
						params.put(Types.VARCHAR,openServerData.getBeginTime());
						params.put(Types.VARCHAR,openServerData.getEndTime());
						params.put(Types.VARCHAR,openServerData.getOperator());
						params.put(Types.TIMESTAMP,openServerData.getCreateTime());
						params.put(Types.INTEGER,openServerData.getOpenState());
						params.put(Types.VARCHAR,openServerData.getMessage());
						params.put(Types.TIMESTAMP,openServerData.getOpenTime());
						params.put(Types.INTEGER,openServerData.getMonitorServerID());
						params.put(Types.VARCHAR,openServerData.getMonitorCondition1());
						params.put(Types.VARCHAR,openServerData.getMonitorCondition2());
						params.put(Types.VARCHAR,openServerData.getBeginTime());
						params.put(Types.VARCHAR,openServerData.getEndTime());
						params.put(Types.VARCHAR,openServerData.getOperator());
						params.put(Types.TIMESTAMP,openServerData.getCreateTime());
						params.put(Types.INTEGER,openServerData.getOpenState());
						params.put(Types.VARCHAR,openServerData.getMessage());
						statement = getDBHelper().prepareCommand(statement,params.getParams());
						statement.addBatch();
					}
					return statement.executeBatch();
				}
			});
		return effectedRows;
	}

	@Override
	public int[] deleteBatch(List<OpenServerData> openServerDatas)
	{
		String sql = "delete from t_p_open_server where `ServerID`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, openServerDatas, new DataExecutor<int[]>()
		{
			@Override
		public int[] execute(PreparedStatement statement, Object... objects) throws Exception
		{
			
			@SuppressWarnings("unchecked")
			List<OpenServerData>openServerDatas = (List<OpenServerData>)objects[0];
			for (OpenServerData openServerData : openServerDatas)
			{
					DBParamWrapper params = new DBParamWrapper();
					params.put(Types.INTEGER,openServerData.getServerID());
					statement = getDBHelper().prepareCommand(statement,params.getParams());
					statement.addBatch();
				}
				return statement.executeBatch();
			}
		});
		return effectedRows;
	}
	@Override
	public OpenServerData rsToEntity(ResultSet rs) throws SQLException
	{
		OpenServerData openServerData = new OpenServerData();
		openServerData.setServerID(rs.getInt("ServerID"));
		openServerData.setOpenTime(rs.getTimestamp("OpenTime"));
		openServerData.setMonitorServerID(rs.getInt("MonitorServerID"));
		openServerData.setMonitorCondition1(rs.getString("MonitorCondition1"));
		openServerData.setMonitorCondition2(rs.getString("MonitorCondition2"));
		openServerData.setBeginTime(rs.getString("BeginTime"));
		openServerData.setEndTime(rs.getString("EndTime"));
		openServerData.setOperator(rs.getString("Operator"));
		openServerData.setCreateTime(rs.getTimestamp("CreateTime"));
		openServerData.setOpenState(rs.getInt("OpenState"));
		openServerData.setMessage(rs.getString("Message"));
		return openServerData;
	}

}