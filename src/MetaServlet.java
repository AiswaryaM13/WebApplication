import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.DatabaseMetaData;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "MetaServlet", urlPatterns = "/api/meta")
public class MetaServlet extends HttpServlet {
	private static final long serialVersionUID = 2L;

	// Create a dataSource which registered in web.xml
	//@Resource(name = "jdbc/moviedb")
	//private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json"); // Response mime type


		// Output stream to STDOUT
		PrintWriter out = response.getWriter();

		try {
			
			//pooling
            // the following few lines are for connection pooling
            // Obtain our environment naming context

            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                response.getWriter().println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");

            // the following commented lines are direct connections without pooling
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            if (ds == null)
            	response.getWriter().println("ds is null.");
            System.out.println("pooling done:");
            System.out.println(ds);
            Connection dbcon = ds.getConnection();
            if (dbcon == null)
            	response.getWriter().println("dbcon is null.");
        	//pooling
			
			// Get a connection from dataSource
			//Connection dbcon = dataSource.getConnection();

			java.sql.DatabaseMetaData md = dbcon.getMetaData();

			// Perform the query
			ResultSet rs = md.getColumns(null, null, "%", null);
			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {
				String table = rs.getString("TABLE_NAME");
				String field = rs.getString("COlUMN_NAME");
				String type = rs.getString("TYPE_NAME");
				String key= rs.getString("COLUMN_SIZE");
				String pos= rs.getString("ORDINAL_POSITION");
				
				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("table", table);
				jsonObject.addProperty("field", field);
				jsonObject.addProperty("type", type);
				jsonObject.addProperty("key", key);
				jsonObject.addProperty("pos", pos);
				jsonArray.add(jsonObject);
			}
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

			rs.close();
			dbcon.close();
		} catch (Exception e) {
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());

			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
		}
		out.close();

	}

}