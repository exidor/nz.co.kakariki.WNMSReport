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
import java.util.ArrayList;
import java.util.List;

import com.alcatel_lucent.nz.wnmsreport.ReportGenerator.ReportType;
import com.alcatel_lucent.nz.wnmsreport.graph.NetworkNodeGraph;
import com.alcatel_lucent.nz.wnmsreport.graph.TNZNetworkNodeGraph_IuR;


public class ALUReport_VccUtilisationSummary extends ALUReport {
	
	public static String IMG_FNAME = "tnznetgraph.png";
	
	public NetworkNodeGraph nng;
	
	public ALUReport_VccUtilisationSummary(){
		/*
		java.sql.Timestamp t = new java.sql.Timestamp((long) 12.3);
		String s = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(t);
		java.util.Date.parse(s);
		*/

	}

	/**
	 * Generate NNG and drop image in common image directory
	 */
	@Override
	public void getExternal(){
		nng = new TNZNetworkNodeGraph_IuR();
		nng.setImgName(IMG_FNAME);
		nng.populate();
		nng.display();
	}

	public String getName(){
		return "vccutilisation_summary";
	}
	
	@Override
	public String getDisplayName(){
		return "VCC RNC Summary";
	}
	
	@Override
	public String getQuery(){
		return "select distinct rid from int_inode_vcc_t order by rid asc";
	}
	
	@Override
	public ReportType getReportType() {
		return ReportType.PDF;
	}
	
	@Override
	public List<ALUReport> getSubReport() {
		List<ALUReport> rl = new ArrayList<ALUReport>();
		rl.add(new ALUReport_VccUtilisationSummary_chart());
		return rl;
	}
	
	/**
	 * setUImagePath. Gets overridden for additional image parameters
	 */
	@Override
	public void setImagePath(String path) {
		super.setImagePath(path);	
		params.put("tnznetgraph", path+nng.getImgName());	
	}

}
