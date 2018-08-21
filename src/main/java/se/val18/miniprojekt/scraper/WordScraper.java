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

public class WordScraper {
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

    public Elements allElementsBySelector(String selector) {
        return doc.select(selector);
    }

    /**
     * searches for occurences of the name of the given politician
     * returns a list of appearances
     *
     *
     * @return
     */

    /**
     * Searches the current document for hits on search-string with specified selector
     * @param string
     * @param cssSelector
     * @return A list of hits on the page
     */
    List<Hit> searchForAppearance(Search string, String cssSelector) {
        return listAllAppearancesOfSearchString(splitDocumentIntoChunks(cssSelector),string);
    }

     List<Hit> searchForAppearance(Search string) {
        return listAllAppearancesOfSearchString(removeDuplciates(
                splitDocumentIntoChunks(this.selector)
        ), string);
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

    /**
     * takes a css selector and splits the current document into chunks where each
     * chunk is a string containing the text content of each child of every element that matches the css selector
     *
     * @param cssSelector
     * @return ArrayList List of bits of text extracted from the document
     */
    private List<String> splitDocumentIntoChildrenBySelector(String cssSelector) {
        var lines = new ArrayList<String>();
        doc.select(cssSelector).forEach(x -> x.children().forEach(y -> lines.add(y.text())));
        return lines;
    }

    private List<Hit> listAllAppearancesOfSearchString(List<String> strings, Search string) {
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
