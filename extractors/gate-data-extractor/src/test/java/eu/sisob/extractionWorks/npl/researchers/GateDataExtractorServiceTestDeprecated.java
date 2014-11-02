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
import java.io.File;
import eu.sisob.uma.api.concurrent.threadpoolutils.CallbackableTaskExecutionWithResource;
import eu.sisob.uma.npl.researchers.GateDataExtractorSingle;
import eu.sisob.uma.npl.researchers.GateDataExtractorTask;
import eu.sisob.uma.api.prototypetextmining.RepositoryPreprocessDataMiddleData;
import java.util.List;
import java.util.ArrayList;
import eu.sisob.uma.npl.researchers.GateDataExtractorService;
import java.util.logging.Level;
import org.junit.Test;
import static org.junit.Assert.*;

public class GateDataExtractorServiceTestDeprecated 
{  
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(GateDataExtractorServiceTestDeprecated.class.getName());
    
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
            
            String documents_filepath = "C:\\Users\\dlopez\\Documents\\NetBeansProjects\\data_extracted\\CVsOfResearchers\\cvs";                                        
            File documents_dir = new File(documents_filepath);
            if(!(documents_dir.exists()))
            {
                LOG.log(Level.SEVERE, documents_dir + " do not exist");                                                
                assertEquals(true, false);
                return;
            }
            
            String ids_filepath = "C:\\Users\\dlopez\\Documents\\NetBeansProjects\\data_extracted\\CVsOfResearchers\\researcher_ids_experiment.txt";                                        
            File ids_file = new File(ids_filepath);
            if(!(ids_file.exists()))
            {
                LOG.log(Level.SEVERE, ids_filepath + " do not exist");                                                
                assertEquals(true, false);
                return;
            }
            
            //Copy files
            String gatepath = "test_gate_destination";                 
            
            GateDataExtractorService.setServiceSettings(gatepath + File.separator + "GATE-6.0", "KEYWORDS", 1, 5, null, null);
            GateDataExtractorService.createInstance();                                

            List<RepositoryPreprocessDataMiddleData> preprocessedReps = new ArrayList<RepositoryPreprocessDataMiddleData>();
            List<GateDataExtractorTask> tasks = new ArrayList<GateDataExtractorTask>();
            for(int i = 0; i < 1; i++)
            {
                RepositoryPreprocessDataMiddleData preprocessedRep = GateDataExtractorSingle.createPreprocessRepositoryOfCVsFromTxt(ids_file, documents_dir, true, false, null);
                preprocessedReps.add(preprocessedRep);                                 
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
                ViewCreator_CSVandSheets.createViewFilesFromDataExtracted(task.getXMLResults(), new File(gatepath), true, true);
            }               

            GateDataExtractorService.releaseInstance();                    
    }
}
