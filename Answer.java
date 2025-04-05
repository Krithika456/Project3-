package application;


public class Answer {
    private String content;

    private String responder;






    public String getContent() {

        return content;

    }



    public String getResponder() {

        return responder;

    }
    private int id;
    private int questionId;
    private String answerText;
    private String createdBy;
    private boolean resolved;

    public Answer(int id, int questionId, String answerText, String createdBy) {
    	this.id = id;
    	this.questionId = questionId;
    	this.answerText = answerText;
    	this.createdBy = createdBy;
    	this.resolved = false;
    }

    // Getters and Setters from answer
    public int getId() {
        return id;
    }

    public void setId(int id) {   // sets the id  of the answer
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {  // sets the id of the questions
        this.questionId = questionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) { // gets the text from answer
        this.answerText = answerText;
    }

    public String getCreatedBy() {  // sets the name of the user who creaded the answers
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {  // gets the name of the user who creaded the answers
        this.createdBy = createdBy;
    }
    public boolean getResolved() {
    	return resolved;
    }

    public void markAsResolved() {
    	this.resolved = true;
    }

    public void markAsUnresolved() {
    	this.resolved = false;
    }
}
