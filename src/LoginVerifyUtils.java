import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.jasypt.util.password.StrongPasswordEncryptor;
import com.google.gson.JsonObject;

public class LoginVerifyUtils {
//	@Resource(name = "jdbc/moviedb")
//    private static DataSource dataSource;
    
    public static JsonObject verifyUsernamePassword(String id_q, String username, String password, String c_id, String c_password, String count1, String count2) {
        // after recatpcha verification, then verify username and password
    //	JsonObject jsonObject = new JsonObject();
    	
       //username.equals(c_id)
            if (username.equals(c_id)) {
            if(new StrongPasswordEncryptor().checkPassword(password, c_password)) {
            // Login succeeds
            // Set this user into current session            
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            responseJsonObject.addProperty("id", id_q);
            return responseJsonObject;
            }
            }else if (count1.equals("0")) {
        	JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
            return responseJsonObject;
            }
            //else if(count1.equals("1") && !(new StrongPasswordEncryptor().checkPassword(password, c_password))){
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "incorrect password");
            return responseJsonObject;
     //   }
       
    }    
 }
