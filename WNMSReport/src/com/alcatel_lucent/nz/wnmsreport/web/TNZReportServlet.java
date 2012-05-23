package com.alcatel_lucent.nz.wnmsreport.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.alcatel_lucent.nz.wnmsreport.*;

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
 * TNZ specific Report servlet
 */
public class TNZReportServlet extends BaseServlet {

	private static Logger jlog = Logger.getLogger("com.alcatel_lucent.nz.wnmsreport.web.TNZReportServlet");

	private static final long serialVersionUID = 20100503113500L;
	private boolean regen;
	private String nrs,ors;

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

	/**
	 * Main frame body section. Contains data and headers
	 */
	protected String getBody(){
		String ss = super.getBody();//brings <body>
		//ss += "<h2>WNMS Report Generator for Telecom New Zealand</h2>\n";
		ss += "&nbsp;<br/>&nbsp;<br/>&nbsp;<br/>\n";
		ss += getSectionHead1();
		ss += getNavBarText();
		ss += getEndSection();
		//--------------------
		ss += getSectionHead2();
		ss += getMainFrameText();
		ss += instructions;
		ss += getInfo();
		ss += getEndSection();

		return ss;
	}

	protected String getTail(){
		String ss =	super.getTail();
		return ss;
	}
	//-----------------------------

	/**
	 * Navigation bar text. This is where the report links are located
	 */
	protected String getNavBarText(){
		String ss = "<h4 class=\"light\">Report List</h4>";
		ss += getReportLinks();
		return ss;
	}

	/**
	 * Text description and hardcoded links to fetch Borg images
	 * @return HTML body text
	 */
	protected String getMainFrameText(){
		String ss = "<h2>WNMS Report Generator for Telecom New Zealand</h2>\n";
		ss += "&nbsp;<br/>&nbsp;<br/>&nbsp;<br/>\n";
		ss += descriptivetext;
		ss += "<hr/>\n&nbsp;<br/>\n";
		ss += "<h3>Brazen linkjacking of Borg TMU charts</h3>";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/CH-RNC01-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/CH-RNC02-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/CH-RNC03-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/MDR-RNC01-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/MDR-RNC02-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/MDR-RNC03-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/WN-RNC01-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/WN-RNC02-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/HN-RNC01-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		ss+="<img src=\"https://borg.anz.lucent.com/rnc_stats/HN-RNC02-tmu_rnc_cpu_rolling.png\">&nbsp;\n";
		//ss += getWarning();
		//ss += getReportLinks();
		return ss;
	}

	/**
	 * Not used in production, this is a dev environment warning message
	 * @return A warning message (if needed)
	 */
	protected String getWarning(){
		String ss = "<marquee><b><font color='red' size=30>*** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***           </font></b></marquee>";
		ss += "<b><font color='red' size=30><blink>THIS IS A CAPACITY PLANNING INTERNAL TEST WEBSITE ONLY AND NOT!!! FOR PRODUCTION USE</blink></font></b>";
		ss += "<marquee><b><font color='red' size=30>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*** WARNING ***           *** WARNING ***           *** WARNING ***           *** WARNING ***</font></b></marquee>";
		return ss;
	}

	//CSS Sections-----------------
	/** Framecontent (Nav) div start tags */
	protected String getSectionHead1(){return "<div id=\"framecontent\"><div class=\"secondary\">";}
	/** Maincontent (Body) div start tags */
	protected String getSectionHead2(){return "<div id=\"maincontent\"><div class=\"primary\">";}
	/** Double div end tags */
	protected String getEndSection()  {return "</div></div>";}
	//-----------------------------

