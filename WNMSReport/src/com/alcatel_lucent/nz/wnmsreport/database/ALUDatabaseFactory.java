package com.alcatel_lucent.nz.wnmsreport.database;

/*
 * This file is part of wnmsextract.
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
 * Not used so much anymore. Originally for selecting 
 * from among the two different DBs; test and production. Expect it 
 * would be used again if GTA migrates to its own server
 */
public class ALUDatabaseFactory {


	public static XType DEFTYPE = XType.NPO;
	public enum XType {NPO,MPM};
	
	private XType currentType;
	
	public ALUDatabaseFactory(){
		this(DEFTYPE);
	}

	public ALUDatabaseFactory(String type){
		this(XType.valueOf(type));
	}
	public ALUDatabaseFactory(XType type){
		this.currentType = type;
	}
	
	
	public ALUDatabase getInstance(){
		switch(currentType){
		case NPO:
			return new TNZDatabase();
		case MPM:
			return null;
		default:
			return null;
		}
	}
	
	
}
