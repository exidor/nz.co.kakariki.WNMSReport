package com.alcatel_lucent.nz.wnmsreport.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.alcatel_lucent.nz.wnmsreport.graph.NetworkNodeGraph;
import com.alcatel_lucent.nz.wnmsreport.graph.TNZNetworkNodeGraph_IuR;
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
public class TestGraphReader extends TestCase {
	NetworkNodeGraph g = null;
	/**
	 * Sets up the test fixture.
	 * Called before every test case method.
	 */
	@Override
	protected void setUp() {
		g = new TNZNetworkNodeGraph_IuR();

	}

	/**
	 * Tears down the test fixture.
	 * Called after every test case method.
	 */
	@Override
	protected void tearDown() {
		this.g = null;
	}

	/**
	 * Tests reading a test xcm file.
	 */
	@Test
	public void testRead(){
		//g.populate();
		System.out.println(g);
		assertTrue(g!=null);
	}

	@Test
	public void testGraph(){
		g.populate();
		g.display();
		assertTrue(g!=null);
	}
}
