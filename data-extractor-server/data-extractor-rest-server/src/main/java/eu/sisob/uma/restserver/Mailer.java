/*
    Copyright (c) 2014 "(IA)2 Research Group. Universidad de Málaga"
                        http://iaia.lcc.uma.es | http://www.uma.es

    This file is part of SISOB Data Extractor.

    SISOB Data Extractor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SISOB Data Extractor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SISOB Data Extractor. If not, see <http://www.gnu.org/licenses/>.
*/
package eu.sisob.uma.restserver;

import com.sun.mail.smtp.SMTPTransport;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer 
{    
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Mailer.class.getName());
    
    public static void sendMail(String to, String subject, String content) throws MessagingException
    {    
        String server_email = TheConfig.getProperty(TheConfig.SYSTEMEMAIL_ADDRESS);
        String server_pwd = TheConfig.getProperty(TheConfig.SYSTEMEMAIL_PASSWORD);
                
        Properties props = System.getProperties();
        props.put("mail.smtps.host",TheConfig.getProperty("mail.smtps.host"));
        props.put("mail.smtps.auth","true");
        Session session = Session.getInstance(props, null);
        
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(server_email));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setSubject(subject);
        msg.setText(content);
        msg.setHeader("X-Mailer", "Sisob Data Extractor System");
        msg.setSentDate(new Date());
        
        SMTPTransport t =(SMTPTransport)session.getTransport("smtps");
        t.connect(TheConfig.getProperty("mail.smtps.host"), server_email, server_pwd);
        t.sendMessage(msg, msg.getAllRecipients());
        
        LOG.info("Email Response To " + to + ": " + t.getLastServerResponse());
        t.close(); 
    }
    
    /*
     * This notifies via email the file results of a task (this must be called in the end of a task, for example in the executeCallBackOfTask of ResearchersCrawlerTaskInRest)
     */
    /**
     *
     * @param user
     * @param pass
     * @param task_code
     * @param email
     * @param task_kind
     * @param extra_msg
     * @return
     */
    public static boolean notifyResultsOfTask(String user, String pass, String task_code, String email, String task_kind, String extra_msg)
    {
        LOG.info("Notyfing task is finish (" + user + ", " + task_code + ", " + task_kind + ")");
        boolean success = false;
        String files = "";
        List<String> fresults = AuthorizationManager.getResultFiles(user, task_code);
        
        for(String fresult : fresults)
        {
            String file_url = AuthorizationManager.getGetFileUrl(user, pass, task_code, fresult, "results");
            files += fresult + ": " + file_url + "\r\n\r\n";
        }
       
        try {        
            Mailer.sendMail(email,
                                      "Your " + task_kind + " task has been finished [code=" + task_code + "]", 
                                      "Dear user " + email + "r\n\r\n" + 
                                      "Your task with the code " + task_code + " has been finished.\r\n\r\n" + 
                                      "The results of the task there are in these files:\r\n\r\n" + 
                                      files + "\r\n\r\n" + 
                                      "You can download these files now." + 
                                      "\r\n" + "Access again to the platform, go the tasks list and press 'view' to task number " + task_code + 
                                      "\r\n" + TheConfig.SERVER_URL + 
                                      "\r\n\r\n" + extra_msg);            
            
            success = true;
        } 
        catch (MessagingException ex) 
        {
            LOG.log(Level.SEVERE, "Error sending email to " + email, ex);            
            AuthorizationManager.notifyResultError(user, task_code, "Error sending email to " + email);
        } 
        
        return success;
    }
}
