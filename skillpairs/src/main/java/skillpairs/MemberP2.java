package skillpairs;

import java.io.*;
import java.sql.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class MemberP2
 */
@WebServlet("/MemberP2")
public class MemberP2 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "get out here");
	}

	private Connection conn;

	public MemberP2() {
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
			Statement stmt = conn.createStatement();

			String query = "SELECT m.UserName,m.UserDate,c.City,j.Job,sk.Skill,g.gender,m.UserImg,m.UserIntroduction FROM member m "
					+ "left join memberskill ms on m.UserID=ms.MSUserID "
					+ "left join city c on m.UserCID=c.CID "
					+ "left join job j on m.UserJID=J.JID "
					+ "left join skill sk on ms.MSUserSID=sk.SID "
					+ "left join gender g on m.UserGID=g.GID "
					+ "WHERE m.UserID = '"+ request.getSession().getAttribute("UID") +"'"; //here
			
			ResultSet rs = stmt.executeQuery(query);
			rs.next();

			String UserName = rs.getString("UserName");
			String UserDate = rs.getString("UserDate");
			String Gender = rs.getString("Gender");
			String city = rs.getString("city");
			String job = rs.getString("job");
			String introduction = rs.getString("UserIntroduction");
			
			request.setAttribute("UserName", UserName);
			request.setAttribute("UserDate", UserDate);
			request.setAttribute("UserGender", Gender);
			request.setAttribute("city", city);
			request.setAttribute("job", job);
			LinkedList<String> skillsforsql=new LinkedList<String>();
			do {
				skillsforsql.add(rs.getString("Skill"));
			}while(rs.next());
			request.setAttribute("skill", skillsforsql);
			request.setAttribute("introduction", introduction);
			System.out.println(skillsforsql);
			request.getRequestDispatcher("MemberP.jsp").forward(request, response);
		} else {
			response.sendRedirect("HomeLogin.html");
		}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}