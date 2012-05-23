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
import java.awt.BasicStroke;
import java.awt.Stroke;

import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

public class TimeSeriesIuRChartCustomiser extends JRAbstractChartCustomizer {
	
	public final Stroke stroke_normal = new BasicStroke(1f); 
	public final Stroke stroke_dashed = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{5f,3f},0f); 
	
	/**
	 * Customiser method for IuR charts which sets first data line as a 
	 * dashed line and removes it from the legend since it is a 
	 * threshold
	 */
	public void customize(JFreeChart chart, JRChart jasperchart) {customise(chart,jasperchart);}   
	public void customise(JFreeChart chart, JRChart jasperchart){        
	    XYPlot xyplot = chart.getXYPlot();            
	    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyplot.getRenderer();            
	    XYDataset xyDataset = xyplot.getDataset();
	    LegendItemCollection legend = xyplot.getLegendItems();
	    LegendItemCollection revised = new LegendItemCollection();
	    
	    if(xyDataset != null) {
	        for(int i = 0; i < xyDataset.getSeriesCount(); i++)	{
	            if (i<1) {
	            	//make line 1 dashed
	            	renderer.setSeriesStroke(i, stroke_dashed);
	            }
	            else {
	            	//make other lines filled and add to new legend
	            	renderer.setSeriesStroke(i, stroke_normal);
	            	revised.add(legend.get(i));
	            }
	            renderer.setSeriesLinesVisible(i, true);
	        }
	    }
	    xyplot.setFixedLegendItems(revised);
	}

}

