package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFCalculator {
    private final Map<Integer, Map<String, Double>> docVectors = new HashMap<>();
    private final InvertedIndex invertedIndex;
    private final Map<Integer, String> docs;

    public TFIDFCalculator(InvertedIndex invertedIndex, Map<Integer, String> docs) {
        this.invertedIndex = invertedIndex;
        this.docs = docs;
        computeTFIDFVectors();
    }

    private void computeTFIDFVectors() {
        int totalDocs = docs.size();

        Map<String, Double> idfScores = new HashMap<>();
        for (String term : invertedIndex.getInvertedIndex().keySet()) {
            int df = invertedIndex.getInvertedIndex().get(term).size();
            double idf = Math.log10((double) totalDocs / df);
            idfScores.put(term, idf);
        }

        for (Map.Entry<Integer, String> entry : docs.entrySet()) {
            int docId = entry.getKey();
            Map<String, Double> tfidfVector = new HashMap<>();

            // For each term that exists in the index
            for (String term : invertedIndex.getInvertedIndex().keySet()) {
                List<Posting> postings = invertedIndex.getInvertedIndex().get(term);

                // Find if this document contains this term
                for (Posting posting : postings) {
                    if (posting.getDocID() == docId) {
                        int tf = posting.getTermFrequency();
                        double tfWeight = 1 + Math.log10(tf);
                        double idf = idfScores.get(term);
                        double tfidf = tfWeight * idf;
                        tfidfVector.put(term, tfidf);
                        break; // found term for this document
                    }
                }
            }

            docVectors.put(docId, tfidfVector);
        }
    }

    public Map<String, Double> getTFIDFVector(int docId) {
        return docVectors.get(docId);
    }


    public Map<Integer, Map<String, Double>> getDocVectors() {
        return docVectors;
    }
}