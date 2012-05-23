package com.alcatel_lucent.nz.wnmsreport.graph;
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
import java.awt.BasicStroke;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.StrokeAction;
import prefuse.action.layout.graph.FruchtermanReingoldLayout;
import prefuse.activity.Activity;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

import com.alcatel_lucent.nz.wnmsreport.database.ALUDatabase;
//import com.alcatel_lucent.nz.wnmsreport.database.TNZDatabase;


/**
 * NetworkNodeGraph. Abstract graph generating class. Worker methods perform common tasks, db
 * queries, vis generation and image export. 
 * This class is under recent development and subject to change
 */
public abstract class NetworkNodeGraph {

	public Graph g;

	public ALUDatabase db;
	
	//TODO FileSep on path
	public static String DEF_IMG_PATH = "image"+File.pathSeparator;
	public static String DEF_IMG_NAME = "graph.png";
	
	public String img_path,img_name;

	/**
	 * NetworkNodeGraph. Constructor
	 */
	public NetworkNodeGraph(){
		this.g = null;
	}

	/**
	 * populate. Selection of population method, from a supplied ML file
	 * or indirectly from config data.
	 */
	public void populate(){
		populateFromSNAPTable();
		//populateFromGraphML("test/testmap.xml");
	}
	
	/**
	 * populateFromGraphML. Reads a suplied GraphML file for node/edge data. 
	 * To implement this dynamically would need a new XML parser that triggers on 
	 * a WiPS read for config. The display data would still come from WNMS so a 
	 * DB read would also be required. Might a well just do the whole lot in a 
	 * single DB read using a join. Useful for testing though 
	 * @param filename
	 */
	public void populateFromGraphML(String filename){
	    try {
	        setGraph(new GraphMLReader().readGraph(filename));
	    /* load the data from an XML file */
	    } catch (DataIOException e) {
	        e.printStackTrace();
	        System.err.println("Error loading graph. Exiting...");
	        System.exit(1);
	    }
	}
	
	/*abstract the edge and node queries to their respective subclasses*/
	protected abstract String getNodeQuery();
	protected abstract String getEdgeQuery();
	
	/**
	 * populateFromSNAPTable. Populates both config and display data using previously
	 * parsed WiPS tables. 
	 * NB. Depends on specially aliased field names in the node and edge queries specifically:
	 * id : node identity (must be a unique integer, use sha1 hash functions for this)
	 * src : edge point 1
	 * dst : edge point 2
	 * val : value field to differentiate (colour) node/edge
	 * name : display name
	 */
	public void populateFromSNAPTable(){
		
		getDb().openConnection();
		
		try {
			DatabaseDataSource dds = ConnectionFactory.getDatabaseConnection(db.getDBCConnector().getConn());
			Table nt = dds.getData(getNodeQuery());
			Table et = dds.getData(getEdgeQuery());
			/* for debugging
			System.out.println("NODEID:"+nt.getColumnType("id")+"::"+!TypeLib.isIntegerType(nt.getColumnType("id")));
			System.out.println("SRCID:"+et.getColumnType("src")+"::"+!TypeLib.isIntegerType(et.getColumnType("src")));
			System.out.println("DESTID:"+et.getColumnType("dst")+"::"+!TypeLib.isIntegerType(et.getColumnType("dst")));
			*/
			//                                node edge1 edge2
			setGraph(new Graph(nt,et,Boolean.FALSE,"id","src","dst"));
		}
		catch (SQLException sqle) {
			System.err.println("Database Connectivity/SQL Error :: "+sqle);
		}
		catch (DataIOException dioe) {
			System.err.println("Prefuse DataSource Error :: "+dioe);
		}
		
		getDb().closeConnection();

	}
	
