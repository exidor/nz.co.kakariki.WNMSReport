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

public class ALUReport_Cell3gSummary extends ALUReport {
	
	public ALUReport_Cell3gSummary(){
		super();
	}
	
	public String getName(){
		return "cell3gsummary";
	}

	@Override
	public String getQuery(){
		return "select id, imadlmmm,imadlamm,rlsreqm,rlsunsm,rlrm,pcmnb from iub_wmm(now()::timestamp)";
	}
	
	@Override
	public ReportType getReportType(){
		return ReportType.PDF;
	}
	
	
	@Override
	public List<ALUReport> getSubReport() {
		List<ALUReport> rl = new ArrayList<ALUReport>();
		rl.add(new ALUReport_Cell3gSummary_clfs());
		rl.add(new ALUReport_Cell3gSummary_clfscount());
		rl.add(new ALUReport_Cell3gSummary_ppu());
		rl.add(new ALUReport_Cell3gSummary_ppucount());
		rl.add(new ALUReport_Cell3gSummary_uld());
		rl.add(new ALUReport_Cell3gSummary_uldcount());
		rl.add(new ALUReport_Cell3gSummary_ult());
		rl.add(new ALUReport_Cell3gSummary_ultcount());
		return rl;
	}

}
