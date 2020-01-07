package com.data.info;

public final class FriendInfo
{
    /**
     * 玩家ID
     */
    private int userID;

    /**
     * 头像
     */
    private int header;

    /**
     * 昵称
     */
    private String nickName;

    public int getUserID()
    {
        return userID;
    }

    public void setUserID(int userID)
    {
        this.userID = userID;
    }

    public int getHeader()
    {
        return header;
    }

    public void setHeader(int header)
    {
        this.header = header;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

}
