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


    public String getUrl() {
        return url;
    }

    public Domain getDomain() {
        return domain;
    }

    /**
     * The WordScraper uses a domain and extracts the URL.
     * It has functionality to search for a search-term (Search)
     * The selector is the top level, all text contained within the selector is parsed.
     * Text which lies outside that which the selector matches is not parsed.
     * @param domain The start domain
     * @param selector A css selector
     */
    public WordScraper(Domain domain, String selector) {
        this.url = domain.getURL();
        this.domain = domain;
        this.selector = selector;
        connectToUrl();
    }

    /**
     * Switches domain and connects to its' url
     * @param newDomain the new domain
     * @return this
     */
    public WordScraper switchDomainAndConnect(Domain newDomain)  {
        switchDomain(newDomain);
        connectToUrl();
        return this;
    }

    private WordScraper switchDomain(Domain newDomain) {
        this.url = newDomain.getURL();
        this.domain = newDomain;
        return this;
    }

    /**
     * Connects to current url and fetches the document.
     * @return
     */
     private WordScraper connectToUrl() {
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

    /**
     * The public method for searching the current web-page.
     * Uses a regex pattern matcher.
     * @param string  The string to search for.
     * @return A list of all hits with specified string
     */
    public List<Hit> searchWithMatcher(Search string) {
        return listAllHitsUsingMatcher(string);
    }

    /**
     * Method to split a document into text by selector specified in constructor
     * Method is not currently in use.
     * @return List of text-chunks
     */
    @Deprecated
    private List<String> splitDocumentIntoChunks() {
        var lines = new ArrayList<String>();
        doc.select(selector).forEach(x -> lines.add(x.text()));
        return lines;
    }

    /**
     * Method to remove duplicated in a list of string
     * Method is not currently in use.
     * @param strings list of strings
     * @return list of strings without duplicates
     */
    private List<String> removeDuplciates(List<String> strings) {
        var map = new HashSet<>(strings);
        return new ArrayList<>(map);
    }


    /**
     * Lists all the hits on a page, using a regex pattern matcher.
     * Creates the context by using the matcher start and end points, making sure not to fall out of bounds
     *
     * @param string
     * @return
     */
    private List<Hit> listAllHitsUsingMatcher(Search string) {
        var hits = new ArrayList<Hit>();
        Pattern pattern = Pattern.compile(string.getWord().toLowerCase());
        String documentText = getDocumentText().toLowerCase();
        Matcher matcher = pattern.matcher(documentText);
        while (matcher.find()) {
            hits.add(new Hit(string.getId(),
                    this.domain.getId(),
                    documentText.substring(
                            Math.max((matcher.start() - CONTEXT_LENGTH), 0), //So as to not fall out of string bounds
                            Math.min((matcher.end() + CONTEXT_LENGTH), documentText.length() - 1)
                    )
            ));
        }
        return hits;
    }

    /**Returns the plain text of a document as a String
     * uses the selector specified
     *
     * @return the plain text in a document
     */
    private String getDocumentText() {
        return this.doc.select(this.selector).text();
    }

    /**
     * Returns all the links at a url
     * @return the urls
     */
    public List<String> returnAllLinksInDomain() {
        var links = new ArrayList<String>();
        doc.select("a").stream().map(x -> x.attr("href")).forEach(links::add);
        return links;
    }
}
