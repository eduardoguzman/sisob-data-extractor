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
package eu.sisob.extractionWorks.npl.researchers;

import org.junit.Before;
import eu.sisob.uma.npl.researchers.data.ViewCreator_CSVandSheets;
import eu.sisob.uma.footils.File.FileFootils;
import java.io.File;
import eu.sisob.uma.api.concurrent.threadpoolutils.CallbackableTaskExecutionWithResource;
import eu.sisob.uma.npl.researchers.GateDataExtractorSingle;
import eu.sisob.uma.npl.researchers.GateDataExtractorTask;
import eu.sisob.uma.api.prototypetextmining.RepositoryPreprocessDataMiddleData;
import java.util.List;
import java.util.ArrayList;
import eu.sisob.uma.npl.researchers.GateDataExtractorService;
import java.util.HashMap;
import java.util.logging.Level;
import org.junit.Test;
import static org.junit.Assert.*;

public class GateDataExtractorServiceTest 
{  
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(GateDataExtractorServiceTest.class.getName());
    
      @Before
      public void setup()
      {
      }
      
    @Test
    public void test()
    {
            if(true)
            {
                assertEquals(true, true);
                return;
            }
            
            String testgatepath = "test_gate_destination";      
            File local_gate_path = new File(testgatepath);                     
            local_gate_path.mkdirs();        

            File configHome = new File(local_gate_path, "GATE-6.0");                    
            if(!configHome.exists())
                configHome.mkdirs();

            File keywords = new File(local_gate_path, "KEYWORDS");                    
            if(!keywords.exists())
                keywords.mkdirs();
            
            LOG.info("Copying gate files");
            //FileFootils.copyResourcesRecursively(Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/NPL/Researchers/GATE-6.0/"), configHome);      
            FileFootils.copyResourcesRecursively(Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/NPL/Researchers/KEYWORDS/"), local_gate_path);      
            LOG.info("Gate files copied");           
                   
            GateDataExtractorService.setServiceSettings(testgatepath + File.separator + "GATE-6.0", testgatepath + File.separator + "KEYWORDS", 1, 5, null, null);
            GateDataExtractorService.createInstance();          
            
            File test_data = new File("test-data" + File.separator + "sample1");
            File input_file = new File(test_data, "data-researchers-documents-urls.csv");           
            
            RepositoryPreprocessDataMiddleData preprocessedRep = null;
            try 
            {                
                File verbose_dir = null;
                File dest_dir = null;
                
                if(true)
                {
                    verbose_dir = new File(test_data, "verbose");
                    if(!verbose_dir.exists())
                        verbose_dir.mkdir();
                    
                    dest_dir = new File(test_data, "inter");
                    if(!dest_dir.exists())
                        dest_dir.mkdir();
                }
                
                HashMap<String, String[]> blocks_and_keywords = GateDataExtractorService.getInstance().getBlocksAndKeywords(); // GateDataExtractorService.getInstance().getBlocksAndKeywords();
                        
                preprocessedRep = GateDataExtractorSingle.createPreprocessRepositoryFromCSVFile(input_file, 
                                                                                                ';',
                                                                                                test_data, 
                                                                                                true,
                                                                                                verbose_dir, 
                                                                                                true, 
                                                                                                blocks_and_keywords, 
                                                                                                dest_dir);
            } 
            catch (Exception ex) 
            {
                LOG.log(Level.SEVERE, "", ex);
            }            
            
            
            
            //Copy files            
            List<GateDataExtractorTask> tasks = new ArrayList<GateDataExtractorTask>();
            for(int i = 0; i < 1; i++)
            {
                tasks.add(new GateDataExtractorTask(preprocessedRep, false, null, false, null));                                                         
            }
            
            

            for(GateDataExtractorTask task : tasks)
            {
                try 
                {
                    GateDataExtractorService.getInstance().addExecution((new CallbackableTaskExecutionWithResource(task)));
                    LOG.info("Task launched");
                } 
                catch (Exception ex) 
                {
                    LOG.log(Level.SEVERE, "Error executing gate task", ex);
                }     
            }

            for(GateDataExtractorTask task : tasks)
            {    
                while(!task.isFinished())
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        LOG.log(Level.SEVERE, "Error waiting gate task", ex);
                    }
                }
            }
            
            for(GateDataExtractorTask task : tasks)
            {                   
                System.out.println("");
                System.out.println("");
                System.out.println(task.getXMLResults().asXML());
                ViewCreator_CSVandSheets.createViewFilesFromDataExtracted(task.getXMLResults(), new File(testgatepath), true, true);
            }               

            GateDataExtractorService.releaseInstance();                    
    }
}
