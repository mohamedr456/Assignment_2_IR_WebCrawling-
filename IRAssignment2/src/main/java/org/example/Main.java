package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Initialize WebCrawler and InvertedIndex
        WebCrawler crawler = new WebCrawler();
        Map<Integer, String> docs = crawler.crawl(10);

        // Build Inverted Index
        InvertedIndex index = new InvertedIndex();
        index.buildIndex(docs);

//       uncomment law 3ayz t4of el inverted index 
//        index.printIndex();

    }
}
