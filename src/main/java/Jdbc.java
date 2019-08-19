
import java.sql.*;
import java.util.Date;

public class Jdbc {
    void test(String[] d, Date Time){
        String Name=d[0];
        String Entry=d[1];
        String Link=d[2];
        try{
            Class.forName("com.sql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pricemobiile","root","root");
            //here sonoo is database name, root is username and password
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("update pricespec set flipkartPrice="+Entry+",FlipkartLink="+Link+",FlipkartTimeStamp="+Time+"where name="+Name);
            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
            con.close();
        }catch(Exception e){System.out.println(e);}
    }
}
