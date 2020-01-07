package com.base.rmi;

import java.util.ArrayList;
import java.util.List;

/**
 * RMI数据请求Data
 * @author dream
 */
public class RequestData
{
    public String sql;
    
    public List<Object> param = new ArrayList<>();
}
