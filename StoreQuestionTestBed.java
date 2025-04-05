package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for storing a list of Question objects.
 */
public class StoreQuestionTestBed {

    public static void main(String[] args) {
        System.out.println("\n--- Store Question List Test ---\n");

        testStoreQuestionList();
    }

    /**
     * Creates and stores multiple Question objects in a list.
     * Verifies that the list size is correct and that individual
     * questions retain their expected properties, such as text
     * and creator. This ensures reliable storage of Question objects
     * in memory.
     */
    public static void testStoreQuestionList() {
        System.out.println("Running testStoreQuestionList...");

        List<Question> questions = new ArrayList<>();
        questions.add(new Question(1, "What is 5+5?", "user1"));
        questions.add(new Question(2, "Explain gravity.", "user2"));
        questions.add(new Question(3, "What is OOP?", "user3"));

        assert questions.size() == 3 : "Questions list should contain 3 elements";
        assert questions.get(0).getQuestionText().equals("What is 5+5?") : "First question mismatch";
        assert questions.get(1).getCreatedBy().equals("user2") : "Second question creator mismatch";

        System.out.println("Question successfully stored!!");
    }
}
