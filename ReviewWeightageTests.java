package application;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.*;

import databasePart1.DatabaseHelper;

class ReviewWeightageTests {
    private static DatabaseHelper dbHelper;
    private static final String TEST_STUDENT = "test_student";
    private static final String TEST_REVIEWER1 = "reviewer1";
    private static final String TEST_REVIEWER2 = "reviewer2";

    @BeforeAll
    static void setupClass() throws SQLException {
        dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase();

        // Use clean test database
        dbHelper.getConnection().createStatement().execute("DROP ALL OBJECTS");
        dbHelper.createTables();
    }

    @BeforeEach
    void setupTest() throws SQLException {
        dbHelper.addTrustedReviewer(TEST_STUDENT, TEST_REVIEWER1, 4.5);
        dbHelper.addTrustedReviewer(TEST_STUDENT, TEST_REVIEWER2, 2.0);
    }

    @AfterEach
    void cleanTest() throws SQLException {
        dbHelper.getConnection().createStatement().execute("DELETE FROM TrustedReviewers");
        dbHelper.getConnection().createStatement().execute("DELETE FROM Reviews");
    }

    @Test
    void testValidWeightageInput() throws SQLException {
        dbHelper.updateReviewerWeightage(TEST_STUDENT, TEST_REVIEWER1, 0.0);
        dbHelper.updateReviewerWeightage(TEST_STUDENT, TEST_REVIEWER2, 5.0);

        List<String> reviewers = dbHelper.getTrustedReviewers(TEST_STUDENT);

        assertAll("Weightage boundaries",
            () -> assertTrue(reviewers.get(0).contains("5.0")),
            () -> assertTrue(reviewers.get(1).contains("0.0"))
        );
    }

    @Test
    void testInvalidWeightageInput() {
        assertAll("Invalid weightage handling",
            () -> assertThrows(IllegalArgumentException.class, () ->
                dbHelper.updateReviewerWeightage(TEST_STUDENT, TEST_REVIEWER1, -1.0)),
            () -> assertThrows(IllegalArgumentException.class, () ->
                dbHelper.updateReviewerWeightage(TEST_STUDENT, TEST_REVIEWER1, 5.1))
        );
    }

    @Test
    void testSortReviewsByWeight() throws SQLException {
        dbHelper.addReview(TEST_REVIEWER1, "Good answer", true, 1);
        dbHelper.addReview(TEST_REVIEWER2, "Needs improvement", true, 1);

        StudentQuestionPage page = new StudentQuestionPage(dbHelper, new User(TEST_STUDENT, "pass", List.of("student")));
        List<String> rawReviews = dbHelper.readReviewsForReviewed(1);
        List<StudentQuestionPage.ReviewData> sorted = page.processAndSortReviews(rawReviews);

        assertAll("Review sorting",
            () -> assertEquals(4.5, sorted.get(0).weightage, "Highest weightage first"),
            () -> assertEquals(2.0, sorted.get(1).weightage, "Lower weightage second"),
            () -> assertEquals("Good answer", sorted.get(0).text)
        );
    }

    @Test
    void testUntrustedReviewerWeightage() throws SQLException {
        dbHelper.addReview("unknown_reviewer", "Random comment", true, 1);

        StudentQuestionPage page = new StudentQuestionPage(dbHelper, new User(TEST_STUDENT, "pass", List.of("student")));
        List<String> rawReviews = dbHelper.readReviewsForReviewed(1);
        List<StudentQuestionPage.ReviewData> sorted = page.processAndSortReviews(rawReviews);

        assertEquals(0.0, sorted.get(0).weightage, "Untrusted reviewers should have 0 weightage");
    }
}