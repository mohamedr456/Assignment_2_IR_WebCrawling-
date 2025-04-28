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
        TFIDFCalculator calculator = new TFIDFCalculator(index, docs);
        // you can use calculator.getTFIDFVector(docID) as bellow

        // print TF-IDF vector for each document

        for (Integer docId : docs.keySet()) {
            System.out.println("TF-IDF vector for document " + docId + ":");
            Map<String, Double> tfidfVector = calculator.getTFIDFVector(docId);
            for (Map.Entry<String, Double> entry : tfidfVector.entrySet()) {
                System.out.println("  Term: " + entry.getKey() + ", TF-IDF: " + entry.getValue());
            }
            System.out.println();
        }

    }
}
