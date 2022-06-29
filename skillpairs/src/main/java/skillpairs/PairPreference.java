package skillpairs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PairPreference
 */
@WebServlet("/PairPreference")
public class PairPreference extends HttpServlet {
	 private static final long serialVersionUID = 1L;
	 private Connection conn;

	 /**
	  * @see HttpServlet#HttpServlet()
	  */
	 public PairPreference() {
	  super();
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

	 /**
	  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	  *      response)
	  */
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
	   throws ServletException, IOException {

	  // request.getSession().setAttribute("login", true);// TODO 測試版指令，正式版請刪除此指令
	  // response.getWriter().append("Served at:
	  // ").append(request.getContextPath());//在網頁print這個servlet的url
	  // 這個sql可以正確執行 cao
	  String sqlForPairInsert = "INSERT INTO pair (PUserID,PUserAge1,PUserGID,PUserCID,PUserAge2)"
	    + "VALUES (?,?,?,?,?)";
	  // 這個sql可以正確執行 cao
	  String sqlForPairSkillInsert = "INSERT INTO pairskill (PSUserID,PSUserSID)" + "VALUES (?,?)";
	  // 這個sql可以正確執行 cao
	  String sqlForPairUpdate = "UPDATE pair p " + "SET PUserAge1=?,PUserGID=?,PUserCID=?,PUserAge2=? "
	    + "WHERE p.PUserID=?";
	  // 這個sql可以正確執行 cao
	  String sqlForPairSkillUpdate = "UPDATE pairskill ps " + "SET PSUserSID=? " + "WHERE ps.PSUserID=?";
	  // 這個sql可以正確執行 cao
	  String sqlForPairSelectExist = "SELECT * " + "FROM pair p " + "WHERE p.PUserID=?";
	  // 這個sql可以正確執行 cao
	  String sqlForPairSkillSelectExist = "SELECT * " + "FROM pairskill ps " + "WHERE ps.PSUserID=?";
	  // 這個sql可以正確執行 慎用(不要拿來測試是否有連結資料庫等等跟刪除無關的任務) cao
	  String sqlForPairSkillDelete="DELETE FROM pairskill WHERE PSUserID=?";

	  String gender = (String) request.getParameter("gender");
	//  System.out.println(gender);
	  
	  
	  
	  String[] ages=((String)request.getParameter("age")).split(",");
	  
	  String age1 =ages[0];String age2=ages[1];
	  
	  //System.out.println(age);
	  String city = (String) request.getParameter("city");
	//  System.out.println(city);
	  String[] skills = (String[]) request.getParameterValues("skill");
	//  System.out.println(skills.length);
	//  for (String skill:skills) {
	//   System.out.println(skill);
	//  }
	  try {
	   if ((boolean) request.getSession().getAttribute("login") == true) {
	    // 是否為登入狀態
	    if (gender != null &&  city != null && skills.length != 0) {
	     // 填入的資料是否為空

	     PreparedStatement stmForCheckExist = conn.prepareStatement(sqlForPairSelectExist);
	     //stmForCheckExist.setInt(1, 20);//TODO 測試版指令
	     stmForCheckExist.setString(1,(String)request.getSession().getAttribute("UID"));//正式版指令
	     ResultSet rs= stmForCheckExist.executeQuery();
	     if(rs.next()) {
	      //目前已經存在以前設定的資料
//	      System.out.println("有資料了");
	      //int pid=rs.getInt(1);
	      String pid=(String)request.getSession().getAttribute("UID");
	      PreparedStatement stmForUpdatde = conn.prepareStatement(sqlForPairUpdate);
//	      String sqlForPairUpdate = "UPDATE pair p " + "SET PUserAge1=?,PUserGID=?,PUserCID=?,PUserAge2=? "
//	        + "WHERE p.PUserID=?";
	      stmForUpdatde.setString(1, age1);
	      stmForUpdatde.setString(2, gender);
	      stmForUpdatde.setString(3, city);//
	      stmForUpdatde.setString(4,age2);
	      stmForUpdatde.setString(5,pid);
	      stmForUpdatde.execute();
	      //刪除此用戶之前在pairskill中的資料並且重新設定
	      PreparedStatement stmForDelete = conn.prepareStatement(sqlForPairSkillDelete);
	      //String sqlForPairSkillDelete="DELETE FROM pairskill WHERE PSUserID=?";
	      stmForDelete.setString(1, pid);
	      stmForDelete.execute();
	      PreparedStatement stmForSkillChange = conn.prepareStatement(sqlForPairSkillInsert);
	      
//	      String sqlForPairSkillInsert = "INSERT INTO pairskill (PSUserID,PSUserSID)" + "VALUES (?,?)";
	      for(String skill:skills) {
	       stmForSkillChange.setString(1, pid);
	       stmForSkillChange.setString(2,skill);
	       stmForSkillChange.execute();
	       stmForSkillChange.clearParameters();
	      }
	      
	     }
	     else {
	      //目前還沒有設定過
	      //int pid=20;//TODO 測試版指令
	      //String pid="5";
	      String pid=(String)request.getSession().getAttribute("UID");//TODO 正式版指令
	      PreparedStatement stmForInsert = conn.prepareStatement(sqlForPairInsert);
//	      String sqlForPairInsert = "INSERT INTO pair (PID,PUserAge1,PUserGID,PUserCID,PUserAge2)"
//	        + "VALUES (?,?,?,?,?)";
//	      String sqlForPairInsert = "INSERT INTO pair (PUserID,PUserAge1,PUserGID,PUserCID,PUserAge2)"
	      stmForInsert.setString(1,pid);//改過setInt
	      stmForInsert.setString(2, age1);
	      stmForInsert.setString(3, gender);
	      stmForInsert.setString(4, city);//
	      stmForInsert.setString(5, age2);
	      stmForInsert.execute();
	      PreparedStatement stmForSkillChange = conn.prepareStatement(sqlForPairSkillInsert);
//	      String sqlForPairSkillInsert = "INSERT INTO pairskill (PSUserID,PSUserSID)" + "VALUES (?,?)";
	      for(String skill:skills) {
	       stmForSkillChange.setString(1, pid);//改過setInt
	       stmForSkillChange.setString(2,skill);
	       stmForSkillChange.execute();
	       stmForSkillChange.clearParameters();
	      }
//	      System.out.println("還沒有資料");
	     }
	     
//	    System.out.println("成功");
	     response.sendRedirect("Startpair.html");
	    } 
	    else {
	     response.setContentType("text/html; charset=UTF-8");
	     PrintWriter ps= response.getWriter();
	     ps.append("沒有填寫完全<br>");
	     ps.append("<a href='home.html'>回首頁</a>");
//	    System.out.println("fail");
	    }
	    // System.out.println("login true");
	   } else {
	    response.setContentType("text/html; charset=UTF-8");
	    PrintWriter ps= response.getWriter();
	    ps.append("沒有登入<br>");
	    ps.append("<a href='HomeLogin.html'>回登入頁面</a>");
	    // System.out.println("login false");
	   }
	  } catch (SQLException e) {
	   // 
	   e.printStackTrace();
	  }

	//  try {
	//   PreparedStatement stm=conn.prepareStatement(sql);
	//   stm.setInt(1, 550563);
	//   stm.setString(2, "haha");
	//   stm.setDate(3, null);
	//   stm.setString(4, "M");
	//   stm.setString(5,"j1" );
	//   stm.setString(6, "xxx@gmail.com");
	//   stm.setString(7, "c1");
	//   PrintWriter x=response.getWriter();
	//   stm.execute();
	//   x.append("good");
	//   
	//  } catch (SQLException e) {
	//   
	//   e.printStackTrace();
	//  }

	  
	 }

	 /**
	  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	  *      response)
	  */
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	   throws ServletException, IOException {

	  doGet(request, response);
	 }

	}