import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.TextStyle;
import java.util.Date;

public class SnapDealDB extends DBOperations {
    static PreparedStatement PrepareStat;
    static Date date= new Date();
    static Timestamp ts = new Timestamp(date.getTime());

    protected static void updatePrice(String bName) throws SQLException {
        Connection Conn=new DBOperations().makeJDBCConnection();
        try {
            bName = bName.replaceAll("%20", " ");
            String getQueryStatement = "Update phonedatabase set Timestamp='" + ts + "' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated ts");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Conn.close();
        }
    }

    protected static void updatePrice(String bName, String bprice, String bstock, String burl) throws SQLException {
        Connection Conn=new DBOperations().makeJDBCConnection();
        try {

            bName = bName.replaceAll("%20", " ");
            String getQueryStatement = "Update phonedatabase set SnapPrice='" + bprice
                    + "', Timestamp='" + ts
                    + "' ,SnapStock='" + bstock
                    + "',SnapLink='"
                    + burl + "' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Conn.close();
        }
    }
    protected static void updatePrice(String bprice,String bstock,String bName) throws SQLException {
        Connection Conn = new DBOperations().makeJDBCConnection();
        try {
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            String getQueryStatement = "Update phonedatabase set SnapPrice='" + bprice
                    + "', SnapTimestamp='" + ts
                    + "' ,SnapStock='" + bstock + "' where Name = '" + bName + "'";
            PrepareStat = Conn.prepareStatement(getQueryStatement);
            PrepareStat.executeUpdate();
            System.out.println("Updated");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            Conn.close();
        }
    }
}