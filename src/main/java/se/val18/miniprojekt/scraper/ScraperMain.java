package se.val18.miniprojekt.scraper;

import se.val18.miniprojekt.repo.Domain;
import se.val18.miniprojekt.repo.Hit;
import se.val18.miniprojekt.repo.Search;

public class ScraperMain {
    public static void main(String[] args) {
        var scrape = new WordScraper(new Domain("https://dn.se", "DN", 123), "body");
        scrape.connectToUrl().searchWithMatcher(new Search(124, "men")).stream().map(Hit::getContext).forEach(System.out::println);


    }
}
