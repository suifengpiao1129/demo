package com.tescomm.demo.javamail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class Javamail {
	public Session createSession(String protocol){
		 Properties property = new Properties();  
	     property.setProperty("mail.transport.protocol", protocol);  
	     property.setProperty("mail.smtp.auth", "true");  
	     Session session = Session.getInstance(property);  	          
	     // 启动JavaMail调试功能，可以返回与SMTP服务器交互的命令信息  
	     // 可以从控制台中看一下服务器的响应信息  
	     session.setDebug(true);   
	     return session;  		
	}
	 public void sendMail(Session session, MimeMessage msg) throws Exception {           
	        // 设置发件人使用的SMTP服务器、用户名、密码  
	        String smtpServer = "smtp.126.com";  
	        String user = "yanminzhou0808@126.com";  
	        String pwd = "1129324ymz";  	 
	        // 由 Session 对象获得 Transport 对象  
	        Transport transport = session.getTransport();  
	        // 发送用户名、密码连接到指定的 smtp 服务器  
	        transport.connect(smtpServer, user, pwd);  
	 
	        transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));  
	        transport.close();  
	    }
	 public MimeMessage  createMessage(Session session) throws Exception {
		 String from = "yanminzhou0808@126.com";  
	        String to = "806035179@qq.com";  
	        String subject = "创建内含附件、图文并茂的邮件！";  
	        String body = "<h4>内含附件、图文并茂的邮件测试！！！</h4> </br>" 
	                + "<a href = http://haolloyin.blog.51cto.com/> 蚂蚁</a></br>" 
	                + "<img src = \"cid:logo_jpg\"></a>";  
	 
	        MimeMessage msg = new MimeMessage(session);  
	        msg.setFrom(new InternetAddress(from));  
	        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));  
	        msg.setSubject(subject);
	        // 创建邮件的各个 MimeBodyPart 部分  
//	        MimeBodyPart attachment01 = createAttachment("F:\\java\\Snake.java");  
	        MimeBodyPart attachment02 = createAttachment("E:\\javawatcher测试.docx");  
	        MimeBodyPart content = createContent(body, "E:\\Desert.jpg"); 	 
	        // 将邮件中各个部分组合到一个"mixed"型的 MimeMultipart 对象  
	        MimeMultipart allPart = new MimeMultipart("mixed");  
//	        allPart.addBodyPart(attachment01);  
	        allPart.addBodyPart(attachment02);  
	        allPart.addBodyPart(content);  	 
	        // 将上面混合型的 MimeMultipart 对象作为邮件内容并保存  
	        msg.setContent(allPart);  
	        msg.saveChanges();  
	        return msg;  
		 
	 }
	  /**  
	   * 根据传入的文件路径创建附件并返回  
	  */ 
	 public MimeBodyPart createAttachment(String fileName) throws Exception {  
		 MimeBodyPart attachmentPart = new MimeBodyPart();  
		 FileDataSource fds = new FileDataSource(fileName);  
		 attachmentPart.setDataHandler(new DataHandler(fds));  
		 attachmentPart.setFileName(MimeUtility.encodeText(fds.getName(),"UTF-8",null));  
		 return attachmentPart;  
	 } 
	    /**  
	     * 根据传入的邮件正文body和文件路径创建图文并茂的正文部分  
	     */ 
	 public MimeBodyPart createContent(String body, String fileName)throws Exception {  
	        // 用于保存最终正文部分  
		 	MimeBodyPart contentBody = new MimeBodyPart();  
	        // 用于组合文本和图片，"related"型的MimeMultipart对象  
		 	MimeMultipart contentMulti = new MimeMultipart("related");  
	 
	        // 正文的文本部分  
	        MimeBodyPart textBody = new MimeBodyPart();  
	        textBody.setContent(body, "text/html;charset=UTF-8");  
	        contentMulti.addBodyPart(textBody);  	 
	        // 正文的图片部分  
	        MimeBodyPart jpgBody = new MimeBodyPart();  
	        FileDataSource fds = new FileDataSource(fileName);  
	        jpgBody.setDataHandler(new DataHandler(fds));  
	        jpgBody.setContentID("logo_jpg");  
	        contentMulti.addBodyPart(jpgBody);  
	 
	        // 将上面"related"型的 MimeMultipart 对象作为邮件的正文  
	        contentBody.setContent(contentMulti);  
	        return contentBody;  
	    } 
	 public static void main(String[] args) throws Exception {  
		 Javamail sender = new Javamail();  
	          
	        // 指定使用SMTP协议来创建Session对象  
	        Session session = sender.createSession("smtp");  
	        // 使用前面文章所完成的邮件创建类获得 Message 对象  
	        MimeMessage mail = sender.createMessage(session);  
	        sender.sendMail(session, mail);  
	    }  
}
