package com.alcatel_lucent.nz.wnmsreport.chart;
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
import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

public class TimeSeriesChartCustomiser extends JRAbstractChartCustomizer {
	
	/*my bypass of american spelling*/
	public void customize(JFreeChart chart, JRChart jasperchart) {customise(chart,jasperchart);}
	public void customise(JFreeChart chart, JRChart jasperchart) {

		//LineAndShapeRenderer renderer = (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer(); 
        //renderer.setSeriesPaint(1, Color.green); 
        //renderer.setSeriesPaint(4, Color.orange);        

		//chart.setTitle("Customiser has set a new Title"); 
		
		try {

			   XYPlot plot = chart.getXYPlot();

			   NumberAxis x = (NumberAxis) plot.getDomainAxis();
			   //x.setLowerBound(x.getLowerBound()+100);
			   x.setUpperBound(x.getUpperBound()+100);
			   
			   NumberAxis y = (NumberAxis) plot.getRangeAxis();
			   y.setLowerBound(0);
			   y.setUpperBound(100);

		}
		catch(NullPointerException npe){
			System.err.println("Error setting chart axis ranges :: "+npe);
		}


	}        

}

