package com.webcrawler.crawler.controller;


import com.webcrawler.crawler.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * This controller class is responsible for serving REST API requests on crawling.
 */

@RestController
@RequestMapping("/api")
public class CrawlerController {

    @Autowired
    CrawlerService crawlerService;

    /**
     * Health check endpoint to check if the service is up and running
     * @return HTTP Response String
     */
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok().body("Crawler Service is up and running!!!");
    }

    /**
     * This API takes an input URL and initiates the crawling process
     * which then returns the textual related links.
     *
     * @param rootURL - The input URL
     * @return -  HTTP JSON response of link mappings
     */
    @RequestMapping(value = "/crawl", method = RequestMethod.GET, produces ="application/json")
    public ResponseEntity<Map<String, List<String>>> doCrawl(@RequestParam(value = "url") final String rootURL) {
        Map<String, List<String>> results = crawlerService.initiateCrawlingProcess(rootURL);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
