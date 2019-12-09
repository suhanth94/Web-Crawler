package com.webcrawler.crawler.controller;

import com.webcrawler.crawler.service.CrawlerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CrawlerControllerTest {

    @InjectMocks
    CrawlerController crawlerController;

    @Mock
    CrawlerService crawlerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHealthCheck() {
        ResponseEntity<String> result = crawlerController.healthCheck();
        assertNotNull(result);
    }

    @Test
    void testDoCrawl() {
        String rootURL = "https://www.google.com";
        when(crawlerService.initiateCrawlingProcess(rootURL)).thenReturn(new HashMap<>());
        ResponseEntity<Map<String, List<String>>> result = crawlerController.doCrawl(rootURL);
        assertTrue(result.getStatusCode().is2xxSuccessful());
    }
}
