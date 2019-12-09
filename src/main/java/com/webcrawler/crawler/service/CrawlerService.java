package com.webcrawler.crawler.service;

import com.webcrawler.crawler.dataobject.CrawlerStorage;
import com.webcrawler.crawler.utils.CrawlingUtils;
import com.webcrawler.crawler.dataobject.CrawlerLink;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CrawlerService {

    @Autowired
    public CrawlingUtils crawlUtils;

    /**
     * Placeholder to set crawler properties defined.
     */

    @Value("${crawler.threadCount}")
    private int crawlerThreads;

    @Value("${crawler.includeExternal}")
    private boolean includeExternal;

    @Value("${crawler.maxTimeout}")
    private long maxTimeout;

    @Value("${crawler.maxResults}")
    private long maxResults;

    /**
     * Placeholder for logger instance
     **/
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    /**
     * This method is responsible for initiating the crawling process. It first creates a new
     * executor service and thread pool based on given parameters and submits runnable to future
     * tasks to trigger crawling process concurrently.
     * @param rootURL - The input URL
     */
    public Map<String, List<String>> initiateCrawlingProcess(String rootURL) {

        CrawlerStorage storage = CrawlerStorage.initialize();
        storage.getLinkQueue().add(crawlUtils.convertToCrawlerLink(rootURL).get());


        ExecutorService crawlerExecutor = Executors.newFixedThreadPool(crawlerThreads);
        List<Future> crawlerFutures = new ArrayList<>();

        //Trigger crawling process
        IntStream.rangeClosed(1, crawlerThreads).forEach(thread -> {
            crawlerFutures.add(
                    crawlerExecutor.submit(() -> {
                    while (true) {
                        try {
                                //Poll each item from queue
                                CrawlerLink currentLink = storage.getLinkQueue().poll(maxTimeout, TimeUnit.SECONDS);
                                if (currentLink == null) {
                                    break;
                                }

                                //Check max results
                                if(storage.getLinkMap().size() > maxResults){
                                    break;
                                }

                                //If not visited yet, mark to visited and process crawling.
                                String currentLinkUrl = currentLink.getUrl();
                                if (!storage.getVisitedLinks().contains(currentLinkUrl)) {
                                        storage.getVisitedLinks().add(currentLinkUrl);
                                        this.crawlPage(currentLink, storage);
                                }

                            }catch (InterruptedException e) {
                                LOGGER.error("Polling interrupted with exception - {}",e.getMessage());
                                if (storage.getLinkQueue().isEmpty()) {
                                    break;
                                }

                            }
                        }
                    }));
        });

        crawlerFutures.stream().forEach(futureTask -> {
            try {
                futureTask.get();
            } catch (InterruptedException ie) {
                LOGGER.error("Future tasks interrupted with exception- {}", ie.getMessage());
            } catch(ExecutionException ee){
                LOGGER.error("Execution exception- {}", ee.getMessage());
            }
        });

        return storage.getLinkMap();
    }


    /**
     * This method is to extract all adjacent links for a given URL and based on visit flag,
     * add to the queue to process recursively.
     *
     * It does also adds the link and its related links to the result map.
     *
     * @param linkToCrawl - The crawler link object
     * @param storage - The storage object
     */
    public void crawlPage(CrawlerLink linkToCrawl, CrawlerStorage storage) {


        //Extraction Process
        String currentURL = linkToCrawl.getUrl();
        Elements elements = crawlUtils.extractElements(currentURL);
        List<CrawlerLink> filteredLinks = crawlUtils.filterLinks(elements, includeExternal, linkToCrawl);


        //Add to the result map
        if(!storage.getLinkMap().containsKey(currentURL)){
            List<String> relatedLinks = Collections.synchronizedList(new ArrayList<>());
            relatedLinks.addAll(
                     filteredLinks.stream().distinct()
                    .map(CrawlerLink::getUrl)
                    .collect(Collectors.toList())
            );
            if(!relatedLinks.isEmpty()) {
                storage.getLinkMap().put(currentURL, relatedLinks);
            }
        }

        //Process filtered links and add to link queue if not visited/added.
        filteredLinks.stream().distinct().forEach(link -> {
            String linkStr = link.getUrl();
            if (!storage.getVisitedLinks().contains(linkStr) && !storage.getLinkQueue().contains(link)) {
                storage.getLinkQueue().offer(link);
                LOGGER.info("Link added to queue - {}", linkStr);
            }
        });
    }
}