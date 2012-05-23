package com.alcatel_lucent.nz.wnmsreport.database;
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


import static java.sql.Types.BIGINT;
import static java.sql.Types.DATE;
import static java.sql.Types.DOUBLE;
import static java.sql.Types.FLOAT;
import static java.sql.Types.INTEGER;
import static java.sql.Types.NULL;
import static java.sql.Types.TIME;
import static java.sql.Types.TIMESTAMP;
import static java.sql.Types.VARCHAR;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * DB connector is a simple database connector wrapper. 
 * It has a lot of unused legacy stuff in it. This could be culled
 * but a better solution might be to make a new project of DB connectivity
 * for use in other projects 
 * TODO: Split out to seperate project (50% done, see ALUDatabaseConnector)
 */

/* 
	 expected methods
	 dbc.setDriver(cp.getProperty("DB.driver"));
	 dbc.setHost(cp.getProperty("DB.host"));
	 dbc.setPort(cp.getProperty("DB.port"));
	 dbc.setUser(cp.getProperty("DB.user"));
	 dbc.setPass(cp.getProperty("DB.pass"));
	 dbc.setName(cp.getProperty("DB.name"));
	 dbc.setTable(cp.getProperty("DB.table"));
	 dbc.setQuery(cp.getProperty("DB.query"));
	 dbc.executeQuery();
	 dbc.getData();
 */

public class DBConnector {

	public static String DEF_DATE_FMT = "yyyy-MM-dd";

	/**
	 * Allowed datatypes property names
	 * <ul>
	 * <li>S String</li>
	 * <li>I Integer</li>
	 * <li>L Long</li>
	 * <li>F Float</li>
	 * <li>P Double (Precision)</li>
	 * <li>D Date</li>
	 * <li>T Time</li>
	 * <li>X Timestamp (D T)</li>
	 * <li>N Null</li>
	 * </ul>
	 */
	public enum SQLType {
		S(VARCHAR),I(INTEGER),L(BIGINT),F(FLOAT),P(DOUBLE),D(DATE),T(TIME),X(TIMESTAMP),N(NULL);
		private final int t;
		private SQLType(int t){this.t = t;}
		public int t(){return this.t;}
	}

	/**
	 * Some convenient prefix/driver/port defaults for different 
	 * database types. MSJet is Access but not tested
	 */
	public enum ConnDefs {
		POSTGRESQL("jdbc:postgresql","org.postgresql.Driver",5432),//v>6.5
		FIREBIRD("jdbc:odbc:cdmaCapacityDB","org.firebirdsql.jdbc.FBDriver",3050),
		MYSQL("jdbc:mysql","com.mysql.jdbc.Driver",3306),//
		ORACLE("jdbc:oracle:oci","oracle.jdbc.driver.OracleDriver",1524),//oci=9,oci8=8
		MSSQL("jdbc:microsoft:sqlserver","com.microsoft.jdbc.sqlserver.SQLServerDriver",1433),
		MSJET("jdbc:odbc:cdmaCapacityDB","sun.jdbc.odbc.JdbcOdbcDriver",0);//no port?

		private final String prefix;
		private final String driver;	
		private final int port;
		private ConnDefs(String pr, String dr, int po){
			this.prefix = pr;
			this.driver = dr;
			this.port = po;
		}
		public String prefix(){return prefix;}
		public String driver(){return driver;}
		public int port(){return port;}
	}
	
	/*debugging status flag to interrogate point of failure*/
	byte status;

	/** Local database connection instance variable */
	Connection conn;

	//fields. select fields, format-string. insert types
	String driver,prefix,host,user,pass,name,table,fields,formatstring,select;
	int port,fieldcount,delay;
	ArrayList<String> fieldlist;
	ArrayList<ArrayList<String>> data;


