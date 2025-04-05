package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.DatabaseHelper;

/**
 * JUnit test class for verifying user role management and database interactions.
 * Tests the functionality of requesting reviewer roles and instructor review of questions and answers.
 */
class ReviewerRequestAndApprovalTests {

	/**
	 * Default constructor for the DanielTests class.
	 * Initializes necessary resources for testing.
	 */
	public ReviewerRequestAndApprovalTests() {
	    // constructor code
	}

	/** The database helper instance for interacting with the database. */
	private DatabaseHelper databaseHelper;
	/** The admin user used in the tests. */
	private User adminUser;
	/** The instructor user used in the tests. */
	private User instructorUser;
	/** A student user for role testing. */
	private User studentUser;
	/** Another student user for role testing. */
	private User studentUser2;

	/**
     * Initializes the test environment by setting up a connection to the database,
     * registering users, and creating sample roles.
     *
     * @throws SQLException If there is an issue connecting to the database.
     */
	@BeforeEach
	public void setUp() throws SQLException {
		databaseHelper = new DatabaseHelper(); // Ensure this is properly initialized
		databaseHelper.connectToDatabase();

		List<String> instructorRoles = new ArrayList<>();
		List<String> studentRoles = new ArrayList<>();
		List<String> adminRoles = new ArrayList<>();



		adminRoles.add("admin");
		instructorRoles.add("instructor");
		studentRoles.add("student");

		instructorUser = new User("Instructor2", "Password123!", instructorRoles);
		studentUser = new User("User2", "Password123!", studentRoles);
		adminUser = new User("Admin2", "Password123!", adminRoles);
		studentUser2 = new User("Student2", "Password123!", studentRoles);

		databaseHelper.register(adminUser);
		databaseHelper.register(instructorUser);
		databaseHelper.register(studentUser);
		databaseHelper.register(studentUser2);

	}

	/**
     * Tests the ability of a student to request the reviewer role.
     * Ensures that duplicate requests are not allowed.
     */
	@Test
    public void testStudentRequestReviewerRole() {
        assertEquals("Success: Reviewer Request has been sent!", databaseHelper.addReviewerRequest(studentUser.getUserName()));
        assertEquals("Success: Reviewer Request has been sent!", databaseHelper.addReviewerRequest(studentUser2.getUserName()));
        assertEquals("Error: User has already sent a request.", databaseHelper.addReviewerRequest(studentUser.getUserName()));
    }

	/**
     * Tests the instructor's ability to review a student's submitted questions and answers.
     * Also verifies the ability to assign the reviewer role.
     *
     * @throws SQLException If there is an issue interacting with the database.
     */
	@Test
	public void testInstructorReview() throws SQLException {
		String questionText = "What is life?";
		String questionText2 = "Who am I?";
		String questionText3 = "Last question?";
		String answerText = "test";
		String answerText2 = "Daniel";
		String answerText3 = "Nope";

		databaseHelper.createQuestion(questionText, studentUser2.getUserName());
		databaseHelper.createQuestion(questionText2, studentUser.getUserName());
		databaseHelper.createQuestion(questionText3, studentUser2.getUserName());
		int questionId = databaseHelper.getQuestionIdByText(questionText);
		int questionId2 = databaseHelper.getQuestionIdByText(questionText2);
		int questionId3 = databaseHelper.getQuestionIdByText(questionText3);
		databaseHelper.createAnswer(questionId, answerText, studentUser.getUserName());
		databaseHelper.createAnswer(questionId2, answerText2, studentUser2.getUserName());
		databaseHelper.createAnswer(questionId3, answerText3, studentUser2.getUserName());

		List<String> studentExpectedQ = databaseHelper.readUserQuestions(studentUser.getUserName());
		List<String> student2ExpectedQ = databaseHelper.readUserQuestions(studentUser2.getUserName());
		List<String> studentExpectedA = databaseHelper.getUserAnswers(studentUser.getUserName());
		List<String> student2ExpectedA = databaseHelper.getUserAnswers(studentUser2.getUserName());
		//studentExpectedA.clear();

		// Student has 1 question asked
		System.out.println(studentExpectedQ);
		assertEquals(1, studentExpectedQ.size());
		// Student 2 has 2 questions asked
		System.out.println(student2ExpectedQ);
		assertEquals(2, student2ExpectedQ.size());

		// Student has 1 answer
		System.out.println(studentExpectedA);
		assertEquals(1, studentExpectedA.size());
		// Student 2 has 2 answers
		assertEquals(2, student2ExpectedA.size());

		// Able to added reviewer role to users, but cannot add it twice.
		assertTrue(databaseHelper.addUserRoles(studentUser.getUserName(), "reviewer"));
		assertTrue(databaseHelper.addUserRoles(studentUser2.getUserName(), "reviewer"));
		assertFalse(databaseHelper.addUserRoles(studentUser2.getUserName(), "reviewer"));
	}

	/**
     * Cleans up the database by deleting users and questions after each test.
     * Ensures that test data does not persist between test runs.
     *
     * @throws SQLException If there is an issue interacting with the database.
     */
	@AfterEach
	public void tearDown() throws SQLException {
		databaseHelper.deleteUser(adminUser, instructorUser.getUserName());
		databaseHelper.deleteUser(adminUser, adminUser.getUserName());
		databaseHelper.deleteUser(adminUser, studentUser.getUserName());
		databaseHelper.deleteUser(adminUser, studentUser2.getUserName());
		databaseHelper.removeReviewerRequest(studentUser.getUserName());
		databaseHelper.removeReviewerRequest(studentUser2.getUserName());
		int q1 = databaseHelper.getQuestionIdByText("What is life?");
		int q2 = databaseHelper.getQuestionIdByText("Who am I?");
		int q3 = databaseHelper.getQuestionIdByText("Last question?");
		int a1 = databaseHelper.getIdOfAnswer("test");
		int a2 = databaseHelper.getIdOfAnswer("Daniel");
		int a3 = databaseHelper.getIdOfAnswer("Nope");
		databaseHelper.deleteAnswer(a1);
		databaseHelper.deleteAnswer(a2);
		databaseHelper.deleteAnswer(a3);
		databaseHelper.deleteQuestion(q1);
		databaseHelper.deleteQuestion(q2);
		databaseHelper.deleteQuestion(q3);

	}

}
