package application;

/**
 * Test class to simulate a feedback box mechanism indicating
 * whether an answer is correct or wrong.
 */
public class FeedbackAnswerTestBed {

    public static void main(String[] args) {
        System.out.println("\n--- Feedback Box Test ---\n");

        testAnswerFeedbackCorrect();
        testAnswerFeedbackWrong();
    }

    /**
     * Simulates a scenario where the user provides the correct answer.
     * Validates that the feedback mechanism returns \"Correct!\" as expected.
     */
    public static void testAnswerFeedbackCorrect() {
        String correctAnswer = "Paris";
        String userAnswer = "Paris";

        String feedback = getFeedback(userAnswer, correctAnswer);
        assert feedback.equals("Correct!") : "Feedback should be 'Correct!'";

        System.out.println("Feedback for correct answer: " + feedback);
    }

    /**
     * Simulates a scenario where the user provides an incorrect answer.
     * Confirms that the feedback mechanism returns \"Wrong!\" appropriately.
     */
    public static void testAnswerFeedbackWrong() {
        String correctAnswer = "Paris";
        String userAnswer = "London";

        String feedback = getFeedback(userAnswer, correctAnswer);
        assert feedback.equals("Wrong!") : "Feedback should be 'Wrong!'";

        System.out.println("Feedback for wrong answer: " + feedback);
    }

    /**
     * Compares user answer with the correct one and returns feedback.
     *
     * @param userAnswer    the answer given by the user
     * @param correctAnswer the actual correct answer
     * @return feedback string indicating if the answer is correct or wrong
     */
    public static String getFeedback(String userAnswer, String correctAnswer) {
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim()) ? "Correct!" : "Wrong!";
    }
}
