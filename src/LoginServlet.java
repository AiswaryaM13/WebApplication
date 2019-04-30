import com.google.gson.JsonObject;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class Login
 */
@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;
    public LoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String id_q="";
        String c_id="";
        String c_password = "";
        String count1 ="";
        String count2 = "";
        // determine request origin by HTTP Header User Agent string
        String userAgent = request.getHeader("User-Agent");
        System.out.println("recieved login request");
        System.out.println("userAgent: " + userAgent);

        // only verify recaptcha if login is from Web (not Android)
        if (userAgent != null && !userAgent.contains("Android")) {
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            // verify recaptcha first
            try {
                RecaptchaVerifyUtils.verify(gRecaptchaResponse, RecaptchaConstants.SECRET_KEY);
            } catch (Exception e) {
                System.out.println("recaptcha success");
                JsonObject responseJsonObject = new JsonObject();
                responseJsonObject.addProperty("status", "fail");
                responseJsonObject.addProperty("message", e.getMessage());
                response.getWriter().write(responseJsonObject.toString());
                return;
            }
        }

        // then verify username / password
        JsonObject jsonObject = new JsonObject();
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
            String query = "select id, count(email), email , password, count(password) from customers where email=?";
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, username);
            // Perform the query
            ResultSet rs = statement.executeQuery();
                       
            while (rs.next()) {
            id_q = rs.getString("id");
            c_id = rs.getString("email");
            c_password = rs.getString("password");
            count1 = rs.getString("count(email)");
            count2 = rs.getString("count(password)");
            System.out.println(id_q);
            System.out.println(c_id);
            System.out.println(c_password);
            System.out.println(count1);
            System.out.println(count2);
            JsonObject loginResult = LoginVerifyUtils.verifyUsernamePassword(id_q ,username, password, c_id, c_password, count1, count2);
            System.out.println("xx");
            System.out.println(loginResult.get("status"));
            if (loginResult.get("status").getAsString().equals("success")) {
                // login success
            	String sessionId = ((HttpServletRequest) request).getSession().getId();
                Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
                request.getSession().setAttribute("user", new User(username));
                String id=loginResult.get("id").getAsString();
                request.getSession().setAttribute("id", new User2(id));
                response.getWriter().write(loginResult.toString());
            } else {
            	System.out.println(loginResult.get("status"));
                response.getWriter().write(loginResult.toString());
            }
            rs.close();
            statement.close();
            dbcon.close();
    } }catch (Exception e) {
    			// write error message JSON object to output
    			//JsonObject jsonObject = new JsonObject();
    			jsonObject.addProperty("errorMessage", e.getMessage());
    			e.printStackTrace();
    			// set response status to 500 (Internal Server Error)
    			//response.setStatus(500);
            }
        
    }

}