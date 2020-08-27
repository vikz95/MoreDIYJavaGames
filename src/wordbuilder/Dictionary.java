package wordbuilder;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Dictionary {

    private static final String FILENAME = "enable1_3-15.txt";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @SuppressWarnings("unchecked")
    private ArrayList<String>[] wordList = (ArrayList<String>[]) new ArrayList[26];

    public Dictionary() {
        for (int i = 0; i < wordList.length; i++) {
            wordList[i] = new ArrayList<>();
        }

        try (BufferedReader in = new BufferedReader(new FileReader(new File("src/wordbuilder/" + FILENAME)));) {
            String word = in.readLine();
            while (word != null) {
                char letter = word.charAt(0);
                int list = ALPHABET.indexOf(letter);
                wordList[list].add(word);
                word = in.readLine();
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File " + FILENAME + " was not found.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "File " + FILENAME + " could not be opened.");
        }
    }

    public boolean isAWord(String word) {
        word = word.toUpperCase();
        char letter = word.charAt(0);
        int list = ALPHABET.indexOf(letter);
        return wordList[list].contains(word);
    }

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        System.out.println(dictionary.isAWord("INTERMEDIARIES"));
        System.out.println(dictionary.isAWord("TDKY"));
    }
}
