package org.example.App;

import org.example.Classes.RelevanceRecord;
import org.example.Classes.Report;

import java.util.List;

public interface SearchEngine {

  void indexReports(List<Report> reports);

  List<RelevanceRecord> search(String query, int maxResults);

}
