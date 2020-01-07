
package com.base.web.gm;

import com.google.gson.annotations.SerializedName;

/**
 * 查询分页信息
 */
public class PagerBean
{
    /**
     * 查询的页码默认从0开始
     */
    @SerializedName("pageIndex")
    private Integer pageIndex;
    /**
     * 查询的页的大小
     */
    @SerializedName("pageSize")
    private Integer pageSize;
    /**
     * 查询的条件不带where,且是根据activityinfo 的字段查询
     */
    @SerializedName("condition")
    private String condition;
    /**
     * 查询的排序，也是根据activityinfo 的字段排序
     */
    @SerializedName("orderBy")
    private String orderBy;
    /**
     * 表示排序顺序，0表示asc升序，1表示desc降序
     */
    @SerializedName("sort")
    private Integer sort;

    /**
     * @return the pageIndex
     */
    public Integer getPageIndex()
    {
        return pageIndex;
    }

    /**
     * @param pageIndex
     *            the pageIndex to set
     */
    public void setPageIndex(Integer pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    /**
     * @return the pageSize
     */
    public Integer getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * @return the condition
     */
    public String getCondition()
    {
        return condition;
    }

    /**
     * @param condition
     *            the condition to set
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    /**
     * @return the orderBy
     */
    public String getOrderBy()
    {
        return orderBy;
    }

    /**
     * @param orderBy
     *            the orderBy to set
     */
    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }

    /**
     * @return the sort
     */
    public Integer getSort()
    {
        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

}
