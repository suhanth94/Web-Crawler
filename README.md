# Web Crawler

This application is a web crawler i.e. given a root URL, it all does crawl all web URL's within the root URL's domain and returns a HTTP response returning the textual link map and relations between them.

# Tools & Build

* Java 8+
* Spring Boot
* Maven Build - 3.3.9 version
* Jsoup (for HTML extractions)
* Mockito (for tests)

# Startup Instructions

* From Command Line:
    * Make sure maven and java is installed and added to system path
    * Run Command - mvn clean install
    * Run Command - java -jar target/crawler-0.0.1-SNAPSHOT.jar from the application root folder
    
* From IDE (like IntelliJ)
    * Import the project using pom.xml
    * Run as SpringBootApplication/ Go to main class CrawlerApplication -> Right Click -> Run As 
  
# API

There are two API's defined in the controller. One is for healthcheck and another one is for crawling.

* Use any API client like "Postman" or can be directly run from the browser since these are GET requests.
* Check if the service is up and running : http://localhost:8086/api/ping
* Pass any URL to crawl API like for example: http://localhost:8086/api/crawl?url=https://www.google.com
* Return the response in the format of JSON (each key mapping to list of adjacent links)

# Configuration Properties

There are few configuration properties/settings for crawler defined in application.properties 

* threadCount : This is to mention number of threads needed to intialize the executor service to run parallely.
* includeExternal: This flag is to include URL's outside the root URL domain during the crawling process
* maxTimeout: This property is the maximum timeout the call can wait to be polled from queue before ending the process.
* maxResults: This is to limit number of the results/keys in the textual sitemap response.

# Implementation Details

* A queue is maintained for all the links coming in order and to recursively crawl them once after the other (like breadth first search in a graph).
* A visited set is maintained to mark a link/URL as visited so as to not repeat the crawl process again once completed.
* A hashmap is maintain inorder to store the link URL and its adjacent links to return as output (similar to adjacency list representation of a graph).
* Crawling is processed by executor service tasks parallely based on the configuration parameters.
