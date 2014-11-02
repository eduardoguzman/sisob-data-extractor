<%-- 
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
--%>
<%@page import="eu.sisob.uma.crawler.ResearchersCrawlerService"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    File local_crawler_data_path = new File(System.getProperty("com.sun.aas.instanceRoot") + File.separator + "crawler-data-service");                                                                    
    ResearchersCrawlerService.releaseInstance();
    ResearchersCrawlerService.setServiceSettings(local_crawler_data_path.getAbsolutePath(), Thread.currentThread().getContextClassLoader().getResource("eu/sisob/uma/crawler/keywords"), true, false);
                    
    ResearchersCrawlerService.createInstance();
%>

Crawler local: <%=  local_crawler_data_path %>