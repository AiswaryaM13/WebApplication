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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Declaring a WebServlet called SingleStarServlet, which maps to url "/api/single-star"
@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single-movie")
public class SingleMovieServlet extends HttpServlet {
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

		// Retrieve parameter id from url request.
		String id = request.getParameter("id");
		System.out.println(id);
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
			//**Connection dbcon = dataSource.getConnection();

//			String query = "SELECT m.id as movie_id, m.title as title, group_CONCAT(Distinct s.id order by s.name) as star_id, m.year as year, m.director as director, group_concat(Distinct g.name) as genre_name,"
//					+ " group_concat(Distinct s.name) as star_name from movies as m, stars as s, stars_in_movies as sim, genres as g, "
//					+ "genres_in_movies as gen where m.id=sim.movieId and sim.starId=s.id and m.id=gen.movieId and gen.genreId=g.id and m.id=?";

			String query = "select distinct p.id, p.title, p.year, p.director, group_concat(Distinct g.name) as gen, group_concat(Distinct s.name) as star_name, group_concat(s.id order by s.name) as star_id from (select distinct m.id, title, year, director, rating from movies as m, ratings where m.id=ratings.movieId and m.id=?) as p, genres as g, genres_in_movies as gm, stars as s, stars_in_movies as sm where p.id=gm.movieId And g.id=gm.genreId And p.id=sm.movieId And sm.starId=s.id";
			// Declare our statement
			PreparedStatement statement = dbcon.prepareStatement(query);

			// Set the parameter represented by "?" in the query to the id we get from url,
			// num 1 indicates the first "?" in the query
			statement.setString(1, id);

			// Perform the query
			ResultSet rs = statement.executeQuery();

			JsonArray jsonArray = new JsonArray();
			
			// Iterate through each row of rs
			while (rs.next()) {

				String genre_Name = rs.getString("gen");
				String star_Name = rs.getString("star_name");
				String star_Id= rs.getString("star_id");

				String movie_Id = rs.getString("id");
				String movie_Title = rs.getString("title");
				String movie_Year = rs.getString("year");
				String movie_Director = rs.getString("director");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("genre__name", genre_Name);
				jsonObject.addProperty("star__name", star_Name);
				jsonObject.addProperty("movie__id", movie_Id);
				jsonObject.addProperty("movie__title", movie_Title);
				jsonObject.addProperty("movie__year", movie_Year);
				jsonObject.addProperty("movie__director", movie_Director);
				jsonObject.addProperty("star__id", star_Id);

				jsonArray.add(jsonObject);
			}
			
            // write JSON string to output
            out.write(jsonArray.toString());
            // set response status to 200 (OK)
            response.setStatus(200);

			rs.close();
			statement.close();
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
