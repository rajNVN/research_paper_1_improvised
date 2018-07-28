package IntialWork;
import util.DataBase.DBConstants;

import java.sql.*;
import java.util.*;

import static Algorithm3.Haversine.calculateHaversine;

public class UpdateScore {

    public static void main(String args[]) throws SQLException {
      /*  HashMap<Integer, String> index = new HashMap<Integer, String>();
        Statement con = DriverManager.getConnection(DBConstants.DB_DRIVER_TYPE.getConstValue() + ":" + DBConstants.DB_LOCATION.getConstValue()).createStatement();
        int i=1;
        double lon1=0.0,lat1=0.0;
        while(i<=62858)
        {
            ResultSet rs=con.executeQuery("SELECT Long,Lat FROM queries WHERE id="+i+";");
            double lat=rs.getDouble("Long");
            double lon=rs.getDouble("Lat");
            String res=new Double(calculateHaversine(lon1,lat1,lon,lat)).toString();
            System.out.println("Result :"+i);
            con.executeUpdate("UPDATE queries SET Weight='"+res+"' WHERE id="+i+" ;");
            i++;
        }
    */
   System.out.println(calculateHaversine(-115.45,56.56, -2.52115,57.66489 ));
    }
}