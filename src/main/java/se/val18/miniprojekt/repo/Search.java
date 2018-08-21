package se.val18.miniprojekt.repo;

public class Search {
    int id;
    String word;

    public Search(int id, String word) {
        this.id = id;
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }
}
