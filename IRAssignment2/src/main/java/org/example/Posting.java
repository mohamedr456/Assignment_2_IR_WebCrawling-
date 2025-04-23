package org.example;

public class Posting {
    private final int docID;
    private final int termFrequency;

    public Posting(int docID, int termFrequency) {
        this.docID = docID;
        this.termFrequency = termFrequency;
    }

    public int getDocID() {
        return docID;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    @Override
    public String toString() {
        return "(docID=" + docID + ", tf=" + termFrequency + ")";
    }
}
