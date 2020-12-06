import java.sql.*;
import javax.swing.JOptionPane;
import static  javax.swing.JOptionPane.ERROR_MESSAGE;
public class Main {
    public static void main(String args[]){
    try{
        Class.forName("java.sql.DriverManager");
        Connection conn=(Connection)DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/feedReader","root","");
        new MainInterface();
    }catch(SQLSyntaxErrorException e){
        JOptionPane.showMessageDialog(null,"Error:Database Does not exist\nAttempting to create a new one\n","Error",ERROR_MESSAGE);
        try{
            Class.forName("java.sql.DriverManager");
            Connection conn=(Connection)DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/","root","");
            Statement stmt=(Statement)conn.createStatement();
            String q="CREATE DATABASE feedReader;";
            stmt.executeUpdate(q);
            q="USE feedReader;";
            stmt.executeUpdate(q);
            //create both tables
            q="CREATE TABLE feedSources(id int(3) primary key auto_increment,name varchar(100),link varchar(300));";
            stmt.executeUpdate(q);
            q="CREATE TABLE feedContent(feedID int(3),title varchar(300),description varchar(110) default 'No Description',publish_date varchar(50),link varchar(300));";
            stmt.executeUpdate(q);
            new MainInterface();
        }catch (Exception ex){
               JOptionPane.showMessageDialog(null,"Error:"+e,"Error",ERROR_MESSAGE);
               System.exit(0);
        }
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(null,"Error:"+e,"Error",ERROR_MESSAGE);
        System.exit(0);
    }
    }
}