	/**
	 * display. Generate a prefuse display and populate it with actionlists and
	 * export as image file
	 */
	public void display(){
        Visualization vis = new Visualization();
        vis.add("network", getGraph());

        LabelRenderer nameLabel = new LabelRenderer("name");
        nameLabel.setRoundedCorner(2, 2);

        vis.setRendererFactory(new DefaultRendererFactory(nameLabel));

        //COLOURS----------------
        vis.putAction("colour", getColourActionList());

        //LAYOUT-----------------
        vis.putAction("layout", getLayoutActionList());
        
        //STROKE-----------------
        vis.putAction("stroke", getStrokeActionList());


        Display display = new Display(vis);
        display.setSize(600, 600);
        
        /* start the visualization working */
        vis.run("colour");
        vis.run("layout");
        
        //output as an image
        try {
			display.saveImage(new FileOutputStream(new File(getImgPath()+getImgName())), "png", 1.0);
		} catch (FileNotFoundException fnfe) {
			// TODO Auto-generated catch block
			fnfe.printStackTrace();
		}
        

	}
	//actionlists
	//dont forget FontAction, ShapeAction, SizeAction, StrokeAction
	
	/**
	 * getColouractionList. Set node/edge colours. Note use of 'val' field identifier.
	 * @return Colour action list
	 */
	private ActionList getColourActionList(){
		//new int[]{ColorLib.rgb(255, 180, 180), ColorLib.rgb(180, 255, 180),ColorLib.rgb(180, 180, 255)};
		//int[] hotpalette = ColorLib.getHotPalette();
		//int[] coolpalette = ColorLib.getCoolPalette();
		int[] palette = ColorLib.getInterpolatedPalette(10,ColorLib.rgb(0,0,255), ColorLib.rgb(255,0,0));

		DataColorAction fill = new DataColorAction("network.nodes", "val", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);

        ColorAction text = new ColorAction("network.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));

        DataColorAction edges = new DataColorAction("network.edges", "val", Constants.NOMINAL, VisualItem.STROKECOLOR, palette);
        
		ActionList colour = new ActionList();
        colour.add(fill);
        colour.add(text);
        colour.add(edges);
        
        return colour;
	}
	
	/**
	 * getLayoutActionList. Set graph layout, in this case to FruchtermanReingold
	 * which seems to produce the best node distribution
	 * @return Layout action list
	 */
	private ActionList getLayoutActionList(){
		ActionList layout = new ActionList(Activity.INFINITY);
		FruchtermanReingoldLayout frl = new FruchtermanReingoldLayout("network");
		frl.setMaxIterations(10000);
		layout.add(frl);

	    //layout.add(new RepaintAction());
		return layout;
	}
	
	/**
	 * setStrokeActionList. Simple stroke modifier. Default graph lines too thin, this
	 * thickens them up a bit
	 * @return Stroke action list
	 */
	private ActionList getStrokeActionList(){
		ActionList stroke = new ActionList(Activity.INFINITY);
		StrokeAction thicker = new StrokeAction();
		thicker.setDefaultStroke(new BasicStroke(2.0f));
		stroke.add(thicker);

		return stroke;
	}
	/*
	public ForceSimulator getForceSimulator(){
		ForceSimulator fs = new ForceSimulator();
		fs.addForce(new SpringForce(10.0f,10.0f));
		return fs;
	}
	*/
	
	/*getters/setters*/
	
	public Graph getGraph() {
		return g;
	}

	public void setGraph(Graph g) {
		this.g = g;
	}
	
	public ALUDatabase getDb() {
		return db;
	}

	public void setDb(ALUDatabase db) {
		this.db = db;
	}
	
	/*use default image path unless set*/
	public String getImgPath() {
		return img_path==null?DEF_IMG_PATH:img_path;
	}

	public void setImgPath(String img_path) {
		this.img_path = img_path;
	}
	
	public void setImgName(String img_name) {
		this.img_name = img_name;
		
	}
	
	/*use default image name unless set*/
	public String getImgName() {
		return img_name==null?DEF_IMG_NAME:img_name;
		
	}

}
