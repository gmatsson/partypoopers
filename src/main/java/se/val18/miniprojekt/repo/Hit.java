package se.val18.miniprojekt.repo;

import java.time.LocalDateTime;

public class Hit {
    long id;
    long wordId;
    long domainId;
    String context;
    LocalDateTime createTime;

    public Hit(long wordId, long domainId, String context) {
        this.id = id;
        this.wordId = wordId;
        this.domainId = domainId;
        this.context = context;
    }
}
