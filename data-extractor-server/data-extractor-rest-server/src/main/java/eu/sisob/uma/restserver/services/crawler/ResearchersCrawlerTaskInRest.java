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
package eu.sisob.uma.restserver.services.crawler;

import eu.sisob.uma.crawler.ResearchersCrawlerTask;
import eu.sisob.uma.restserver.AuthorizationManager;
import eu.sisob.uma.restserver.FeedbackManager;
import eu.sisob.uma.restserver.Mailer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ResearchersCrawlerTaskInRest  extends ResearchersCrawlerTask
{       
    private static final Logger LOG = Logger.getLogger(ResearchersCrawlerTaskInRest.class.getName());
    
    String user;    
    String pass;    
    String email;    
    String task_code;
    String task_code_folder;
    
    /*
     * @param document - xml document with the information     
     */    
    public ResearchersCrawlerTaskInRest(org.dom4j.Document document, File crawler_data_dir, File middledata_dir, File resultsdata_dir, File output_file_csv, String user, String pass, String task_code, String task_code_folder, String email)
    {        
        //(org.dom4j.Document document, String keywords_dir, String middle_data_dir, String results_dir, String output_filename_xml, String output_filename_csv, boolean split)
        super(document, 
              crawler_data_dir, 
              middledata_dir,
              resultsdata_dir,   
              output_file_csv,              
              true);
        this.user = user;
        this.pass = pass;
        this.email = email;
        this.task_code = task_code;
        this.task_code_folder = task_code_folder;  
    }           
    
    /*
     * Callback function to manage the results
     */
    public void executeCallBackOfTask() 
    {   
        synchronized(AuthorizationManager.getLocker(user))
        {        
            super.executeCallBackOfTask();                
        }        
        
        String feedback_filename = user + "-crawler-feedback-task-" + task_code;
        String feedback_message = "";
        String feedback_url = FeedbackManager.createNewFeedBackDoc(user, task_code, feedback_filename);
        
        if(!feedback_url.equals(""))
        {
            CrawlerFeedBack.initializeFeedback(user, task_code, feedback_filename);            
            AuthorizationManager.updateFeedbackFile(user, task_code, feedback_url);
            feedback_message = "You can give us the feedback using this document shared with you: " + feedback_url;
        }
        else
        {
            feedback_message = "The feedback document cant be generated by you. Ask to this email for that. Thank you very much.";
            AuthorizationManager.notifyResultError(this.user, this.task_code, "Error creating feedback using google docs.");
        }        
        
        Mailer.notifyResultsOfTask(user, pass, task_code, email, "crawler", feedback_message);  
        
        synchronized(AuthorizationManager.getLocker(user))
        {
            try 
            {
                (new File(task_code_folder + File.separator + AuthorizationManager.end_flag_file)).createNewFile();
            } 
            catch (IOException ex) 
            {
                LOG.log(Level.SEVERE, "Error creating " + AuthorizationManager.end_flag_file + "(" + task_code_folder + ")", ex);  //FIXME                
                AuthorizationManager.notifyResultError(this.user, this.task_code, "Error creating end notification flag.");
            }
        }
            
        setFinished(true);
    }   
    
}
