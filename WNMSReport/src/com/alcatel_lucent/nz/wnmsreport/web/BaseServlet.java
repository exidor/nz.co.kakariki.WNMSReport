package com.alcatel_lucent.nz.wnmsreport.web;

/*
 * This file is part of wnmsextract.
 * 
 * wnmsextract is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * wnmsextract is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Basic doget,doset functionality. Extends HttpServlet like theyre
 * all supposed to do but should be used for common functionality, formatting
 * information and textual data. Intended to be overridden since most functionality 
 * will be found in subclasses 
 */
public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 20100503113600L;

	/** warning notice */
	public final String notice = "<b>NOTICE</b><p>Beta Software." + "This page is still under development pending user verification checks."
			+ " Though it is reported that data queried from the database using this tool appears valid but you are advised that you"
			+ " use it at your own risk and should be aware that estimation of site capacity in this manner has some potential" + " pitfalls (including influence of outliers and trend approximation)"
			+ "</p><p>This notice will be removed when the confidence level is higher.</p>";

	/**
	 * Standard repeating top of the html Note. Does not include
	 * trailing head tag!
	 */
	protected String getHead() {
		String ss = "<html>";
		ss += "<head>\n<title>WNMS Report Generator</title>\n";
		ss += "<link rel=\"stylesheet\" href=\"main.css\" type=\"text/css\" media=\"all\" />\n";
		ss += "<link rel=\"shortcut icon\" href=\"image/favicon.ico\" />\n";
		/*
		 * <SCRIPT LANGUAGE="JavaScript"
		 * SRC="/javascript/calendarpopup/combined-compact/CalendarPopup.js"
		 * ></SCRIPT> <SCRIPT LANGUAGE="JavaScript"> var cal = new
		 * CalendarPopup(); </SCRIPT>
		 */

		// ss += "<script language='javascript' src=>";
		// ss += "document.location='script/AnchorPosition.js';";
		// ss += "document.location='script/CalendarPopup.js';";
		// ss += "document.location='script/date.js';";
		// ss += "document.location='script/PopupWindow.js';";
		// ss += "</script>";
		return ss;
	}

	/**
	 * Repeating body beginning
	 */
	protected String getBody() {
		String ss = "<body>\n";
		ss += "<a href=\"http://www.alcatel-lucent.com/\"><img align=\"right\" border=\"0\" src=\"image/logo.gif\"></a>\n";
		return ss;
	}

	/**
	 * Standard repeating bottom of the html
	 */
	protected String getTail() {
		String ss = "<br/>&nbsp;<hr><br/>&nbsp;\n";
		ss += "</body>\n</html>\n";
		return ss;
	}

	/**
	 * Informational message displaying contact info
	 * @return message text
	 */
	protected String getInfo() {
		String ss = "<p><i>any problems/complaints/questions please contact ";
		ss += "<a href=\"mailto:joseph.ramsay@alcatel-lucent.com\">Joseph</a> or ";
		ss += "<a href=\"mailto:ryan.scott@alcatel-lucent.com\">Ryan</a></i></p>\n";
		return ss;
	}

	/**
	 * init
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			String debug = config.getInitParameter("debug");
			if (debug != null && debug.equals("false"))
				debug = "false";
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dummy do service method. Too sepcific to act as useful super
	 * class
	 */
	public void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("text/html");
			response.setBufferSize(8192);

			PrintWriter out = response.getWriter();

			out.println(getHead());
			out.println("I DO NOTHING");
			out.println(getTail());

			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doService(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doService(request, response);
	}

	public String getServletInfo() {
		return "This WNMSReport 1 servlet says; override me.";
	}
}
