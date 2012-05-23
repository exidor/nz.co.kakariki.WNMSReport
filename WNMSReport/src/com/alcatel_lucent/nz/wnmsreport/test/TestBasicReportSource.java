package com.alcatel_lucent.nz.wnmsreport.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.junit.Test;

import com.alcatel_lucent.nz.wnmsreport.ALUReport;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_RncAp;
import com.alcatel_lucent.nz.wnmsreport.ReportGenerator;
import com.alcatel_lucent.nz.wnmsreport.TNZReportGenerator;
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
public class TestBasicReportSource extends TestCase {
	ResultSet rs = null;
	/**
   * Sets up the test fixture.
   * Called before every test case method.
   */
  protected void setUp() {
      ReportGenerator gen = new TNZReportGenerator();
      ALUReport r1 = new ALUReport_RncAp();
    
      gen.db.openConnection();
  		this.rs = gen.db.execute(r1.getQuery());
  		gen.db.closeConnection();

  }

  /**
   * Tears down the test fixture.
   * Called after every test case method.
   */
  protected void tearDown() {
  	this.rs = null;
  }

  /**
   * Tests reading a test xcm file.
   */
  @Test
  public void testRead(){
  	System.out.println(rs);
  	try {
  		//ResultSetMetaData rsmd = rs.getMetaData();
  		while (rs.next()) {
  			System.out.println(rs.getString(1)+","+rs.getTimestamp(2)+","+rs.getDouble(3));
  		}
		}
  	catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
  	assertTrue(true);
  }
}
