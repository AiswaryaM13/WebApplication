import com.google.gson.JsonArray;

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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


// Declaring a WebServlet called MovieServlet, which maps to url "/api/movies"
@WebServlet(name = "MovieServlet", urlPatterns = "/api/movies")
public class Top20movies extends HttpServlet {
    private static final long serialVersionUID = 1L;
    int page=1;
    // Create a dataSource which registered in web.xml
//    @Resource(name = "jdbc/moviedb")
//    private DataSource dataSource;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	long totalJDBCtime=0;
        DataOutputStream data = null;
        try {
            data = new DataOutputStream(new FileOutputStream("/home/ubuntu/test.txt",true)); 
		//File test = new File("/home/ubuntu/test.txt");
//		try {
//			if(test.exists()==false) {
//				test.createNewFile();
//			}
//		PrintWriter out1 = new PrintWriter(new FileWriter(test,true));
    	long startquerytime = System.nanoTime();
        //response.setContentType("application/json"); // Response mime type
        String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        String genre1="%"+genre+"%";
        String letter = request.getParameter("letter");
        String letter1=letter+"%";
       // String pagenum = request.getParameter("pagenum");
        page=Integer.parseInt(request.getParameter("pagenum"));
        String numperpage = request.getParameter("numperpage");
        String sortby = request.getParameter("sortby");
        String sort = request.getParameter("sort");
        String order=sortby+" "+sort;
        int num_perpage=Integer.parseInt(request.getParameter("numperpage"));
        int pn=(Integer.parseInt(request.getParameter("pagenum"))-1)*num_perpage;
        String index=Integer.toString(pn);
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();
      
        try {
//        	
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
        	if(!title.equals(null)&&!title.isEmpty()) {
        	String query2[]=title.split(" ");
     		String str="";
     		for(int i=0;i<query2.length;i++) {
                str+= "+"+query2[i]+"*";
                }
            //or ed(title,?)<=2
     		//Statement statement = dbcon.createStatement();
            String query1 = "select distinct p.id, p.title, p.year, p.director, group_concat(Distinct g.name) as gen, p.rating, group_concat(Distinct s.name) as star_name, group_concat(s.id order by s.name) as star_id from (select distinct m.id, title, year, director, rating from movies as m, ratings where m.id=ratings.movieId and MATCH(title) AGAINST (? IN BOOLEAN MODE)) as p, genres as g, genres_in_movies as gm, stars as s, stars_in_movies as sm where p.id=gm.movieId And g.id=gm.genreId And p.id=sm.movieId And sm.starId=s.id and g.name like ? group by p.title order by %s limit ? offset ?";
            //String query1 = "select distinct p.id, p.title, p.year, p.director, group_concat(Distinct g.name) as gen, p.rating, group_concat(Distinct s.name) as star_name, group_concat(s.id order by s.name) as star_id from (select distinct m.id, title, year, director, rating from movies as m, ratings where m.id=ratings.movieId and MATCH(title) AGAINST ('"+str+"' IN BOOLEAN MODE)) as p, genres as g, genres_in_movies as gm, stars as s, stars_in_movies as sm where p.id=gm.movieId And g.id=gm.genreId And p.id=sm.movieId And sm.starId=s.id group by p.title order by "+order+" limit "+num_perpage+" offset "+pn+"";
            //ResultSet rs = statement.executeQuery(query1);
            String query = String.format(query1, order);
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, str);
            //statement.setString(2, title);
            statement.setString(2, genre1);
            statement.setInt(3, num_perpage);
            statement.setInt(4, pn);
            // Perform the query  
            long startJDBCtime = System.nanoTime();
            ResultSet rs = statement.executeQuery();
            long endJDBCtime = System.nanoTime();
            totalJDBCtime = endJDBCtime - startJDBCtime;
            JsonArray jsonArray = new JsonArray();

             //Iterate through each row of rs
            while (rs.next()) {
            	String id = rs.getString("id");
                String m_title = rs.getString("title");
                String m_year = rs.getString("year");
                String m_director = rs.getString("director");
                String genreName = rs.getString("gen");
                String rating = rs.getString("rating");
                String star_name = rs.getString("star_name");
                String star_id = rs.getString("star_id");

//            while (rs1.next()) {
//            	String id = rs1.getString("id");
//                String m_title = rs1.getString("title");
//                String m_year = rs1.getString("year");
//                String m_director = rs1.getString("director");
//                String genreName = rs1.getString("gen");
//                String rating = rs1.getString("rating");
//                String star_name = rs1.getString("star_name");
//                String star_id = rs1.getString("star_id");
            
                // Create a JsonObject based on the data we retrieve from rs
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", id);
                jsonObject.addProperty("title", m_title);
                jsonObject.addProperty("year", m_year);
                jsonObject.addProperty("director", m_director);
                jsonObject.addProperty("genreName", genreName);
                jsonObject.addProperty("rating", rating);
                jsonObject.addProperty("star_name", star_name);
                jsonObject.addProperty("star_id", star_id);
                jsonObject.addProperty("index", index);
                jsonArray.add(jsonObject);
            }
            
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

            rs.close();
            statement.close();
        	}
            if(!letter1.equals("%")) {
                String q1 = "select distinct p.id, p.title, p.year, p.director, group_concat(Distinct g.name) as gen, p.rating, group_concat(Distinct s.name) as star_name, group_concat(s.id order by s.name) as star_id from (select distinct m.id, title, year, director, rating from movies as m, ratings where m.id=ratings.movieId and title like ? ) as p, genres as g, genres_in_movies as gm, stars as s, stars_in_movies as sm where p.id=gm.movieId And g.id=gm.genreId And p.id=sm.movieId And sm.starId=s.id and g.name like ? group by p.title order by %s limit ? offset ?";
                String q2 = String.format(q1, order);
                PreparedStatement state = dbcon.prepareStatement(q2);
                state.setString(1, letter1);
                state.setString(2, genre1);
                state.setInt(3, num_perpage);
                state.setInt(4, pn);
                
             // Perform the query       
                ResultSet rs = state.executeQuery();

                JsonArray jsonArray = new JsonArray();

                // Iterate through each row of rs
                while (rs.next()) {
                	String id = rs.getString("id");
                    String m_title = rs.getString("title");
                    String m_year = rs.getString("year");
                    String m_director = rs.getString("director");
                    String genreName = rs.getString("gen");
                    String rating = rs.getString("rating");
                    String star_name = rs.getString("star_name");
                    String star_id = rs.getString("star_id");

                    // Create a JsonObject based on the data we retrieve from rs
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", id);
                    jsonObject.addProperty("title", m_title);
                    jsonObject.addProperty("year", m_year);
                    jsonObject.addProperty("director", m_director);
                    jsonObject.addProperty("genreName", genreName);
                    jsonObject.addProperty("rating", rating);
                    jsonObject.addProperty("star_name", star_name);
                    jsonObject.addProperty("star_id", star_id);
                    jsonObject.addProperty("index", index);
                    jsonArray.add(jsonObject);
                }
                
                // write JSON string to output
                out.write(jsonArray.toString());
                // set response status to 200 (OK)
                response.setStatus(200);

                rs.close();
                state.close();
                }
            
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
        long endQueryTime = System.nanoTime();
        long totalQuerytime = endQueryTime - startquerytime;
        //out1.printf("%d %d",totalQuerytime, totalJDBCtime);
//        out1.append(totalQuerytime, totalJDBCtime);
//        out1.println();
//        out1.close();
//		}
//		catch(IOException e) {
//			System.out.println("cannot");
//		}
     
            data.writeUTF(" totalQuerytime: "+Long.toString(totalQuerytime)+" totalJDBCtime: "+Long.toString(totalJDBCtime)+"\n"); 
            }
            catch(FileNotFoundException fnfe) {
            	System.out.println("alala");
            }       
            catch (IOException ioe) {};
            data.close();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
    	
