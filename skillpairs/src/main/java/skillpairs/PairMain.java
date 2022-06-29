package skillpairs;

import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/PairMain")
public class PairMain extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "get out here");
	}

	private Connection conn;

	public PairMain() {
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
				String query = "SELECT p.PUserID,p.PUserAge1,p.PUserAge2,p.PUserGID,p.PUserCID,ps.PSUserSID FROM pair p "
						+ "left join pairskill ps on ps.PSUserID=p.PUserID " + "WHERE PUserID='"
						+ request.getSession().getAttribute("UID") + "'"; // HERE

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				rs.next();
				String PUserID = rs.getString("PUserID");
				Integer PUserAge1 = rs.getInt("PUserAge1");
				Integer PUserAge2 = rs.getInt("PUserAge2");
				String PUserGID = rs.getString("PUserGID");
				String PUserCID = rs.getString("PUserCID");
				String s = "";

				String O = "O";
				do {
					s += "'" + rs.getString("PSUserSID") + "\',";
				} while (rs.next());
				s += "";
				s = s.substring(0, s.length() - 1);

				Integer count;
				if (request.getSession().getAttribute("counter") == null) {
					count = 0;
				} else {
					count = (int) request.getSession().getAttribute("counter") + 1;
				}
				request.getSession().setAttribute("counter", count);
				String query1 = "SELECT SecondUserID FROM Room WHERE FirstUserID='"
						+ request.getSession().getAttribute("UID") + "' "; // HERE
				Statement stmt1 = conn.createStatement();
				ResultSet rs1 = stmt1.executeQuery(query1);
				if (rs1.next()) {
					String s1 = "";
					do {
						s1 += "" + rs1.getString("SecondUserID") + ",";
					} while (rs1.next());
					s1 += "";
					s1 = s1.substring(0, s1.length() - 1);
					if (PUserGID.equals(O)) {
						String query2 = "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID + "' AND UserID NOT IN(" + s1
								+ ") AND  m.UserAge between (? and ?) AND m.UserCID=? " + "UNION "
								+ "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID "
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID
								+ "' AND UserID NOT IN(" + s1 + ") AND (m.UserAge between ? and ? OR m.UserCID=?) "
								+ "LIMIT ?,1 ";
						PreparedStatement stmt2 = conn.prepareStatement(query2);
						stmt2.setInt(1, (int) PUserAge1);
						stmt2.setInt(2, (int) PUserAge2);
						stmt2.setString(3, PUserCID);

						stmt2.setInt(4, (int) PUserAge1);
						stmt2.setInt(5, (int) PUserAge2);
						stmt2.setString(6, PUserCID);
						stmt2.setInt(7, count);
						
						ResultSet rs2 = stmt2.executeQuery();

						if (rs2.next()) {
							String UserID = rs2.getString("UserID");
							String query3 = "SELECT sk.skill from memberskill ms "
									+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "' "
									+ "AND ms.MSUserSID in (" + s + ")";
							PreparedStatement stmt3 = conn.prepareStatement(query3);
							ResultSet rs3 = stmt3.executeQuery(query3);
							if (rs3.next()) {
								
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								rs4.next();
								LinkedList<String> skillsforsql = new LinkedList<String>();
								do {
									skillsforsql.add(rs4.getString("Skill"));
								} while (rs4.next());
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
	
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(有相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							} else {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
							
								LinkedList<String> skillsforsql = new LinkedList<String>();
								while (rs4.next()){
									skillsforsql.add(rs4.getString("Skill"));
								} 
								
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");

								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(無相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							}
						} else {
							response.sendRedirect("NoPair.html");
						}
					} else {
						String query2 = "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID + "' AND UserID NOT IN(" + s1
								+ ") AND m.UserGID=? AND m.UserAge between (? and ?) AND m.UserCID=? " + "UNION "
								+ "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID
								+ "' AND UserID NOT IN(" + s1
								+ ") AND (m.UserGID=? OR m.UserAge between ? and ? OR m.UserCID=?) " + "LIMIT ?,1 ";

						PreparedStatement stmt2 = conn.prepareStatement(query2);
						stmt2.setString(1, PUserGID);
						stmt2.setInt(2, (int) PUserAge1);
						stmt2.setInt(3, (int) PUserAge2);
						stmt2.setString(4, PUserCID);

						stmt2.setString(5, PUserGID);
						stmt2.setInt(6, (int) PUserAge1);
						stmt2.setInt(7, (int) PUserAge2);
						stmt2.setString(8, PUserCID);
						stmt2.setInt(9, count);

						ResultSet rs2 = stmt2.executeQuery();
						if (rs2.next()) {
							String UserID = rs2.getString("UserID");
							String query3 = "SELECT sk.skill from memberskill ms "
									+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "' " // here
									+ "AND ms.MSUserSID in (" + s + ")";
							PreparedStatement stmt3 = conn.prepareStatement(query3);
							ResultSet rs3 = stmt3.executeQuery(query3);
							if (rs3.next()) {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								rs4.next();
								LinkedList<String> skillsforsql = new LinkedList<String>();
								do {
									skillsforsql.add(rs4.getString("Skill"));
								} while (rs4.next());
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
								
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(有相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							} else {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								rs4.next();
								LinkedList<String> skillsforsql = new LinkedList<String>();
								do {
									skillsforsql.add(rs4.getString("Skill"));
								} while (rs4.next());
								
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
								
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(無相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							}
						} else {
							response.sendRedirect("NoPair.html");
						}

					}
				} else {
					if (PUserGID.equals(O)) {
						String query2 = "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID + "' "
								+ "AND m.UserAge between (? and ?) AND m.UserCID=? " + "UNION "
								+ "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID + "' " + "AND (m.UserAge between ? and ? OR m.UserCID=?) "
								+ "LIMIT ?,1 ";

						PreparedStatement stmt2 = conn.prepareStatement(query2);
						stmt2.setInt(1, (int) PUserAge1);
						stmt2.setInt(2, (int) PUserAge2);
						stmt2.setString(3, PUserCID);

						stmt2.setInt(4, (int) PUserAge1);
						stmt2.setInt(5, (int) PUserAge2);
						stmt2.setString(6, PUserCID);
						stmt2.setInt(7, count);

						ResultSet rs2 = stmt2.executeQuery();
						if (rs2.next()) {
							String UserID = rs2.getString("UserID");
							String query3 = "SELECT sk.skill from memberskill ms "
									+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "' "
									+ "AND ms.MSUserSID in (" + s + ")";

							PreparedStatement stmt3 = conn.prepareStatement(query3);
							ResultSet rs3 = stmt3.executeQuery(query3);
							if (rs3.next()) {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								rs4.next();
								LinkedList<String> skillsforsql = new LinkedList<String>();
								do {
									skillsforsql.add(rs4.getString("Skill"));
								} while (rs4.next());
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
								
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(有相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							} else {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								
								LinkedList<String> skillsforsql = new LinkedList<String>();
								while (rs4.next()) {
									skillsforsql.add(rs4.getString("Skill"));
								} 
								
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
								
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(無相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							}

						} else {
							response.sendRedirect("NoPair.html");
						}
					} else {
						String query2 = "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID + "' "
								+ "AND m.UserGID=? AND m.UserAge between (? and ?) AND m.UserCID=? " + "UNION "
								+ "SELECT m.UserID,m.UserName,m.UserAge,c.City,m.UserIntroduction,m.UserImg,j.Job FROM member m "
								+ "left join city c on m.UserCID=c.CID " 
								+ "left join room r on m.UserID=r.FirstUserID "
								+ "left join Job j on j.JID=m.UserJID "
								+ "WHERE UserID!='" + PUserID + "' "
								+ "AND (m.UserGID=? OR m.UserAge between ? and ? OR m.UserCID=?) " + "LIMIT ?,1 ";

						PreparedStatement stmt2 = conn.prepareStatement(query2);
						stmt2.setString(1, PUserGID);
						stmt2.setInt(2, (int) PUserAge1);
						stmt2.setInt(3, (int) PUserAge2);
						stmt2.setString(4, PUserCID);

						stmt2.setString(5, PUserGID);
						stmt2.setInt(6, (int) PUserAge1);
						stmt2.setInt(7, (int) PUserAge2);
						stmt2.setString(8, PUserCID);
						stmt2.setInt(9, count);

						ResultSet rs2 = stmt2.executeQuery();
						if (rs2.next()) {
							String UserID = rs2.getString("UserID");
							String query3 = "SELECT sk.skill from memberskill ms "
									+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "' " // here
									+ "AND ms.MSUserSID in (" + s + ")";

							PreparedStatement stmt3 = conn.prepareStatement(query3);
							ResultSet rs3 = stmt3.executeQuery(query3);
							if (rs3.next()) {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								rs4.next();
								LinkedList<String> skillsforsql = new LinkedList<String>();
								do {
									skillsforsql.add(rs4.getString("Skill"));
								} while (rs4.next());
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
								
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(有相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							} else {
								String query4 = "SELECT sk.skill from memberskill ms "
										+ "left join Skill sk on ms.MSUserSID=sk.SID " + "WHERE MSUserID= '" + UserID + "'";
								PreparedStatement stmt4 = conn.prepareStatement(query4);
								ResultSet rs4 = stmt4.executeQuery(query4);
								rs4.next();
								LinkedList<String> skillsforsql = new LinkedList<String>();
								do {
									skillsforsql.add(rs4.getString("Skill"));
								} while (rs4.next());
								
								String UserName = rs2.getString("UserName");
								Integer UserAge = rs2.getInt("UserAge");
								String city = rs2.getString("City");
								String introduction = rs2.getString("UserIntroduction");
								String job = rs2.getString("Job");
								
								request.setAttribute("job", job);
								request.setAttribute("UserName", UserName);
								request.setAttribute("UserAge", UserAge);
								request.setAttribute("city", city);
								request.setAttribute("skill", skillsforsql);
								request.setAttribute("introduction", introduction);
								request.setAttribute("UserID", UserID);
								String lang = "(無相關技能)";
								request.setAttribute("lang", lang);
								request.getRequestDispatcher("pairPage.jsp").forward(request, response);
							}

						} else {
							response.sendRedirect("NoPair.html");
						}
					}
				}
			} else {
				response.sendRedirect("HomeLogin.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}