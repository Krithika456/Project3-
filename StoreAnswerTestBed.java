package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for storing a list of Answer objects.
 */
public class StoreAnswerTestBed {

    public static void main(String[] args) {
        System.out.println("\n--- Store Answer List Test ---\n");

        testStoreAnswerList();
    }

    /**
     * Creates and stores multiple Answer objects in a list.
     * Validates that the list maintains correct size and that
     * each Answer object preserves its intended content and creator.
     * This test ensures proper in-memory handling of answer data.
     */
    public static void testStoreAnswerList() {
        System.out.println("Running testStoreAnswerList...");

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(1, 101, "Answer A", "userA"));
        answers.add(new Answer(2, 101, "Answer B", "userB"));
        answers.add(new Answer(3, 102, "Answer C", "userC"));

        assert answers.size() == 3 : "Answers list should contain 3 elements";
        assert answers.get(0).getAnswerText().equals("Answer A") : "First answer text mismatch";
        assert answers.get(1).getCreatedBy().equals("userB") : "Second answer creator mismatch";

        System.out.println("Answer stored successfully!!");
    }
}
