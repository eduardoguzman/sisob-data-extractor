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
/*
    This code was initially developed by Yasser Ganjisaffar (yganjisa@uci.dot.edu)
    under an Apache Software Foundation License. 
*/


package eu.sisob.uma.api.crawler4j.robotstxt;

import java.util.StringTokenizer;


public class RobotstxtParser {

	private static final String PATTERNS_USERAGENT = "(?i)^User-agent:.*";
	private static final String PATTERNS_DISALLOW = "(?i)Disallow:.*";
	private static final String PATTERNS_ALLOW = "(?i)Allow:.*";
	
	private static final int PATTERNS_USERAGENT_LENGTH = 11;
	private static final int PATTERNS_DISALLOW_LENGTH = 9;
	private static final int PATTERNS_ALLOW_LENGTH = 6;
	
	public static HostDirectives parse(String content, String myUserAgent) {
		
		HostDirectives directives = null;
		boolean inMatchingUserAgent = false;		
		
		StringTokenizer st = new StringTokenizer(content, "\n");
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			
			int commentIndex = line.indexOf("#");
			if (commentIndex > -1) {				
				line = line.substring(0, commentIndex);
			}

			// remove any html markup
			line = line.replaceAll("<[^>]+>", "");

			line = line.trim();

			if (line.length() == 0) {
				continue;
			}

			if (line.matches(PATTERNS_USERAGENT)) {
				String ua = line.substring(PATTERNS_USERAGENT_LENGTH).trim().toLowerCase();
				if (ua.equals("*") || ua.contains(myUserAgent)) {
					inMatchingUserAgent = true;
					if (directives == null) {
						directives = new HostDirectives();
					}
				} else {
					inMatchingUserAgent = false;
				}
			} else if (line.matches(PATTERNS_DISALLOW)) {
				if (!inMatchingUserAgent) {
					continue;
				}
				String path = line.substring(PATTERNS_DISALLOW_LENGTH).trim();
				if (path.endsWith("*")) {
					path = path.substring(0, path.length() - 1);
				}
				path = path.trim();
				if (path.length() > 0) {
					directives.addDisallow(path);	
				}								
			} else if (line.matches(PATTERNS_ALLOW)) {
				if (!inMatchingUserAgent) {
					continue;
				}
				String path = line.substring(PATTERNS_ALLOW_LENGTH).trim();
				if (path.endsWith("*")) {
					path = path.substring(0, path.length() - 1);
				}
				path = path.trim();
				directives.addAllow(path);
			}			
		}
		
		return directives;
	}
}
