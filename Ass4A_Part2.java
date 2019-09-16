import java.sql.*;
import java.util.Scanner;

public class Ass4A_Part2 {
    private static final String url = "jdbc:postgresql://localhost:5270/postgres";
    private static final String user = "mayanka";
    private static final String password = "";
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID: ");
        String i_id = sc.nextLine();

        noPrepStmt(i_id);
        withPrepStmt(i_id);
    }

    private static void noPrepStmt(String id) {

        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            conn.setAutoCommit(false);

            try(Statement stmt = conn.createStatement();
            	Statement stmt0 = conn.createStatement();
            	Statement stmt1 = conn.createStatement()) {
            	String q0 = "drop table instructordup";
            	String q1 = "CREATE TABLE instructordup as SELECT * from instructor";
            	String query = "update instructordup " +
                        "set salary = salary * 1.10 " +
                        "where id = '" + id + "'";
            	
            	stmt1.executeUpdate(q0);
            	stmt0.executeUpdate(q1);
                stmt.executeUpdate(query);
//                System.out.print("c1: "+ c1);
                conn.commit();
            }
            catch(Exception ex)
            {
                conn.rollback();
                throw ex;
            }
            finally{
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void withPrepStmt(String id) {

        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            conn.setAutoCommit(false);

            String query = "update instructordup set salary = salary * 1.10 where id = ?";
            String q1 = "CREATE TABLE instructordup as SELECT * from instructor";
            String q0 = "drop table instructordup";
            try(PreparedStatement stmt1 = conn.prepareStatement(q0);
            	PreparedStatement stmt0 = conn.prepareStatement(q1);
            	PreparedStatement stmt = conn.prepareStatement(query)) {
//                String query = "update instructordup " +
//                        "set salary = salary * 1.10 " +
//                        "where id = '" + id + "'";
                stmt.setString(1, id);
                stmt1.executeUpdate();
                stmt0.executeUpdate();
            	stmt.executeUpdate();
//            	System.out.print("c2 "+ c2);
//            	System.out.print("c3 "+ c3);
                conn.commit();
            }
            catch(Exception ex)
            {
                conn.rollback();
                throw ex;
            }
            finally{
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
}
