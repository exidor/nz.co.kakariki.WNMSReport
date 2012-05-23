package com.alcatel_lucent.nz.wnmsreport.test;

import junit.framework.Test;
import junit.framework.TestSuite;
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
public class TestSuiteGraph {

    public static Test suite() {

        TestSuite suite = new TestSuite();

        //suite.addTestSuite(TestBasicReportSource.class);
        //suite.addTestSuite(TestBasicReportGenerator.class);
        suite.addTestSuite(TestGraphReader.class);

        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
