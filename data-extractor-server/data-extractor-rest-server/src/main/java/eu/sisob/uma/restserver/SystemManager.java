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

import eu.sisob.uma.npl.researchers.GateDataExtractorService;
import eu.sisob.uma.api.h2dbpool.H2DBCredentials;
import eu.sisob.uma.api.h2dbpool.H2DBPool;
import eu.sisob.uma.crawler.ResearchersCrawlerService;
import eu.sisob.uma.extractors.adhoc.cvfilesinside.InternalCVFilesExtractorService;
import eu.sisob.uma.extractors.adhoc.email.EmailExtractorService;
import eu.sisob.uma.extractors.adhoc.websearchers.WebSearchersExtractorService;
import eu.sisob.uma.footils.File.FileFootils;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.commons.configuration.Configuration;
/**
 * This class setups all the system and its components and then manage the releasing of all resources and components.
 */
public class SystemManager  {
    
    
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(SystemManager.class.getName());
        
        
    private static SystemManager instance = null;       
    private boolean is_running = false;  
    H2DBPool systemdbpool;
    
    /**
     *
     * @return
     */
    public static SystemManager getInstance() {
      if(instance == null) {
         init();
      }
      return instance;
    }
    
    /**
     *
     * @return
     */
    public static void init() {
        instance = new SystemManager();
        try {
            instance.setup();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "System Manager cannot be initialized", ex);
        }
    }
    
    public static void stop() {
        if (instance  != null) {
            instance.shutdown();
        }
    }
    
    /**
     *
     */
    protected SystemManager()  {       
        this.is_running = false;
        this.systemdbpool = null;
    }    
        
    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    /*
    public static String deserializeString(File file) throws IOException
    {
      int len;
      char[] chr = new char[4096];
      final StringBuffer buffer = new StringBuffer();
      final FileReader reader = new FileReader(file);
      try {
          while ((len = reader.read(chr)) > 0) {
              buffer.append(chr, 0, len);
          }
      } finally {
          reader.close();
      }
      return buffer.toString();
    }
    */
    /**
     *
     * @return
     */
    public H2DBPool getSystemDbPool()
    {
        return this.systemdbpool;
    }
    
    /**
     * Intialize thre system
     * @throws SQLException 
     */
    
    protected void setup() throws SQLException {       
        if(!this.is_running) {                   
            try {              
                //Database
                //TheDBPool.getInstance();
                //ProjectLogger.LOGGER.info("Connection with H2 database done.");
                
                //Crawler
                
                Configuration config = TheConfig.getInstance();
                
                H2DBCredentials credentials_systemdb = new H2DBCredentials(TheConfig.getProperty("server.docs.folder") + File.separator + "db",
                                                                           "system", 
                                                                           "sa", 
                                                                           "sa");
                this.systemdbpool = new H2DBPool(credentials_systemdb);
                
                if(config.containsKey(TheConfig.SERVICES_CRAWLER) && config.getString(TheConfig.SERVICES_CRAWLER).equals("enabled"))
                {
                    
                    File local_crawler_data_path = new File(TheConfig.getProperty("server.docs.folder") + File.separator + "crawler-data-service");                                                                    
                    
                    boolean traceUrls = config.containsKey(TheConfig.SERVICES_CRAWLER_TRACEURLS) && config.getString(TheConfig.SERVICES_CRAWLER_TRACEURLS).equals("true");
                    boolean traceSearch = config.containsKey(TheConfig.SERVICES_CRAWLER_TRACESEARCH) && config.getString(TheConfig.SERVICES_CRAWLER_TRACESEARCH).equals("true");
                    ResearchersCrawlerService.setServiceSettings(local_crawler_data_path.getAbsolutePath(), Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/crawler/keywords"), traceUrls, traceSearch);
                    
                    ResearchersCrawlerService.createInstance();    
                    LOG.info("Crawler service initialized.");
                }
                
                if(config.containsKey(TheConfig.SERVICES_WEBSEARCHER) && config.getString(TheConfig.SERVICES_WEBSEARCHER).equals("enabled"))
                {
                    WebSearchersExtractorService.createInstance();                    
                }        
                
                if(config.containsKey(TheConfig.SERVICES_INTERNAL_CV_FILES) && config.getString(TheConfig.SERVICES_INTERNAL_CV_FILES).equals("enabled"))
                {
                    InternalCVFilesExtractorService.createInstance();
                }
                
                if(config.containsKey(TheConfig.SERVICES_EMAIL) && config.getString(TheConfig.SERVICES_EMAIL).equals("enabled"))
                {
                    EmailExtractorService.createInstance();
                }
                
                if(TheConfig.getInstance().getString(TheConfig.SERVICES_GATE).equals("enabled"))
                {
                    //Gate                 
                    File local_gate_path = new File(TheConfig.getProperty("server.docs.folder") + File.separator + "gate-data-extractor-service");                                                                    
                    //local_gate_path.mkdirs();        
                    if (!local_gate_path.exists()) {
                        LOG.log(Level.SEVERE, "Local path of gate files does not exist. Current path: " + local_gate_path.getAbsolutePath());
                    } else {
                    
                        File gate_content = new File(local_gate_path.getAbsolutePath() + File.separator + "GATE-6.0");                    
                        if(!gate_content.exists())
                            gate_content.mkdirs();
                    
                        File keywords = new File(local_gate_path.getAbsolutePath() + File.separator + "KEYWORDS");                    
                        if(!keywords.exists())
                            keywords.mkdirs();
                            
                        //LOG.info("Copying gate files [" + Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/NPL/Researchers/GATE-6.0/") + "]");
                        //FileFootils.copyResourcesRecursively(Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/NPL/Researchers/GATE-6.0/"), local_gate_path);      
                        //FileFootils.copyResourcesRecursively(Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/NPL/Researchers/KEYWORDS/"), local_gate_path);      
                        //LOG.info("Gate files copied");                        
                    }
                    
                    {    
                        H2DBCredentials credentials_resolver = new H2DBCredentials(TheConfig.getProperty("server.docs.folder") + File.separator + "db",
                                                                                   "locations", 
                                                                                   "sa", 
                                                                                   "sa");                        

                        H2DBCredentials credentials_trad_tables_academic = new H2DBCredentials(TheConfig.getProperty("server.docs.folder") + File.separator + "db",
                                                                                   "academic_tables_traductions", 
                                                                                   "sa", 
                                                                                   "sa");
                        
                        GateDataExtractorService.setServiceSettings(local_gate_path.getAbsolutePath() + File.separator + "GATE-6.0", 
                                                                    local_gate_path.getAbsolutePath() + File.separator + "KEYWORDS", 
                                                                    1, 5, credentials_trad_tables_academic, credentials_resolver);
                        
                        GateDataExtractorService.createInstance(); 
                        LOG.info("Gate service initialized.");
                    }
                                
                }
                
                is_running = true;                
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "An exception ocurred while initializing directory and database structure: ", ex);
            
            } finally {               
                if(!is_running)
                {
                    LOG.log(Level.SEVERE, "Something has failed in initialization. Proceeed to object releasing.");
                    
                    
                    
                    if(TheConfig.getInstance().getString(TheConfig.SERVICES_CRAWLER).equals("enabled"))
                    {
                        //Crawler
                        ResearchersCrawlerService.releaseInstance();
                        LOG.info("Crawler service initialized.");
                    }

                    if(TheConfig.getInstance().getString(TheConfig.SERVICES_GATE).equals("enabled"))
                    {
                        //Gate                
                        GateDataExtractorService.releaseInstance();
                        LOG.info("Gate service release.");                        
                    }
                    
                    //DB
                    this.systemdbpool = null;
                    LOG.info("System db pool initialized.");
                    
                    this.is_running = false;
                }
                else
                {
                    LOG.info("System initialized!");
                }
            }
        }
    }
    
    
    /**
     * 
     */
    protected void shutdown()
    {    
        if(is_running)
        {
            

            if(TheConfig.getInstance().getString(TheConfig.SERVICES_CRAWLER).equals("enabled"))
            {
                //Crawler
                ResearchersCrawlerService.releaseInstance();
                LOG.info("Crawler service released.");
            }

            if(TheConfig.getInstance().getString(TheConfig.SERVICES_GATE).equals("enabled"))
            {
                //Gate                
                GateDataExtractorService.releaseInstance();
                LOG.info("Gate service released.");                        
            }        
            
            //DB
            this.systemdbpool = null;
            LOG.info("DB Pool service release.");

            this.is_running = false;
            LOG.info("System going off!");
        }
    }    
    
    /**
     *
     * @return
     */
    public boolean IsRunning()
    {
        return is_running;
    }
    
}
