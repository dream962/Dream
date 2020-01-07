package com.upload.mail;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.upload.util.LogFactory;

/**
 * 邮件工具类(只支持QQ邮箱)
 * 需要先
 * 
 * @author dream
 *
 */
public class MailUtil
{
    /**
     * 发送简单文本邮件
     * 
     * @param sender
     * @param senderKey
     * @param receiveList
     * @param title
     * @param content
     */
    public static boolean sendMail(String title, String content, String sender, String senderKey, List<String> receiveList)
    {
        // 1、连接邮件服务器的参数配置
        Properties props = new Properties();
        // 设置用户的认证方式
        props.setProperty("smtp.qq.com", "true");
        // 设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        // 设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        // 2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        // 设置调试信息在控制台打印出来
        session.setDebug(true);
        Transport transport = null;
        try
        {
            // 3、创建邮件的实例对象
            MimeMessage msg = new MimeMessage(session);
            // 设置发件人地址
            msg.setFrom(new InternetAddress(sender));
            /**
             * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
             * MimeMessage.RecipientType.TO:发送
             * MimeMessage.RecipientType.CC：抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            for (String rec : receiveList)
            {
                msg.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(rec));
            }

            // 设置邮件主题
            msg.setSubject(title, "UTF-8");
            // 设置邮件正文
            msg.setContent(content, "text/html;charset=UTF-8");
            // 设置邮件的发送时间,默认立即发送
            msg.setSentDate(new Date());

            // 4、根据session对象获取邮件传输对象Transport
            transport = session.getTransport();
            // 设置发件人的账户名和密码
            transport.connect(sender, senderKey);
            // 发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg, msg.getAllRecipients());

            // 5、关闭邮件连接
            transport.close();

            return true;
        }
        catch (Exception e)
        {
            try
            {
                if (transport != null)
                    transport.close();
            }
            catch (Exception e2)
            {
                LogFactory.error("", e);
            }
            LogFactory.error("", e);
        }

        return false;
    }

    public static void main(String[] args) throws Exception
    {
        // 发件人地址
        String senderAddress = "342370472@qq.com";
        // 收件人地址
        String recipientAddress = "wangwenhua@zhuohuamg.com";
        // 发件人账户名
        String senderAccount = "342370472@qq.com";
        // 发件人账户密码
        String senderPassword = "xcrmyzzdaioybghi";
        // 1、连接邮件服务器的参数配置
        Properties props = new Properties();
        // 设置用户的认证方式
        props.setProperty("smtp.qq.com", "true");
        // 设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        // 设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        // 2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        // 设置调试信息在控制台打印出来
        session.setDebug(true);
        // 3、创建邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        // 设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientAddress));
        // 设置邮件主题
        msg.setSubject("邮件主题", "UTF-8");
        // 设置邮件正文
        msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
        // 设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        // 4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        // 设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        // 发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg, msg.getAllRecipients());

        // 如果只想发送给指定的人，可以如下写法
        // transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});

        // 5、关闭邮件连接
        transport.close();
    }
}
