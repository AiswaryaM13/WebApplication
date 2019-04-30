public class Actor {
public String insertid;
public String mid;
private String name;
private String year;

public Actor(){}


public Actor(String name) {
this.name = name;
}

public Actor(String name, String year) {
this.name = name;
this.year = year;
}


public String getYear() {
return year;
}

public String getName() { 
return name; }





}
