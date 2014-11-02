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
package eu.sisob.uma.crawler;

import au.com.bytecode.opencsv.CSVWriter;
import eu.sisob.uma.crawler.researcherscrawlers.CandidateTypeURL;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlerTrace 
{
    private static final Logger LOG = Logger.getLogger(CrawlerTrace.class.getName());
    
    static boolean traceUrls = false;
    
    static boolean traceSearch = false;
    
    public static void setActivate(boolean traceUrls, boolean traceSearch)
    {
        CrawlerTrace.traceUrls = traceUrls;
        CrawlerTrace.traceSearch = traceSearch;
    }
    
    public static boolean isTraceSearchActive()    
    {
        return traceSearch;
    }
    
    public static boolean isTraceUrlsActive()    
    {
        return traceUrls;
    }
    
    HashMap<String, CSVWriter> urls_trace_files;
    
    public CrawlerTrace(String dest_path, String[] params)
    {
        urls_trace_files = new HashMap<String, CSVWriter>();
       
        for(String param : params)
        {
            CSVWriter writer;
            try {
                writer = new CSVWriter(new FileWriter(dest_path + File.separator + param + ".urls"), '\t');
                this.urls_trace_files.put(param, writer);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
                this.urls_trace_files.put(param, null);
            }            
        }            
    }
    
    public synchronized void anotate(String type, CandidateTypeURL c)
    {
        try {            
            CSVWriter writer = this.urls_trace_files.get(type);
            if(writer != null){
                writer.writeNext(new String[] {c.sURL, c.sText, c.sFromURL});
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);                    
        }
    }    
    
    public synchronized void anotate(String type, String line)
    {
        try {            
            CSVWriter writer = this.urls_trace_files.get(type);
            if(writer != null){
                writer.writeNext(line.split("\t"));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);                    
        }
    }    
    
    
    public void close()
    {
        for(String key : urls_trace_files.keySet())
        {
            CSVWriter writer = this.urls_trace_files.get(key);
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);                    
                }
                urls_trace_files.put(key, null);
            }            
        }
        urls_trace_files.clear();
        urls_trace_files = null;
    }
}
