package org.example;

import java.util.*;
import java.util.regex.Pattern;

public class InvertedIndex {
    private final Map<String, List<Posting>> invertedIndex = new HashMap<>();

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't",
            "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but",
            "by", "can't", "cannot", "could", "couldn't", "couldn't", "couldnt", "did", "didn't", "didnt", "does",
            "doesn't", "doesnt", "don't", "don't", "dont", "down", "during", "each", "few", "for", "from", "further",
            "had", "hadn't", "hadnt", "has", "hasn't", "hasnt", "haven't", "havent", "having", "here", "here's",
            "here's", "hereafter", "herein", "hereupon", "hers", "here's", "herewith", "how", "how's", "however",
            "howsoever", "i", "i'd", "i'll", "i'm", "i've", "i", "if", "if's", "in", "in's", "into", "is", "isn't",
            "isnt", "isn't", "it", "it's", "it", "it's", "it's", "its", "itself", "it", "let's", "let", "me", "more",
            "most", "much", "my", "myself", "my", "myself", "of", "on", "once", "only", "or", "other", "ought",
            "ourselves", "ours", "ours", "ourselves", "out", "over", "per", "so", "some", "that", "that’s",
            "that's", "that", "the", "that’s", "their", "theirs", "themselves", "they", "they're", "they've",
            "they", "they're", "those", "through", "throughout", "to", "together", "too", "until", "very", "was",
            "wasn't", "wasnt", "we", "we'll", "we're", "we've", "we", "we'll", "we're", "we", "we're", "with",
            "within", "without", "you", "you'd", "you'll", "you're", "you've", "you", "your", "yourself",
            "yours", "yourselves", "you", "you're"
    ));

    //  is the term Stopword
    private boolean isStopword(String token) {
        return STOPWORDS.contains(token);
    }

    //  Tokenizer
    private String[] tokenize(String text) {
        return text.toLowerCase().split("\\W+");
    }

    public void buildIndex(Map<Integer, String> docs) {
        for (Map.Entry<Integer, String> entry : docs.entrySet()) {
            int docID = entry.getKey();
            String text = entry.getValue();

            String[] tokens = tokenize(text);
            Map<String, Integer> termFreq = new HashMap<>();

            // Count term frequency
            for (String token : tokens) {
                if (token.isBlank() || isStopword(token)) continue;
                termFreq.put(token, termFreq.getOrDefault(token, 0) + 1);
            }

            // Adding term frequency to inverted index
            for (Map.Entry<String, Integer> tfEntry : termFreq.entrySet()) {
                String term = tfEntry.getKey();
                int freq = tfEntry.getValue();

                invertedIndex.putIfAbsent(term, new ArrayList<>());
                invertedIndex.get(term).add(new Posting(docID, freq));
            }
        }
    }
    //  print inverted index ; ana bast5dmha 3a4an a4of el results bs.
    public void printIndex() {
        for (Map.Entry<String, List<Posting>> entry : invertedIndex.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
    }

    public Map<String, List<Posting>> getIndex() {
        return invertedIndex;
    }
}
