import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * handles POST requests to store session information
     */
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
    
    /**
     * handles GET requests to add and show the item list information
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
     	try {
     	  JsonArray jsonArray = new JsonArray();
          String letter = request.getParameter("letter");
          String query = request.getParameter("query");
          System.out.println(query);
      
          if (query == null || query.trim().isEmpty()) {
        	  JsonObject jsonObject = new JsonObject();
        	  jsonObject.addProperty("letter", letter);
        	  jsonArray.add(jsonObject);
          response.getWriter().write(jsonArray.toString());
          return;
          }
     		String query1[]=query.split(" ");
     		String str="";
     		for(int i=0;i<query1.length;i++) {
                str+= "+"+query1[i]+"*";
                }
     		
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
     		
     		
     		
            //**Connection dbcon = dataSource.getConnection();
            String q1 = "select distinct id, title from movies where MATCH(title) AGAINST (? IN BOOLEAN MODE) or ed(title, ?)<=2 limit 10";
            PreparedStatement statement = dbcon.prepareStatement(q1);
            statement.setString(1, str);
            statement.setString(2, query);
       //or ed(title, ?)<=2    
            System.out.println(query);
            // Perform the query
            ResultSet rs = statement.executeQuery();

            // Iterate through each row of rs
            while (rs.next()) {
            	String id_q = rs.getString("id");
                String title_q = rs.getString("title");
                System.out.println(id_q);
                System.out.println(title_q);
               jsonArray.add(generateJsonObject(id_q, title_q));
            } 	       
            // write JSON string to output
            response.getWriter().write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
            rs.close();
            statement.close();
            dbcon.close();
            return;
          
        } catch (Exception e) {
        	System.out.println(e);
			response.sendError(500, e.getMessage());
			e.printStackTrace();
        }	      
      }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    		
    		
            //Connection dbcon = dataSource.getConnection();
            Statement statement = dbcon.createStatement();
            String query = "select name from genres";
            
            // Perform the query
            ResultSet rs = statement.executeQuery(query);
            JsonArray jsonArray = new JsonArray();

            // Iterate through each row of rs
            while (rs.next()) {
            	String name = rs.getString("name");
                
                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("name", name);
                jsonArray.add(jsonObject);
            }
            
            // write JSON string to output
            response.getWriter().write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
            dbcon.close();
        } catch (Exception e) {
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			response.getWriter().write(jsonObject.toString());
			e.printStackTrace();
			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);

        }
        response.getWriter().close();
    }
    private static JsonObject generateJsonObject(String id, String mtitle) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", mtitle);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("id_q", id);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}
 }

