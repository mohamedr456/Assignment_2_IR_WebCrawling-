package org.example;

import java.util.Map;
import java.util.*;

public class Main {

    private static Map<String, Map<String, Double>> convertDocIdToString(Map<Integer, Map<String, Double>> intKeyMap) {
        Map<String, Map<String, Double>> result = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> entry : intKeyMap.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return result;
    }

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

        //query processor
        Map<String, Map<String, Double>> tfidfVectors = convertDocIdToString(calculator.getDocVectors());
        Map<String, Double> idfValues = calculator.getIdfScores();

        QueryProcessor queryProcessor = new QueryProcessor(tfidfVectors, idfValues);
        Scanner scanner = new Scanner(System.in);
        System.out.print("please Enter your query: ");
        String query = scanner.nextLine();

        List<String> results = queryProcessor.search(query);

        if (results.isEmpty()) {
            System.out.println("No documents matched your query.");
        } else {
            System.out.println("Top matching documents:");
            for (String doc : results) {
                System.out.println(doc);
            }
        }
    }
}
