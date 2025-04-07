package org.example.Parser;

import org.example.Classes.Report;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class CsvReportReader implements ReportReader {
  private final char delimiter;

  private final TextPreprocessor preprocessor;

  public CsvReportReader(char delimiter, TextPreprocessor preprocessor) {
    this.delimiter = delimiter;
    this.preprocessor = preprocessor;
  }

  public List<Report> readReports(String filePath) {
    List<Report> reports = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(Pattern.quote(String.valueOf(delimiter)));
        if (parts.length >= 3) {
          try {
            String id = parts[0].trim();
            String rawDescription = parts[2].trim();
            String[] words = preprocessor.preprocess(rawDescription);
            HashSet<String> processedWords = new HashSet<>(Arrays.asList(words));
            reports.add(new Report(id, rawDescription, processedWords));
          } catch (IllegalArgumentException e) {
            System.out.println("Некорректный UUID: " + parts[0]);
          }
        } else {
          System.out.println("Некорректный формат строки: " + line);
        }
      }
    } catch (IOException e) {
      System.out.println("Ошибка чтения файла: " + e.getMessage());
    }
    return reports;
  }

}
