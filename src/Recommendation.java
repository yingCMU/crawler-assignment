package Brightedge;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
/*
 * Ying Li- liying@cmu.edu
 * 
 */


public class Recommendation {
	String db = "junkdb";
	MySQL ms = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String keyword="sa";
		Recommendation solution = new Recommendation();
		solution.searchMenu(keyword);
		//parseArgs(args);

	}
	public Recommendation(){
		ms  = new MySQL();
	}
	public static void parseArgs(String[] args){  
        if(args.length ==1){
        	Recommendation solution = new Recommendation();
   
			System.out.println("Our Recommendation for keyword-"+args[0]+" \n "+solution.searchMenu(args[0]));
        }
        
        else
        	System.out.println("invalid input\nusage: java Recommendation <keyword>");
	
	}
	private String searchMenu(String string) {
		try {String st = //"DROP TABLE REST;" +
				"select rid,text from  "+ this.db+"."+"menu "+
				
				"where text REGEXP '"+string+"';";
		
			
			ResultSet rs = ms.query(st);
			ResultSetMetaData meta = rs.getMetaData();
			
			while(rs.next()){
				System.out.println("-----------\n"+rs.getString("text"));
				String rid = rs.getString("rid");
				searchRest(rid);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String searchRest(String string) {
		try {String st = //"DROP TABLE REST;" +
				"select name from  "+ this.db+"."+"restaurant where id ="+string+";";
		
			
			ResultSet rs = ms.query(st);
			
			while(rs.next()){
				System.out.println("restaurant > "+rs.getString("name"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
