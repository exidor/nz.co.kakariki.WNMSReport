package com.alcatel_lucent.nz.wnmsreport.database;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * GTADatabase. GTA specific database implementation. Primarily used for its init()
 * function where paramaters are set. open/close/execute all just wrap a connector instance
 * @author jnramsay
 *
 */
public class GTADatabase implements ALUDatabase {

	public DBConnector dbc;

	public GTADatabase(){
		init();
	}
	public DBConnector getDBCConnector() {
		return dbc;
	}

	public void setDBCConnector(DBConnector dbc) {
		this.dbc = dbc;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return dbc.getConn().prepareStatement(sql);

	}
	@Override
	public void openConnection() {
		dbc.connect();
	}

	@Override
	public void closeConnection() {
		dbc.disconnect();
	}

	public void init(){
		dbc = new DBConnector();
		dbc.setPrefix("POSTGRESQL");
		//dbc.setHost("139.188.126.204");
		dbc.setHost("139.188.42.164");
		dbc.setUser("pguser");
		dbc.setPass("pgpass");
		dbc.setName("gta");
		/*
		dbc.setTable("raw_nodeb_btscell");
		dbc.setDriver(cp.getProperty(pref+".driver"));
		dbc.setPrefix(cp.getProperty(pref+".prefix"));
		dbc.setHost(cp.getProperty(pref+".host"));
		dbc.setPort(cp.getProperty(pref+".port"));
		dbc.setUser(cp.getProperty(pref+".user"));
		dbc.setPass(cp.getProperty(pref+".pass"));
		dbc.setName(cp.getProperty(pref+".name"));
		dbc.setTable(cp.getProperty(pref+".table"));
		dbc.setSelect(cp.getProperty(pref+".select"));
		dbc.setDelay(cp.getProperty(pref+".delay"));
		dbc.setFormat(cp.getProperty(pref+".format"));
		dbc.setFlush(cp.getProperty(pref+".flush"));
		jlog.info("Contact DB TP "+id);
		*/
	}

	/**
	 * execute. Executes arbitrary test query. Used for testing.
	 * @param s Arbitrary query string
	 * @return Resultset from query execution
	 */
	public ResultSet execute(String s){
		return dbc.execute(s);
	}

}
