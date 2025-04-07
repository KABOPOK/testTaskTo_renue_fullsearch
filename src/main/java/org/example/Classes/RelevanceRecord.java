package org.example.Classes;

// Простой поиск отчётов без использования сторонних библиотек
public class RelevanceRecord {

  Report report;
  double relevance;

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }

  public double getRelevance() {
    return relevance;
  }

  public void setRelevance(double relevance) {
    this.relevance = relevance;
  }

  public RelevanceRecord(Report report, double relevance) {
    this.report = report;
    this.relevance = relevance;
  }

}
