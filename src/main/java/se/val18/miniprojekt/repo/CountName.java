package se.val18.miniprojekt.repo;

public class CountName {
    String name;
    int count;
    String color;

    public CountName(String name, int count, String color) {
        this.name = name;
        this.count = count;
        this.color = color;
    }


    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
