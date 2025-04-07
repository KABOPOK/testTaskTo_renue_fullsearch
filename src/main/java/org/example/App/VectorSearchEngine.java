package org.example.App;

import org.example.Classes.RelevanceRecord;
import org.example.Classes.Report;
import org.example.Parser.TextPreprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VectorSearchEngine implements SearchEngine {
  private List<Report> reports;
  private final Map<String, Integer> vocabulary;
  private final Map<Report, double[]> vectors;

  private final TextPreprocessor preprocessor;

  public VectorSearchEngine(TextPreprocessor preprocessor) {
    this.reports = new ArrayList<>();
    this.vocabulary = new HashMap<>();
    this.vectors = new HashMap<>();
    this.preprocessor = preprocessor;
  }

  @Override
  public void indexReports(List<Report> reports) {
    this.reports = reports;
    Set<String> allWords = new HashSet<>();
    for (Report report : reports) {
      allWords.addAll(report.getPreprocessedDescription());
    }
    int index = 0;
    for (String word : allWords) {
      vocabulary.put(word, index++);
    }
    for (Report report : reports) {
      double[] vec = new double[vocabulary.size()];
      for (String word : report.getPreprocessedDescription()) {
        Integer i = vocabulary.get(word);
        if (i != null) {
          vec[i] = idf(word); // TF = 1
        }
      }
      normalize(vec);
      vectors.put(report, vec);
    }
  }

  private double idf(String word) {
    int count = 0;
    for (Report report : reports) {
      if (report.getPreprocessedDescription().contains(word)) {
        ++count;
      }
    }
    return Math.log((double) reports.size() / (1 + count));
  }

  private void normalize(double[] vec) {
    double norm = 0.0;
    for (double v : vec) norm += v * v;
    norm = Math.sqrt(norm);
    if (norm != 0) {
      for (int i = 0; i < vec.length; i++) {
        vec[i] /= norm;
      }
    }
  }

  @Override
  public List<RelevanceRecord> search(String query, int maxResults, double minRelevantPercent) {
    if (minRelevantPercent > 1) {
      minRelevantPercent /= 100;
    }
    String[] queryWords = preprocessor.preprocess(query);
    double[] queryVec = new double[vocabulary.size()];
    for (String word : queryWords) {
      if (vocabulary.containsKey(word)) {
        int i = vocabulary.get(word);
        queryVec[i] = idf(word);  // TF = 1
      }
    }
    normalize(queryVec);
    List<RelevanceRecord> results = new ArrayList<>();
    for (Report report : vectors.keySet()) {
      double[] vec = vectors.get(report);
      double sim = 0.0;
      for (int i = 0; i < vec.length; ++i) {
        sim += vec[i] * queryVec[i];
      }
      if (sim > minRelevantPercent) {
        results.add(new RelevanceRecord(report, sim));
      }
    }
    results.sort((a, b) -> Double.compare(b.getRelevance(), a.getRelevance()));
    return results.subList(0, Math.min(maxResults, results.size()));
  }


}
