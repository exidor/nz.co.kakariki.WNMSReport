package com.alcatel_lucent.nz.wnmsreport.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.alcatel_lucent.nz.wnmsreport.ALUReport_BorgDaily;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_VccUtilisationIuR;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_VccUtilisationIuXS;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_VccUtilisationSummary;
import com.alcatel_lucent.nz.wnmsreport.ALUReport_VccUtilisationSummaryNetwork;
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
public class TestBasicReportGenerator extends TestCase {
	TNZReportGenerator gen = null;
	/**
   * Sets up the test fixture.
   * Called before every test case method.
   */
  protected void setUp() {
      this.gen = new TNZReportGenerator();
  }

  /**
   * Tears down the test fixture.
   * Called after every test case method.
   */
  protected void tearDown() {
  	this.gen = null;
  }

  /**
   * Tests reading a test xcm file.
   */
  @Test
  public void testRead(){
	  System.out.println(gen.getLinks(new ALUReport_BorgDaily()));
	  System.out.println(gen.getLinks(new ALUReport_VccUtilisationIuR()));
	  //System.out.println(gen.getLinks(new ALUReport_VccUtilisationIuXS()));
	  //System.out.println(gen.getLinks(new ALUReport_VccUtilisationSummary()));
	  //System.out.println(gen.getLinks(new ALUReport_VccUtilisationSummaryNetwork()));

  }
  
}
