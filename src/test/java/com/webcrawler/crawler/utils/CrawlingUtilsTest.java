package com.webcrawler.crawler.utils;

import com.webcrawler.crawler.dataobject.CrawlerLink;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CrawlingUtilsTest {

    private static String VALID_TEST_URL = "https://www.google.com";
    private static String INVALID_TEST_URL  = "example.com";

    @InjectMocks
    private CrawlingUtils crawlingUtils;

    @Mock
    Element element;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testExtractElementsValidURL() {
        Elements result = crawlingUtils.extractElements(VALID_TEST_URL);
        assertNotNull(result);
    }

    @Test
    void testExtractElementsInvalidURL() {
        assertThrows(IllegalArgumentException.class, ()->crawlingUtils.extractElements(INVALID_TEST_URL));
    }

    @Test
    void testConvertToCrawlerLinkValidElement() {
        when(element.attr(anyString())).thenReturn(VALID_TEST_URL);
        Optional<CrawlerLink> result = crawlingUtils.convertToCrawlerLink(element);
        assertTrue(result.isPresent());
    }

    @Test
    void testConvertToCrawlerLinkInvalidElement() {
        when(element.attr(anyString())).thenReturn(INVALID_TEST_URL);
        Optional<CrawlerLink> result = crawlingUtils.convertToCrawlerLink(element);
        assertFalse(result.isPresent());
    }

    @Test
    void testConvertToCrawlerLinkValidURL() {
        Optional<CrawlerLink> result = crawlingUtils.convertToCrawlerLink(VALID_TEST_URL);
        assertTrue(result.isPresent());
    }

    @Test
    void testConvertToCrawlerLinkInvalidURL() {
        Optional<CrawlerLink> result = crawlingUtils.convertToCrawlerLink(INVALID_TEST_URL);
        assertFalse(result.isPresent());
    }
}
