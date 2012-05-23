package com.alcatel_lucent.nz.wnmsreport.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alcatel_lucent.nz.wnmsreport.ALUReport;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_AtmPort;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_Cell3g;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_Cell3gSummary;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_EtherLP;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_IpRan;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_Iub;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_Iub_raw;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_Iub_weeks;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_NodeB;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_Rnc;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_RncAp;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_RncTraffic;
import com.alcatel_lucent.nz.wnmsreport.GTAReportGenerator;

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

/**
 * BasicReportServlet.
 */
public class GTAReportServlet extends BaseServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 20100503113500L;
	private boolean regen;

	protected String getHead(HttpServletRequest request, HttpServletResponse response){
		String ss = super.getHead();//brings <head>
		//ss += "<script type=\"text/javascript\" src=\"script/bci.js\"></script>\n";
		//ss += "<script language=\"JavaScript\">";
		//ss += "var cal = new CalendarPopup();";
		//ss += "</script>";
		ss += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />";
		ss += "<meta http-equiv=\"refresh\" content=\"600\">";
		ss += "<title>WNMS Report Server</title>";
		ss += "</head>";
		getScript(request,response);
		getParameters(request,response);

		return ss;
	}

	protected String getUpperBody(){
		String ss = super.getBody();//brings <body>
		//ss += "<h2>WNMS Report Generator for GTA Teleguam</h2>\n";
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
		//ss += super.getTail();//brings </body>
		return ss;
	}

	protected String getTail(){
		String ss =	super.getTail();
		return ss;
	}
	//-----------------------------

	protected String getNavBarText(){
		String ss = "<h4 class=\"light\">Report List</h4>";
		ss += getReportLinks();
		return ss;
	}


	protected String getMainFrameText(){
		String ss = "<h2>WNMS Report Generator for GTA Teleguam</h2>\n";
		ss += "&nbsp;<br/>&nbsp;<br/>&nbsp;<br/>\n<hr/>\n&nbsp;<br/>\n";
		//ss+="<frame name=\"GTATeleGuam\" src=\"http://www.gta.net/\"/>";
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
	protected String getSection1(){return "<div id=\"framecontent\"><div class=\"inner\">";}
	protected String getSection2(){return "</div></div><div id=\"maincontent\"><div class=\"inner\">";}
	protected String getSection3(){return "</div></div>";}
	//-----------------------------

	protected void getScript(HttpServletRequest req, HttpServletResponse res){
		//ServletContext sc = this.getServletContext();
		/*
		RequestDispatcher rd = sc.getRequestDispatcher("/script/CalendarPopup.js");
		if (rd != null) {
			try {
				rd.include(req, res);
			}
			catch (Exception e) {
				System.out.println("Script Include Error: " + e.getMessage());
			}
		}
		*/
	}

	protected void getParameters(HttpServletRequest request, HttpServletResponse response){
		//ReportGenerator rg = new ReportGenerator();
		String param1 = request.getParameter("regen");
		if (param1!=null){
			regen = "1".compareTo(param1)==0;
		}

		//button req to regen all current reports regardless of timestamp
		//String date = request.getParameter("date");//the report generated for that date
	}

	protected String getReportSelectMenu(){
		String ss = "<select>";
		ALUReport rep1 = new ALUReport_AtmPort();
		ss += "<option value=\""+rep1.getName()+"\">"+rep1.getName()+"</option>";
		ALUReport rep2 = new ALUReport_IpRan();
		ss += "<option value=\""+rep2.getName()+"\">"+rep2.getName()+"</option>";
		ss += "</select>";
		return ss;
	}

	/* <SCRIPT LANGUAGE="JavaScript" ID="jscal1xx">
	 * var cal1xx = new CalendarPopup("testdiv1");
	 * cal1xx.showNavigationDropdowns();
	 * </SCRIPT>
	 */
	protected String getDateSelectMenu(){
		String ss = "<script language=\"JavaScript\" id=\"cal\">";
		ss += "var cal = new CalendarPopup(\"dateselect\")";
		ss += "cal.showNavigationDropdowns()";
		ss += "</script>";
		ss += "<input type=\"text\" name=\"date\" value=\"\" size=15>";
		ss += "<a href=\"#\" onClick=\"cal.select(document.forms[0].date,'anchor','dd/MM/yyyy'); return false;\" title=\"cal.select(document.forms[0].date,'anchor','dd/MM/yyyy'); return false;\" name=\"anchor\" id=\"anchor\">V</a>";
		return ss;
	}

	protected String getRegenerateButton(){
		String ss = "<input type=\"submit\" value=\"REBUILD\"/>";
		ss += "<input type=\"hidden\" name=\"regen\" value=\"1\"/>";
		return ss;
	}

	protected String getReportLinks(){
		GTAReportGenerator r = new GTAReportGenerator();
		if(regen) r.setRegenerate(true);
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
		//ss += r.getLinks(new ALUReport_BorgDaily());
		ss += "</ul>";
		ss += "<hr/><form name=\"repsel\">";
		ss += "<table>";
		//ss += "<tr><td>"+getReportSelectMenu()+"</td></tr>";
		//ss += "<tr><td>"+getDateSelectMenu()+"</td></tr>";
		ss += "<tr><td>"+getRegenerateButton()+"</td></tr>";
		ss += "</table></form></br>";
		ss += regen?"+":"-";
		return ss;
	}

	public static final String instructions = "<hr/><h3>Instructions</h3>"
		+" <p>Click a link, get that report.</p>\n";



	//--------------------------------------------------------------------------

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.regen = false;
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
		out.println(getUpperBody());
		out.println(getLowerBody());
		out.println(getTail());

		out.close();
	}
}
