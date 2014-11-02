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

import java.io.StringWriter;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

public class JSONDataConversor 
{
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(JSONDataConversor.class.getName());
    
    /**
     * 
     * @param data
     * @return
     */
    public static String getJSONString(Object data)
    {
        StringWriter output = new StringWriter();            
        JAXBContext jc = null;        
        Marshaller marshaller = null;       
        String json = "";
        try {
            jc = JAXBContext.newInstance(data.getClass());
            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Configuration c = new Configuration();
            MappedNamespaceConvention convention = new MappedNamespaceConvention(c);
            AbstractXMLStreamWriter xsw = new MappedXMLStreamWriter(convention, output);
            marshaller.marshal(data, xsw);
            xsw.close();
            json = output.toString();
        } catch (JAXBException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } catch (XMLStreamException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }        
        
        return json;
    }
    
}
