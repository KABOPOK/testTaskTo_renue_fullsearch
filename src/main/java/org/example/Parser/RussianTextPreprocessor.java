package org.example.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RussianTextPreprocessor implements TextPreprocessor {
  private Set<String> stopWords;

  public RussianTextPreprocessor() {
    stopWords = new HashSet<>(Arrays.asList(
            "через", "когда",
            "который", "этот", "такой", "меня", "мной", "(или)", "отчет", "сведения"
    ));
  }

  public String[] preprocess(String text) {
    if (text == null || text.trim().isEmpty()) return new String[0];

    text = text.toLowerCase();
    String[] words = text.split("\\s+");
    List<String> result = new ArrayList<>();

    for (String word : words) {
      if (word.length() > 3 && !stopWords.contains(word)) {
        if (word.length() > 4) {
          word = word.substring(0, word.length() - 2);
        }
        result.add(word);
      }
    }
    return result.toArray(new String[0]);
  }

}