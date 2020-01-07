package com.upload.util;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 
 * @author Hoan.Zou
 * @date 2018-04-27 17:40:31
 * @description
 *              上传文件的工具类
 */
public class UploadUtils
{
    /**
     * 获取文件上传请求中的文件
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public static List<FileItem> getRequestFiles(HttpServletRequest request) throws Exception
    {
        File repository = (File) request.getServletContext().getAttribute("javax.servlet.context.tempdir");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        // 把请求数据转换为一个个FileItem对象，再用集合封装
        List<FileItem> list = upload.parseRequest(request);
        return list;
    }
}
