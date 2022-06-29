package skillpairs;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
/**
 * Servlet implementation class AccountUploadImage
 */
@WebServlet("/AccountUploadImage")
@MultipartConfig(maxFileSize = 16177215) //// upload file's size up to 16MB
public class AccountUploadImage extends HttpServlet {

	// database connection settings
	private String dbURL = "jdbc:mysql://localhost:3306/skillpairs";
	private String dbUser = "root";
	private String dbPass = "root";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		if ((boolean) request.getSession().getAttribute("login") == true) {
			InputStream inputStream = null; // input stream of the upload file

			// obtains the upload file part in this multipart request
			Part filePart = request.getPart("UserImg");
			String empty=new String();

			if (filePart == null||filePart.getSize()==0){
				response.sendRedirect("AccountSet.html");
			}else{

				// prints out some information for debugging
				System.out.println(filePart.getName());
				System.out.println(filePart.getSize());
				System.out.println(filePart.getContentType());

				// obtains input stream of the upload file
				inputStream = filePart.getInputStream();
			

			Connection conn = null; // connection to the database
			String message = null; // message will be sent back to client
			try {
				// connects to the database
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

				// constructs SQL statement
				String sql = "Update member set UserImg= ? where UserID='" + request.getSession().getAttribute("UID")
						+ "'";
				PreparedStatement statement = conn.prepareStatement(sql);

				if (inputStream != null) {
					// fetches input stream of the upload file for the blob column
					statement.setBlob(1, inputStream);
				}

				// sends the statement to the database server
				int row = statement.executeUpdate();
				if (row > 0) {
					message = "File uploaded and saved into database";
				}
			} catch (SQLException ex) {
				message = "ERROR: " + ex.getMessage();
				ex.printStackTrace();
			} finally {
				if (conn != null) {
					// closes the database connection
					try {
						conn.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				getServletContext().getRequestDispatcher("/AccountSet.html").forward(request, response);
			}
			}
		} else {
			response.sendRedirect("HomeLogin.html");
		}
	}
}