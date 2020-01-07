package com.upload.web.servlet.serverlist;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.upload.component.ServerListComponent;
import com.upload.data.data.ServerListData;
import com.upload.util.ExcelUtils;
import com.upload.web.servlet.FileHandlerServlet;
import com.upload.web.servlet.WebHandleAnnotation;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-05-04 15:51:05
 * @description
 *              导出服务器数据
 */
@WebHandleAnnotation(cmdName = "/exportServers", description = "导出服务器数据")
public class ExportServersServlet extends FileHandlerServlet
{
    private static final long serialVersionUID = -8609743296253628700L;

    @Override
    public String execute(String json, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String fileName = "serverList";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setContentType("application/octet-stream; charset=UTF-8");
            List<ServerListData> list = ServerListComponent.getAllServerList();
            HSSFWorkbook workbook = ExcelUtils.exportAllServers(list);
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "exit";
    }

}
