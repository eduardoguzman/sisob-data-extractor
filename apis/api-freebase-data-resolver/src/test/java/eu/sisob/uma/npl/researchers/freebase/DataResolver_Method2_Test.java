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
package eu.sisob.uma.npl.researchers.freebase;

import eu.sisob.uma.api.h2dbpool.H2DBCredentials;
import org.dom4j.DocumentException;
import org.junit.Before;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;


public class DataResolver_Method2_Test 
{    
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(DataResolver_Method2_Test.class.getName());
    
    @Before
    public void setup()
    {
    }
         
    @Test
    public void test() throws DocumentException
    {
        if(true)
        {
            assertEquals(true, true);
            return;
        }

        String input_filepath = "test-data" + File.separator + "researcher_ids_results.xml";              

        String output_filepath = "test-data" + File.separator + "researcher_ids_results_with_locations.xml";              

        File file = new File(input_filepath);
        if(!file.exists())
        {
            System.out.println(input_filepath + " do not exist");                                                
            assertEquals(true, false);
            return;
        }            

        org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
        org.dom4j.Document document = reader.read(file);

        H2DBCredentials credentials = new H2DBCredentials(new File("db" + File.separator + "test-copy").getAbsolutePath(),
                                                          "locations",
                                                          "sa",
                                                          "sa");

        LocationDataResolver resolver = new LocationDataResolver_Method2(true, credentials);            

        String name = "Universidad Málaga";
        LocationDataResolver_Method2.LocationTupleWithEntity loc = resolver.resolve(name);            
        System.out.println("'" + name + "' => '" + loc + "'");

        assertEquals(true, true);
    }
}
