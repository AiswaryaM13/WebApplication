import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "ConfirmServlet", urlPatterns = "/api/confirm")
public class ConfirmServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       
        HttpSession session = request.getSession();
        JsonArray jsonArray= new JsonArray();
        
        // get the previous items in a ArrayList
        ArrayList<Element> cartItems = (ArrayList<Element>) session.getAttribute("prevItems");
       
        	int size=cartItems.size();
        	for(int i=1;i<size;i++)
        	{
        		for(int n=0;n<Integer.parseInt(cartItems.get(i).getCount());n++) {
        			response.getWriter().write(cartItems.get(i).getSaleId(n));
        			response.getWriter().write(",");
        			response.getWriter().write(cartItems.get(i).getMovieTitle());
        			response.getWriter().write(",");
        			response.getWriter().write(cartItems.get(i).getCount());
        			response.getWriter().write(",");
        		}
        	}
        	response.getWriter().write(jsonArray.toString());
    
}
}
