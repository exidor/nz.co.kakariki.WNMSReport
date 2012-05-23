package com.alcatel_lucent.nz.wnmsreport.test;

import java.sql.ResultSet;

import junit.framework.TestCase;

import org.junit.Test;

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
public class TestBasicReportConversion extends TestCase {
	ResultSet rs = null;
	TNZReportGenerator rg = null;
	/**
   * Sets up the test fixture.
   * Called before every test case method.
   */
  protected void setUp() {
      this.rg = new TNZReportGenerator();
      this.rg.db.openConnection();
  }

  /**
   * Tears down the test fixture.select * from report_atmport_t
   * Called after every test case method.
   */
  protected void tearDown() {
  	rg.db.closeConnection();
  	this.rs = null;
  }

  /**
   * Tests reading a test xcm file.
   */
  @Test
  public void testRead_atmport(){
  	rg.generateReport(new ALUReport_AtmPort());
  	assertTrue(true);
  }
  @Test
  public void testRead_iub(){
  	rg.generateReport(new ALUReport_Iub());

  }
  @Test
  public void testRead_iub_weeks(){
	  	rg.generateReport(new ALUReport_Iub_weeks());

	  }
  @Test
  public void testRead_nodeb(){
  	rg.generateReport(new ALUReport_NodeB());
  	assertTrue(true);
  }

  @Test
  public void testRead_rncap(){
  	rg.generateReport(new ALUReport_RncAp());
  	assertTrue(true);
  }

  @Test
  public void testRead_etherlp(){
  	rg.generateReport(new ALUReport_EtherLP());
  	assertTrue(true);
  }

  @Test
  public void testRead_rnctraffic(){
  	rg.generateReport(new ALUReport_RncTraffic());
  	assertTrue(true);
  }

  @Test
  public void testRead_cell3g(){
  	rg.generateReport(new ALUReport_Cell3g());
  	assertTrue(true);
  }

  @Test
  public void testRead_cell3gsummary(){
  	rg.generateReport(new ALUReport_Cell3gSummary());
  	assertTrue(true);
  }

  @Test
  public void testRead_rnc(){
  	rg.generateReport(new ALUReport_Rnc());
  	assertTrue(true);
  }

  @Test
  public void testRead_ipran(){
  	rg.generateReport(new ALUReport_IpRan());
  	assertTrue(true);
  }

  @Test
  public void testRead_borgdaily(){
	  rg.generateReport(new ALUReport_BorgDaily());
	  assertTrue(true);
  }
  
  @Test
  public void testRead_vcc(){
	  rg.generateReport(new ALUReport_VccUtilisationIuR());
	  assertTrue(true);
  }

}

