package se.val18.miniprojekt.repo;

import java.time.LocalDateTime;

public class Hit {
    int id;
    int wordId;
    int domainId;
    String context;
    LocalDateTime createTime;

    public Hit(int wordId, int domainId, String context) {
        this.wordId = wordId;
        this.domainId = domainId;
        this.context = context;
    }

    public Hit(int id, int wordId, int domainId, String context) {
        this.id = id;
        this.wordId = wordId;
        this.domainId = domainId;
        this.context = context;
    }


    public int getId() {
        return id;
    }

    public int getWordId() {
        return wordId;
    }

    public int getDomainId() {
        return domainId;
    }

    public String getContext() {
        return context;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
