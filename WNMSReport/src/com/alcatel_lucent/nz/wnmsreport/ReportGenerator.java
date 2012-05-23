package com.alcatel_lucent.nz.wnmsreport;
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
import java.io.File;

import org.apache.log4j.Logger;

import com.alcatel_lucent.nz.wnmsreport.database.ALUDatabase;

public class ReportGenerator {
	
	private static Logger jlog = Logger.getLogger("com.alcatel_lucent.nz.wnmsreport.ReportGenerator");


	//public static final String DEF_LNX_ABS_PATH = "/srv/tomcat6/webapps/tnz/";
	//public static final String DEF_SUN_ABS_PATH = "/var/apache/tomcat55/webapps/tnz/";
	//public static final String DEF_WIN_ABS_PATH = "C:\\service\\tomcat\\webapps\\tnz\\";

	public static final String DEF_REL_PATH = "";
	public static final String DEF_PREFIX = "report_";

	public enum ReportType {PDF,XML,HTML,XLS}

	public String fnds;

	public String RPATH,IPATH;

	public ALUDatabase db;

	/* regenerate current batch of reports */
	public boolean regenerate;

	/**
	 * ReportGenerator constructor
	 */
	public ReportGenerator(){
		//set report regeneration off
		this.setRegenerate(false);
	}

	//--------------------------------------------------------------------


	/**
	 * Generates a report chain from ALUReport template
	 * whether this report is subreport or not
	 * (if a sub then don't need mkdir/fill/export)
	 * @param report ALUReport object required
	 * @return returns a URL to the completed document
	 */
	public String generateReport(ALUReport report){
		String rn = report.getName();
		boolean sub = report.getLevel()>0;

		//fetch list of subreports
		for(ALUReport subrep : report.getSubReport()){
			generateReport(subrep);
		}
		

		String fs = System.getProperty("file.separator");
		//get report type/suffix ie pdf/xls etc
		String suff = report.getReportType().toString().toLowerCase();

		///path/to/YYYYMMDD
		String datedir = RPATH+fs+fnds;

		//e.g. /path/to/file/report_nodeb_ct.jrxml
		String barejrxml = RPATH+fs+DEF_PREFIX+rn+".jrxml";

		//e.g. /path/to/file/report_nodeb_ct.jasper
		String barejasper = RPATH+fs+DEF_PREFIX+rn+".jasper";

		//e.g. /path/to/file/YYYYMMDD/report_nodeb_ct.jasper
		String datejasper = datedir+fs+DEF_PREFIX+rn+".jasper";

		//e.g. /path/to/file/YYYYMMDD/report_nodeb_ct.pdf
		String dateoutput = datedir+fs+DEF_PREFIX+rn+"."+suff;

		//IF f.jrxml available AND [f.jasper not available OR f.jrxml.date()>f.jasper.date()] THEN compile
		if((new File(barejrxml)).exists()
				&& (!(new File(barejasper)).exists()
						|| (new File(barejrxml)).lastModified() > (new File(barejasper)).lastModified())
						|| isRegenerate() ){
			jlog.info("RG: Compile Report : RN="+rn+", R="+isRegenerate());
			report.compileReport(barejrxml,barejasper);
		}

		//IF /YYYYMMDD not available THEN mkdir
		if(!sub && !(new File(datedir).exists())){
			jlog.info("RG: Make Date Directory");
			(new File(datedir)).mkdir();
		}

		//IF f.jasper available AND [f.YYMMDD.jasper not available OR f.jasper.date()>f.YYMMDD.jasper.date()] THEN fill
		if(!sub && (new File(barejasper)).exists()
				&& (!(new File(datejasper)).exists()
						|| (new File(barejasper)).lastModified() > (new File(datejasper)).lastModified())){
			jlog.info("RG: Populate Report : RN="+rn);
			db.openConnection();
			//report.fillReport(db.execute(report.getQuery()),fnds,barejasper,datejasper);
			report.getExternal();
			report.setImagePath(IPATH+fs);
			report.fillReport(db.getDBCConnector().getConn(),fnds,barejasper,datejasper);
			db.closeConnection();
		}


		//IF f.YYMMDD.jasper available AND [f.YYMMDD.out not available OR f.YYMMDD.jasper.date()>f.YYMMDD.out.date()] THEN export
		if(!sub && (new File(datejasper)).exists()
				&& (!(new File(dateoutput)).exists()
						|| (new File(datejasper)).lastModified() > (new File(dateoutput)).lastModified())){
			jlog.info("RG: Export Report : RN="+rn);
			report.exportReport(report.getReportType(),fnds,datejasper,dateoutput);
		}

		return DEF_REL_PATH+"report/"+fnds+"/"+DEF_PREFIX+rn+"."+suff;
	}

	public void identifyReports(){
		//TODO. Mechanism to dynamically build new reports based on config file or jrxml in report directory
	}


	/** 
	 * Flag to rebuild reports regardless of timestamps
	 * @return regen flag
	 */
	public boolean isRegenerate() {
		return regenerate;
	}
	public void setRegenerate(boolean regenerate) {
		this.regenerate = regenerate;
	}

	/**
	 * Returns HTML string containing link to reports in <ul></ul> format
	 * NB. Since links are generated by the generateReport() method this actually triggers report creation 
	 * @param r
	 * @return link text to report
	 */
	public String getLinks(ALUReport r){
		return "<li><a class=\"light\" href=\""+generateReport(r)+"\">"+r.getDisplayName()+" Report</a></li>\n";

	}

}
