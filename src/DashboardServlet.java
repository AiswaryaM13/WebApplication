import com.google.gson.JsonArray;


import com.google.gson.JsonObject;
import com.mysql.jdbc.CallableStatement;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "DashboardServlet", urlPatterns = "/api/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
 // Create a dataSource which registered in web.xml
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String star = request.getParameter("star");
    	String year = request.getParameter("year");
        
        //response.setContentType("application/json;charset=utf-8"); // Response mime type
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
            DataSource ds = (DataSource) envCtx.lookup("jdbc/MasterDB");

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
            //**Connection dbcon = dataSource.getConnection();
                        
            if(!(star.equals(""))) {
        	String query2 = "INSERT INTO stars VALUES(?,?,?);";
        	PreparedStatement preparedStmt = dbcon.prepareStatement(query2);
        		
        		Statement statement1 = dbcon.createStatement();
        		String query1 = "select max(id) from stars";
        		ResultSet rs1 = statement1.executeQuery(query1);
        		String count="";
        		String bthyr="";
        		if(!(year.equals(""))) {
        			bthyr=year;
        		}
        		while(rs1.next()) {
        			count=rs1.getString("max(id)");}
        		rs1.close();
        		statement1.close();   
        		//System.out.println(count);
        		String trial=count.substring(2);
        		//System.out.println(trial);
        		trial=(Integer.parseInt(trial)+1)+"";
        		String trial2= count.substring(0,2)+trial;
        		//System.out.println(trial2);
        		   
        		
        	preparedStmt.setString(1, trial2);
        	preparedStmt.setString(2, star);
        	preparedStmt.setInt(3, Integer.parseInt(bthyr));
            preparedStmt.executeUpdate();
        	preparedStmt.close();
        			
        	JsonObject responseJsonObject = new JsonObject();
        	responseJsonObject.addProperty("status", "success");
        	responseJsonObject.addProperty("message", "Update Successful");
        	responseJsonObject.addProperty("message1", star);
        	responseJsonObject.addProperty("message2", bthyr);
        	response.getWriter().write(responseJsonObject.toString()); 
        	dbcon.close();
        	}
        }
     catch (Exception e) {
        	JsonObject jsonObject = new JsonObject();
        	jsonObject.addProperty("errorMessage", e.getMessage());
        	response.getWriter().write(jsonObject.toString());
        	// set reponse status to 500 (Internal Server Error)
        	response.setStatus(500);
        	}
        out.close();
 }     
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String movietitle = request.getParameter("moviename");
        String movieyear = request.getParameter("movieyear");
        String moviedirector = request.getParameter("moviedirector");
        String genre = request.getParameter("moviegenre");
        String starname = request.getParameter("moviestarname");


        try {
        	
        	//pooling
            // the following few lines are for connection pooling
            // Obtain our environment naming context

            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                response.getWriter().println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/MasterDB");

            // the following commented lines are direct connections without pooling
            //Class.forName("org.gjt.mm.mysql.Driver");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

            if (ds == null)
            	response.getWriter().println("ds is null.");
            System.out.println("pooling done:");
            System.out.println(ds);
            Connection conn = ds.getConnection();
            if (conn == null)
            	response.getWriter().println("dbcon is null.");
        	//pooling
        	
            //**Connection conn = dataSource.getConnection();
            int check=0;
            if(!movietitle.equals(null)&&!movietitle.isEmpty()
                    &&!movieyear.equals(null)&&!movieyear.isEmpty()
                    && !moviedirector.equals(null)&&!moviedirector.isEmpty()
                    &&!genre.equals(null)&&!genre.isEmpty()
                    && !starname.equals(null)&&!starname.isEmpty()) {
                String query = "call movie_add (?,?,?,?,?,?)";
                java.sql.CallableStatement statement = conn.prepareCall(query);
                statement.registerOutParameter(6, Types.INTEGER);
                statement.setString(1, movietitle);
                statement.setString(2, movieyear);
                statement.setString(3, moviedirector);
                statement.setString(4, starname);
                statement.setString(5, genre);
                statement.setInt(6, 0);

                statement.execute();
                check = statement.getInt(6);
                
                if (check==1) {
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "success");
                    responseJsonObject.addProperty("message", "success");
                    out.write(responseJsonObject.toString());
                }
                else if(check==2){
                    // Login fails
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "The movie was already present");
                    out.write(responseJsonObject.toString());
                }
                else if(check==0){
                    // Login fails
                    JsonObject responseJsonObject = new JsonObject();
                    responseJsonObject.addProperty("status", "fail");
                    responseJsonObject.addProperty("message", "There is no appropriate input to add to movie");
                    out.write(responseJsonObject.toString());
                }
               
                statement.close();
                conn.close();
            }
            
        }
        catch (Exception e){

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("errorMessage", e.getMessage());
            out.write(jsonObject.toString());
            e.printStackTrace();
            response.setStatus(500);

        }

}

}
