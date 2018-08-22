package se.val18.miniprojekt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.val18.miniprojekt.repo.Domain;
import se.val18.miniprojekt.repo.Hit;
import se.val18.miniprojekt.repo.Repository;
import se.val18.miniprojekt.repo.Search;
import se.val18.miniprojekt.scraper.WordScraper;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class MiniprojektApplication implements CommandLineRunner {

    @Autowired
    Repository repo;
    public static void main(String[] args) {
        SpringApplication.run(MiniprojektApplication.class, args);

    }

    @Override
    public void run(String... strings) throws Exception {

        Domain dom = repo.getAllDomains().get(0);
        Search search = repo.getAllSearch().get(0);


        var scr = new WordScraper(dom, "body");
        String link = scr.returnAllLinksInDomain().get(30);
        scr.switchDomainAndConnect(new Domain(scr.getUrl() + link,
                scr.getDomain().getName(),
                scr.getDomain().getId()));
        scr.returnAllLinksInDomain().forEach(System.out::println);

    }
}
