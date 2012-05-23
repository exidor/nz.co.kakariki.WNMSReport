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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.alcatel_lucent.nz.wnmsreport.database.GTADatabase;

/**
 * GTA specific report generator subclass redefining report path locations
 * @author jnramsay
 *
 */
public class GTAReportGenerator extends ReportGenerator {

	private static Logger jlog = Logger.getLogger("com.alcatel_lucent.nz.wnmsreport.GTAReportGenerator");

	public static final String DEF_LNX_ABS_PATH = "/srv/tomcat6/webapps/default/";
	public static final String DEF_SUN_ABS_PATH = "/var/apache/tomcat55/webapps/gta/";
	public static final String DEF_WIN_ABS_PATH = "C:\\service\\tomcat\\webapps\\gta\\";

	public GTAReportGenerator(){
		super();
		jlog.info("GRG: Init");
		this.db = new GTADatabase();
		//leave this here since US probably want their screwy date format
		this.fnds = (new SimpleDateFormat("yyyyMMdd")).format(new Date());// Calendar.getInstance());

		if("Win".compareTo(System.getProperty("os.name").substring(0,3))==0){
			this.RPATH = DEF_WIN_ABS_PATH+"report";
			this.IPATH = DEF_WIN_ABS_PATH+"image";
		}
		else if("Sun".compareTo(System.getProperty("os.name").substring(0,3))==0){
			this.RPATH = DEF_SUN_ABS_PATH+"report";
			this.IPATH = DEF_SUN_ABS_PATH+"image";
		}
		else {
			this.RPATH = DEF_LNX_ABS_PATH+"report";
			this.IPATH = DEF_LNX_ABS_PATH+"image";
		}
	}

	//--------------------------------------------------------------------


	/**
	 * Generates a report chain from ALUReport template
	 * @param report ALUReport object required
	 * @param sub Whether this report is subreport or not
	 * (if a sub then don't need mkdir/fill/export)
	 * @return returns a URL to the completed document
	 */
	//sub=whether this report is subreport or not (if a sub then don't need mkdir/fill/export)
	/*
	public String generateReport(ALUReport report){
		String rn = report.getName();
		boolean sub = report.getLevel()>0;

		for(ALUReport subrep : report.getSubReport()){
			generateReport(subrep);
		}

		String fs = System.getProperty("file.separator");
		//ReportType rt = ReportType.PDF;
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
			report.compileReport(barejrxml,barejasper);
		}

		//IF /YYYYMMDD not available THEN mkdir
		if(!sub && !(new File(datedir).exists())){
			(new File(datedir)).mkdir();
		}

		//IF f.jasper available AND [f.YYMMDD.jasper not available OR f.jasper.date()>f.YYMMDD.jasper.date()] THEN fill
		if(!sub && (new File(barejasper)).exists()
				&& (!(new File(datejasper)).exists()
						|| (new File(barejasper)).lastModified() > (new File(datejasper)).lastModified())){
			db.openConnection();
			//report.fillReport(db.execute(report.getQuery()),fnds,barejasper,datejasper);
			report.setImagePath(IPATH+fs);
			report.fillReport(db.getDBCConnector().getConn(),fnds,barejasper,datejasper);
			db.closeConnection();
		}


		//IF f.YYMMDD.jasper available AND [f.YYMMDD.out not available OR f.YYMMDD.jasper.date()>f.YYMMDD.out.date()] THEN export
		if(!sub && (new File(datejasper)).exists()
				&& (!(new File(dateoutput)).exists()
						|| (new File(datejasper)).lastModified() > (new File(dateoutput)).lastModified())){
			report.exportReport(report.getReportType(),fnds,datejasper,dateoutput);
		}

		return DEF_REL_PATH+"report/"+fnds+"/"+DEF_PREFIX+rn+"."+suff;
	}
	*/

}
