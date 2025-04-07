package org.example.Parser;

import org.example.Classes.Report;

import java.util.List;

public interface ReportReader {

  List<Report> readReports(String filePath);

}
