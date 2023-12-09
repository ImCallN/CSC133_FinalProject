import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager {
    private static final String HIGH_SCORES_FILE = "highscores.txt";
    private List<Integer> highScores;
    private int maxScoresToKeep;

    public HighScoreManager(int maxScoresToKeep) {
        this.highScores = new ArrayList<>();
        this.maxScoresToKeep = maxScoresToKeep;
        loadHighScoresFromFile();
    }

    public void addScore(int score) {
        highScores.add(score);
        Collections.sort(highScores, Collections.reverseOrder());
        trimScores();
        saveHighScoresToFile();
    }

    private void trimScores() {
        if (highScores.size() > maxScoresToKeep) {
            highScores = highScores.subList(0, maxScoresToKeep);
        }
    }

    public List<Integer> getHighScores() {
        return Collections.unmodifiableList(highScores);
    }

    private void loadHighScoresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int score = Integer.parseInt(line);
                highScores.add(score);
            }
            Collections.sort(highScores, Collections.reverseOrder());
            trimScores();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
        }
    }

    private void saveHighScoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (int score : highScores) {
                writer.write(Integer.toString(score));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    public void displayHighScores() {
        System.out.println("High Scores:");
        int rank = 1;
        for (int score : highScores) {
            System.out.println(rank + ". " + score);
            rank++;
        }
    }

    // Example usage
    public static void main(String[] args) {
        HighScoreManager manager = new HighScoreManager(5); // Keep top 5 high scores
        manager.addScore(100);
        manager.addScore(75);
        manager.addScore(50);
        manager.addScore(25);
        manager.addScore(10);
        manager.addScore(5);

        manager.displayHighScores();
    }
}