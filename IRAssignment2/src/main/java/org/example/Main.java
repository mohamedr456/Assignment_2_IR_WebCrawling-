package org.example;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, String> docs = new WebCrawler().crawl(10);
        System.out.println(docs);
    }
}