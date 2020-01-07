package com.base.rmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataTable implements Serializable
{
    private static final long serialVersionUID = 8005496017730509629L;
    
    public static class TableColumn
    {
        public String name = "";
        public int pos = -1;
        public String type = "";
        public int typeValue;
    }
    
    public static class TableCell
    {
        public List<Object> value = new ArrayList<>();
    }
    
    public List<TableColumn> columns = new ArrayList<>();
    
    public List<TableCell> cells = new ArrayList<>();
    
    public String errorMessage; 

}
