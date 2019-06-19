package com.longdatech.decryptcode.mailutils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * @description 发送邮件工具类
 * @author: liyinlong
 * @date 2019-06-18 11:10
 */

public class MailUtil {

    /**
     * @description 发送简单文本消息
     * @author: liyinlong
     * @date 2019-06-18 11:53
     * @param receiveMail
     * @param message
     * @throws Exception
     */
    public static void sendSimpleMessage(String receiveMail,String message) throws Exception{
        //跟smtp服务器建立一个连接
        Properties p = new Properties();
        // 开启debug调试，以便在控制台查看MAIL
        p.setProperty("mail.debug", "true");
        p.setProperty("mail.host", "smtp.qq.com");//指定邮件服务器，默认端口 25
        p.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        p.setProperty("mail.transport.protocol", "smtp");
        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);
        // 创建session
        Session session = Session.getInstance(p);
        // 通过session得到transport对象
        Transport ts = session.getTransport();

        // 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
        // 后面的字符是授权码，不能用qq密码
        ts.connect("smtp.qq.com", "469757429@qq.com", "此处填写授权码");

        //声明一个Message对象(代表一封邮件),从session中创建
        MimeMessage msg = new MimeMessage(session);
        //邮件信息封装
        //1发件人
        msg.setFrom( new InternetAddress("469757429@qq.com") );
        //2收件人
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveMail) );
        //3邮件内容:主题、内容
        msg.setSubject("您好！测试邮件已发送至您的邮箱，请留意查收！");

        //添加附件部分
        //邮件内容部分1---文本内容
        MimeBodyPart body0 = new MimeBodyPart(); //邮件中的文字部分
        body0.setContent("<p>" + message + "</p><p>感谢您对海博工作室的支持!</p>","text/html;charset=utf-8");

        //把上面的2部分组装在一起，设置到msg中
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(body0);
        msg.setContent(mm);

        // 发送邮件
        ts.sendMessage(msg,msg.getAllRecipients());
        ts.close();
    }

    /**
     * @description 发送带有文件附件的邮件，文件大小不得超过50MB
     * @author: liyinlong
     * @date 2019-06-18 11:17
     * @param path
     * @param mailAddress
     * @param fileName
     * @throws Exception
     */
    public static void sendMessageWithFile(String path,String mailAddress,String fileName) throws Exception{
        //跟smtp服务器建立一个连接
        Properties p = new Properties();
        // 开启debug调试，以便在控制台查看MAIL
        p.setProperty("mail.debug", "true");
        p.setProperty("mail.host", "smtp.sina.com");//指定邮件服务器，默认端口 25
        p.setProperty("mail.smtp.auth", "true");//要采用指定用户名密码的方式去认证
        // 发送邮件协议名称
        p.setProperty("mail.transport.protocol", "smtp");
        // 开启SSL加密，否则会失败
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.ssl.socketFactory", sf);
        // 创建session
        Session session = Session.getInstance(p);
        // 通过session得到transport对象
        Transport ts = session.getTransport();

        // 连接邮件服务器：邮箱类型，帐号，授权码代替密码（更安全）
//        ts.connect("smtp.qq.com", "2370775541", "otvnlubmiurgdjbb");

        ts.connect("smtp.exmail.qq.com", "liyinlong@fangcuntech.com", "sMBth7DykGjyqbcm");


        // 后面的字符是授权码，不能用qq密码

        //声明一个Message对象(代表一封邮件),从session中创建
        MimeMessage msg = new MimeMessage(session);
        //邮件信息封装
        //1发件人
//        msg.setFrom( new InternetAddress("2370775541@qq.com") );
        msg.setFrom( new InternetAddress("liyinlong@fangcuntech.com") );


        //2收件人
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress) );
        //3邮件内容:主题、内容
        msg.setSubject("您好！活动报名表已发送至您的邮箱，请注意查收！");

        //添加附件部分
        //邮件内容部分1---文本内容
        MimeBodyPart body0 = new MimeBodyPart(); //邮件中的文字部分
        body0.setContent("活动报名表.<p>感谢您对龙达科技团队的支持!</p>","text/html;charset=utf-8");

        //邮件内容部分2---附件1
        MimeBodyPart body1 = new MimeBodyPart(); //附件1
        body1.setDataHandler( new DataHandler( new FileDataSource(path)) );//./代表项目根目录下
        body1.setFileName( MimeUtility.encodeText(fileName + "_报名表.xls") );//中文附件名，解决乱码

        //把上面的2部分组装在一起，设置到msg中
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(body0);
        mm.addBodyPart(body1);
        msg.setContent(mm);

        // 发送邮件
        ts.sendMessage(msg,msg.getAllRecipients());
        ts.close();
    }
}
