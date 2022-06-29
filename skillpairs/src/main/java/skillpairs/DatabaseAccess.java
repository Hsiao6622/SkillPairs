package skillpairs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javatuples.Pair;
import org.javatuples.Triplet;


@WebServlet("/DatabaseAccess")
public class DatabaseAccess extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/skillpairs";
    
    static final String USER = "root";
    static final String PASS = "root"; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatabaseAccess() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType ("text/html") ;
    	String fr = new String() ;
    	// long UserID = 5211254865625401L;//TODO 正式版需移除
    	// request.getSession().setAttribute("UID", UserID);//TODO 正式版需移除
    	// long userid = (long)request.getSession().getAttribute("UID");//TODO 正式版需移除
     if ((boolean) request.getSession().getAttribute("login") == true) {
    	 String userid = (String)request.getSession().getAttribute("UID");//TODO 正式版啟用
    	   	
     	try {
     		Class.forName("com.mysql.cj.jdbc.Driver");
     		Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
     		Statement stmt = conn.createStatement();
             String sql;
//             sql = "SELECT * FROM member where UserName='"+fr+"'";
             sql = "SELECT * FROM member where UserID='"+userid+"'";
             ResultSet rs = stmt.executeQuery(sql);
             
             
             if(rs.next()) {
             	fr =rs.getString("UserName");
         		Statement stmt2 = conn.createStatement();
                 String sqlb;
                 sqlb = "SELECT * FROM room where FirstUserID='"+userid+"' || SecondUserID='"+userid+"' ";
                 ResultSet rsa = stmt2.executeQuery(sqlb);
                 
                 HashSet<HashSet<String>> rooms=new HashSet<HashSet<String>>();
                // HashMap<Integer, HashSet<String>> roomsssss=new HashMap<Integer, HashSet<String>>();
                 HashMap<Integer,Pair<String, String>> roomsssss=new HashMap<Integer, Pair<String,String>>();

                 
                 while(rsa.next()) {
                 	
                 	Statement stmt3 = conn.createStatement();
                     String sqlc;
                     sqlc = "SELECT message FROM history WHERE HRoomID='"+rsa.getInt("RoomID")+"' ORDER BY date DESC LIMIT 0,1";
                     ResultSet rsc = stmt3.executeQuery(sqlc);
                     if(rsc.next()) {
                     	Pair<String, String>PTemp=new Pair<String, String>(null, null);
                     	//temp.add(rsa.getString("FirstUserName"));
                     	PTemp=PTemp.setAt0(rsa.getString("FirstUserName"));
                     	if(fr.equals(rsa.getString("FirstUserName"))) {
//                     		temp.add(rsa.getString("SecondUserName"));
                     		PTemp=PTemp.setAt0(rsa.getString("SecondUserName"));
                     	}
                     	
//                     	temp.remove(fr);
//                     	rooms.add(new HashSet<String>(temp));
//                     	temp.add(rsc.getString("message"));
                     	PTemp=PTemp.setAt1(rsc.getString("message"));
                     	roomsssss.put(Integer.parseInt(rsa.getString("RoomID")),PTemp);
//                     	temp.clear();
                     }
                     else {
//                     	temp.add(rsa.getString("FirstUserName"));
//                     	temp.add(rsa.getString("SecondUserName"));
//                     	temp.remove(fr);
//                     	rooms.add(new HashSet<String>(temp));
                     	Pair<String, String>PTemp=new Pair<String, String>(null, null);
                     	PTemp=PTemp.setAt0(rsa.getString("FirstUserName"));
                     	if(fr.equals(rsa.getString("FirstUserName"))) {

                     		PTemp=PTemp.setAt0(rsa.getString("SecondUserName"));
                     	}
//                     	temp.add("歷史訊息");
                     	PTemp=PTemp.setAt1("歷史訊息");
                     	roomsssss.put(Integer.parseInt(rsa.getString("RoomID")), PTemp);
//                     	temp.clear();
                     	
                     }
                 }
                 
                 System.out.println(roomsssss);
                 request.setAttribute("rooms", rooms);
                 request.setAttribute("name", fr);
                 request.setAttribute("roomsssss", roomsssss);
                 
             	request.getRequestDispatcher("chatroom1.jsp").forward(request, response);
                 
                 	
                 	
              }
                 
     	

                 
             else {
             	response.sendRedirect("home.html");
             }
 			
             
     	}catch (Exception e) {
 			System.out.println(e.toString());
 			
 		}
     }else {
    	   response.setContentType("text/html; charset=UTF-8");
		   PrintWriter ps = response.getWriter();
		   ps.print("<script language='javascript'>alert('您尚未登入');window.location.href='HomeLogin.html';</script>");
     }
    	
    	
    	
    }

}