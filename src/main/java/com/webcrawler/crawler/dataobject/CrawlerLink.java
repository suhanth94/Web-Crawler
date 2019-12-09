package com.webcrawler.crawler.dataobject;

import org.springframework.util.StringUtils;

import java.net.URL;

/**
 * This class is a data placeholder for link objects which takes in a URL object
 * and stores the url string and the hostname into the link object
 */
public class CrawlerLink {

    private String url;
    private String host;

    public CrawlerLink(URL url) {

        /**
         * Pre-processing the URL string. For example (monzo.com) has duplicate URL's
         * with /# at the ending. This logic trims such characters. This can be extended
         * to custom pre-processing logic as per needs.
         */
        this.url = StringUtils.trimTrailingCharacter
                (StringUtils.trimTrailingCharacter
                        (url.toString(),'#'),'/');
        this.host = url.getHost();
    }

    public String getUrl() {
        return url;
    }

    public String getHost() {
        return host;
    }


    @Override
    public int hashCode() {
        return this.getUrl().hashCode();
    }

    /**
     * Two crawler link objects are considered equal is the url string
     * is same for both
     * @param obj - CrawlerLink Object
     * @return - Result after comparison true/false
     */
    @Override
    public boolean equals(Object obj) {
        return ((CrawlerLink)obj).getUrl().equals(this.getUrl());
    }
}
