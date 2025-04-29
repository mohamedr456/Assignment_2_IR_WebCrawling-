package org.example;

import java.util.Map;
import java.util.*;

public class Main {
    private static void printMenu(){
        System.out.println("\n--- Information Retrieval System ---");
        System.out.println("1. Crawl and Index Documents");
        System.out.println("2. Print Inverted Index");
        System.out.println("3. Print TF-IDF Vectors");
        System.out.println("4. Search");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }
    private static Map<String, Map<String, Double>> convertDocIdToString(Map<Integer, Map<String, Double>> intKeyMap) {
        Map<String, Map<String, Double>> result = new HashMap<>();
        for (Map.Entry<Integer, Map<String, Double>> entry : intKeyMap.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        WebCrawler crawler = new WebCrawler();
        InvertedIndex index = new InvertedIndex();
        TFIDFCalculator calculator = null;
        Map<Integer, String> docs = new HashMap<>();
        boolean isCrawled = false;

        while (true) {
            printMenu();

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter number of documents to crawl: ");
                    int count = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    docs = crawler.crawl(count);
                    index.buildIndex(docs);
                    calculator = new TFIDFCalculator(index, docs);
                    isCrawled = true;
                    System.out.println("Crawling and indexing completed.");
                    break;

                case 2:
                    if (!isCrawled) {
                        System.out.println("You need to crawl and index documents first.");
                        break;
                    }
                    index.printIndex();
                    break;

                case 3:
                    if (!isCrawled || calculator == null) {
                        System.out.println("You need to crawl and index documents first.");
                        break;
                    }
                    for (Integer docId : docs.keySet()) {
                        System.out.println("TF-IDF vector for document " + docId + ":");
                        Map<String, Double> tfidfVector = calculator.getTFIDFVector(docId);
                        for (Map.Entry<String, Double> entry : tfidfVector.entrySet()) {
                            System.out.println("  Term: " + entry.getKey() + ", TF-IDF: " + entry.getValue());
                        }
                        System.out.println();
                    }
                    break;

                case 4:
                    if (!isCrawled || calculator == null) {
                        System.out.println("You need to crawl and index documents first.");
                        break;
                    }
                    Map<String, Map<String, Double>> tfidfVectors = convertDocIdToString(calculator.getDocVectors());
                    Map<String, Double> idfValues = calculator.getIdfScores();
                    QueryProcessor queryProcessor = new QueryProcessor(tfidfVectors, idfValues);

                    System.out.print("Enter your query: ");
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
                    break;

                case 5:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

}
