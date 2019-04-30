import com.google.gson.JsonArray;



import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is declared as LoginServlet in web annotation, 
 * which is mapped to the URL pattern /api/dashboardlogin
 */
@WebServlet(name = "DashboardLoginServlet", urlPatterns = "/api/dashboardlogin")
public class DashboardLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
 // Create a dataSource which registered in web.xml
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        PrintWriter out = response.getWriter();
        
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        //RecaptchaConstants.SECRET_KEY
        try {
            RecaptchaVerifyUtils.verify(gRecaptchaResponse, RecaptchaConstants.SECRET_KEY);
        } catch (Exception e) {
            out.println("<html>");
            out.println("<head><title>Error</title></head>");
            out.println("<body>");
            out.println("<p>recaptcha verification error</p>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</body>");
            out.println("</html>");
            out.close();
            return;
        }
        
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
            //**Connection dbcon = dataSource.getConnection();
            // Declare our statement
            String query = "select count(email), email , password, count(password) from employees where email=?";
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, username);
            // Perform the query
            
            ResultSet rs = statement.executeQuery();
           
            
            while (rs.next()) {
            //String id = rs.getString("id");
            String c_id = rs.getString("email");
            String c_password = rs.getString("password");
            String count1 = rs.getString("count(email)");
            String count2 = rs.getString("count(password)");
          
       //username.equals(c_id)
            if (username.equals(c_id)) {
            if(new StrongPasswordEncryptor().checkPassword(password, c_password)) {
           
            String sessionId = ((HttpServletRequest) request).getSession().getId();
            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
            request.getSession().setAttribute("user", new User(username));
            //request.getSession().setAttribute("id", new User2(id));
            
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

            response.getWriter().write(responseJsonObject.toString());
            }
            }else if (count1.equals("0")) {
        	JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            response.getWriter().write(responseJsonObject.toString());}
            else if(count1.equals("1") && !(password.equals(c_password))){
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "incorrect password");
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
