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
import com.alcatel_lucent.nz.wnmsreport.ReportGenerator.ReportType;


public class ALUReport_Cell3gSummary_ppu extends ALUReport {
	
	public ALUReport_Cell3gSummary_ppu(){
		super();
	}
	
	public String getName(){
		return "cell3gsummary_ppu";
	}

	@Override
	public String getQuery(){
		return "select id, speech_ppua, data_ppua, combined_ppua, attempts_ppua from report_cell3g_summary_ppua";
	}
	
	@Override
	public ReportType getReportType(){
		return ReportType.PDF;
	}
	@Override
	public int getLevel() {
		return 1;
	}
	
}
