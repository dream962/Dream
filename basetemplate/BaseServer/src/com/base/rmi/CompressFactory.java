package com.base.rmi;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.util.CompressUtil;
import com.util.JsonUtil;

public class CompressFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CompressFactory.class);

    public static CaptureData compress(DataTable table)
    {
        byte[] data = null;
        try
        {
            String str = JsonUtil.parseObjectToString(table);
            data = CompressUtil.compress(str);
        }
        catch (Exception e)
        {
            LOGGER.error("Exception:", e);

            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String error = String.format("CaptureClient Exception \n Trace：%s", writer.toString());

            DataTable newTable = new DataTable();
            newTable.errorMessage = error;
            String str = JsonUtil.parseObjectToString(newTable);
            data = CompressUtil.compress(str);
        }

        CaptureData captureData = new CaptureData();
        captureData.data = data;
        return captureData;
    }

    public static DataTable uncompress(CaptureData data)
    {
        DataTable table = null;
        if (data != null && data.data != null)
        {
            String json = CompressUtil.decompress(data.data);
            table = JsonUtil.parseStringToObject(json, DataTable.class);
        }
        return table;
    }
}
