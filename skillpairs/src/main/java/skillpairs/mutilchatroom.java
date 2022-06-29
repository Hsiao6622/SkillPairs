package skillpairs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;

/**
 * Servlet implementation class mutilchatroom
 */
@ServerEndpoint("/mutilChatroomServerEndpoint")
public class mutilchatroom {

	static Map<Integer, Set<Session>> chatrooms = Collections.synchronizedMap(new HashMap<Integer, Set<Session>>());
	static Set<Integer> existingroomID = Collections.synchronizedSet(new HashSet<Integer>());
	static Set<Session> chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/skillpairs";

	static final String USER = "root";
	static final String PASS = "root";
	Connection conn;
	String sqlForHisInsert="INSERT INTO history (HRoomID,HUserID,message)VALUES (?,?,?)";
	
	String sqlForHisSelect="SELECT message FROM history h WHERE h.HRoomID=? ORDER BY h.date";
	@OnOpen
	public void handleOpen(Session userSession) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		System.out.println(message);
		JSONObject jobj = new JSONObject(message);

		if (jobj.has("ConnectToroomID")) {
			ResultSet rs=null;
			
			if (jobj.getBoolean("modify")) {
				for (Map.Entry<Integer, Set<Session>> s : chatrooms.entrySet()) {
					chatrooms.get(s.getKey()).remove(userSession);
				}
				try {
					PreparedStatement psForSelect=conn.prepareStatement(sqlForHisSelect);
					psForSelect.setInt(1, jobj.getInt("ConnectToroomID"));
					rs=psForSelect.executeQuery();
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("MODIFY ACCESS");
			}
			if (existingroomID.contains(jobj.getInt("ConnectToroomID"))) {
				chatrooms.get(jobj.getInt("ConnectToroomID")).add(userSession);
				System.out.println("good connect2");
				try {
					while(rs.next()) {
						//System.out.println("{\"message\":\""+rs.getString(1)+"\"}");
						userSession.getBasicRemote().sendText("{\"message\":\""+rs.getString(1)+"\"}");
						//System.out.println("send!!");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//chatrooms.get(Integer.parseInt(jobj.get("roomID").toString()));
				return;
			} else {
				existingroomID.add(jobj.getInt("ConnectToroomID"));
				chatrooms.put(jobj.getInt("ConnectToroomID"), Collections.synchronizedSet(new HashSet<Session>()));
				chatrooms.get(jobj.getInt("ConnectToroomID")).add(userSession);
				//chatrooms.get(Integer.parseInt(jobj.get("roomID").toString()));
				try {
					while(rs.next()) {
						//System.out.println("{\"message\":\""+rs.getString(1)+"\"}");
						userSession.getBasicRemote().sendText("{\"message\":\""+rs.getString(1)+"\"}");
						//System.out.println("send!!");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("good connect3");
				return;
			}

		} else {
			System.out.println("is a message");
		}
		Iterator<Session> iterator = chatrooms.get(Integer.parseInt(jobj.get("roomID").toString())).iterator();
		System.out.println(jobj.getString("message"));
		try {
			PreparedStatement ps=conn.prepareStatement(sqlForHisInsert);
//			System.out.println(jobj.toString());
			ps.setInt(1, jobj.getInt("roomID"));
			ps.setInt(2, 5);//TODO 正式版要改
			ps.setString(3, jobj.getString("message"));
			ps.execute();
//			System.out.println(jobj.getInt("roomID"));
//			System.out.println(jobj.getInt("username"));
//			System.out.println(jobj.getString("message"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (iterator.hasNext()) {
//			System.out.println(jobj.toString());
			iterator.next().getBasicRemote().sendText(jobj.toString());
		}
		
//		String username=(String) userSession.getUserProperties().get("username");
//		if (username==null){
//			userSession.getUserProperties().put("username", message);
//			userSession.getBasicRemote().sendText(buildJsonData("System","you are now connected as "+message));
//		} else {
//			
//			//System.out.println(jobj.get("message"));
//			//System.out.println( jobj.get("roomID"));
//			//Iterator<Session> iterator=chatroomUsers.iterator();
//			Iterator<Session> iterator=chatrooms.get(Integer.parseInt(jobj.get("roomID").toString())).iterator();
//			//while (iterator.hasNext()) iterator.next().getBasicRemote().sendText(buildJsonData(username,message));
//			while (iterator.hasNext())iterator.next().getBasicRemote().sendText(jobj.toString());
//		}
	}

	@OnClose
	public void handleClose(Session userSession) {
		//System.out.println();
		//chatrooms.get(Integer.parseInt(jobj.get("roomID").toString()));
		for(Set<Session> i:chatrooms.values()) {
			i.remove(userSession);
		}
	}

//	private String buildJsonData(String username, String message) {
//		JsonObject jsonobject = Json.createObjectBuilder().add("message", username + ": " + message).build();
//		StringWriter stringWriter = new StringWriter();
//		try (JsonWriter jsonwriter = Json.createWriter(stringWriter)) {
//			jsonwriter.write(jsonobject);
//		}
//		return stringWriter.toString();
//	}
}
