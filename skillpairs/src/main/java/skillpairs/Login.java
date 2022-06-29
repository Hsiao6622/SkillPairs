package skillpairs;

import java.io.*;
import java.sql.*;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "get out here");
	}

	private Connection conn;

	public Login() {
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

	public String readJSONString(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
				System.out.println("line:"+line);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return json.toString();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//response.setContentType("text/html;charset=UTF-8");
		
		String json = readJSONString(request);
		JSONObject jsonObject = null;

		try  {

			jsonObject = new JSONObject(json);
			String UserID = jsonObject.getString("userID");
			String UserName = jsonObject.getString("userName");
			String UserEmail = jsonObject.getString("userEmail");
			System.out.println("JSON:"+json);
			
			request.getSession().setAttribute("login", true);
			request.getSession().setAttribute("UID", UserID);
	
			
				if (login(UserID)) {
					System.out.println("OK1");
					
					//response.sendRedirect("home.html");
					response.setHeader("URL", "home.html"); 
					System.out.println("OK1");
				} else {
					Statement stmt = conn.createStatement();
					
					stmt.executeUpdate(
							"insert into member(UserID,UserName,UserEmail) values(' " + UserID + "','" + UserName + "','" + UserEmail + "')");
					System.out.println("OK2");
					response.setHeader("URL", "AccountSet.html"); 
				}
					
					System.out.println("ok");
					System.out.println(UserID);
					System.out.println(UserName);
					System.out.println(UserEmail);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private boolean login(String UserID) throws Exception {

		boolean isRight = false;
		String sql = "SELECT * FROM member WHERE UserID = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, UserID);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			System.out.println("account exist");
			isRight = true;
		}
		return isRight;
	}

}