	/**
	 * Direct insertion of JS methods into http response object
	 * @param req
	 * @param res
	 */
	protected void getScript(HttpServletRequest req, HttpServletResponse res){
		//ServletContext sc = this.getServletContext();
		/*
		RequestDispatcher rd = sc.getRequestDispatcher("/script/CalendarPopup.js");
		RequestDispatcher rd = sc.getRequestDispatcher("/script/HelloWorld.js");
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

	/**
	 * Response reader and param setter method
	 * @param req
	 * @param res
	 */
	protected void getParameters(HttpServletRequest req, HttpServletResponse res){
		//ReportGenerator rg = new ReportGenerator();
		//String param = req.getParameter("regen");
		if (req.getParameter("regen")!=null){
			regen = getORS().compareTo(req.getParameter("regen"))==0;
			jlog.info("regen="+regen);
		}

		//button req to regen all current reports regardless of timestamp
		//String date = request.getParameter("date");//the report generated for that date
	}

	/**
	 * Incomplete implemenataion of select menu where diffeent reports would be made
	 * available through a drop down select
	 * @return HTML option text
	 */
	@Deprecated
	protected String getReportSelectMenu(){
		String ss = "<select>";
		ALUReport rep1 = new ALUReport_AtmPort();
		ss += "<option value=\""+rep1.getName()+"\">"+rep1.getName()+"</option>";
		ALUReport rep2 = new ALUReport_IpRan();
		ss += "<option value=\""+rep2.getName()+"\">"+rep2.getName()+"</option>";
		ss += "</select>";
		return ss;
	}

	/**
	 * Menu element so far incomplete that should inser JS to pop up a date select menu
	 * <SCRIPT LANGUAGE="JavaScript" ID="jscal1xx">
	 * var cal1xx = new CalendarPopup("testdiv1");
	 * cal1xx.showNavigationDropdowns();
	 * </SCRIPT>
	 *
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

	/**
	 * Creates HTML button that resets the regenerate variable so reports get
	 * rewritten
	 * @return button HTML text
	 */
	protected String getRegenerateButton(){
		String ss = "<input type=\"submit\" value=\"REBUILD\"/>";
		ss += "<input type=\"hidden\" name=\"regen\" value=\""+getNRS()+"\"/>";
		return ss;
	}

	/* New/Old Regen String getters/setters*/
	private String getNRS(){return nrs;}
	private void setNRS(String nrs){this.nrs = nrs;}

	private String getORS(){return ors;}
	private void setORS(String ors){this.ors = ors;}


	/**
	 * Navigation pane link generators.
	 * More than that generating these links this creates the actual reports
	 * @return HTML navigation header and link text
	 */
	protected String getReportLinks(){
		TNZReportGenerator r = new TNZReportGenerator();
		if(regen) r.setRegenerate(true);
		//define reports here
		String ss = "<ul><b>Cellsite</b><br/>";
		ss += r.getLinks(new ALUReport_NodeB());
		ss += r.getLinks(new ALUReport_Cell3g());
		ss += r.getLinks(new ALUReport_Cell3gSummary());
		ss += "<hr/><b>RNC</b><br/>";
		ss += r.getLinks(new ALUReport_Rnc());
		ss += r.getLinks(new ALUReport_RncAp());
		ss += r.getLinks(new ALUReport_RncTraffic());
		ss += r.getLinks(new ALUReport_BorgDaily());
		ss += "<hr/><b>Transport</b><br/>";
		ss += r.getLinks(new ALUReport_AtmPort());
		ss += r.getLinks(new ALUReport_EtherLP());
		ss += r.getLinks(new ALUReport_IpRan());
		ss += r.getLinks(new ALUReport_Iub());
		ss += r.getLinks(new ALUReport_Iub_weeks());
		ss += r.getLinks(new ALUReport_Iub_raw());
		ss += "<hr/><b>VCC</b><br/>";
		ss += r.getLinks(new ALUReport_VccUtilisationIuR());
		ss += r.getLinks(new ALUReport_VccUtilisationIuXS());
		ss += r.getLinks(new ALUReport_VccUtilisationSummary());
		ss += r.getLinks(new ALUReport_VccUtilisationSummaryNetwork());
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

	//textual data
	public static final String instructions = "<hr/><h3>Instructions</h3>"
		+" <p>Click a link, get that report.</p>\n";
	public static final String descriptivetext = "<hr/><h3>Description</h3>"
		+" <p>The Capacity Reporting page represents a dynamic interface to generate daily reports used by Capacity Planning."
		+" Reports are generated on the first load of this page for the day."
		+" If the page is loaded too early (before the backing data has populated) reports will likely be incomplete."
		+" Reloading the page in this case will not help as timestamps are truncated by day and overwrites are timestamp based."
		+" For cases such as this the 'REBUILD' button has been provided."
		+" This will reload the base compiled (not source) reports and refresh the data.</p>"
		+" <p>The page will display incorrectly in IE. If you are experiencing difficulties please consider using another browser</p>"
		+" <p>TL;DR. If the reports are wrong click REBUILD and don't use IE</p>";


	//--------------------------------------------------------------------------

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.regen = false;
	}

	/**
	 * Main servlet method.
	 */
	public void doService(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		/* This session code is designed to only trigger a rebuild when the stored
		 * session code matches the request parameter from the client hopefully
		 * avoiding the problem we obsered with browsers autorefreshing after a rebuild
		 * and requesting the same once again. Possible problems, user doesn't have
		 * cookies enabled. Multiple independent sessions could interfere with one
		 * another with multi users requesting a rebuild but nobody syncing on the
		 * correct random number (maybe not that last one) */
		HttpSession sess = request.getSession();
		setNRS(String.valueOf((int)(Math.random()*100000000))); //8 digit int
		setORS((String) sess.getAttribute("regen"));
		sess.setAttribute("regen", getNRS());


		response.setContentType("text/html");
		response.setBufferSize(8192);

		//debugging to see where requests come from and who is creating excessive load
		jlog.info("TRS: Request from :: "+request.getRemoteHost());
		jlog.info("TRS: ORS="+getORS()+", NRS="+getNRS());


		PrintWriter out = response.getWriter();

		out.println(getHead(request,response));
		out.println(getBody());
		out.println(getTail());

		out.close();
	}


}
