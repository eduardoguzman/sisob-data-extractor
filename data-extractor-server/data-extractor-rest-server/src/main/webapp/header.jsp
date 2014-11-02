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
<%@page import="eu.sisob.uma.restserver.TheConfig"%>
<%@page import="eu.sisob.uma.restserver.TheResourceBundle"%>
<html lang="en">
<head>
<!-- Force latest IE rendering engine or ChromeFrame if installed -->
<!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
<meta charset="utf-8">
<title><%=TheResourceBundle.getString("Jsp System Title")%></title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width">
<link rel="stylesheet" href="css/bootstrap.min.2.3.2.css">
<!-- Generic page styles -->
<link rel="stylesheet" href="css/style.css">
<!-- Bootstrap styles for responsive website layout, supporting different screen sizes -->
<link rel="stylesheet" href="css/bootstrap-responsive.min.2.3.2.css">
<!-- Bootstrap CSS fixes for IE6 -->
<!--[if lt IE 7]><link rel="stylesheet" href="css/bootstrap-ie6.min.css"><![endif]-->
<!-- Bootstrap Image Gallery styles -->
<link rel="stylesheet" href="css/bootstrap-image-gallery.min.2.3.2.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<!-- Shim to make HTML5 elements usable in older Internet Explorer versions -->
<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->
<script src="js/sha256.js"></script>    
<!-- blueimp Gallery styles -->
<link rel="stylesheet" href="css/blueimp-gallery.min.css">
<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
<link rel="stylesheet" href="css/jquery.fileupload-ui.css">
<!-- CSS adjustments for browsers with JavaScript disabled -->
<script src="js/jquery-1.8.2.min.js"></script>
<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
<script src="js/vendor/jquery.ui.widget.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-image-gallery.min.js"></script>
</head>
<% String back_to_list = request.getParameter("back_to_list");   
   String logout = request.getParameter("logout"); 
%>
<body>  
<div class="container">   
    <div class="page-header">
    <table>
        <tr>
            <td>
                <img src="img/sisob_logo.png" />
            </td>
            <td>
     
        <h1><%=TheResourceBundle.getString("Jsp System Title")%></h1>          
            </td>
        </tr>   
    </table>   
    <blockquote>        
            
<%
    if (!request.getParameter("user").equals("")) {
%>        
      <h4 class="text-${param.reason_type}">(${param.user} : ${param.reason})</h4>                        
        
<%
    }    
    if(logout.equals("true")){
%>
        <h4><span style="float:right;"><a href="<%=TheConfig.getInstance().getString(TheConfig.SERVER_URL)%>">Logout</a></span>   </h4>
<%
    }
%>  
    </blockquote>
    </div>
<% 
        if(back_to_list.equals("true")) { 
%>        
            
            <h5><a href="list-tasks.jsp?user=${param.user}&pass=${param.pass}">Back to listing</a></h5>   
        
<% 
        } 
%>            
</div>

