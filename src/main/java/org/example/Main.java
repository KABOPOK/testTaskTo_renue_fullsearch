package org.example;

import org.example.App.SearchEngine;
import org.example.App.VectorSearchEngine;
import org.example.Classes.DataArgs;
import org.example.Classes.RelevanceRecord;
import org.example.Classes.Report;
import org.example.Classes.SearchResult;
import org.example.Parser.CmdParser;
import org.example.Parser.CsvReportReader;
import org.example.Parser.ReportReader;
import org.example.Parser.RussianTextPreprocessor;
import org.example.Parser.TextPreprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    long initTime = System.currentTimeMillis();

    CmdParser cmdParser = new CmdParser();
    TextPreprocessor preprocessor = new RussianTextPreprocessor();
    ReportReader reader = new CsvReportReader('|', preprocessor);
    SearchEngine searchEngine = new VectorSearchEngine(preprocessor);

    DataArgs dataArgs = cmdParser.getData(args);
    List<Report> reports = reader.readReports(dataArgs.getInFile());
    System.out.println("Load reports: " + reports.size());
    searchEngine.indexReports(reports);
    long endTime = System.currentTimeMillis();
    initTime = endTime - initTime;

    List<SearchResult.SearchData> searchResults = new ArrayList<>();
    try (BufferedReader queryReader = new BufferedReader(new FileReader(dataArgs.getTargetFile()))) {
      String query;
      while ((query = queryReader.readLine()) != null) {
        if (query.trim().isEmpty()) continue;
        System.out.println("Processing query: " + query);
        long queryStartTime = System.currentTimeMillis();
        List<RelevanceRecord> results = searchEngine.search(query, 5, 0.1);
        long queryEndTime = System.currentTimeMillis();
        long queryTime = queryEndTime - queryStartTime;
        List<String> resultIds = new ArrayList<>();
        if (results.isEmpty()) {
          System.out.println("Not found.");
        } else {
          for (RelevanceRecord res : results) {
            System.out.printf("[%s]\nDescription: %s\nRelevant: %.2f%%\n\n",
                    res.getReport().getId(), res.getReport().getDescription(), res.getRelevance() * 100);
            resultIds.add(res.getReport().getId());
          }
        }
        searchResults.add(new SearchResult.SearchData(query, resultIds, queryTime));
      }
      SearchResult searchResult = new SearchResult(initTime, searchResults);
      try {
        searchResult.writeToFile(dataArgs.getOutFile());
        System.out.println("Results saved to: " + dataArgs.getOutFile());
      } catch (IOException e) {
        System.err.println("Error writing to output file: " + e.getMessage());
        e.printStackTrace();
      }
    } catch (IOException e) {
      System.err.println("Error reading from input file: " + e.getMessage());
      e.printStackTrace();
    }
  }
}