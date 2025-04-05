package application;

/**
 * Test class for automated testing of Question features.
 */
public class CreateQuestionTestBed {

    public static void main(String[] args) {
        System.out.println("\n--- HW3 Test Mainline ---\n");

        testCreateQuestion();

    }

    /**
     * Creates a sample Question object and verifies that its core fields
     * (ID, text, creator, parent ID, curated answers list, and resolved status)
     * are correctly initialized. This ensures the Question constructor
     * works as expected for a basic, standalone question.
     */
     	public static void testCreateQuestion() {
    	System.out.println("Running testCreateQuestion...");

        Question question = new Question(1, "What is 2+2?", "Madhuri");

        assert question.getId() == 1 : "ID not set correctly";
        assert question.getQuestionText().equals("What is 2+2?") : "Question text mismatch";
        assert question.getCreatedBy().equals("Madhuri") : "CreatedBy not set correctly";
        assert question.getParentQuestionId() == null : "Parent ID should be null";
        assert question.getCuratedAnswers().isEmpty() : "Curated answers list should be empty";
        assert !question.getResolved() : "Question should not be marked resolved";



        System.out.println("Question created successfully!!");

    }


}
