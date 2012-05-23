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
import com.alcatel_lucent.nz.wnmsreport.ALUReport_VccUtilisationSummary;
import com.alcatel_lucent.nz.wnmsreport.database.TNZDatabase;
/**
 * Network Node Graph for TNZ's IuR network.
 * TODO Subclass this further with CP vs UP, Acc vs Ret etc
 * @author jnramsay
 *
 */
public class TNZNetworkNodeGraph_IuR extends NetworkNodeGraph {

	//public Graph g;
	//public ALUDatabase db;

	public TNZNetworkNodeGraph_IuR(){
		//setGraph(null);
		setDb(new TNZDatabase());
	}

	/**
	 * Take image name from the one in the associated report
	 * @return path and name of image file
	 */
	public String getImageName() {
		return getImgPath()+ALUReport_VccUtilisationSummary.IMG_FNAME;
	}

	public void populate(){
		populateFromSNAPTable();
		//populateFromGraphML("test/testmap.xml");
	}

	/**
	 * Requires id::int, name and val fields representing unique identifier,
	 * display name and colour setting value.
	 * id -> name since its the display name eg MDR_RNC01.
	 * sha1_dec(id) takes a hash of the RNC name, truncates the result and does a hex to dec conversion so it
	 * fits in an int. See the functions sha1_hex and sha1_dec.
	 * val aliases the average accessibility/retainability for voice + data
	 */
	protected final String getNodeQuery(){
		return "select id as name,"
		+"sha1_dec(id) as id,"
		+"avg(acc_voicerab)+avg(acc_hsxpa) as val "
		//+"avg(ret_voice)+avg(ret_hsxpa) as ret "
		+"from int_rnc_t "
		+"where id in (select distinct split_part(iutype,'/',2) from int_inode_vcc_t) "
		+"group by id";
	}

	/**
	 * Requires src::int, dst::int and val fields to indicate either end of the edge plus colour
	 * src and dst names must match the corresponding node.id values
	 * sha1_dec hashes the RNC name for the source and destination nodes of the IuR
	 * val aliases the average tx and rx utilisations per IuR
	 */
	protected final String getEdgeQuery(){
		return "select sha1_dec(rid) as src,"
		+"sha1_dec(split_part(iutype,'/',2)) as dst,"
		+"avg(rxutilisation/rx_rate)+avg(txutilisation/tx_rate) as val "
		+"from int_inode_vcc_t "
		+"where split_part(iutype,'/',1) like 'IuR' "
		+"group by src,dst";
	}

	//NOTE
	//actionlists - commented out but probably something you'd want to customise per graph
	//dont forget FontAction, ShapeAction, SizeAction, StrokeAction

	/**
	 * Set node/edge colours
	 * @return
	 */
	/*
	private ActionList getColourActionList(){
		//new int[]{ColorLib.rgb(255, 180, 180), ColorLib.rgb(180, 255, 180),ColorLib.rgb(180, 180, 255)};
		//int[] hotpalette = ColorLib.getHotPalette();
		//int[] coolpalette = ColorLib.getCoolPalette();
		int[] palette = ColorLib.getInterpolatedPalette(10,ColorLib.rgb(0,0,255), ColorLib.rgb(255,0,0));

		DataColorAction fill = new DataColorAction("tnznet.nodes", "acc", Constants.NOMINAL, VisualItem.FILLCOLOR, palette);

        ColorAction text = new ColorAction("tnznet.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));

        DataColorAction edges = new DataColorAction("tnznet.edges", "val", Constants.NOMINAL, VisualItem.STROKECOLOR, palette);

		ActionList colour = new ActionList();
        colour.add(fill);
        colour.add(text);
        colour.add(edges);

        return colour;
	}

	private ActionList getLayoutActionList(){
		ActionList layout = new ActionList(Activity.INFINITY);
		FruchtermanReingoldLayout frl = new FruchtermanReingoldLayout("tnznet");
		frl.setMaxIterations(10000);
		layout.add(frl);

	    //layout.add(new RepaintAction());
		return layout;
	}

	private ActionList getStrokeActionList(){
		ActionList stroke = new ActionList(Activity.INFINITY);
		StrokeAction thicker = new StrokeAction();
		thicker.setDefaultStroke(new BasicStroke(2.0f));
		stroke.add(thicker);

		return stroke;
	}
	*/

}
