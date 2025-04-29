package org.example;
import java.util.*;

public class QueryProcessor {

    private final Map<String, Map<String, Double>> documentVectors;
    private final Map<String, Double> idfValues;

    public QueryProcessor(Map<String, Map<String, Double>> documentVectors,
                          Map<String, Double> idfValues) {
        this.documentVectors = documentVectors;
        this.idfValues = idfValues;
    }

    private List<String> tokenizeAndNormalize(String query) {
        query = query.toLowerCase();
        return Arrays.asList(query.split("\\W+"));
    }

    private Map<String, Double> buildQueryVector(List<String> tokens) {
        Map<String, Integer> termFreq = new HashMap<>();
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            termFreq.put(token, termFreq.getOrDefault(token, 0) + 1);
        }

        Map<String, Double> queryVector = new HashMap<>();
        for (String term : termFreq.keySet()) {
            if (idfValues.containsKey(term)) {
                double tf = 1 + Math.log10(termFreq.get(term));
                double idf = idfValues.get(term);
                queryVector.put(term, tf * idf);
            }
        }
        return queryVector;
    }

    public List<String> search(String query) {
        List<String> tokens = tokenizeAndNormalize(query);
        Map<String, Double> queryVector = buildQueryVector(tokens);

        if (queryVector.isEmpty()) {
            System.out.println("No matching terms for this query.");
            return new ArrayList<>();
        }

        List<DocumentScore> scores = new ArrayList<>();


        for (String docId : documentVectors.keySet()) {
            Map<String, Double> docVector = documentVectors.get(docId);
            double score = computeCosineSimilarity(queryVector, docVector);
            if (score > 0.0) {
                scores.add(new DocumentScore(docId, score));
            }
        }

        scores.sort((a, b) -> Double.compare(b.score, a.score));

        List<String> topDocs = new ArrayList<>();
        for (int i = 0; i < Math.min(10, scores.size()); i++) {
            topDocs.add(scores.get(i).docId);
        }
        return topDocs;
    }

    private double computeCosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> allTerms = new HashSet<>(vec1.keySet());
        allTerms.retainAll(vec2.keySet());

        double dotProduct = 0.0;
        for (String term : allTerms) {
            dotProduct += vec1.get(term) * vec2.get(term);
        }

        double norm1 = Math.sqrt(vec1.values().stream().mapToDouble(v -> v * v).sum());
        double norm2 = Math.sqrt(vec2.values().stream().mapToDouble(v -> v * v).sum());

        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dotProduct / (norm1 * norm2);
    }

    private static class DocumentScore {
        String docId;
        double score;

        DocumentScore(String docId, double score) {
            this.docId = docId;
            this.score = score;
        }
    }
}


