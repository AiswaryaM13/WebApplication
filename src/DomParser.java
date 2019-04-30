import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class DomParser {
    Document dmovies;
    Document cast;
    Document actor;
    HashMap<String,Movies> map=new HashMap<String,Movies>();
    HashMap<String,Actor> amap=new HashMap<String, Actor>();
    public void runExample() {
        parseXmlFile();
        parseDocument();
    }
    private void parseXmlFile() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parse using builder to get DOM representation of the XML file
            dmovies = db.parse("mains243.xml");
            cast=db.parse("casts124.xml");
            actor=db.parse("actors63.xml");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        String year="";
        ArrayList<String> genres=new ArrayList<String>();
        Element docEle = dmovies.getDocumentElement();
        Element castdoc= cast.getDocumentElement();
        Element actEle=actor.getDocumentElement();
        NodeList n1 = docEle.getElementsByTagName("film");
        NodeList n2 = castdoc.getElementsByTagName("m");
        NodeList n3 = actEle.getElementsByTagName("actor");
        String ayear="";
        String sname="";
        if (n3 != null && n3.getLength() > 0) {
            for (int i = 0; i < n3.getLength(); i++) {
                Element e3 = (Element) n3.item(i);
                if (getText(e3, "stagename") != null) {
                    sname=getText(e3,"stagename");
                    if(getText(e3, "dob")!=null&&checkifint(getText(e3,"dob"))) {
                        ayear = getText(e3, "dob");
                    }
                    else {
                        ayear=null;
                        System.out.println("Node value: "+(i+1)+" Stars: "+sname+" year of birth not mentioned, taken NULL.");
                    }
                    Actor acto=new Actor(sname,ayear);
                    amap.put(sname,acto);
                }
                else System.out.println("Node value: " + (i+1) + "star name does not exist");
            }
            System.out.println("----------------parsed actor.xml-------------------");
        }
        if (n1 != null && n1.getLength() > 0) {
                for (int i = 0; i < n1.getLength(); i++) {
                    Element e1 = (Element) n1.item(i);
                    if (getText(e1, "fid") != null&&getText(e1,"dirn")!=null&&
                            getText(e1,"t")!=null&&getText(e1,"cat")!=null) {
                        if(getText(e1, "year")!=null&&checkifint(getText(e1,"year"))) {
                             year = getText(e1, "year");
                        }
                        else {
                            year="0";
                            System.out.println("Node value: "+(i+1)+" Movie: "+getText(e1,"t")+" movie year not mentioned using 0 as default");
                        }
                        genres = getText1(e1, "cat");
                        String fid=getText(e1,"fid");
                        String name = getText(e1, "t");
                        String director = getText(e1, "dirn");
                        Movies e = new Movies(fid,name,year,director,(i+1));
                        e.genres=genres;
                        map.put(name,e);
                    }

                    else System.out.println("Node value: " + (i+1) + " Cannot insert movie, data inconsistant.");
                }

            System.out.println("----------------parsed mains.xml-----------------");
        }

        String dname="";
        String mname="";

        if (n2 != null && n2.getLength() > 0) {
            for (int i = 0; i < n2.getLength(); i++) {
                Element e2 = (Element) n2.item(i);
                if (getText(e2, "f") != null&&getText(e2, "a") != null
                        && getText(e2, "t") != null) {

                    dname = getText(e2, "a");
                    mname=getText(e2, "t") ;
                    if(map.containsKey(mname)){
                        Movies m=map.get(mname);
                        String temp=m.insertid;
                        m.addStarname(dname);

                        Actor act1=new Actor(dname);
                        act1.mid=temp;
                        amap.put(dname,act1);
                    }

                }
                else System.out.println("Node value: " + (i+1) + "cannot insert, star data does not exist");
            }
            System.out.println("----------------parsed cast.xml-------------------");
        }
        Iterator iterator1 = amap.entrySet().iterator();
        int counter=0;
        while (iterator1.hasNext()){
            counter++;
            HashMap.Entry entry = (HashMap.Entry) iterator1.next();
            Actor act=(Actor) entry.getValue();
            act.insertid="xx"+counter;
        }
    }

    public Boolean checkifint(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private String getText(Element ele, String tagName) {
        String textVal = null;
        NodeList n1 = ele.getElementsByTagName(tagName);
        try {
            if (n1 != null && n1.getLength() > 0) {
                Element el = (Element) n1.item(0);
                textVal = el.getFirstChild().getNodeValue();
            }
            return textVal;
        }catch (Exception E){
            return null;
        }
    }
    private ArrayList<String> getText1(Element ele, String tagName) {
        String textVal = null;
        ArrayList<String> x=new ArrayList<String>();
        NodeList n1 = ele.getElementsByTagName(tagName);
        try {
            if (n1 != null && n1.getLength() > 0) {
                for(int i=0;i<n1.getLength();i++) {
                    Element el = (Element) n1.item(i);
                    textVal = el.getFirstChild().getNodeValue();
                    x.add(textVal);
                }
            }
            return x;
        }catch (Exception E){
            return null;
        }
    }

    public static void main(String[] args) throws Exception{
        DomParser dpe = new DomParser();
        dpe.runExample();
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://172.31.3.218:3306/moviedb", "mytestuser", "mypassword");
     PreparedStatement psInsertRecord=null;
        String sqlInsertRecord=null;
        String sqlInsertRecord2=null;
        PreparedStatement psInsertRecord2=null;
        PreparedStatement psInsertRecord3=null;
        String sqlInsertRecord3=null;
        PreparedStatement psInsertRecord4=null;
        String sqlInsertRecord4=null;

      ////----------------------------Batch---------------------------------//////////

        long begin_overall = System.currentTimeMillis();
        try {
            sqlInsertRecord="insert into movies values(?,?,?,?); ";
            sqlInsertRecord2="insert into ratings values(?,9,100); ";
            sqlInsertRecord3="call addgenres(?,?); ";
            int counter=0;
            System.out.println("---------------------inserting movies to database-----------------------------");
            conn.setAutoCommit(false);
            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            psInsertRecord2=conn.prepareStatement(sqlInsertRecord2);
            psInsertRecord3=conn.prepareStatement(sqlInsertRecord3);
            Iterator iterator1 = dpe.map.entrySet().iterator();
            long begin = System.currentTimeMillis();
            while (iterator1.hasNext()){
                counter++;
                HashMap.Entry entry = (HashMap.Entry) iterator1.next();
                Movies movies=(Movies) entry.getValue();
                if(!movies.starname.isEmpty()&&!movies.genres.isEmpty()) {
                    psInsertRecord.setString(1, movies.insertid);
                    psInsertRecord.setString(2, movies.getName());
                    psInsertRecord.setString(3, movies.getYear());
                    psInsertRecord.setString(4, movies.getDirctor());
                    psInsertRecord.addBatch();
                    psInsertRecord2.setString(1, movies.insertid);
                    psInsertRecord2.addBatch();
                    for(int j=0;j<movies.genres.size();j++) {
                        psInsertRecord3.setString(1, movies.genres.get(j));
                        psInsertRecord3.setString(2, movies.insertid);
                        psInsertRecord3.addBatch();
                    }
                }
            }
            psInsertRecord.executeBatch();
            psInsertRecord2.executeBatch();
            psInsertRecord3.executeBatch();
            conn.commit();
            System.out.println("-------------------inserting movies to database finished---------------------------");
            System.out.println("Movie size: "+counter);
            long end = System.currentTimeMillis();
            System.out.println("Takes time: "+(end-begin));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            sqlInsertRecord="insert into stars values(?,?,?);";
            sqlInsertRecord4="insert into stars_in_movies values(?,?); ";
            int counter=0;
            System.out.println("---------------------inserting actors to database-----------------------------");
            conn.setAutoCommit(false);
            psInsertRecord=conn.prepareStatement(sqlInsertRecord);
            psInsertRecord4=conn.prepareStatement(sqlInsertRecord4);
            Iterator iterator1 = dpe.amap.entrySet().iterator();
            long begin = System.currentTimeMillis();
            while (iterator1.hasNext()){
                counter++;
                HashMap.Entry entry = (HashMap.Entry) iterator1.next();
                Actor act=(Actor) entry.getValue();
                psInsertRecord.setString(1,act.insertid);
                psInsertRecord.setString(2, act.getName());
                psInsertRecord.setString(3, act.getYear());
                psInsertRecord.addBatch();
                if(act.mid!=null) {
                        psInsertRecord4.setString(1, act.insertid);
                        psInsertRecord4.setString(2, act.mid);
                        psInsertRecord4.addBatch();
                }
            }
            psInsertRecord.executeBatch();
            psInsertRecord4.executeBatch();
            conn.commit();
            System.out.println("-------------------inserting actors to database finished---------------------------");
            System.out.println("Movie size: "+counter);
            long end = System.currentTimeMillis();
            System.out.println("Takes time: "+(end-begin));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long end_overall = System.currentTimeMillis();
        System.out.println("Total Time: "+(end_overall-begin_overall));
        conn.close();
    }
}

