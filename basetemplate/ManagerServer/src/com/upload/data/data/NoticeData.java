package com.upload.data.data;

public final class NoticeData
{
    /**
     * 公告类型
     */
    private int noticeType;

    /**
     * 语言类型
     */
    private String languageType;

    /**
     * 公告
     */
    private String noticeMessage;

    /**
     * ID
     */
    private int iD;

    /**
     * 标题
     */
    private String title;

    public int getID()
    {
        return iD;
    }

    public void setID(int iD)
    {
        this.iD = iD;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public NoticeData()
    {
        super();
    }

    /**
     * 公告类型
     */
    public int getNoticeType()
    {
        return noticeType;
    }

    /**
     * 公告类型
     */
    public void setNoticeType(int noticeType)
    {
        if (noticeType != this.noticeType)
        {
            this.noticeType = noticeType;
        }
    }

    /**
     * 语言类型
     */
    public String getLanguageType()
    {
        return languageType;
    }

    /**
     * 语言类型
     */
    public void setLanguageType(String languageType)
    {
        if (languageType != null)
        {
            if (!languageType.equals(this.languageType))
            {
                this.languageType = languageType;
            }
        }
        else
        {
            if (languageType != this.languageType)
            {
                this.languageType = languageType;
            }
        }
    }

    /**
     * 公告
     */
    public String getNoticeMessage()
    {
        return noticeMessage;
    }

    /**
     * 公告
     */
    public void setNoticeMessage(String noticeMessage)
    {
        if (noticeMessage != null)
        {
            if (!noticeMessage.equals(this.noticeMessage))
            {
                this.noticeMessage = noticeMessage;
            }
        }
        else
        {
            if (noticeMessage != this.noticeMessage)
            {
                this.noticeMessage = noticeMessage;
            }
        }
    }

    /**
     * x.clone() != x
     */
    public NoticeData clone()
    {
        NoticeData clone = new NoticeData();
        clone.setNoticeType(this.getNoticeType());
        clone.setLanguageType(this.getLanguageType());
        clone.setNoticeMessage(this.getNoticeMessage());
        return clone;
    }

    /**
     * 重置信息
     */
    public void reset(NoticeData info)
    {
        this.setNoticeType(info.getNoticeType());
        this.setLanguageType(info.getLanguageType());
        this.setNoticeMessage(info.getNoticeMessage());
    }

}
