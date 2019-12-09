package com.webcrawler.crawler.service;

import com.webcrawler.crawler.dataobject.CrawlerLink;
import com.webcrawler.crawler.dataobject.CrawlerStorage;
import com.webcrawler.crawler.utils.CrawlingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CrawlerServiceTest {

    private static String VALID_TEST_URL = "https://www.google.com";
    private static String VALID_EXTERNAL_URL = "https://mail.google.com/mail/?tab=wm&ogbl";
    private static String INVALID_TEST_URL  = "example.com";

    @InjectMocks
    private CrawlerService crawlerService;

    @Spy
    CrawlingUtils crawlingUtils;

    @Mock
    CrawlerLink crawlerLink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(crawlerService, "crawlerThreads", 5);
        ReflectionTestUtils.setField(crawlerService, "includeExternal", false);
        ReflectionTestUtils.setField(crawlerService, "maxTimeout", 2);
        ReflectionTestUtils.setField(crawlerService, "maxResults", 5);
    }


    @Test
    void testInitiateCrawlingProcessRootURL() {
        ReflectionTestUtils.setField(crawlerService, "includeExternal", false);
        when(crawlerLink.getUrl()).thenReturn(VALID_TEST_URL);
        when(crawlerLink.getHost()).thenReturn(VALID_TEST_URL);
        Map<String, List<String>> result = crawlerService.initiateCrawlingProcess(VALID_TEST_URL);
        assertTrue(result.get(VALID_TEST_URL).size() > 0);
        assertFalse(result.get(VALID_TEST_URL).contains(VALID_EXTERNAL_URL));


    }

    @Test
    void testInitiateCrawlingProcessRootURLWithExternal() {
        ReflectionTestUtils.setField(crawlerService, "includeExternal", true);
        when(crawlerLink.getUrl()).thenReturn(VALID_TEST_URL);
        when(crawlerLink.getHost()).thenReturn(VALID_TEST_URL);
        Map<String, List<String>> result = crawlerService.initiateCrawlingProcess(VALID_TEST_URL);
        assertTrue(result.get(VALID_TEST_URL).contains(VALID_EXTERNAL_URL));
    }

    @Test
    void testCrawlPageValidURL() {
        CrawlerStorage storage = CrawlerStorage.initialize();
        when(crawlerLink.getUrl()).thenReturn(VALID_TEST_URL);
        when(crawlerLink.getHost()).thenReturn(VALID_TEST_URL);
        crawlerService.crawlPage(crawlerLink, storage);
    }

    @Test
    void testCrawlPageInvalidURL() {
        CrawlerStorage storage = CrawlerStorage.initialize();
        when(crawlerLink.getUrl()).thenReturn(INVALID_TEST_URL);
        when(crawlerLink.getHost()).thenReturn(INVALID_TEST_URL);
        assertThrows(IllegalArgumentException.class
                ,()->crawlerService.crawlPage(crawlerLink, storage));
    }
}