        String numperpage = request.getParameter("numperpage");
        String sortby="";
        String pagenext=(page+1)+"";
        String pageprev="";
        if(page!=1) {
        	pageprev=(page-1)+"";
        }
        else if(page==1) {
        	pageprev=(page)+"";
        }

        String sorttitleasc="p.title";
        String sorttitleasc1="ASC";
        String sorttitledesc="p.title";
        String sorttitledesc1="DESC";
        String sortratingasc="p.rating";
        String sortratingasc1="ASC";
        String sortratingdesc="p.rating";
        String sortratingdesc1="DESC";
try {
            JsonObject jsonObject = new JsonObject();     
            jsonObject.addProperty("pageprev", pageprev);
            jsonObject.addProperty("pagenext", pagenext);
            jsonObject.addProperty("numperpage", numperpage);
            jsonObject.addProperty("sorttitleasc", sorttitleasc);
            jsonObject.addProperty("sorttitleasc1", sorttitleasc1);  
            jsonObject.addProperty("sorttitledesc", sorttitledesc);
            jsonObject.addProperty("sorttitledesc1", sorttitledesc1); 
            jsonObject.addProperty("sortratingasc", sortratingasc);
            jsonObject.addProperty("sortratingasc1", sortratingasc1); 
            jsonObject.addProperty("sortratingdesc", sortratingdesc);
            jsonObject.addProperty("sortratingdesc1", sortratingdesc1); 
            // write JSON string to output
            response.getWriter().write(jsonObject.toString());
            // set response status to 200 (OK)
            response.setStatus(200);
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

