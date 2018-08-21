package se.val18.miniprojekt.repo;

public class Domain {
    String URL;
    String name;
    long id;

    public Domain(String URL, String name, long id) {
        this.URL = URL;
        this.name = name;
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
