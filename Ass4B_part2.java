import java.sql.*;
import java.util.*;
import java.util.Scanner;


public class Ass4B_part2_160050027_160050038 {
	public static void main(String[] args) {
		try (
				Connection conn = DriverManager.getConnection(
	    		"jdbc:postgresql://localhost:5038/postgres", "chanchal", "");
				Statement stmt = conn.createStatement();
		)
		{
			stmt.executeUpdate("create temporary table cost("+
					"ID varchar(20) primary key,"+
					"cost int)");
			
			//String table="";
			//PreparedStatement stmt2 = conn.prepareStatement(table);
			ResultSet maxh = stmt.executeQuery("with recursive heights(id, h) as ( "+
			"select part.id, 1 from part union select subpart.pid, heights.h + 1 from "+
					"heights, subpart where heights.id = subpart.spID) select id, max(h)"+
			"from heights group by id "
			+ "order by max(h) asc");
			
			//ResultSet maxh = stmt2.executeQuery();
			ResultSetMetaData maxhmd = maxh.getMetaData();
			while(maxh.next()) {
				String ID = maxh.getString(1);
				String q1="select cost from part where ID = "+ "'"+ID+"'";
				PreparedStatement stmt3 = conn.prepareStatement(q1);
				ResultSet c1 = stmt3.executeQuery();
				Integer cost = 0;
			
				String q2="select spID,number from subpart where pID = "+ "'"+ID+"'";
				PreparedStatement stmt4 = conn.prepareStatement(q2);
				ResultSet c2 = stmt4.executeQuery();
			
				while(c1.next()) {
					cost=c1.getInt(1);
				}
				while(c2.next()) {
					String q3="select cost from cost where ID = "+ "'"+c2.getString(1)+"'";
					PreparedStatement stmt5= conn.prepareStatement(q3);
					ResultSet c3 = stmt5.executeQuery();
					Integer cc3=0;
					while(c3.next()) {cc3 = c3.getInt(1);}
					cost = cost + cc3*c2.getInt(2);
				}
			
				String q4="insert into cost values('"+ID+"',"+cost+")";
				PreparedStatement stmt6 = conn.prepareStatement(q4);
				stmt6.executeUpdate();
			
			}
		
		
			while(true) {
				Scanner sc = new Scanner(System.in);
				String input = sc.nextLine();
				String query = "select cost from cost where ID='"+input+"'";

				conn.setAutoCommit(false);
				try(PreparedStatement stmt1 = conn.prepareStatement(query))
				{
	        			ResultSet rs = stmt1.executeQuery();
	        			while(rs.next()) {
	        				System.out.print(rs.getInt(1));
	        				System.out.print("\n");
	        			}
				}
				catch (Exception ex){
					conn.rollback();
					throw ex;
				} finally {
					conn.setAutoCommit(true);
				}
	        }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
