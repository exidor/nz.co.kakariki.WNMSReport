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
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.jasperreports.engine.JRChartCustomizer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import com.alcatel_lucent.nz.wnmsreport.ReportGenerator.ReportType;

/**
 * Super Class to be overwritten by JasperReports backing classes.
 * Provides report creation function for compiling jrxml, populating from DB
 * and exporting pdf/xls (HTML/XML not tested)  
 * @author jnramsay
 *
 */
public abstract class ALUReport {

	private static Logger jlog = Logger.getLogger("com.alcatel_lucent.nz.wnmsreport.ALUReport");

	protected Map<String,Object> params;
	protected String path;

	/**
	 * ALUReport constructor.
	 * instantiates params map as a hashmap
	 */
	public ALUReport(){
		params = new HashMap<String,Object>();
	}

	/**
	 * Common image path setter for company logo and object path instantiation
	 * @param path
	 */
	public void setImagePath(String path) {
		this.path = path;
		params.put("logo", this.path+"logo.gif");	
	}
	
	/**
	 * Build any externally generated resources. Not normally needed so not
	 * abstract but should override if required. 
	 */
	public void getExternal(){}

	/**
	 * Wrapper for JR report compiler
	 */
	public void compileReport(String f_in,String f_out) {
		System.out.println("CMP > "+f_in+":"+f_out);
		try {
			JasperCompileManager.compileReportToFile(f_in,f_out);
		} 
		catch (JRException jre) {
			jlog.error("AR: Error compiling JRXML to Jasper :: "+jre);
			System.err.println("Error compiling JRXML to Jasper :: "+jre);
		}

	}

	/**
	 * Connection instance data filler wrapper using JR FillManager
	 * @param conn DB Connection
	 * @param fnds date labeled directory string
	 * @param f_in Unpopulated template file, ./f.jasper
	 * @param f_out Populated report file in date directory ./yyyymmdd/f.jasper
	 */
	public void fillReport(Connection conn,String fnds,String f_in,String f_out){
		System.out.println("FL1 > "+f_in+":"+f_out);
		try{
			JasperFillManager.fillReportToFile(f_in, f_out,	params, conn);
		}
		catch (JRException jre){
			jlog.error("AR: Error populating Jasper Report from Connection :: "+jre);
			System.err.println("Error populating Jasper Report from Connection :: "+jre);
			jre.printStackTrace();
		} 
		catch(Exception e){
			jlog.error("AR: Undefined Error populating Jasper Report :: "+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Wrapper for JR FillManager using ResultSet.
	 * This is where your would use the alternate query mechanism, ie the getQuery
	 * from the ALUReport_* subclasses
	 * NB. Not tested
	 * @param rs
	 */
	public void fillReport(ResultSet rs,String fnds,String f_in,String f_out){
		System.out.println("FL2 > "+f_in+":"+f_out);
		try{
			JasperFillManager.fillReportToFile(f_in, f_out,	params, new JRResultSetDataSource(rs));
		}
		catch (JRException jre){
			jlog.error("AR: Error populating Jasper Report from ResultSet :: "+jre);
			System.err.println("Error populating Jasper Report from ResultSet :: "+jre);
		}
	}

	/**
	 * Wrapper for JR ExportManager also sets up defaults
	 * for the xls report type.
	 * TODO Move XL parameters to config properties file
	 */
	public void exportReport(ReportType rt,String fnds,String f_in,String f_out){
		System.out.println("EXP > "+f_in+":"+f_out);
		try{
			switch (rt){
			case PDF:
				JasperExportManager.exportReportToPdfFile(f_in,f_out); break;
			case XML:
				JasperExportManager.exportReportToXmlFile(f_in,f_out,true); break;
			case HTML:
				JasperExportManager.exportReportToHtmlFile(f_in,f_out); break;
			case XLS:
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRXlsExporterParameter.INPUT_FILE_NAME, f_in);
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, f_out);
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
				exporter.exportReport(); break;
			default:
				JasperExportManager.exportReportToPdfFile(f_in,f_out);
			}
		}
		catch (JRException jre){
			jlog.error("AR: Error exporting "+rt+" Report to File :: "+jre);
			System.err.println("Error exporting "+rt+" Report to File :: "+jre);
		}
	}

	/*getters*/

	public abstract String getQuery();

	public abstract ReportType getReportType();

	public abstract String getName();
	
	/**
	 * Display name (report name on webpage) defaults
	 * to CAPS report_name
	 * @return Display name for report
	 */
	public String getDisplayName(){
		return getName().toUpperCase();
	}

	/**
	 * List of subreports contained in 'this' report.
	 * Defaults to empty list
	 * @return list of subreports
	 */
	public List<ALUReport> getSubReport(){
		return new ArrayList<ALUReport>();
	}

	/**
	 * Returns nesting level of report. For the default case
	 * this is the top level, 0
	 * @return report level
	 */
	public int getLevel() {
		return 0;
	}

	/**
	 * No default customiser, could implement for more consistent themeing
	 * @return Generic (in this case null) chart customiser
	 */
	public JRChartCustomizer getChartCustomiser(){
		return null;

	}

}
