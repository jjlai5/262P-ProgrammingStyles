import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

public class CalculateWord {
    public static void main(String[] args) {
        // Ensure the text file is provided as a command-line argument.
        if (args.length < 1) {
            System.out.println("Usage: java WordFrequency <text_file>");
            return;
        }

        String textFile = args[0];
        String stopWordsFile = "../stop_words.txt";
        String text = "";

        // Read the entire text file into a String.
        try {
            text = new String(Files.readAllBytes(Paths.get(textFile)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Convert to lowercase.
        text = text.toLowerCase();

        // Use a regex to tokenize words (only words with 2+ letters).
        List<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-z]{2,}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            words.add(matcher.group());
        }

        // Read the stop words from stopWordsFile.
        Set<String> stopWords = new HashSet<>();
        try {
            String stopContent = new String(Files.readAllBytes(Paths.get(stopWordsFile)));
            // Assuming stop words are separated by commas.
            String[] stops = stopContent.split(",");
            for (String stop : stops) {
                stopWords.add(stop.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading stop words file: " + e.getMessage());
            return;
        }

        // Build a frequency map for words that are not stopwords.
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            if (!stopWords.contains(word)) {
                frequency.put(word, frequency.getOrDefault(word, 0) + 1);
            }
        }

        // Create a list of map entries and sort it by frequency (in descending order).
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(frequency.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Print the top 25 words.
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (count >= 25) break;
            System.out.println(entry.getKey() + " - " + entry.getValue());
            count++;
        }
    }
}