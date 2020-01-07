package com.upload.data.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import com.upload.data.dao.IServerListDataDao;
import com.upload.data.data.ServerListData;
import com.upload.database.BaseDao;
import com.upload.database.DBParamWrapper;
import com.upload.database.DataExecutor;
import com.upload.database.pool.DBHelper;


public class ServerListDataDaoImpl extends BaseDao<ServerListData> implements IServerListDataDao
{
	public ServerListDataDaoImpl(DBHelper helper)
	{
		super(helper);
	}


	@Override
	public boolean add(ServerListData serverListData)
	{
		boolean result = false;
		String sql = "insert into t_p_server_list(`ServerID`, `ServerName`, `ServerGroup`, `OutState`, `ServerIp`, `Type`, `GamePort`, `GatePort`, `Status`, `GateStatus`, `ServerAddress`, `GateAddress`, `LastUpdateTime`, `LastOpenTime`, `DbUsername`, `DbPassword`, `DbIp`, `DbPort`, `DbGameName`, `DbLogName`, `DbMartName`, `DbAddress`, `DbStartAddress`, `SSHUsername`, `SSHPassword`, `SSHPort`, `SSHKeyPath`, `SSHKeyPassword`, `Tmux`, `Remarks`, `RedisIp`, `RedisPort`, `RedisAddress`, `RedisStartAddress`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,serverListData.getServerID());
		params.put(Types.VARCHAR,serverListData.getServerName());
		params.put(Types.INTEGER,serverListData.getServerGroup());
		params.put(Types.INTEGER,serverListData.getOutState());
		params.put(Types.VARCHAR,serverListData.getServerIp());
		params.put(Types.INTEGER,serverListData.getType());
		params.put(Types.INTEGER,serverListData.getGamePort());
		params.put(Types.INTEGER,serverListData.getGatePort());
		params.put(Types.INTEGER,serverListData.getStatus());
		params.put(Types.INTEGER,serverListData.getGateStatus());
		params.put(Types.VARCHAR,serverListData.getServerAddress());
		params.put(Types.VARCHAR,serverListData.getGateAddress());
		params.put(Types.TIMESTAMP,serverListData.getLastUpdateTime());
		params.put(Types.TIMESTAMP,serverListData.getLastOpenTime());
		params.put(Types.VARCHAR,serverListData.getDbUsername());
		params.put(Types.VARCHAR,serverListData.getDbPassword());
		params.put(Types.VARCHAR,serverListData.getDbIp());
		params.put(Types.VARCHAR,serverListData.getDbPort());
		params.put(Types.VARCHAR,serverListData.getDbGameName());
		params.put(Types.VARCHAR,serverListData.getDbLogName());
		params.put(Types.VARCHAR,serverListData.getDbMartName());
		params.put(Types.VARCHAR,serverListData.getDbAddress());
		params.put(Types.VARCHAR,serverListData.getDbStartAddress());
		params.put(Types.VARCHAR,serverListData.getSSHUsername());
		params.put(Types.VARCHAR,serverListData.getSSHPassword());
		params.put(Types.INTEGER,serverListData.getSSHPort());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPath());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPassword());
		params.put(Types.VARCHAR,serverListData.getTmux());
		params.put(Types.VARCHAR,serverListData.getRemarks());
		params.put(Types.VARCHAR,serverListData.getRedisIp());
		params.put(Types.VARCHAR,serverListData.getRedisPort());
		params.put(Types.VARCHAR,serverListData.getRedisAddress());
		params.put(Types.VARCHAR,serverListData.getRedisStartAddress());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean update(ServerListData serverListData)
	{
		boolean result = false;
		String sql = "update t_p_server_list set `ServerName`=?, `ServerGroup`=?, `OutState`=?, `ServerIp`=?, `Type`=?, `GamePort`=?, `GatePort`=?, `Status`=?, `GateStatus`=?, `ServerAddress`=?, `GateAddress`=?, `LastUpdateTime`=?, `LastOpenTime`=?, `DbUsername`=?, `DbPassword`=?, `DbIp`=?, `DbPort`=?, `DbGameName`=?, `DbLogName`=?, `DbMartName`=?, `DbAddress`=?, `DbStartAddress`=?, `SSHUsername`=?, `SSHPassword`=?, `SSHPort`=?, `SSHKeyPath`=?, `SSHKeyPassword`=?, `Tmux`=?, `Remarks`=?, `RedisIp`=?, `RedisPort`=?, `RedisAddress`=?, `RedisStartAddress`=? where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR,serverListData.getServerName());
		params.put(Types.INTEGER,serverListData.getServerGroup());
		params.put(Types.INTEGER,serverListData.getOutState());
		params.put(Types.VARCHAR,serverListData.getServerIp());
		params.put(Types.INTEGER,serverListData.getType());
		params.put(Types.INTEGER,serverListData.getGamePort());
		params.put(Types.INTEGER,serverListData.getGatePort());
		params.put(Types.INTEGER,serverListData.getStatus());
		params.put(Types.INTEGER,serverListData.getGateStatus());
		params.put(Types.VARCHAR,serverListData.getServerAddress());
		params.put(Types.VARCHAR,serverListData.getGateAddress());
		params.put(Types.TIMESTAMP,serverListData.getLastUpdateTime());
		params.put(Types.TIMESTAMP,serverListData.getLastOpenTime());
		params.put(Types.VARCHAR,serverListData.getDbUsername());
		params.put(Types.VARCHAR,serverListData.getDbPassword());
		params.put(Types.VARCHAR,serverListData.getDbIp());
		params.put(Types.VARCHAR,serverListData.getDbPort());
		params.put(Types.VARCHAR,serverListData.getDbGameName());
		params.put(Types.VARCHAR,serverListData.getDbLogName());
		params.put(Types.VARCHAR,serverListData.getDbMartName());
		params.put(Types.VARCHAR,serverListData.getDbAddress());
		params.put(Types.VARCHAR,serverListData.getDbStartAddress());
		params.put(Types.VARCHAR,serverListData.getSSHUsername());
		params.put(Types.VARCHAR,serverListData.getSSHPassword());
		params.put(Types.INTEGER,serverListData.getSSHPort());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPath());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPassword());
		params.put(Types.VARCHAR,serverListData.getTmux());
		params.put(Types.VARCHAR,serverListData.getRemarks());
		params.put(Types.VARCHAR,serverListData.getRedisIp());
		params.put(Types.VARCHAR,serverListData.getRedisPort());
		params.put(Types.VARCHAR,serverListData.getRedisAddress());
		params.put(Types.VARCHAR,serverListData.getRedisStartAddress());
		params.put(Types.INTEGER,serverListData.getServerID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean delete(ServerListData serverListData)
	{
		boolean result = false;
		String sql = "delete from t_p_server_list where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,serverListData.getServerID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean addOrUpdate(ServerListData serverListData)
	{
		boolean result = false;
		String sql = "insert into t_p_server_list(`ServerID`, `ServerName`, `ServerGroup`, `OutState`, `ServerIp`, `Type`, `GamePort`, `GatePort`, `Status`, `GateStatus`, `ServerAddress`, `GateAddress`, `LastUpdateTime`, `LastOpenTime`, `DbUsername`, `DbPassword`, `DbIp`, `DbPort`, `DbGameName`, `DbLogName`, `DbMartName`, `DbAddress`, `DbStartAddress`, `SSHUsername`, `SSHPassword`, `SSHPort`, `SSHKeyPath`, `SSHKeyPassword`, `Tmux`, `Remarks`, `RedisIp`, `RedisPort`, `RedisAddress`, `RedisStartAddress`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `ServerName`=?,`ServerGroup`=?,`OutState`=?,`ServerIp`=?,`Type`=?,`GamePort`=?,`GatePort`=?,`Status`=?,`GateStatus`=?,`ServerAddress`=?,`GateAddress`=?,`LastUpdateTime`=?,`LastOpenTime`=?,`DbUsername`=?,`DbPassword`=?,`DbIp`=?,`DbPort`=?,`DbGameName`=?,`DbLogName`=?,`DbMartName`=?,`DbAddress`=?,`DbStartAddress`=?,`SSHUsername`=?,`SSHPassword`=?,`SSHPort`=?,`SSHKeyPath`=?,`SSHKeyPassword`=?,`Tmux`=?,`Remarks`=?,`RedisIp`=?,`RedisPort`=?,`RedisAddress`=?,`RedisStartAddress`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,serverListData.getServerID());
		params.put(Types.VARCHAR,serverListData.getServerName());
		params.put(Types.INTEGER,serverListData.getServerGroup());
		params.put(Types.INTEGER,serverListData.getOutState());
		params.put(Types.VARCHAR,serverListData.getServerIp());
		params.put(Types.INTEGER,serverListData.getType());
		params.put(Types.INTEGER,serverListData.getGamePort());
		params.put(Types.INTEGER,serverListData.getGatePort());
		params.put(Types.INTEGER,serverListData.getStatus());
		params.put(Types.INTEGER,serverListData.getGateStatus());
		params.put(Types.VARCHAR,serverListData.getServerAddress());
		params.put(Types.VARCHAR,serverListData.getGateAddress());
		params.put(Types.TIMESTAMP,serverListData.getLastUpdateTime());
		params.put(Types.TIMESTAMP,serverListData.getLastOpenTime());
		params.put(Types.VARCHAR,serverListData.getDbUsername());
		params.put(Types.VARCHAR,serverListData.getDbPassword());
		params.put(Types.VARCHAR,serverListData.getDbIp());
		params.put(Types.VARCHAR,serverListData.getDbPort());
		params.put(Types.VARCHAR,serverListData.getDbGameName());
		params.put(Types.VARCHAR,serverListData.getDbLogName());
		params.put(Types.VARCHAR,serverListData.getDbMartName());
		params.put(Types.VARCHAR,serverListData.getDbAddress());
		params.put(Types.VARCHAR,serverListData.getDbStartAddress());
		params.put(Types.VARCHAR,serverListData.getSSHUsername());
		params.put(Types.VARCHAR,serverListData.getSSHPassword());
		params.put(Types.INTEGER,serverListData.getSSHPort());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPath());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPassword());
		params.put(Types.VARCHAR,serverListData.getTmux());
		params.put(Types.VARCHAR,serverListData.getRemarks());
		params.put(Types.VARCHAR,serverListData.getRedisIp());
		params.put(Types.VARCHAR,serverListData.getRedisPort());
		params.put(Types.VARCHAR,serverListData.getRedisAddress());
		params.put(Types.VARCHAR,serverListData.getRedisStartAddress());
		params.put(Types.VARCHAR,serverListData.getServerName());
		params.put(Types.INTEGER,serverListData.getServerGroup());
		params.put(Types.INTEGER,serverListData.getOutState());
		params.put(Types.VARCHAR,serverListData.getServerIp());
		params.put(Types.INTEGER,serverListData.getType());
		params.put(Types.INTEGER,serverListData.getGamePort());
		params.put(Types.INTEGER,serverListData.getGatePort());
		params.put(Types.INTEGER,serverListData.getStatus());
		params.put(Types.INTEGER,serverListData.getGateStatus());
		params.put(Types.VARCHAR,serverListData.getServerAddress());
		params.put(Types.VARCHAR,serverListData.getGateAddress());
		params.put(Types.TIMESTAMP,serverListData.getLastUpdateTime());
		params.put(Types.TIMESTAMP,serverListData.getLastOpenTime());
		params.put(Types.VARCHAR,serverListData.getDbUsername());
		params.put(Types.VARCHAR,serverListData.getDbPassword());
		params.put(Types.VARCHAR,serverListData.getDbIp());
		params.put(Types.VARCHAR,serverListData.getDbPort());
		params.put(Types.VARCHAR,serverListData.getDbGameName());
		params.put(Types.VARCHAR,serverListData.getDbLogName());
		params.put(Types.VARCHAR,serverListData.getDbMartName());
		params.put(Types.VARCHAR,serverListData.getDbAddress());
		params.put(Types.VARCHAR,serverListData.getDbStartAddress());
		params.put(Types.VARCHAR,serverListData.getSSHUsername());
		params.put(Types.VARCHAR,serverListData.getSSHPassword());
		params.put(Types.INTEGER,serverListData.getSSHPort());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPath());
		params.put(Types.VARCHAR,serverListData.getSSHKeyPassword());
		params.put(Types.VARCHAR,serverListData.getTmux());
		params.put(Types.VARCHAR,serverListData.getRemarks());
		params.put(Types.VARCHAR,serverListData.getRedisIp());
		params.put(Types.VARCHAR,serverListData.getRedisPort());
		params.put(Types.VARCHAR,serverListData.getRedisAddress());
		params.put(Types.VARCHAR,serverListData.getRedisStartAddress());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean deleteByKey(Object... ids)
	{
		boolean result = false;
		String sql = "delete from t_p_server_list where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public ServerListData getByKey(Object... ids)
	{
		String sql = "select * from t_p_server_list where `ServerID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		ServerListData serverListData = query(sql, params);		return serverListData;
	}


	@Override
	public List<ServerListData> listAll()
	{
		String sql = "select * from t_p_server_list;";
		List<ServerListData> serverListDatas = queryList(sql);		return serverListDatas;
	}

	@Override
	public int[] addOrUpdateBatch(List<ServerListData> serverListDatas)
	{
		String sql = "insert into t_p_server_list(`ServerID`, `ServerName`, `ServerGroup`, `OutState`, `ServerIp`, `Type`, `GamePort`, `GatePort`, `Status`, `GateStatus`, `ServerAddress`, `GateAddress`, `LastUpdateTime`, `LastOpenTime`, `DbUsername`, `DbPassword`, `DbIp`, `DbPort`, `DbGameName`, `DbLogName`, `DbMartName`, `DbAddress`, `DbStartAddress`, `SSHUsername`, `SSHPassword`, `SSHPort`, `SSHKeyPath`, `SSHKeyPassword`, `Tmux`, `Remarks`, `RedisIp`, `RedisPort`, `RedisAddress`, `RedisStartAddress`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `ServerName`=?,`ServerGroup`=?,`OutState`=?,`ServerIp`=?,`Type`=?,`GamePort`=?,`GatePort`=?,`Status`=?,`GateStatus`=?,`ServerAddress`=?,`GateAddress`=?,`LastUpdateTime`=?,`LastOpenTime`=?,`DbUsername`=?,`DbPassword`=?,`DbIp`=?,`DbPort`=?,`DbGameName`=?,`DbLogName`=?,`DbMartName`=?,`DbAddress`=?,`DbStartAddress`=?,`SSHUsername`=?,`SSHPassword`=?,`SSHPort`=?,`SSHKeyPath`=?,`SSHKeyPassword`=?,`Tmux`=?,`Remarks`=?,`RedisIp`=?,`RedisPort`=?,`RedisAddress`=?,`RedisStartAddress`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, serverListDatas, new DataExecutor<int[]>()
			{
				@Override
				public int[] execute(PreparedStatement statement, Object... objects) throws Exception
				{
					@SuppressWarnings("unchecked")
					List<ServerListData>serverListDatas = (List<ServerListData>)objects[0];
					for (ServerListData serverListData : serverListDatas)
					{
						DBParamWrapper params = new DBParamWrapper();
						params.put(Types.INTEGER,serverListData.getServerID());
						params.put(Types.VARCHAR,serverListData.getServerName());
						params.put(Types.INTEGER,serverListData.getServerGroup());
						params.put(Types.INTEGER,serverListData.getOutState());
						params.put(Types.VARCHAR,serverListData.getServerIp());
						params.put(Types.INTEGER,serverListData.getType());
						params.put(Types.INTEGER,serverListData.getGamePort());
						params.put(Types.INTEGER,serverListData.getGatePort());
						params.put(Types.INTEGER,serverListData.getStatus());
						params.put(Types.INTEGER,serverListData.getGateStatus());
						params.put(Types.VARCHAR,serverListData.getServerAddress());
						params.put(Types.VARCHAR,serverListData.getGateAddress());
						params.put(Types.TIMESTAMP,serverListData.getLastUpdateTime());
						params.put(Types.TIMESTAMP,serverListData.getLastOpenTime());
						params.put(Types.VARCHAR,serverListData.getDbUsername());
						params.put(Types.VARCHAR,serverListData.getDbPassword());
						params.put(Types.VARCHAR,serverListData.getDbIp());
						params.put(Types.VARCHAR,serverListData.getDbPort());
						params.put(Types.VARCHAR,serverListData.getDbGameName());
						params.put(Types.VARCHAR,serverListData.getDbLogName());
						params.put(Types.VARCHAR,serverListData.getDbMartName());
						params.put(Types.VARCHAR,serverListData.getDbAddress());
						params.put(Types.VARCHAR,serverListData.getDbStartAddress());
						params.put(Types.VARCHAR,serverListData.getSSHUsername());
						params.put(Types.VARCHAR,serverListData.getSSHPassword());
						params.put(Types.INTEGER,serverListData.getSSHPort());
						params.put(Types.VARCHAR,serverListData.getSSHKeyPath());
						params.put(Types.VARCHAR,serverListData.getSSHKeyPassword());
						params.put(Types.VARCHAR,serverListData.getTmux());
						params.put(Types.VARCHAR,serverListData.getRemarks());
						params.put(Types.VARCHAR,serverListData.getRedisIp());
						params.put(Types.VARCHAR,serverListData.getRedisPort());
						params.put(Types.VARCHAR,serverListData.getRedisAddress());
						params.put(Types.VARCHAR,serverListData.getRedisStartAddress());
						params.put(Types.VARCHAR,serverListData.getServerName());
						params.put(Types.INTEGER,serverListData.getServerGroup());
						params.put(Types.INTEGER,serverListData.getOutState());
						params.put(Types.VARCHAR,serverListData.getServerIp());
						params.put(Types.INTEGER,serverListData.getType());
						params.put(Types.INTEGER,serverListData.getGamePort());
						params.put(Types.INTEGER,serverListData.getGatePort());
						params.put(Types.INTEGER,serverListData.getStatus());
						params.put(Types.INTEGER,serverListData.getGateStatus());
						params.put(Types.VARCHAR,serverListData.getServerAddress());
						params.put(Types.VARCHAR,serverListData.getGateAddress());
						params.put(Types.TIMESTAMP,serverListData.getLastUpdateTime());
						params.put(Types.TIMESTAMP,serverListData.getLastOpenTime());
						params.put(Types.VARCHAR,serverListData.getDbUsername());
						params.put(Types.VARCHAR,serverListData.getDbPassword());
						params.put(Types.VARCHAR,serverListData.getDbIp());
						params.put(Types.VARCHAR,serverListData.getDbPort());
						params.put(Types.VARCHAR,serverListData.getDbGameName());
						params.put(Types.VARCHAR,serverListData.getDbLogName());
						params.put(Types.VARCHAR,serverListData.getDbMartName());
						params.put(Types.VARCHAR,serverListData.getDbAddress());
						params.put(Types.VARCHAR,serverListData.getDbStartAddress());
						params.put(Types.VARCHAR,serverListData.getSSHUsername());
						params.put(Types.VARCHAR,serverListData.getSSHPassword());
						params.put(Types.INTEGER,serverListData.getSSHPort());
						params.put(Types.VARCHAR,serverListData.getSSHKeyPath());
						params.put(Types.VARCHAR,serverListData.getSSHKeyPassword());
						params.put(Types.VARCHAR,serverListData.getTmux());
						params.put(Types.VARCHAR,serverListData.getRemarks());
						params.put(Types.VARCHAR,serverListData.getRedisIp());
						params.put(Types.VARCHAR,serverListData.getRedisPort());
						params.put(Types.VARCHAR,serverListData.getRedisAddress());
						params.put(Types.VARCHAR,serverListData.getRedisStartAddress());
						statement = getDBHelper().prepareCommand(statement,params.getParams());
						statement.addBatch();
					}
					return statement.executeBatch();
				}
			});
		return effectedRows;
	}

	@Override
	public int[] deleteBatch(List<ServerListData> serverListDatas)
	{
		String sql = "delete from t_p_server_list where `ServerID`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, serverListDatas, new DataExecutor<int[]>()
		{
			@Override
		public int[] execute(PreparedStatement statement, Object... objects) throws Exception
		{
			
			@SuppressWarnings("unchecked")
			List<ServerListData>serverListDatas = (List<ServerListData>)objects[0];
			for (ServerListData serverListData : serverListDatas)
			{
					DBParamWrapper params = new DBParamWrapper();
					params.put(Types.INTEGER,serverListData.getServerID());
					statement = getDBHelper().prepareCommand(statement,params.getParams());
					statement.addBatch();
				}
				return statement.executeBatch();
			}
		});
		return effectedRows;
	}
	@Override
	public ServerListData rsToEntity(ResultSet rs) throws SQLException
	{
		ServerListData serverListData = new ServerListData();
		serverListData.setServerID(rs.getInt("ServerID"));
		serverListData.setServerName(rs.getString("ServerName"));
		serverListData.setServerGroup(rs.getInt("ServerGroup"));
		serverListData.setOutState(rs.getInt("OutState"));
		serverListData.setServerIp(rs.getString("ServerIp"));
		serverListData.setType(rs.getInt("Type"));
		serverListData.setGamePort(rs.getInt("GamePort"));
		serverListData.setGatePort(rs.getInt("GatePort"));
		serverListData.setStatus(rs.getInt("Status"));
		serverListData.setGateStatus(rs.getInt("GateStatus"));
		serverListData.setServerAddress(rs.getString("ServerAddress"));
		serverListData.setGateAddress(rs.getString("GateAddress"));
		serverListData.setLastUpdateTime(rs.getTimestamp("LastUpdateTime"));
		serverListData.setLastOpenTime(rs.getTimestamp("LastOpenTime"));
		serverListData.setDbUsername(rs.getString("DbUsername"));
		serverListData.setDbPassword(rs.getString("DbPassword"));
		serverListData.setDbIp(rs.getString("DbIp"));
		serverListData.setDbPort(rs.getString("DbPort"));
		serverListData.setDbGameName(rs.getString("DbGameName"));
		serverListData.setDbLogName(rs.getString("DbLogName"));
		serverListData.setDbMartName(rs.getString("DbMartName"));
		serverListData.setDbAddress(rs.getString("DbAddress"));
		serverListData.setDbStartAddress(rs.getString("DbStartAddress"));
		serverListData.setSSHUsername(rs.getString("SSHUsername"));
		serverListData.setSSHPassword(rs.getString("SSHPassword"));
		serverListData.setSSHPort(rs.getInt("SSHPort"));
		serverListData.setSSHKeyPath(rs.getString("SSHKeyPath"));
		serverListData.setSSHKeyPassword(rs.getString("SSHKeyPassword"));
		serverListData.setTmux(rs.getString("Tmux"));
		serverListData.setRemarks(rs.getString("Remarks"));
		serverListData.setRedisIp(rs.getString("RedisIp"));
		serverListData.setRedisPort(rs.getString("RedisPort"));
		serverListData.setRedisAddress(rs.getString("RedisAddress"));
		serverListData.setRedisStartAddress(rs.getString("RedisStartAddress"));
		return serverListData;
	}

}