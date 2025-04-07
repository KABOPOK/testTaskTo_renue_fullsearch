package org.example.Classes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SearchResult {
  private long initTime;
  private List<SearchData> result;

  public SearchResult(long initTime, List<SearchData> result) {
    this.initTime = initTime;
    this.result = result;
  }

  // Getters and setters
  public long getInitTime() {
    return initTime;
  }

  public void setInitTime(long initTime) {
    this.initTime = initTime;
  }

  public List<SearchData> getResult() {
    return result;
  }

  public void setResult(List<SearchData> result) {
    this.result = result;
  }

  // Manually convert the object to a JSON string
  public String toJson() {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"initTime\":").append(initTime).append(",");
    sb.append("\"result\":").append("[");
    if (result != null && !result.isEmpty()) {
      for (int i = 0; i < result.size(); i++) {
        sb.append(result.get(i).toJson());
        if (i < result.size() - 1) {
          sb.append(",");
        }
      }
    }
    sb.append("]");
    sb.append("}");
    return sb.toString();
  }

  // Method to print the result to the console
  public void printToConsole() {
    System.out.println(this.toJson());
  }

  // Method to write the result to an output file
  public void writeToFile(String filename) throws IOException {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(this.toJson());
    }
  }

  public static class SearchData {
    private String search;
    private List<String> result;
    private long time;

    public SearchData(String search, List<String> result, long time) {
      this.search = search;
      this.result = result;
      this.time = time;
    }

    // Getters and setters
    public String getSearch() {
      return search;
    }

    public void setSearch(String search) {
      this.search = search;
    }

    public List<String> getResult() {
      return result;
    }

    public void setResult(List<String> result) {
      this.result = result;
    }

    public long getTime() {
      return time;
    }

    public void setTime(long time) {
      this.time = time;
    }

    // Manually convert the SearchData object to a JSON string
    public String toJson() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      sb.append("\"search\":").append("\"").append(search).append("\"").append(",");
      sb.append("\"result\":").append("[");
      if (result != null && !result.isEmpty()) {
        for (int i = 0; i < result.size(); i++) {
          sb.append("\"").append(result.get(i)).append("\"");  // Add quotes around UUID
          if (i < result.size() - 1) {
            sb.append(",");
          }
        }
      }
      sb.append("]").append(",");
      sb.append("\"time\":").append(time);
      sb.append("}");
      return sb.toString();
    }
  }
}
