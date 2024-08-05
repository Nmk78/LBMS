package utils;

import java.sql.*;

//public class DB_connection {
//    private static final String URL = "jdbc:mysql://localhost:3306/LBMS";
//    private static final String USER = "root";
//    private static final String PASSWORD = "root";
//
//    static {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//}

public class DB_connection{

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/lbms", "root", "root");
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from users");
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String u_email = rs.getString("email");
			String u_name = rs.getString("name");
	
			System.out.println(id + "\t" + u_email + "\t" + u_name);
		}
		st.close();
		con.close();
	}
}