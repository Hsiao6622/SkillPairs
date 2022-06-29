package skillpairs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class deleteFriend
 */
@WebServlet("/deleteFriend")
public class deleteFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection conn;
    
    public deleteFriend() {
        super();
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Properties prop = new Properties();
			prop.put("user", "root");
			prop.put("password", "root");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillpairs", prop);
	    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
		

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sqlForDeleteRoom="DELETE FROM room WHERE RoomID=?";
		String sqlForDeleteHistory="DELETE FROM history WHERE HRoomID=?";
		try {
			PreparedStatement psForDeleteRoom =conn.prepareStatement(sqlForDeleteRoom);
			PreparedStatement psForDeleteHistory =conn.prepareStatement(sqlForDeleteHistory);
			
			
			psForDeleteHistory.setInt(1, Integer.parseInt((String)request.getParameter("deleteRoomID")));
			psForDeleteHistory.execute();
			psForDeleteRoom.setInt(1, Integer.parseInt((String)request.getParameter("deleteRoomID")));
			psForDeleteRoom.execute();
			request.getRequestDispatcher("DatabaseAccess").forward(request, response);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

