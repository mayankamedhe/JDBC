package ass;

import java.sql.*;
import java.util.Scanner;

public class Main 
{
    private static final String url = "jdbc:postgresql://localhost:5270/postgres";
    private static final String user = "mayanka";
    private static final String password = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String id = sc.next();
        String course_id = sc.next();
        String sec_id = sc.next();
        String semester = sc.next();
        int year = sc.nextInt();
        String grade = sc.next();

 //       System.out.print("ID: " + id);
//        System.out.print("course_ID: " + course_id);
//        System.out.print("sec_ID: " + sec_id);
//        System.out.print("sem: " + semester);
//        System.out.print("year: " + year);
        
        changeGrade(id, course_id, sec_id, semester, year, grade);
//        withPrepStmt(i_id);
    }

    private static void changeGrade(String id, String course_id, String sec_id, String semester, int year, String grade) 
    {

        try (Connection conn = DriverManager.getConnection(url, user, password))
        {
            conn.setAutoCommit(false);
            
            
//            try(Statement stmt = conn.createStatement()) {
//                String query = "update takes " +
//                        "set grade = ? where ID = ? and course_id = ? and sec_id = ? and semester = ? and year = ?";
//                stmt.executeUpdate(query);
//                
//                String query2 = "update student";
//                stmt.executeUpdate(query2);
//                
//                conn.commit();
//            }
            
            
            try(PreparedStatement pStmt = conn.prepareStatement("update takes set grade = ? where id = ? and course_id = ? and sec_id = ? and semester = ? and year = ?");
                PreparedStatement pStmt2 = conn.prepareStatement("update student S set tot_cred = (select sum(credits) from takes, course where takes.course_id = course.course_id and S.ID= takes.ID.and takes.grade <> ’F’ takes.grade is not null)")) 
            {
                
            	pStmt.setString(1, grade);
            	pStmt.setString(2, id);
            	pStmt.setString(3, course_id);
            	pStmt.setString(4, sec_id);
            	pStmt.setString(5, semester);
            	pStmt.setInt(6, year);
                int check = pStmt.executeUpdate();
                int check2 = pStmt2.executeUpdate();
//                System.out.print("check " + check);
//                System.out.print("check2 " + check2);
                conn.commit();
            }
            catch(Exception ex)
            {
                conn.rollback();
                throw ex;
            }
            finally
            {
                conn.setAutoCommit(true);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}

