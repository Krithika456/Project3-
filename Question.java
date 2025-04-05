package application;

import java.util.ArrayList;
import java.util.List;

/**
 * The Question class represents a question in the system.
 * It includes an optional parentQuestionId for the "Re-Ask" feature,
 * allowing a question to reference a previously asked question.
 */
public class Question {

    private int id;
    private String questionText;
    private String createdBy;
    private Integer parentQuestionId;  // New field for linking to a parent question
    private List<Answer> curatedAnswers;
    private boolean resolved;

    /**
     * Constructor for an original question (no parent).
     *
     * @param id           The unique ID of the question.
     * @param questionText The text/content of the question.
     * @param createdBy    The username of the user who created this question.
     */
    public Question(int id, String questionText, String createdBy) {
        this.id = id;
        this.questionText = questionText;
        this.createdBy = createdBy;
        this.parentQuestionId = null;
        this.curatedAnswers = new ArrayList<>();
        this.resolved = false;
    }

    /**
     * Constructor for a question that references a parent (Re-Ask feature).
     *
     * @param id               The unique ID of the question.
     * @param questionText     The text/content of the question.
     * @param createdBy        The username of the user who created this question.
     * @param parentQuestionId The ID of the parent question, if any.
     */
    public Question(int id, String questionText, String createdBy, int parentQuestionId) {
        this.id = id;
        this.questionText = questionText;
        this.createdBy = createdBy;
        this.parentQuestionId = parentQuestionId;
        this.curatedAnswers = new ArrayList<>();
        this.resolved = false;
    }

    // ============================
    // Getters and Setters
    // ============================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean getResolved() {
    	return resolved;
    }

    public void markAsResolved() {
    	this.resolved = true;
    }

    public void markUnresolved() {
    	this.resolved = false;
    }


    /**
     * Gets the parent question ID if this question was re-asked from a previous one.
     *
     * @return the parentQuestionId or null if there is no parent.
     */
    public Integer getParentQuestionId() {
        return parentQuestionId;
    }

    /**
     * Sets the parent question ID, enabling the Re-Ask feature.
     *
     * @param parentQuestionId the ID of the original question, or null if none.
     */
    public void setParentQuestionId(Integer parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }

    // ============================
    // Curated Answers Management
    // ============================

    public List<Answer> getCuratedAnswers() {
        return curatedAnswers;
    }

    public void setCuratedAnswers(List<Answer> curatedAnswers) {
        this.curatedAnswers = curatedAnswers;
    }
}
