package com.data.dao.impl;

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


import com.data.dao.IPlayerInfoDao;
import com.data.info.PlayerInfo;



/**
 * This file is generated by system automatically.Don't Modify It.
 *
 * @author System
 */
public class PlayerInfoDaoImpl extends BaseDao<PlayerInfo> implements IPlayerInfoDao
{
	public PlayerInfoDaoImpl(DBHelper helper)
	{
		super(helper);
	}


	@Override
	public boolean add(PlayerInfo playerInfo)
	{
		boolean result = false;
		String sql = "insert into t_u_player(`UserID`, `PlayerName`, `AccountID`, `AccountName`, `AccuntGName`, `OpenID`, `CreateTime`, `LastLoginTime`, `Gold`, `Diamond`, `Money`, `HeaderID`, `AdExpireTime`, `WinCount`, `FailCount`, `RoleTypes`, `Modes`, `Theme`, `Headers`, `TimeTopScore`, `RaceTopScore`, `EndTopScore`, `TopLength`, `DonateValue`, `isCanUnlockCoinModeByAD`, `LastAdTime`, `AdTriggerCount`, `BuyAdTime`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,playerInfo.getUserID());
		params.put(Types.VARCHAR,playerInfo.getPlayerName());
		params.put(Types.BIGINT,playerInfo.getAccountID());
		params.put(Types.VARCHAR,playerInfo.getAccountName());
		params.put(Types.VARCHAR,playerInfo.getAccuntGName());
		params.put(Types.VARCHAR,playerInfo.getOpenID());
		params.put(Types.TIMESTAMP,playerInfo.getCreateTime());
		params.put(Types.TIMESTAMP,playerInfo.getLastLoginTime());
		params.put(Types.INTEGER,playerInfo.getGold());
		params.put(Types.INTEGER,playerInfo.getDiamond());
		params.put(Types.INTEGER,playerInfo.getMoney());
		params.put(Types.INTEGER,playerInfo.getHeaderID());
		params.put(Types.TIMESTAMP,playerInfo.getAdExpireTime());
		params.put(Types.INTEGER,playerInfo.getWinCount());
		params.put(Types.INTEGER,playerInfo.getFailCount());
		params.put(Types.VARCHAR,playerInfo.getRoleTypes());
		params.put(Types.VARCHAR,playerInfo.getModes());
		params.put(Types.VARCHAR,playerInfo.getTheme());
		params.put(Types.VARCHAR,playerInfo.getHeaders());
		params.put(Types.INTEGER,playerInfo.getTimeTopScore());
		params.put(Types.INTEGER,playerInfo.getRaceTopScore());
		params.put(Types.INTEGER,playerInfo.getEndTopScore());
		params.put(Types.INTEGER,playerInfo.getTopLength());
		params.put(Types.INTEGER,playerInfo.getDonateValue());
		params.put(Types.TINYINT,playerInfo.getIsCanUnlockCoinModeByAD());
		params.put(Types.TIMESTAMP,playerInfo.getLastAdTime());
		params.put(Types.INTEGER,playerInfo.getAdTriggerCount());
		params.put(Types.TIMESTAMP,playerInfo.getBuyAdTime());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean update(PlayerInfo playerInfo)
	{
		boolean result = false;
		String sql = "update t_u_player set `PlayerName`=?, `AccountID`=?, `AccountName`=?, `AccuntGName`=?, `OpenID`=?, `CreateTime`=?, `LastLoginTime`=?, `Gold`=?, `Diamond`=?, `Money`=?, `HeaderID`=?, `AdExpireTime`=?, `WinCount`=?, `FailCount`=?, `RoleTypes`=?, `Modes`=?, `Theme`=?, `Headers`=?, `TimeTopScore`=?, `RaceTopScore`=?, `EndTopScore`=?, `TopLength`=?, `DonateValue`=?, `isCanUnlockCoinModeByAD`=?, `LastAdTime`=?, `AdTriggerCount`=?, `BuyAdTime`=? where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR,playerInfo.getPlayerName());
		params.put(Types.BIGINT,playerInfo.getAccountID());
		params.put(Types.VARCHAR,playerInfo.getAccountName());
		params.put(Types.VARCHAR,playerInfo.getAccuntGName());
		params.put(Types.VARCHAR,playerInfo.getOpenID());
		params.put(Types.TIMESTAMP,playerInfo.getCreateTime());
		params.put(Types.TIMESTAMP,playerInfo.getLastLoginTime());
		params.put(Types.INTEGER,playerInfo.getGold());
		params.put(Types.INTEGER,playerInfo.getDiamond());
		params.put(Types.INTEGER,playerInfo.getMoney());
		params.put(Types.INTEGER,playerInfo.getHeaderID());
		params.put(Types.TIMESTAMP,playerInfo.getAdExpireTime());
		params.put(Types.INTEGER,playerInfo.getWinCount());
		params.put(Types.INTEGER,playerInfo.getFailCount());
		params.put(Types.VARCHAR,playerInfo.getRoleTypes());
		params.put(Types.VARCHAR,playerInfo.getModes());
		params.put(Types.VARCHAR,playerInfo.getTheme());
		params.put(Types.VARCHAR,playerInfo.getHeaders());
		params.put(Types.INTEGER,playerInfo.getTimeTopScore());
		params.put(Types.INTEGER,playerInfo.getRaceTopScore());
		params.put(Types.INTEGER,playerInfo.getEndTopScore());
		params.put(Types.INTEGER,playerInfo.getTopLength());
		params.put(Types.INTEGER,playerInfo.getDonateValue());
		params.put(Types.TINYINT,playerInfo.getIsCanUnlockCoinModeByAD());
		params.put(Types.TIMESTAMP,playerInfo.getLastAdTime());
		params.put(Types.INTEGER,playerInfo.getAdTriggerCount());
		params.put(Types.TIMESTAMP,playerInfo.getBuyAdTime());
		params.put(Types.INTEGER,playerInfo.getUserID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean delete(PlayerInfo playerInfo)
	{
		boolean result = false;
		String sql = "delete from t_u_player where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,playerInfo.getUserID());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean addOrUpdate(PlayerInfo playerInfo)
	{
		boolean result = false;
		String sql = "insert into t_u_player(`UserID`, `PlayerName`, `AccountID`, `AccountName`, `AccuntGName`, `OpenID`, `CreateTime`, `LastLoginTime`, `Gold`, `Diamond`, `Money`, `HeaderID`, `AdExpireTime`, `WinCount`, `FailCount`, `RoleTypes`, `Modes`, `Theme`, `Headers`, `TimeTopScore`, `RaceTopScore`, `EndTopScore`, `TopLength`, `DonateValue`, `isCanUnlockCoinModeByAD`, `LastAdTime`, `AdTriggerCount`, `BuyAdTime`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `PlayerName`=?,`AccountID`=?,`AccountName`=?,`AccuntGName`=?,`OpenID`=?,`CreateTime`=?,`LastLoginTime`=?,`Gold`=?,`Diamond`=?,`Money`=?,`HeaderID`=?,`AdExpireTime`=?,`WinCount`=?,`FailCount`=?,`RoleTypes`=?,`Modes`=?,`Theme`=?,`Headers`=?,`TimeTopScore`=?,`RaceTopScore`=?,`EndTopScore`=?,`TopLength`=?,`DonateValue`=?,`isCanUnlockCoinModeByAD`=?,`LastAdTime`=?,`AdTriggerCount`=?,`BuyAdTime`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,playerInfo.getUserID());
		params.put(Types.VARCHAR,playerInfo.getPlayerName());
		params.put(Types.BIGINT,playerInfo.getAccountID());
		params.put(Types.VARCHAR,playerInfo.getAccountName());
		params.put(Types.VARCHAR,playerInfo.getAccuntGName());
		params.put(Types.VARCHAR,playerInfo.getOpenID());
		params.put(Types.TIMESTAMP,playerInfo.getCreateTime());
		params.put(Types.TIMESTAMP,playerInfo.getLastLoginTime());
		params.put(Types.INTEGER,playerInfo.getGold());
		params.put(Types.INTEGER,playerInfo.getDiamond());
		params.put(Types.INTEGER,playerInfo.getMoney());
		params.put(Types.INTEGER,playerInfo.getHeaderID());
		params.put(Types.TIMESTAMP,playerInfo.getAdExpireTime());
		params.put(Types.INTEGER,playerInfo.getWinCount());
		params.put(Types.INTEGER,playerInfo.getFailCount());
		params.put(Types.VARCHAR,playerInfo.getRoleTypes());
		params.put(Types.VARCHAR,playerInfo.getModes());
		params.put(Types.VARCHAR,playerInfo.getTheme());
		params.put(Types.VARCHAR,playerInfo.getHeaders());
		params.put(Types.INTEGER,playerInfo.getTimeTopScore());
		params.put(Types.INTEGER,playerInfo.getRaceTopScore());
		params.put(Types.INTEGER,playerInfo.getEndTopScore());
		params.put(Types.INTEGER,playerInfo.getTopLength());
		params.put(Types.INTEGER,playerInfo.getDonateValue());
		params.put(Types.TINYINT,playerInfo.getIsCanUnlockCoinModeByAD());
		params.put(Types.TIMESTAMP,playerInfo.getLastAdTime());
		params.put(Types.INTEGER,playerInfo.getAdTriggerCount());
		params.put(Types.TIMESTAMP,playerInfo.getBuyAdTime());
		params.put(Types.VARCHAR,playerInfo.getPlayerName());
		params.put(Types.BIGINT,playerInfo.getAccountID());
		params.put(Types.VARCHAR,playerInfo.getAccountName());
		params.put(Types.VARCHAR,playerInfo.getAccuntGName());
		params.put(Types.VARCHAR,playerInfo.getOpenID());
		params.put(Types.TIMESTAMP,playerInfo.getCreateTime());
		params.put(Types.TIMESTAMP,playerInfo.getLastLoginTime());
		params.put(Types.INTEGER,playerInfo.getGold());
		params.put(Types.INTEGER,playerInfo.getDiamond());
		params.put(Types.INTEGER,playerInfo.getMoney());
		params.put(Types.INTEGER,playerInfo.getHeaderID());
		params.put(Types.TIMESTAMP,playerInfo.getAdExpireTime());
		params.put(Types.INTEGER,playerInfo.getWinCount());
		params.put(Types.INTEGER,playerInfo.getFailCount());
		params.put(Types.VARCHAR,playerInfo.getRoleTypes());
		params.put(Types.VARCHAR,playerInfo.getModes());
		params.put(Types.VARCHAR,playerInfo.getTheme());
		params.put(Types.VARCHAR,playerInfo.getHeaders());
		params.put(Types.INTEGER,playerInfo.getTimeTopScore());
		params.put(Types.INTEGER,playerInfo.getRaceTopScore());
		params.put(Types.INTEGER,playerInfo.getEndTopScore());
		params.put(Types.INTEGER,playerInfo.getTopLength());
		params.put(Types.INTEGER,playerInfo.getDonateValue());
		params.put(Types.TINYINT,playerInfo.getIsCanUnlockCoinModeByAD());
		params.put(Types.TIMESTAMP,playerInfo.getLastAdTime());
		params.put(Types.INTEGER,playerInfo.getAdTriggerCount());
		params.put(Types.TIMESTAMP,playerInfo.getBuyAdTime());
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public boolean deleteByKey(Object... ids)
	{
		boolean result = false;
		String sql = "delete from t_u_player where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		result = getDBHelper().execNoneQuery(sql, params) > -1 ? true : false;
		return result;
	}

	@Override
	public PlayerInfo getByKey(Object... ids)
	{
		String sql = "select * from t_u_player where `UserID`=?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER,ids[0]);
		PlayerInfo playerInfo = query(sql, params);		return playerInfo;
	}


	@Override
	public List<PlayerInfo> listAll()
	{
		String sql = "select * from t_u_player;";
		List<PlayerInfo> playerInfos = queryList(sql);		return playerInfos;
	}

	@Override
	public int[] addOrUpdateBatch(List<PlayerInfo> playerInfos)
	{
		if (playerInfos == null || playerInfos.isEmpty())
			return new int[1];
		String sql = "insert into t_u_player(`UserID`, `PlayerName`, `AccountID`, `AccountName`, `AccuntGName`, `OpenID`, `CreateTime`, `LastLoginTime`, `Gold`, `Diamond`, `Money`, `HeaderID`, `AdExpireTime`, `WinCount`, `FailCount`, `RoleTypes`, `Modes`, `Theme`, `Headers`, `TimeTopScore`, `RaceTopScore`, `EndTopScore`, `TopLength`, `DonateValue`, `isCanUnlockCoinModeByAD`, `LastAdTime`, `AdTriggerCount`, `BuyAdTime`) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on DUPLICATE KEY update `PlayerName`=?,`AccountID`=?,`AccountName`=?,`AccuntGName`=?,`OpenID`=?,`CreateTime`=?,`LastLoginTime`=?,`Gold`=?,`Diamond`=?,`Money`=?,`HeaderID`=?,`AdExpireTime`=?,`WinCount`=?,`FailCount`=?,`RoleTypes`=?,`Modes`=?,`Theme`=?,`Headers`=?,`TimeTopScore`=?,`RaceTopScore`=?,`EndTopScore`=?,`TopLength`=?,`DonateValue`=?,`isCanUnlockCoinModeByAD`=?,`LastAdTime`=?,`AdTriggerCount`=?,`BuyAdTime`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, playerInfos, new DataExecutor<int[]>()
			{
				@Override
				public int[] execute(PreparedStatement statement, Object... objects) throws Exception
				{
					@SuppressWarnings("unchecked")
					List<PlayerInfo>playerInfos = (List<PlayerInfo>)objects[0];
					for (PlayerInfo playerInfo : playerInfos)
					{
						DBParamWrapper params = new DBParamWrapper();
						params.put(Types.INTEGER,playerInfo.getUserID());
						params.put(Types.VARCHAR,playerInfo.getPlayerName());
						params.put(Types.BIGINT,playerInfo.getAccountID());
						params.put(Types.VARCHAR,playerInfo.getAccountName());
						params.put(Types.VARCHAR,playerInfo.getAccuntGName());
						params.put(Types.VARCHAR,playerInfo.getOpenID());
						params.put(Types.TIMESTAMP,playerInfo.getCreateTime());
						params.put(Types.TIMESTAMP,playerInfo.getLastLoginTime());
						params.put(Types.INTEGER,playerInfo.getGold());
						params.put(Types.INTEGER,playerInfo.getDiamond());
						params.put(Types.INTEGER,playerInfo.getMoney());
						params.put(Types.INTEGER,playerInfo.getHeaderID());
						params.put(Types.TIMESTAMP,playerInfo.getAdExpireTime());
						params.put(Types.INTEGER,playerInfo.getWinCount());
						params.put(Types.INTEGER,playerInfo.getFailCount());
						params.put(Types.VARCHAR,playerInfo.getRoleTypes());
						params.put(Types.VARCHAR,playerInfo.getModes());
						params.put(Types.VARCHAR,playerInfo.getTheme());
						params.put(Types.VARCHAR,playerInfo.getHeaders());
						params.put(Types.INTEGER,playerInfo.getTimeTopScore());
						params.put(Types.INTEGER,playerInfo.getRaceTopScore());
						params.put(Types.INTEGER,playerInfo.getEndTopScore());
						params.put(Types.INTEGER,playerInfo.getTopLength());
						params.put(Types.INTEGER,playerInfo.getDonateValue());
						params.put(Types.TINYINT,playerInfo.getIsCanUnlockCoinModeByAD());
						params.put(Types.TIMESTAMP,playerInfo.getLastAdTime());
						params.put(Types.INTEGER,playerInfo.getAdTriggerCount());
						params.put(Types.TIMESTAMP,playerInfo.getBuyAdTime());
						params.put(Types.VARCHAR,playerInfo.getPlayerName());
						params.put(Types.BIGINT,playerInfo.getAccountID());
						params.put(Types.VARCHAR,playerInfo.getAccountName());
						params.put(Types.VARCHAR,playerInfo.getAccuntGName());
						params.put(Types.VARCHAR,playerInfo.getOpenID());
						params.put(Types.TIMESTAMP,playerInfo.getCreateTime());
						params.put(Types.TIMESTAMP,playerInfo.getLastLoginTime());
						params.put(Types.INTEGER,playerInfo.getGold());
						params.put(Types.INTEGER,playerInfo.getDiamond());
						params.put(Types.INTEGER,playerInfo.getMoney());
						params.put(Types.INTEGER,playerInfo.getHeaderID());
						params.put(Types.TIMESTAMP,playerInfo.getAdExpireTime());
						params.put(Types.INTEGER,playerInfo.getWinCount());
						params.put(Types.INTEGER,playerInfo.getFailCount());
						params.put(Types.VARCHAR,playerInfo.getRoleTypes());
						params.put(Types.VARCHAR,playerInfo.getModes());
						params.put(Types.VARCHAR,playerInfo.getTheme());
						params.put(Types.VARCHAR,playerInfo.getHeaders());
						params.put(Types.INTEGER,playerInfo.getTimeTopScore());
						params.put(Types.INTEGER,playerInfo.getRaceTopScore());
						params.put(Types.INTEGER,playerInfo.getEndTopScore());
						params.put(Types.INTEGER,playerInfo.getTopLength());
						params.put(Types.INTEGER,playerInfo.getDonateValue());
						params.put(Types.TINYINT,playerInfo.getIsCanUnlockCoinModeByAD());
						params.put(Types.TIMESTAMP,playerInfo.getLastAdTime());
						params.put(Types.INTEGER,playerInfo.getAdTriggerCount());
						params.put(Types.TIMESTAMP,playerInfo.getBuyAdTime());
						statement = getDBHelper().prepareCommand(statement,params.getParams());
						statement.addBatch();
					}
					return statement.executeBatch();
				}
			});
		return effectedRows;
	}

	@Override
	public int[] deleteBatch(List<PlayerInfo> playerInfos)
	{
		String sql = "delete from t_u_player where `UserID`=?;";
		int[] effectedRows = getDBHelper().sqlBatch(sql, playerInfos, new DataExecutor<int[]>()
		{
			@Override
		public int[] execute(PreparedStatement statement, Object... objects) throws Exception
		{
			
			@SuppressWarnings("unchecked")
			List<PlayerInfo>playerInfos = (List<PlayerInfo>)objects[0];
			for (PlayerInfo playerInfo : playerInfos)
			{
					DBParamWrapper params = new DBParamWrapper();
					params.put(Types.INTEGER,playerInfo.getUserID());
					statement = getDBHelper().prepareCommand(statement,params.getParams());
					statement.addBatch();
				}
				return statement.executeBatch();
			}
		});
		return effectedRows;
	}
	@Override
	public PlayerInfo rsToEntity(ResultSet rs) throws SQLException
	{
		PlayerInfo playerInfo = new PlayerInfo();
		playerInfo.setUserID(rs.getInt("UserID"));
		playerInfo.setPlayerName(rs.getString("PlayerName"));
		playerInfo.setAccountID(rs.getLong("AccountID"));
		playerInfo.setAccountName(rs.getString("AccountName"));
		playerInfo.setAccuntGName(rs.getString("AccuntGName"));
		playerInfo.setOpenID(rs.getString("OpenID"));
		playerInfo.setCreateTime(rs.getTimestamp("CreateTime"));
		playerInfo.setLastLoginTime(rs.getTimestamp("LastLoginTime"));
		playerInfo.setGold(rs.getInt("Gold"));
		playerInfo.setDiamond(rs.getInt("Diamond"));
		playerInfo.setMoney(rs.getInt("Money"));
		playerInfo.setHeaderID(rs.getInt("HeaderID"));
		playerInfo.setAdExpireTime(rs.getTimestamp("AdExpireTime"));
		playerInfo.setWinCount(rs.getInt("WinCount"));
		playerInfo.setFailCount(rs.getInt("FailCount"));
		playerInfo.setRoleTypes(rs.getString("RoleTypes"));
		playerInfo.setModes(rs.getString("Modes"));
		playerInfo.setTheme(rs.getString("Theme"));
		playerInfo.setHeaders(rs.getString("Headers"));
		playerInfo.setTimeTopScore(rs.getInt("TimeTopScore"));
		playerInfo.setRaceTopScore(rs.getInt("RaceTopScore"));
		playerInfo.setEndTopScore(rs.getInt("EndTopScore"));
		playerInfo.setTopLength(rs.getInt("TopLength"));
		playerInfo.setDonateValue(rs.getInt("DonateValue"));
		playerInfo.setIsCanUnlockCoinModeByAD(rs.getBoolean("isCanUnlockCoinModeByAD"));
		playerInfo.setLastAdTime(rs.getTimestamp("LastAdTime"));
		playerInfo.setAdTriggerCount(rs.getInt("AdTriggerCount"));
		playerInfo.setBuyAdTime(rs.getTimestamp("BuyAdTime"));
		return playerInfo;
	}

	@Override
	public PlayerInfo getPlayerInfoByUserID(int userID)
	{
		String sql = "select * from t_u_player where `userID` = ?;";
		DBParamWrapper params = new DBParamWrapper();
		params.put(userID);
		PlayerInfo playerInfo = query(sql,params);
		return playerInfo;
	}

}