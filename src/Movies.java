import java.util.ArrayList;

public class Movies {
private String name;

private String dirctor;

public ArrayList<String> genres=new ArrayList<String>();
//public ArrayList<String> genres=new ArrayList<String>();

private String year;

private String id;

public ArrayList<String> starname=new ArrayList<String>();

public String insertid;
//private String type;


public Movies(){
}


public Movies(String id, String name, String year,String dirctor,int i) {
this.id=id;
this.name = name;
this.dirctor=dirctor;
this.year = year;
this.insertid="xx"+i;
//this.type = type;
}
public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getYear() {
return year;
}


public void setYear(String age) {
this.year = year;
}

public String getName() {
return name;
}

public void setname(String name) {
this.name = name;
}

public String getDirctor() {
return dirctor;
}

public void setDirctor(String dirctor) {
this.dirctor = dirctor;
}

public String getStarname(int i) {
return starname.get(i);
}

public void addStarname(String star) {
starname.add(star);
}
}
