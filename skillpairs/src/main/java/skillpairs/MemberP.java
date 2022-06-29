package skillpairs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 * Servlet implementation class MemberP
 */
@WebServlet("/MemberP")
public class MemberP extends HttpServlet {
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	   response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "get out here");
	  }
	  
	  private Connection conn;

	  public MemberP() {
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
			  if ((boolean) request.getSession().getAttribute("login") == true) {
			   try (PrintWriter out = response.getWriter()) {
			    
			    String UserName1 = request.getParameter("userName");
			    byte[] bytes = UserName1.getBytes(StandardCharsets.ISO_8859_1);
			    UserName1 = new String(bytes, StandardCharsets.UTF_8);

			    String datepicker = request.getParameter("date_picker");
			    String gender = request.getParameter("gender");
			    String job1 = request.getParameter("job");
			    String city1 = request.getParameter("city");
			    String[] skills = (String[]) request.getParameterValues("skill");

			    String introduction1 = request.getParameter("introduction");
			    byte[] bytess = introduction1.getBytes(StandardCharsets.ISO_8859_1);
			    introduction1 = new String(bytess, StandardCharsets.UTF_8);
			    Statement stmt = conn.createStatement();
			    String empty = new String();

			    System.out.println(job1);

			    if (UserName1.equals(empty) || skills == null) {
			     response.sendRedirect("MemberP.html");
			    } else {
			     stmt.executeUpdate("update member set UserName='" + UserName1 + "',UserDate='" + datepicker + "' "
			       + ",UserGID='" + gender + "',UserJID='" + job1 + "',UserCID='" + city1
			       + "',UserIntroduction='" + introduction1 + "' " + "where UserID='"
			       + request.getSession().getAttribute("UID") + "'"); // here

			     for (String x : skills) {
			      if (x != "") {
			       Statement stmt2 = conn.createStatement();
			       stmt2.executeUpdate("DELETE FROM memberskill WHERE MSUserID='"
			         + request.getSession().getAttribute("UID") + "'"); // here

			       String sql = "insert into memberskill(MSUserID,MSUserSID)" + "VALUES ('"
			         + request.getSession().getAttribute("UID") + "',?)";
			       PreparedStatement stmt3 = conn.prepareStatement(sql);

			       for (String skill : skills) {
			        stmt3.setString(1, skill);
			        stmt3.execute();
			        stmt3.clearParameters();
			       }
			      }
			     }

			     System.out.println("ok");

			     String query = "SELECT m.UserName,m.UserDate,g.Gender,c.City,j.Job,sk.Skill,m.UserIntroduction FROM member m "
			       + "left join memberskill ms on m.UserID=ms.MSUserID "
			       + "left join city c on m.UserCID=c.CID " + "left join job j on m.UserJID=j.JID "
			       + "left join skill sk on ms.MSUserSID=sk.SID " + "left join gender g on m.UserGID=g.GID "
			       + "WHERE m.UserID = '" + request.getSession().getAttribute("UID") + "'"; // here

			     ResultSet rs = stmt.executeQuery(query);
			     rs.next();

			     String UserName = rs.getString("UserName");
			     String UserDate = rs.getString("UserDate");
			     String UserGender = rs.getString("Gender");
			     String city = rs.getString("City");
			     String job = rs.getString("Job");
			     String introduction = rs.getString("UserIntroduction");
			     LinkedList<String> skillsforsql = new LinkedList<String>();
			     do {
			      skillsforsql.add(rs.getString("Skill"));
			     } while (rs.next());

			     String query2 = "SELECT FirstUserName FROM room where FirstUserID='"
			       + request.getSession().getAttribute("UID") + "'";
			     String query3 = "SELECT SecondUserName FROM room where SecondUserID='"
			       + request.getSession().getAttribute("UID") + "'";
			     PreparedStatement stm2 = conn.prepareStatement(query2);
			     PreparedStatement stm3 = conn.prepareStatement(query3);
			     ResultSet rs2 = stm2.executeQuery(query2);
			     ResultSet rs3 = stm3.executeQuery(query3);
			     
			     String FirstUserName=new String();
			     String SecondUserName=new String();
			     while(rs2.next()) {
			    	 FirstUserName = rs2.getString("FirstUserName");
			     }
			     
			     while(rs3.next())
			     SecondUserName = rs3.getString("SecondUserName");

			     if (FirstUserName.equals(empty)) {
			      System.out.println("room1 no data");
			     } else {
			      System.out.println("room1 have data");
			      stmt.executeUpdate("UPDATE Room set FirstUserName='" + UserName + "' WHERE FirstUserID='"
			        + request.getSession().getAttribute("UID") + "'");
			     }
			     if (SecondUserName.equals(empty)) {
			      System.out.println("room2 no data");
			     } else {
			      System.out.println("room2 have data");
			      stmt.executeUpdate("UPDATE Room set SecondUserName='" + UserName + "' WHERE SecondUserID='"
			        + request.getSession().getAttribute("UID") + "'");
			     }

			     request.setAttribute("UserName", UserName);
			     request.setAttribute("UserDate", UserDate);
			     request.setAttribute("UserGender", UserGender);
			     request.setAttribute("city", city);
			     request.setAttribute("job", job);
			     request.setAttribute("introduction", introduction);
			     request.setAttribute("skill", skillsforsql);
			     System.out.println(skillsforsql);
			     request.getRequestDispatcher("MemberP.jsp").forward(request, response);

			    }
			   } catch (Exception e) {
			    e.printStackTrace();
			   }
			  } else {
			   // response.sendRedirect("home.html");
			   response.setContentType("text/html; charset=UTF-8");
			   PrintWriter ps = response.getWriter();
			   ps.print("<script language='javascript'>alert('您尚未登入');window.location.href='HomeLogin.html';</script>");
			  }
			 }
			}