package com.alcatel_lucent.nz.wnmsreport.web;
/*
 * This file is part of wnmsreport.
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alcatel_lucent.nz.wnmsreport.*;

/**
 * Primary servlet template for Report type pages. Mostly 
 * overriden by network specific subclasses.
 * TODO shift shared functionality back to this class
 */
public class BasicReportServlet extends BaseServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 20100503113500L;

	protected String getHead(HttpServletRequest request, HttpServletResponse response){
		String ss = super.getHead();//brings <head>
		//ss += "<script type=\"text/javascript\" src=\"script/bci.js\"></script>\n";
		ss += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />";
		ss += "<meta http-equiv=\"refresh\" content=\"600\">";
		ss += "<title>WNMS Report Server</title>";
		ss += "</head>";
		getScript(request,response);
		return ss;
	}

	protected String getUpperBody(){
		String ss = super.getBody();//brings <body>
		ss += "<h2>WNMS Report Generator</h2>\n";
		ss += "&nbsp;<br/>&nbsp;<br/>&nbsp;<br/>\n";
		ss += getSection1();
		ss += getNavBarText();
		ss += getSection2();
		ss += getMainFrameText();
		ss += getSection3();
		return ss;
	}

	protected String getLowerBody(){
		String ss = instructions;
		ss += super.getTail();//brings </body>
		return ss;
	}

	protected String getTail(){
		return null;
	}
	//-----------------------------

	protected String getNavBarText(){
		String ss = "<h4 class=\"light\">Report List</h4>";
		ss += getReportLinks();
		return ss;
	}


	protected String getMainFrameText(){
		String ss = "<h2>WNMS Report Generator</h2>\n";
		ss += "&nbsp;<br/>&nbsp;<br/>&nbsp;<br/>\n<hr/>\n&nbsp;<br/>\n";
		ss += "<h3>Brazen linkjacking of Borg TMU charts</h3>";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/CH-RNC01-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/CH-RNC02-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/MDR-RNC01-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/MDR-RNC02-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		//ss += getWarning();
		//ss += getReportLinks();
		return ss;
	}

	protected String getWarning(){
		String ss = "<marquee><b><font color='red' size=30>*** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***           </font></b></marquee>";
		ss += "<b><font color='red' size=30><blink>THIS IS A CAPACITY PLANNING INTERNAL TEST WEBSITE ONLY AND NOT!!! FOR PRODUCTION USE</blink></font></b>";
		ss += "<marquee><b><font color='red' size=30>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***</font></b></marquee>";
		return ss;
	}

	//CSS Sections-----------------
	protected String getSection1(){return "<div id=\"framecontent\"><div class=\"secondary\">";}
	protected String getSection2(){return "<div id=\"maincontent\"><div class=\"primary\">";}
	protected String getSection3(){return "</div></div>";}
	//-----------------------------

	protected void getScript(HttpServletRequest req, HttpServletResponse res){
		ServletContext sc = this.getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/script/bci.js");
		if (rd != null) {
			try {
				rd.include(req, res);
			}
			catch (Exception e) {
				System.out.println("Script Include Error: " + e.getMessage());
			}
		}
	}

	protected String getReportLinks(){
		TNZReportGenerator r = new TNZReportGenerator();
		//define reports here
		String ss = "<ul>";
		ss += r.getLinks(new ALUReport_AtmPort());
		ss += r.getLinks(new ALUReport_Cell3g());
		ss += r.getLinks(new ALUReport_Cell3gSummary());
		ss += r.getLinks(new ALUReport_EtherLP());
		ss += r.getLinks(new ALUReport_IpRan());
		ss += r.getLinks(new ALUReport_Iub());
		ss += r.getLinks(new ALUReport_Iub_weeks());
		ss += r.getLinks(new ALUReport_Iub_raw());
		ss += r.getLinks(new ALUReport_NodeB());
		ss += r.getLinks(new ALUReport_Rnc());
		ss += r.getLinks(new ALUReport_RncAp());
		ss += r.getLinks(new ALUReport_RncTraffic());
		ss += r.getLinks(new ALUReport_BorgDaily());
		ss += r.getLinks(new ALUReport_VccUtilisationIuR());
		ss += r.getLinks(new ALUReport_VccUtilisationIuXS());
		ss += "</ul>";
		return ss;
	}

	public static final String instructions = "<hr/><h3>Instructions</h3>"
		+" <p>Click a link, get that report.</p>\n";



	//--------------------------------------------------------------------------

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * doService
	 */
	public void doService(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setBufferSize(8192);
		
		//HttpSession ses = request.getSession(true);

		PrintWriter out = response.getWriter();

		out.println(getHead(request,response));
		out.println(getBody());
		out.println(getTail());

		out.close();
	}
}
