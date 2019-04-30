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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 * This IndexServlet is declared in the web annotation below, 
 * which is mapped to the URL pattern /api/index.
 */
@WebServlet(name = "cartServlet", urlPatterns = "/api/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    //@Resource(name = "jdbc/moviedb")
    //private DataSource dataSource;

    /**
     * handles POST requests to store session information
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String movieId=request.getParameter("id");
        
        int oldorzero=0;
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
            Connection conn = ds.getConnection();
            if (conn == null)
            	response.getWriter().println("dbcon is null.");
        	//pooling
        	
        	
            // Get a connection from dataSource
            //**Connection conn = dataSource.getConnection();
            String query ="select id, title, year,director from movies where id=? ";
            PreparedStatement stat = conn.prepareStatement(query);
            stat.setString(1, movieId);
            ResultSet rs = stat.executeQuery();
            String id = "";
            String title ="";

            if(rs.next()){
                id = rs.getString("id");
                title = rs.getString("title");
            }

            ArrayList<Element> prevItems = (ArrayList<Element>) session.getAttribute("prevItems");

            if (prevItems == null) {
                prevItems = new ArrayList<>();
                Element item=new Element("Empty","1","");

                prevItems.add(item);

                if(!title.equals("")) {
                    Element item1=new Element(title,"1",id);
                    prevItems.add(item1);

                }
                session.setAttribute("prevItems", prevItems);
            }

            else {
                int num=prevItems.size();
                for(int i=0;i<num;i++){
                    String str=prevItems.get(i).getMovieTitle();
                    if(str.equals(title))   
                    	oldorzero=1;
                    String str1=prevItems.get(i).getCount();
                    if(str1.equals("0")) 
                    	oldorzero=1;
                }

                if(num>1) {
                    String a[]=new String[num-1];
                    
                  int tot=1;
                  boolean equalszero=true;
                  while(equalszero){
                      int size1=prevItems.size();
                      if(prevItems.get(tot).getCount().equals("0")){
                          prevItems.remove(tot);
                          size1=prevItems.size();
                      }
                      else tot++;
                      if(tot==size1) equalszero=false;
                  }
                    

                    for (int i = 2; i < 2 * num; i += 2) {

                        String string = i + "";
                        a[i/2-1]=request.getParameter(string);
                    }
                    for(int i=1;i<num;i++){
                        boolean corrnum;
                        try {
                            Integer.parseInt(a[i-1]);
                            corrnum=true;
                        } catch (NumberFormatException e) {
                            corrnum= false;
                        }
                        if(a[i-1]!=""&&a[i-1]!=null&&Integer.parseInt(a[i-1])>=0&&corrnum)
                            prevItems.get(i).setCount(a[i-1]);
                    }

                }

                if(!title.equals("")&&oldorzero==0) {
                    synchronized (prevItems) {

                        Element item1=new Element(title,"1",id);
                        prevItems.add(item1);

                    }
                }
                session.setAttribute("prevItems", prevItems);
            }



            int num=prevItems.size();
            for(int i=0;i<num;i++) {
                if(i==(num-1)) {
                    out.write(prevItems.get(i).getMovieTitle());
                    out.write(",");
                    out.write(prevItems.get(i).getCount());
                }
                else{
                    out.write(prevItems.get(i).getMovieTitle());
                    out.write(",");
                    out.write(prevItems.get(i).getCount());
                    out.write(",");



                }
            }

            rs.close();
            stat.close();
            conn.close();
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