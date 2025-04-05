package application;

/**
 * Test class for automated testing of Answer features.
 */
public class CreateAnswerTestBed {

    public static void main(String[] args) {
        System.out.println("\n--- Answer Test Mainline ---\n");

        testCreateAnswer();
    }

    /**
     * Creates a sample Answer object and verifies that all of its fields
     * (ID, question ID, answer text, created by, and resolved status)
     * are initialized correctly. This test ensures that the Answer
     * constructor behaves as expected.
     */
    public static void testCreateAnswer() {
        System.out.println("Running testCreateAnswer...");

        Answer answer = new Answer(101, 10, "The capital of France is Paris", "Madhuri");

        assert answer.getId() == 101 : "ID not set correctly";
        assert answer.getQuestionId() == 10 : "Question ID not set correctly";
        assert answer.getAnswerText().equals("The capital of France is Paris") : "Answer text mismatch";
        assert answer.getCreatedBy().equals("Madhuri") : "CreatedBy not set correctly";
        assert !answer.getResolved() : "Answer should not be marked resolved initially";

        System.out.println("Answer created successfully!!");
    }
}
