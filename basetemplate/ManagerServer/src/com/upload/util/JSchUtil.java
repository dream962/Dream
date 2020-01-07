package com.upload.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.fileupload.FileItem;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * ssh连接工具
 * 
 * @author dream
 *
 */
public class JSchUtil
{
    private JSch jsch;
    private Session session;

    private static final int count = 10;

    /**
     * 使用秘钥连接到指定的服务器
     * 
     * @throws JSchException
     */
    public boolean connect(String user, String keyName, String host, int port, String keyPass, String userPass)
    {
        if (StringUtil.isNullOrEmpty(keyName) || keyName.trim().equalsIgnoreCase("0"))
            return connect(user, userPass, host, port);

        // 重连10次,如果都是失败,返回失败
        for (int i = 0; i < count; i++)
        {
            try
            {
                // 创建JSch对象
                if (jsch == null)
                    jsch = new JSch();
                // 设置key
                String path = System.getProperty("user.dir") + File.separator + "config" + File.separator + keyName;
                jsch.addIdentity(path, keyPass);

                // 根据用户名、主机ip、端口号获取一个Session对象
                if (session != null)
                    close();

                session = jsch.getSession(user, host, port);
                // Logger logger = new SettleLogger();
                // JSch.setLogger(logger);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                // 为Session对象设置properties
                session.setConfig(config);
                // 设置超时
                session.setTimeout(30000);
                // 通过Session建立连接
                session.connect();

                if (session.isConnected())
                {
                    LogFactory.error("******连接SSH服务器成功：" + host + ":" + port + "," + user);
                    return true;
                }
                else
                {
                    LogFactory.error("******连接SSH服务器失败：" + host + ":" + port + "," + user);
                    Thread.sleep(100);
                }
            }
            catch (Exception e)
            {
                LogFactory.error("host:" + host + ",port:" + port, e);
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * 使用用户名和密码连接到指定的服务器
     * 
     * @throws JSchException
     */
    private boolean connect(String user, String passwd, String host, int port)
    {
        // 重连10次,如果都是失败,返回失败
        for (int i = 0; i < count; i++)
        {
            try
            {
                // 创建JSch对象
                if (jsch == null)
                    jsch = new JSch();
                // 根据用户名、主机ip、端口号获取一个Session对象
                if (session != null)
                    close();

                session = jsch.getSession(user, host, port);
                // 设置密码
                session.setPassword(passwd);
                Logger logger = new SettleLogger();
                JSch.setLogger(logger);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                // 为Session对象设置properties
                session.setConfig(config);
                // 设置超时
                session.setTimeout(200000);
                // 通过Session建立连接
                session.connect();

                if (session.isConnected())
                {
                    LogFactory.error("******连接SSH服务器成功：" + host + ":" + port + "," + user);
                    return true;
                }
                else
                {
                    LogFactory.error("******连接SSH服务器失败：" + host + ":" + port + "," + user);
                    Thread.sleep(100);
                }
            }
            catch (Exception e)
            {
                LogFactory.error("host:" + host + ",port:" + port, e);
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * 关闭连接
     */
    public void close()
    {
        if (session != null)
            session.disconnect();

        session = null;
        jsch = null;
        LogFactory.error("******关闭SSH服务器的连接！\n");
    }

    /**
     * 执行查询端口状态的CMD命令
     * 
     * @param command
     * @return
     */
    public boolean execStatusCmd(String command)
    {
        boolean result = false;

        BufferedReader reader = null;
        Channel channel = null;
        try
        {
            if (command != null)
            {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.connect();

                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                String buf = null;
                while ((buf = reader.readLine()) != null)
                {
                    if (buf != null)
                    {
                        result = true;
                    }
                    System.err.println(buf);
                }
            }
        }
        catch (Exception e)
        {
            LogFactory.error("", e);
        }
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
            }
            catch (IOException e)
            {
                LogFactory.error("", e);
            }

            try
            {
                if (channel != null)
                    channel.disconnect();
            }
            catch (Exception e2)
            {
                LogFactory.error("", e2);
            }

        }
        return result;
    }

    /**
     * 执行相关的命令
     * 
     * @throws JSchException
     */
    public String execCmd(String command)
    {
        BufferedReader reader = null;
        Channel channel = null;
        try
        {
            if (command != null)
            {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                channel.connect();

                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder builder = new StringBuilder();
                String buf = null;
                while ((buf = reader.readLine()) != null)
                {
                    builder.append(buf).append("\n");
                }

                return builder.toString();
            }
        }
        catch (Exception e)
        {
            LogFactory.error("cmd exec error:" + command, e);
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            channel.disconnect();
        }

        return "";
    }

    /**
     * 执行SH命令
     * 
     * @throws JSchException
     */
    public String execSh(String command)
    {
        String result = "";
        BufferedReader reader = null;
        Channel channel = null;
        try
        {
            if (command != null)
            {
                channel = session.openChannel("shell");
                ((ChannelShell) channel).connect();
                InputStream in = ((ChannelShell) channel).getInputStream();
                OutputStream out = ((ChannelShell) channel).getOutputStream();

                LogFactory.error("******执行SSH命令：" + command);
                PrintWriter printWriter = new PrintWriter(out);
                printWriter.println(command);
                printWriter.println("exit");
                printWriter.flush();

                reader = new BufferedReader(new InputStreamReader(in));
                String buf = null;
                // 注意readLine方法会阻塞程序
                while ((buf = reader.readLine()) != null)
                {
                    result += (buf + "\n");
                }

                in.close();
                out.close();
            }

            LogFactory.error("******执行SSH命令结果：\n" + result);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            channel.disconnect();
        }
        return "";
    }

    /**
     * 上传文件到服务器
     * 
     * @param directory
     * @param file
     */
    public void upload(String directory, FileItem file)
    {
        try
        {
            InputStream inputStream = file.getInputStream();
            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.connect(3000);
            channelSftp.put(inputStream, directory + file.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件所在的路径
     * @param sftp
     * @throws JSchException
     * @throws SftpException
     * @throws FileNotFoundException
     */
    public void upload(String directory, String fileDir)
    {
        try
        {
            File file = new File(fileDir);
            FileInputStream inputStream = new FileInputStream(file);
            Channel channel = session.openChannel("sftp");
            ChannelSftp channelSftp = (ChannelSftp) channel;
            channelSftp.connect(3000);
            channelSftp.put(inputStream, directory + file.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * 
     * @param src
     * @param dst
     * @throws JSchException
     * @throws SftpException
     */
    public void download(String src, String dst) throws JSchException, SftpException
    {
        // src linux服务器文件地址，dst 本地存放地址
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        channelSftp.get(src, dst);
        channelSftp.quit();
    }

    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     * @throws SftpException
     * @throws JSchException
     */
    public void delete(String directory, String deleteFile) throws SftpException, JSchException
    {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.cd(directory);
        channelSftp.rm(deleteFile);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory
     *            要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     * @throws JSchException
     */
    @SuppressWarnings("rawtypes")
    public Vector listFiles(String directory) throws JSchException, SftpException
    {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        return channelSftp.ls(directory);
    }

    public static void main(String[] args)
    {
        try
        {
            String path = "H:\\web\\uploadTool\\UploadServer\\config\\id_rsa";
            JSchUtil jSchUtils = new JSchUtil();
            // 1.连接到指定的服务器
            jSchUtils.connect("admin", path, "106.75.146.3", 5188, "ZhuoHua_2019", "111111");
            // 2.执行相关的命令
            String result = jSchUtils.execCmd("cd /data/server/;mkdir mytest1");
            System.err.println(result);
            // execCmd("");
            // upload("/data/server/", "C:\\Users\\zou94\\Desktop\est.txt");
            // execSh("cd /root/daomu2/servers/gameserver;rm -f test.txt");
            // boolean result = execStatusCmd("netstat -ntulp |grep 5001");
            // System.out.println(result);
            jSchUtils.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
