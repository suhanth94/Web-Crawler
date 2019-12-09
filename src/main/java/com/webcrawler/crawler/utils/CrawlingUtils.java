package com.webcrawler.crawler.utils;

import com.webcrawler.crawler.dataobject.CrawlerLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class will hold all the utility methods required for crawling operations
 */

@Component
public class CrawlingUtils {

    /**
     * Placeholder for logger instance
     **/
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * Defining metadata constants of href selectors
     *
     */
    private static String HREF_LINK_SELECTOR = "a[href]";
    private static String HREF_ATTR_SELECTOR = "abs:href";


    /**
     * This utilty method extracts all the DOM elements of a given  URL.
     * @param url - Input URL
     * @return - returns the elements based on the selector
     */
    public Elements extractElements(String url) {

        Elements elements = null;
        try {
            elements = Jsoup.connect(url).get().select(HREF_LINK_SELECTOR);
        } catch (IOException e) {
            LOGGER.error("Connection Failed: Unable to retrieve elements");
        }

        return elements;
    }

    /**
     * This utility method converts a given DOM element to crawler link bean and stores its
     * URL and Host name in raw string format
     * @param element - DOM element
     * @return - Converts to crawler link if exists, else empty
     */
    public Optional<CrawlerLink> convertToCrawlerLink(Element element) {
        try {
            return Optional.of(new CrawlerLink(new URL(element.attr(HREF_ATTR_SELECTOR))));

        } catch (MalformedURLException e) {
            LOGGER.warn("Unable to form URL - Malformed state");
        }

        return Optional.empty();
    }

    /**
     * This utility method filters out all the required links which need to be crawled in the next
     * iteration.
     * @param elements - DOM elements
     * @param includeExternal - If this flag is set to true, it also considers external links outside
     *                          the given domain. If not, limits filtering to internal domain.
     * @param crawlerLink - The crawler link to bean to compare the hostname while filtering
     * @return
     */
    public List<CrawlerLink> filterLinks(Elements elements,
                                            boolean includeExternal,
                                            CrawlerLink crawlerLink) {


        List <CrawlerLink> allLinks = elements.stream()
                .map(element -> this.convertToCrawlerLink(element))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (!includeExternal) {
            return allLinks.stream()
                    .filter(link -> link.getHost().equalsIgnoreCase(crawlerLink.getHost()))
                    .collect(Collectors.toList());
        }

        return allLinks;

    }

    /**
     * This utility method converts a url string to crawler link bean and stores its
     * URL and Host name in raw string format
     * @param url - URL
     * @return - Converts to crawler link if exists, else empty
     */
    public Optional<CrawlerLink> convertToCrawlerLink(String url) {
        try {
            return Optional.of(new CrawlerLink(new URL(url)));

        } catch (MalformedURLException e) {
            LOGGER.warn("Unable to form URL - Malformed state");
        }

        return Optional.empty();
    }
}