import java.sql.*;
import java.util.*;
import java.util.Scanner;


public class Ass4B_part1_160050027_160050038 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input = sc.nextLine();
		//tohtml(input);
		tojson(input);
	}
	public  static void tohtml(String input) {
		try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5038/postgres", "chanchal", "");
		    Statement stmt = conn.createStatement();
		)
		{  
			conn.setAutoCommit(false);
	        try(PreparedStatement stmt1 = conn.prepareStatement(input))
	        		{
	        			ResultSet rs = stmt1.executeQuery();
	        			System.out.print("<table>\n");
	        			ResultSetMetaData rsmd = rs.getMetaData();
	        			System.out.print("<tr>");
        			    for (int i = 1; i <rsmd.getColumnCount(); i++) {
        			    	System.out.print("<th>");
        			    	System.out.print(rsmd.getColumnName(i));
        			    	System.out.print("</th>");
        			    }
        			    System.out.print("</tr>\n");
	        			while(rs.next()) {
	        				System.out.print("<tr>");
	        			    for (int i = 1; i <rsmd.getColumnCount(); i++) {
	        			    	System.out.print("<td>");
	        			    	System.out.print(rs.getString(i));
	        			    	System.out.print("</td>");
	        			    }
	        			    System.out.print("</tr>\n");
	        			}
	        			System.out.print("</table>");
	        		}
		catch (Exception ex)
		{
			conn.rollback();
			throw ex;
		} finally {
			conn.setAutoCommit(true);;
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	}
}
	
	
	public  static void tojson(String input) {
		try (
		    Connection conn = DriverManager.getConnection(
		    		"jdbc:postgresql://localhost:5038/postgres", "chanchal", "");
		    Statement stmt = conn.createStatement();
		)
		{
			conn.setAutoCommit(false);
	        try(PreparedStatement stmt1 = conn.prepareStatement(input))
	        {
	        	ResultSet rs = stmt1.executeQuery();
	        	System.out.print("{header:[");
	        	ResultSetMetaData rsmd = rs.getMetaData();
	        	for (int i = 1; i <rsmd.getColumnCount(); i++) {
        			    	if(i>1) {System.out.print(",");}
        			    	System.out.print("\"");
        			    	System.out.print(rsmd.getColumnName(i));
        			    	System.out.print("\"");
        	    }
        		System.out.print("],\n data:[");
	        			while(rs.next()) {
	        				System.out.print("	{");
	        			    for (int i = 1; i <rsmd.getColumnCount(); i++) {
	        			    	if(i>1) {System.out.print(",");}
	        			    	System.out.print(rsmd.getColumnName(i)+":");
	        			    	if(rsmd.getColumnClassName(i)=="java.lang.String") {
	        			    		System.out.print("\""+rs.getString(i)+"\"");
	        			    	}else {
	        			    		System.out.print(rs.getString(i));
	        			    	}
	        			    }
	        			    System.out.print("}\n");
	        			}
	        			System.out.print("}");
	        }
		    catch (Exception ex)
	        {
		    	conn.rollback();
		    	throw ex;
	        } finally {
	        	conn.setAutoCommit(true);;
	        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

