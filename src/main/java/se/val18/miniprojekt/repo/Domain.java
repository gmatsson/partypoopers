package se.val18.miniprojekt.repo;

public class Domain {
    String URL;
    String name;
    String path;
    int id;


    public Domain(String URL, String name, String path, int id) {
        this.URL = URL;
        this.name = name;
        this.path = path;
        this.id = id;
    }

    public Domain(String URL, String name, int id) {
        this.URL = URL;
        this.name = name;
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public String getURL() {
        return URL;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Domain{" +
                "URL='" + URL + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
