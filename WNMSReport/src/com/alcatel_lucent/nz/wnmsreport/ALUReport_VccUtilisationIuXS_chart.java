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
import net.sf.jasperreports.engine.JRChartCustomizer;

import com.alcatel_lucent.nz.wnmsreport.ReportGenerator.ReportType;
import com.alcatel_lucent.nz.wnmsreport.chart.TimeSeriesIuRChartCustomiser;


public class ALUReport_VccUtilisationIuXS_chart extends ALUReport {
	
	public ALUReport_VccUtilisationIuXS_chart(){
	}

	public String getName(){
		return "vccutilisationiuxs_chart";
	}
	
	@Override
	public String getQuery(){
		return "select apid,wk,mmocc from rncapo_mbh";
	}
	
	@Override
	public ReportType getReportType() {
		return ReportType.PDF;
	}

	@Override
	public int getLevel() {
		return 1;
	}
	
	@Override
	public JRChartCustomizer getChartCustomiser(){
		return new TimeSeriesIuRChartCustomiser();
	}
}

