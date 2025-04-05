package application;

import java.util.ArrayList;
import java.util.List;

public class QuestionsList {
    private static ArrayList<Question> recentQuestions = new ArrayList<>();

    public static void addQuestion1(Question q) {
        recentQuestions.add(q);
    }

    public static ArrayList<Question> getRecentQuestions() {
        return recentQuestions;
    }  // lists holds all the questions
    private List<Question> questions;

    public QuestionsList() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {  // Method to add a new question to list
        questions.add(question);
    }

    public List<Question> getQuestions() { // Methods to restore all the questions stored
        return questions;
    }

    public List<Question> searchQuestions(String query) { // method to search for questions
        List<Question> result = new ArrayList<>();
        for (Question q : questions) {
            if (q.getQuestionText().contains(query)) {
                result.add(q);
            }
        }
        return result; // returns the list of matching
    }
}
