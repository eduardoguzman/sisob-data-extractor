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
package eu.sisob.uma.restserver.servlets;
import eu.sisob.uma.restserver.SystemManager;
import eu.sisob.uma.restserver.TheConfig;
import java.util.logging.Level;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Server Context Listener used to initialization stuff
 * Set up gate services, crawler, etc
 */
 
public class ServletContextClass implements ServletContextListener
{  
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(ServletContextClass.class.getName());
    
    
    @Override
    public void contextInitialized(ServletContextEvent arg0)  {        
        try {
            TheConfig.createInstance(arg0.getServletContext().getRealPath(""));            
        } catch (ConfigurationException ex) {
            LOG.log(Level.SEVERE, "A problem has been found with configuration parameters", ex);
        }
        SystemManager.init();            
    }


    @Override
    public void contextDestroyed(ServletContextEvent arg0) 
    {
        try 
        {
            SystemManager.stop();       
        }
        catch (Exception ex)         
        {
            LOG.log(Level.SEVERE, "System Manager had errors in its shutdown process", ex);
        }
    }

}
