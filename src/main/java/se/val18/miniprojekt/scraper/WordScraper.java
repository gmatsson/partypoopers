package se.val18.miniprojekt.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import se.val18.miniprojekt.repo.Domain;
import se.val18.miniprojekt.repo.Hit;
import se.val18.miniprojekt.repo.Search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordScraper {
    private static final int CONTEXT_LENGTH = 50;
    private String url;
    private Document doc;
    private Domain domain;
    private String selector;


    public WordScraper(Domain domain, String selector) {
        this.url = domain.getURL();
        this.domain = domain;
        this.selector = selector;
        connectToUrl();
    }

    public WordScraper switchDomain(Domain newDomain) {
        this.url = newDomain.getURL();
        this.domain = newDomain;
        return this;
    }

    public WordScraper connectToUrl() {
        try {
            this.doc = Jsoup.connect(this.url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public WordScraper connectToFile() {
        try {
            this.doc = Jsoup.parse(new File(url), "UTF-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    public List<Hit> searchWithMatcher(Search string) {
        return listAllHitsUsingMatcher(string);
    }


    private List<String> splitDocumentIntoChunks(String cssSelector) {
        var lines = new ArrayList<String>();
        doc.select(cssSelector).forEach(x ->  lines.add(x.text()));
        return lines;
    }

    private List<String> removeDuplciates(List<String> strings) {
        var map = new HashSet<>(strings);
        return new ArrayList<>(map);
    }

    private List<String> splitDocumentIntoChunks() {
        if (this.domain == null) {
            throw new IllegalStateException("Scraper.domain can not be null when using method without specified selector");
        }
        var lines = new ArrayList<String>();
        doc.select(selector).forEach(x ->  lines.add(x.text()));
        return lines;
    }


    private List<Hit> listAllHitsUsingMatcher(Search string) {
        var hits = new ArrayList<Hit>();
        Pattern pattern = Pattern.compile(string.getWord().toLowerCase());
        String documentText =getDocumentText().toLowerCase();
        Matcher matcher = pattern.matcher(documentText);
        while (matcher.find()) {
            hits.add(new Hit(string.getId(),
                    this.domain.getId(),
                    documentText.substring(
                            Math.max((matcher.start() - CONTEXT_LENGTH),0), //So as to not fall out of string bounds
                            Math.min((matcher.end() + CONTEXT_LENGTH),documentText.length()-1))
            ));
        }
        return hits;
    }

    private String getDocumentText() {
        return this.doc.select(this.selector).text();
    }

    private List<Hit> listAllHitsOnSearch(List<String> strings, Search string) {
        var appearances = new ArrayList<Hit>();
        strings.stream()
                .filter(x -> x.contains(string.getWord()))
                .forEach(x -> appearances.add(new Hit(string.getId(), this.domain.getId(), x)));
        return appearances;
    }

    public List<String> returnAllLinksInDomain() {
        var links = new ArrayList<String>();
        doc.select("a").stream().map(x-> x.attr("href")).forEach(links::add);
        return links;
    }
}
