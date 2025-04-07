package org.example.Classes;

import java.util.HashSet;

public class Report {
  private String id;
  private HashSet<String> description; // Unique words only

  public Report(String id, HashSet<String> description) {
    this.id = id;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public HashSet<String> getDescription() {
    return description;
  }

  public void setDescription(HashSet<String> description) {
    this.description = description;
  }
}