	/**
	 * Constructor sets up connection properties
	 */
	public DBConnector(){
		this.status = 0;
	}

	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/** Sets the DB driver */
	public void setDriver(String driver){
		if(driver!=null)
			this.driver = driver;
	}	
	/** Sets the connection string prefix */
	public void setPrefix(String prefix){
		if (prefix!=null){
			ConnDefs cd = ConnDefs.valueOf(prefix);
			if (cd!=null){
				this.prefix=cd.prefix();
				setDriver(cd.driver());
				setPort(cd.port());
			}
			else
				this.prefix = prefix;
		}
	}
	/** Sets the hostname to connect to */
	public void setHost(String host){this.host = host;}	
	/** Sets the username to connect with */
	public void setUser(String user){this.user = user;}	
	/** Sets the user password to connect with */
	public void setPass(String pass){this.pass = pass;}
	/** Sets the TCP port to connect on (from String). Usually as default per DB */
	public void setPort(String port){if(port!=null)setPort(Integer.parseInt(port));}	
	/** Sets the TCP port to connect on. Usually as default per DB */
	public void setPort(int port){this.port = port;}
	/** Sets the name of the database to connect to */
	public void setName(String name){this.name = name;}
	/** Sets the name of the table to query which would be ignored if using user selects */
	public void setTable(String table){this.table = table;}
	/** Set up a canned SELECT query */
	public void setSelect(String select){this.select = select;}
	/** Some kind of delay, can't remember what this was for... (from String)*/
	public void setDelay(String delay){if(delay!=null)setDelay(Integer.parseInt(delay));}
	/** Some kind of delay, can't remember what this was for...*/
	public void setDelay(int delay){ this.delay = delay;}
	/** Sets names of fields to be used in canned select. No good reason to use this */
	public void setField(String fields){
		this.fields = fields;
		this.fieldlist = extractFields(fields);
	}
	/** Sets expected field datatype list */
	public void setFormat(String formatstring){
		this.formatstring = formatstring;
		//this.fieldcount = formatstring.length();//a bit rough!
	}
	/** Cleans the table on each new write if set. Should be used along with SOURCE retain */
	//this looks like an interface requirement from a different JDBC version, 5?
	public void setFlush(String flush) {
		// TODO Auto-generated method stub	
	}

	/* get/set for local data array*/
	public ArrayList<ArrayList<String>> getData(){return data;}
	public void setData(ArrayList<ArrayList<String>> data){
		this.data = data;
		status += 0x20;
	}
	private void setData(ResultSet data){
		setData(parse(data));
		status += 0x10;
	}

	/**
	 * Basic execution of passed in query string
	 * @param q Query string. Caution, no validation performed.
	 * @return DB resultset
	 */
	public ResultSet execute(String q){
		ResultSet rset = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(q);
			rset = stmt.getResultSet();
		}
		catch (SQLException sqle){
			System.err.println("Error somewhere in select SQL execution "+sqle);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 

		return rset;
	}


