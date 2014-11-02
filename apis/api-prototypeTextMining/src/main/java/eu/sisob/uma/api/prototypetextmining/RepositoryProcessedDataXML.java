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
package eu.sisob.uma.api.prototypetextmining;

import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * DATA STEP 5
 */
public class RepositoryProcessedDataXML implements DataInputRepository
{
    private static final Logger LOG = Logger.getLogger(RepositoryProcessedDataXML.class.getName());
    
    private org.dom4j.Document docXML;
    private org.dom4j.Element rootElement;

    public RepositoryProcessedDataXML()
    {   
        docXML = new org.dom4j.DocumentFactory().createDocument();
        rootElement = docXML.addElement("root");
    } 
    
    public synchronized void addData(MiddleData oData)
    {
        if(oData.getData_out() != null)
        {
            String sHash = oData.getId_entity();                 
            Object sType = oData.getId_annotationrecollecting(); 
            org.dom4j.Element eleParsedContent = (org.dom4j.Element) oData.getData_out();

            if(eleParsedContent!=null && eleParsedContent.elements().size() > 0)
                rootElement.add(eleParsedContent);
            else               
                LOG.log(Level.WARNING, "Block unparsed: " + sHash + " " + sType.toString() + " " + oData.getData_in().toString().substring(0,10));
        }
    }

    /**
     * @return the docXML
     */
    public org.dom4j.Document getDocXML() 
    {
        return docXML;
    }

    public synchronized void clearData() 
    {
        docXML.getRootElement().clearContent();
    } 
}
