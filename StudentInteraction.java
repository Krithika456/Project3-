package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import databasePart1.DatabaseHelper;

public class StudentInteraction {
    private static HashMap<String, String> privateFeedback = new HashMap<>();



    public static void sendFeedback(String asker, String feedback) {

        privateFeedback.put(asker, feedback);

    }



    public static String getFeedback(String asker) {

        return privateFeedback.getOrDefault(asker, "No feedback yet.");

    }
    private DatabaseHelper dbHelper;
    private List<Question> questions;
    private List<Answer> answers;

    public StudentInteraction(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    // Allow students to ask a question
    public void askQuestion(String questionText, String createdBy) {
        try {
            if (questionText == null || questionText.trim().isEmpty()) {
                System.out.println("Question cannot be empty.");
                return;
            }
            dbHelper.createQuestion(questionText, createdBy);
            System.out.println("Question submitted successfully by " + createdBy);
        } catch (SQLException e) {
            System.out.println("Error submitting question: " + e.getMessage());
        }
    }

    // Allow students to receive answers for a specific question
    public void viewAnswers(int questionId) {
        try {
            List<String> answers = dbHelper.readAnswersForQuestion(questionId);
            if (answers.isEmpty()) {
                System.out.println("No answers yet for this question.");
            } else {
                System.out.println("Answers:");
                for (String answer : answers) {
                    System.out.println("- " + answer);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving answers: " + e.getMessage());
        }
    }

    // Allow students to submit an answer
    public void submitAnswer(int questionId, String answerText, String createdBy) {
        try {
            if (answerText == null || answerText.trim().isEmpty()) {
                System.out.println("Answer cannot be empty.");
                return;
            }
            dbHelper.createAnswer(questionId, answerText, createdBy);
            System.out.println("Answer submitted successfully by " + createdBy);
        } catch (SQLException e) {
            System.out.println("Error submitting answer: " + e.getMessage());
        }
    }
    // Method to get the number of unread messages
    public int getUnreadMessagesCount(String studentUsername) {
        try {
            return dbHelper.getUnreadMessagesCount(studentUsername);
        } catch (SQLException e) {
            System.out.println("Error retrieving unread messages count: " + e.getMessage());
            return 0;
        }
    }

    // Method to retrieve unread messages
    public List<String> getUnreadMessages(String studentUsername) {
        try {
            return dbHelper.getUnreadMessages(studentUsername);
        } catch (SQLException e) {
            System.out.println("Error retrieving unread messages: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Method to reply to a private message
    public void replyToMessage(int messageId, String replyText) {
        try {
            dbHelper.addMessageReply(messageId, replyText);
        } catch (SQLException e) {
            System.out.println("Error replying to message: " + e.getMessage());
        }
    }

    // To implement this part of the code, I recieved help from:
    // https://docs.oracle.com/javase/8/docs/api/java/util/List.html
 	// https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
 	public void sortAnswers() {
 		answers.sort((a1, a2) -> Boolean.compare(a2.getResolved(), a1.getResolved()));

 	}
}
