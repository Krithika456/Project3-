package application;

public class Review {
	private int id;
	private String reviewerName; //username of the reviewer creating the review
	private String reviewText;
	private boolean reviewType; //1 is question and 0 is answer
	private int idOfReviewed; //id of either the question or answer the review is reviewing

	public Review(int id, String reviewerName, String reviewText, boolean reviewType, int idOfReviewed) {
		this.id = id;
		this.reviewerName = reviewerName;
		this.reviewText = reviewText;
		this.reviewType = reviewType;
		this.idOfReviewed = idOfReviewed;
	}

	//getters for all aspects of review

	public int getId() {
		return id;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public String getReviewText() {
		return reviewText;
	}

	public boolean getReviewType() {
		return reviewType;
	}

	public int getIdOfReviewed() {
		return idOfReviewed;
	}

	//alllows editing of review text
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

}