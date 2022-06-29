package skillpairs;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class PairMainInsert
 */
@WebServlet("/PairMainInsert")
public class PairMainInsert extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "get out here");
	}

	private Connection conn;

	public PairMainInsert() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Properties prop = new Properties();
			prop.put("user", "root");
			prop.put("password", "root");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillpairs", prop);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		try (PrintWriter out = response.getWriter()) {
			if ((boolean) request.getSession().getAttribute("login") == true) {
				String SecondUserID = request.getParameter("SecondUserID");

				Statement stmt1 = conn.createStatement();
				stmt1.executeUpdate("Insert Into Room(FirstUserID,SecondUserID) VALUES('"
						+ request.getSession().getAttribute("UID") + "','" + SecondUserID + "')");

				String query = "Select UserName From Member where UserID='" + request.getSession().getAttribute("UID")
						+ "'";
				Statement stmt2 = conn.createStatement();
				ResultSet rs = stmt2.executeQuery(query);
				rs.next();
				String FirstUserName = rs.getString("UserName");
				stmt2.executeUpdate("Update Room set FirstUserName='" + FirstUserName + "' where FirstUserID='"
						+ request.getSession().getAttribute("UID") + "' AND SecondUserID='" + SecondUserID + "'");

				String query2 = "Select UserName From Member where UserID='" + SecondUserID + "'";
				Statement stmt3 = conn.createStatement();
				ResultSet rs2 = stmt3.executeQuery(query2);
				rs2.next();
				String SecondUserName = rs2.getString("UserName");
				stmt2.executeUpdate("Update Room set SecondUserName='" + SecondUserName + "' where FirstUserID='"
						+ request.getSession().getAttribute("UID") + "' AND SecondUserID='" + SecondUserID + "'");

				request.getRequestDispatcher("pairPageImage.jsp").forward(request, response);
			} else {
				response.sendRedirect("HomeLogin.html");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
