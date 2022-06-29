package skillpairs;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AccountSet
 */
@WebServlet("/AccountSet")
public class AccountSet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "get out here");
	}

	private Connection conn;

	public AccountSet() {
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

	public static int calculateAge(int cy, int cm, int cd, int uy, int um, int ud) {
		int age = cy - uy;
		int mon = cm - um;
		int date = cd - ud;
		if (date < 0)
			mon--;
		if (mon < 0)
			age--;
		return age;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		try (PrintWriter out = response.getWriter()) {
			if ((boolean) request.getSession().getAttribute("login") == true) {
				String UserName = request.getParameter("userName");
				byte[] bytes = UserName.getBytes(StandardCharsets.ISO_8859_1);
				UserName = new String(bytes, StandardCharsets.UTF_8);
				String datepicker = request.getParameter("date_picker");
				String gender = request.getParameter("gender");
				String job = request.getParameter("job");
				String city = request.getParameter("city");

				String[] skills = (String[]) request.getParameterValues("skill");
				String empty = new String();
				String query2 = "SELECT UserImg From Member WHERE UserID='" + request.getSession().getAttribute("UID")
						+ "'";
				System.out.println(query2);
				PreparedStatement ps2 = conn.prepareStatement(query2);
				ResultSet rs2 = ps2.executeQuery();
				rs2.next();
				String UserImg = rs2.getString("UserImg");
				if (UserImg == null) {
					System.out.println("7895210");
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter ps = response.getWriter();
					ps.print(
							"<script language='javascript'>alert('�ФW�Ǥ@�i�Ӥ�');window.location.href='AccountSet.html';</script>");
				} else {
					if (UserName.equals(empty) || skills == null) {
						response.sendRedirect("../skillpairs/AccountSet.html");
					} else {
						Statement stmt = conn.createStatement();
						stmt.executeUpdate("Update member set UserName='" + UserName + "',UserDate='" + datepicker
								+ "',UserGID='" + gender + "'," + "UserJID='" + job + "',UserCID='" + city
								+ "' WHERE UserID='" + request.getSession().getAttribute("UID") + "' ");
						String sql = "Insert Into memberskill (MSUserID,MSUserSID)VALUES('"
								+ request.getSession().getAttribute("UID") + "',?) ";
						PreparedStatement stmt2 = conn.prepareStatement(sql);

						for (String skill : skills) {
							stmt2.setString(1, skill);
							stmt2.execute();
							stmt2.clearParameters();
						}

						String query = "SELECT UserDate " + "FROM member m " + "WHERE m.UserID='"
								+ request.getSession().getAttribute("UID") + "'";
						System.out.println(query);
						PreparedStatement ps = conn.prepareStatement(query);
						ResultSet rs = ps.executeQuery();
						rs.next();

						System.out.println(rs.getDate(1));
						java.util.Date cuDate = new java.util.Date();
						System.out.println(cuDate);
						System.out.println(calculateAge(cuDate.getYear(), cuDate.getMonth(), cuDate.getDate(),
								rs.getDate(1).getYear(), rs.getDate(1).getMonth(), rs.getDate(1).getDate()));
						Integer age = calculateAge(cuDate.getYear(), cuDate.getMonth(), cuDate.getDate(),
								rs.getDate(1).getYear(), rs.getDate(1).getMonth(), rs.getDate(1).getDate());
						stmt.executeUpdate("update member set UserAge='" + age + "' WHERE UserID='"
								+ request.getSession().getAttribute("UID") + "'");

						request.getRequestDispatcher("home.html").forward(request, response);

					}
				}
			} else {
				response.sendRedirect("HomeLogin.html");
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}

