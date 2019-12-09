package com.webcrawler.crawler.dataobject;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is a placeholder which holds the in-memory storage data structures needed
 * for the process of crawling.
 */
public class CrawlerStorage {

    private Set<String> visitedLinks;
    private BlockingQueue<CrawlerLink> linkQueue;
    private Map<String, List<String>> linkMap;

    private CrawlerStorage() {
        this.visitedLinks = Collections.synchronizedSet(new HashSet<>());
        this.linkQueue = new LinkedBlockingQueue<>();
        this.linkMap = new ConcurrentHashMap<>();
    }

    public Set<String> getVisitedLinks() {
        return visitedLinks;
    }

    public BlockingQueue<CrawlerLink> getLinkQueue() {
        return linkQueue;
    }

    public Map<String, List<String>> getLinkMap() { return linkMap; }

    /**
     * This static method initializes/allocates a fresh storage in memory needed for crawling
     * process.
     * @return - the instance of newly created crawler storage object.
     */
    public static CrawlerStorage initialize(){
        return new CrawlerStorage();
    }
}
