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
  private Map<String, Integer> vocabulary;
  private Map<Report, double[]> vectors;

  private TextPreprocessor preprocessor;

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

    // 1. Собираем все уникальные слова
    for (Report report : reports) {
      allWords.addAll(report.getDescription());
    }

    // 2. Назначаем каждому слову уникальный индекс
    int index = 0;
    for (String word : allWords) {
      vocabulary.put(word, index++);
    }

    // 3. Строим векторы признаков с использованием IDF
    for (Report report : reports) {
      double[] vec = new double[vocabulary.size()];

      for (String word : report.getDescription()) {
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
      if (report.getDescription().contains(word)) {
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

    // Preprocess the query and get unique words
    String[] queryWords = preprocessor.preprocess(query);
    double[] queryVec = new double[vocabulary.size()];

    // For each word in the query, compute its IDF and assign to the query vector
    for (String word : queryWords) {
      if (vocabulary.containsKey(word)) {
        int i = vocabulary.get(word);
        queryVec[i] = idf(word);  // TF = 1, so we just use IDF
      }
    }

    // Normalize the query vector
    normalize(queryVec);

    List<RelevanceRecord> results = new ArrayList<>();

    // Compare the query vector with each report vector (cosine similarity)
    for (Map.Entry<Report, double[]> entry : vectors.entrySet()) {
      double sim = 0.0;
      double[] vec = entry.getValue();
      for (int i = 0; i < vec.length; ++i) {
        sim += vec[i] * queryVec[i];
      }

      // If similarity is above the minimum threshold, add to results
      if (sim > minRelevantPercent) {
        results.add(new RelevanceRecord(entry.getKey(), sim));
      }
    }

    // Sort by relevance (highest first)
    results.sort((a, b) -> Double.compare(b.getRelevance(), a.getRelevance()));

    // Return the top results
    return results.subList(0, Math.min(maxResults, results.size()));
  }


}