	/**
	 * Basic select query for select all on named table. Not used anymore
	 */
	public void executeSelectQuery(){
		String query;// = evaluateSelect(select);
		if(this.select==null)
			query = "SELECT * FROM "+table;	
		else
			query = evaluateSelect(select);

		//System.out.println("SELECT::"+query);
		ResultSet rset = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			rset = stmt.getResultSet();
			setData(rset);
		}
		catch (SQLException sqle){
			System.err.println("Error somewhere in select SQL execution "+sqle);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			if (rset != null) {
				try {
					stmt.close();
				} catch (SQLException sqle) {
					System.err.println("SQLE1 "+sqle);
				} // nothing we can do
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqle) {
					System.err.println("SQLE1 "+sqle);
				} // nothing we can do
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					System.err.println("SQLE2 "+sqle);
				} // nothing we can do
			}
		}	
		status +=	0x08;		
	}

	/**
	 * Executes a prepared insert using local data array and table name
	 * TODO. Eliminate need for formatstrings
	 * NB. using datestring->date->datestring is probably unnecessary if date formats are consistent
	 */
	public void executeInsertQuery(){	
		PreparedStatement stmt = null;			
		try {
			if(data!=null && !data.isEmpty()){	
				assert !data.isEmpty() : "Data Array Empty";
				//throw new Exception("No data in parse array : "+data);

				String query = "INSERT INTO "+table+" VALUES "+ placeholder(data.get(0).size());
				stmt = conn.prepareStatement(query);

				for (ArrayList<String> row : data){
					assert !row.isEmpty() : "Data Row Empty";
					int i=1;
					for (String field : row){
						int trutyp = 0;
						SQLType typ = SQLType.valueOf(this.formatstring.substring(i-1,i).toUpperCase());
						if (field==null || field.length()==0){
							/*
								if (typ==SQLType.I || typ==SQLType.L)
								field = "";//"0";
								else if (typ==SQLType.F || typ==SQLType.P)
								field = "";//"0.0";
								else
							 */
							trutyp = typ.t();
							typ = SQLType.N;
						}
						//System.out.println("Values - col:"+i+" - dat:'"+field+"' - typ:"+typ);
						switch(typ){
						case S: stmt.setString(i,field);break;
						case I: stmt.setInt(i,Integer.parseInt(field));break;
						case L: stmt.setLong(i,Long.parseLong(field));break;
						case F: stmt.setFloat(i,Float.parseFloat(field));break;
						case P: stmt.setDouble(i,Double.parseDouble(field));break;
						case T: stmt.setTime(i,java.sql.Time.valueOf(field),Calendar.getInstance());break;//2000-12-31
						case D: stmt.setDate(i,java.sql.Date.valueOf(field),Calendar.getInstance());break;//23:59:59
						case X: stmt.setTimestamp(i,java.sql.Timestamp.valueOf(tsFormat(field)),Calendar.getInstance());break;
						case N: stmt.setNull(i,trutyp);break;
						default: stmt.setString(i,field);break;//try string if nothing else?
						}
						i++;
					}
					stmt.addBatch();
					//System.out.println(row);
				}

				// submit the batch for execution
				stmt.executeBatch();

				stmt.close();
			}
		}
		//lots of potential for errors to try to report them as concisely as possible
		catch (BatchUpdateException bue){
			System.err.println("Batch Update Exception");

			int [] affectedCount = bue.getUpdateCounts();
			for (int i = 0; i < affectedCount.length; i++) {
				System.err.print(affectedCount[i] + "  ");
			}
			System.err.println();

			System.err.println("Message:     " + bue.getMessage());
			System.err.println("SQLState:    " + bue.getSQLState());
			System.err.println("NativeError: " + bue.getErrorCode());
			System.err.println();

			SQLException sqe = bue.getNextException();
			while (sqe != null) {
				System.err.println("Message:     " + sqe.getMessage());
				System.err.println("SQLState:    " + sqe.getSQLState());
				System.err.println("NativeError: " + sqe.getErrorCode());
				System.err.println();

				sqe = sqe.getNextException();
			}

		}
		catch (SQLException sqle){
			System.err.println("Error in Insert SQL execution "+sqle);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqle) {
					System.err.println("SQLE1 Stmt Close Err : "+sqle);
				} // nothing we can do
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					System.err.println("SQLE2 Conn Close Err : "+sqle);
				} // nothing we can do
			}
		}		
		//System.out.println("insert complete");
		status +=	0x04;		
	}

	/**
	 * Create connection instance using local var {@link DBConnector#conn}
	 */
	public void connect(){
		try {
			Class.forName(this.driver);
			System.out.println("URL:"+url());
			conn = DriverManager.getConnection(url(),this.user,this.pass);
			//dbmd = conn.getMetaData();
			//dbn = dbmd.getDatabaseProductName();
			//dbv = dbmd.getDatabaseProductVersion();
		} catch (ClassNotFoundException cnfe) {
			System.err.println("Can't load database driver, "+ this.driver+ " : " + cnfe);
		} catch (SQLException sqle) {
			System.err.println("Can't connect to database, "+ url() +" : " + sqle);
		}
		finally {
			//disconnect();
		}
		status +=	0x01;		
	}

	/**
	 * Close the current connection instance
	 */
	public void disconnect(){
		try {
			conn.close();
		}
		catch (SQLException sqle){
			System.out.println(sqle);
		}
		//System.out.println("disconnect complete");
		status +=	0x02;		
	}

	/**
	 * Parse a resultset to a list-list-string. Used so that SQL types
	 * dont have to be passed around everywhere
	 */
	private ArrayList<ArrayList<String>> parse(ResultSet rs){

		ArrayList<ArrayList<String>> ral = new ArrayList<ArrayList<String>>();
		ArrayList<String> al = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				//Row processing (should be one only)
				al = new ArrayList<String>();
				/* process each column */
				for (int i=1; i<=rsmd.getColumnCount(); i++){
					//System.out.println(i+"::"+rs.getString(i));//debug
					al.add(rs.getString(i));
				}
				ral.add(al);
			}
		} 
		catch (SQLException sqle) {
			System.err.println("ResultSet read error : "+sqle);
			//System.exit(1);
		}
		catch (NullPointerException npe) {
			System.err.println("DBConnector.parse NPE error "+npe);
			//System.exit(1);
		}
		finally {
			disconnect();
		}
		return ral;
	}

	/**
	 * Generates a statement argument field placeholder string
	 * for example placeholder(6) = "(?,?,?,?,?,?)"
	 */
	private String placeholder(int p){
		String ps = "(";
		for (int i=0; i<p; i++){
			ps+="?,";
		}
		return ps.substring(0,ps.length()-1)+")";
	} 

	/**
	 * Given the correct parameters constructs a prefix string
	 */
	protected String url() {
		return this.prefix+"://"+this.host+":"+this.port+"/"+this.name;
	}

	/**
	 * Splits a cs list into an arraylist. Don't really need this if allowing selects
	 */
	private ArrayList<String> extractFields(String fields){
		ArrayList<String> res = new ArrayList<String>();
		String[] splitf = fields.split(",");
		this.fieldcount = splitf.length;
		for(int i=0;i<fieldcount;i++) {
			res.add(splitf[i]);
		}
		return res;
	}

	/**
	 * Timestamp formatter. Add SDF types as needed
	 */
	private String tsFormat(String tsstr){
		Date d = null;
		DateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//needed for timestamp
		DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");//npo input format
		try {
			d = sdf1.parse(tsstr);
		} catch (ParseException pe) {
			System.err.println("Cannot parse Date String as formatted ds="+tsstr+" :: "+pe);
		}
		return sdf0.format(d);
	}

	/**
	 * Looks in select string for date delimiters which are 
	 * converted to current date
	 */
	private String evaluateSelect(String select){
		Pattern datemarkers = Pattern.compile("#(\\w+)#");
		//need patterns for ECP/CELL?
		Matcher m = datemarkers.matcher(select);
		if (m.find()){
			Calendar today = Calendar.getInstance();	
			if(delay!=0)
				today.add(Calendar.DAY_OF_MONTH, -(delay));
			SimpleDateFormat sdf = new SimpleDateFormat(m.group(1));
			select = select.substring(0,m.start())+sdf.format(today.getTime())+select.substring(m.end());
		}
		//System.out.println("SELECT::"+select);
		return select;

	}
	/**
	 * Completion value method returning a bitmap indicating last function success
	 * used to signal whether source can be cleared. Can be used for debugging or to reset a query
	 * if you have a particularly flakey db/connection-to-it
	 * <ul>
	 * <li>connect       -> 0000 0001 = 0x01</li>
	 * <li>disconnect    -> 0000 0010 = 0x02</li>
	 * <li>executeInsert -> 0000 0100 = 0x04</li>
	 * <li>executeSelect -> 0000 1000 = 0x08</li>
	 * <li>getData       -> 0001 0000 = 0x10</li>
	 * <li>setData       -> 0010 0000 = 0x20</li>
	 * </ul>
	 * @return status bitmask of sucessful functions 
	 */
	public byte getCompletionValue(){
		return status;
	}
	public void clearTable(){}
}
