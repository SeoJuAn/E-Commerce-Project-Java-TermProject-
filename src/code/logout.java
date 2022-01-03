package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class logout {
		public static void logout() {
			String url = "jdbc:mysql://localhost:3306/ecommerceDB?serverTimezone=UTC";
			String user = "root";
			String pwd = "*sja23022302";
			Connection con = null;
			try {
				con = DriverManager.getConnection(url, user, pwd);
				String SQL = "delete from currentUser;";
				
				PreparedStatement p=con.prepareStatement(SQL);
				p.clearParameters();
				//p.setString(1, type);
				
				p.executeUpdate();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
