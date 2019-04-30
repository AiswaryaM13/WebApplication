import java.util.ArrayList;
/**

 */
public class Element {

    private String movieId;
    private String movieTitle;
    private String movieYear;
    private String movieDirector;
    private String count;
    public ArrayList<String> saleId;
       
    public String getMovieId()
    {
    	return movieId;
    }
    
    public String getMovieTitle()
    {
    	return movieTitle;
    }
    
    public String getMovieYear()
    {
    	return movieYear;
    }
    
    public String getMovieDirector()
    {
    	return movieDirector;
    }
    public String getCount()
    {
    	return count;
    }
    public void setCount(String count)
    {
    	this.count=count;
    }
    public void setSaleId(String SaleId, int count) {
    	this.saleId.add(count,SaleId);
    }
    public String getSaleId(int count) {
    	return saleId.get(count);
    }
    public Element(String movieTitle, String count, String movieId) {
        this.movieId = movieId;
        this.movieTitle=movieTitle;
        this.count=count;
        //this.movieYear=movieYear;
        //this.movieDirector=movieDirector;
        saleId=new ArrayList<String>();
        
    }
   
}
