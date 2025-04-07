package org.example.Classes;

import java.util.HashSet;

public class Report {
  private String id;

  private String description;
  private HashSet<String> preprocessedDescription; // Unique words only

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Report(String id, String description, HashSet<String> preprocessedDescription) {
    this.id = id;
    this.description = description;
    this.preprocessedDescription = preprocessedDescription;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public HashSet<String> getPreprocessedDescription() {
    return preprocessedDescription;
  }

  public void setPreprocessedDescription(HashSet<String> preprocessedDescription) {
    this.preprocessedDescription = preprocessedDescription;
  }
}
