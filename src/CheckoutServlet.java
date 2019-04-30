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
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/login
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = "/api/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
 // Create a dataSource which registered in web.xml
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String id = request.getParameter("id");
    	String firstname = request.getParameter("FirstName");
        String lastname = request.getParameter("LastName");
        String expiry = request.getParameter("expiry");
        
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
            
            // Declare our statement
            String query = "select id, firstName, lastName, count(expiration) from creditcards where id=? and expiration=?";
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, id);
            statement.setString(2, expiry);
            // Perform the query
            
            ResultSet rs = statement.executeQuery();     
            while (rs.next()) {
            String c_id = rs.getString("id");
            String c_firstName = rs.getString("firstName");
            String c_lastName = rs.getString("lastName");
            String c_expiry = rs.getString("count(expiration)");
            
       //username.equals(c_id)
            if (id.equals(c_id) && firstname.equals(c_firstName) && lastname.equals(c_lastName) && c_expiry.equals("1")) {

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
           
            Calendar calender=Calendar.getInstance();
        	Date dateRef = new Date(calender.get(Calendar.YEAR) - 1900, calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
            HttpSession session = request.getSession();
            User2 user=(User2) session.getAttribute("id");
            String id1=user.getId();
            responseJsonObject.addProperty("message1", id1);
        	ArrayList<Element> cartItems = (ArrayList<Element>) session.getAttribute("prevItems");
        	int size=cartItems.size();
        	if (size>1) {
        		for(int i=1; i<size; i++) {
        			String name=cartItems.get(i).getMovieId();
        				for(int n=0; n<Integer.parseInt(cartItems.get(i).getCount()); n++) {
        					 try {
        						 
        						//pooling
     				            // the following few lines are for connection pooling
     				            // Obtain our environment naming context

     				            Context initCtx1 = new InitialContext();

     				            Context envCtx1 = (Context) initCtx1.lookup("java:comp/env");
     				            if (envCtx1 == null)
     				                response.getWriter().println("envCtx is NULL");

     				            // Look up our data source
     				            DataSource ds1 = (DataSource) envCtx1.lookup("jdbc/MasterDB");

     				            // the following commented lines are direct connections without pooling
     				            //Class.forName("org.gjt.mm.mysql.Driver");
     				            //Class.forName("com.mysql.jdbc.Driver").newInstance();
     				            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);

     				            if (ds1 == null)
     				            	response.getWriter().println("ds is null.");
     				            System.out.println("pooling done:");
     				            System.out.println(ds1);
     				            Connection dbcon1 = ds1.getConnection();
     				            if (dbcon1 == null)
     				            	response.getWriter().println("dbcon is null.");
     				        	//pooling
        						 
        				            String query2 = "INSERT INTO sales VALUES(?,?,?,?);";
        				            PreparedStatement preparedStmt = dbcon1.prepareStatement(query2);
        				                    				            
        				            	Statement statement1 = dbcon.createStatement();
        					            String query1 = "select count(id) from sales";
        					            ResultSet rs1 = statement1.executeQuery(query1);
        					            String count="";
        					            while(rs1.next()) {
        					            count=rs1.getString("count(id)");}
        					            rs1.close();
        					            statement1.close();
        					            int c=Integer.parseInt(count)+6;
        	    				        cartItems.get(i).setSaleId(c+"",n);       				            
        				            preparedStmt.setInt(1, c);
        				            preparedStmt.setInt(2, Integer.parseInt(id1));
        				            preparedStmt.setString(3, name);
        				            preparedStmt.setDate(4, dateRef);
        				            preparedStmt.executeUpdate();
        				            preparedStmt.close(); 
        				            dbcon1.close();       				            
        					 }
        					 
        					 catch (Exception e) {
        			    			// write error message JSON object to output
        			    	
        			            	 JsonObject jsonObject = new JsonObject();
        			            	jsonObject.addProperty("errorMessage", e.getMessage());
        			    			response.getWriter().write(jsonObject.toString());
        			    			// set reponse status to 500 (Internal Server Error)
        			    			response.setStatus(500);
        					 	}
        					}
        		}
        	}
            response.getWriter().write(responseJsonObject.toString());
        } 
            else {
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "incorrect card details");
            response.getWriter().write(responseJsonObject.toString());
        }
       }
        rs.close();
        statement.close();
        dbcon.close();
} catch (Exception e) {
        	
			// write error message JSON object to output
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			e.printStackTrace();
			// set reponse status to 500 (Internal Server Error)
			response.setStatus(500);
        }     
        
        out.close();
 }
      	
}
