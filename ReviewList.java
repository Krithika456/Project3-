package application;

import java.util.ArrayList;
import java.util.List;

public class ReviewList {

	//list that holds all reviews
	private List<Review> reviews;

	public ReviewList() {
		this.reviews = new ArrayList<>();
	}

    public void addReview(Review review) { // Method to add a new reviews
        reviews.add(review);
    }

    public List<Review> getReviews() { //Methods to return all reviews
        return reviews;
    }

    public List<Review> getSpecificReviews(int idOfReviewed) { // Methods to get the reivews associated with a question or answer
        List<Review> result = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getIdOfReviewed() == idOfReviewed) {
                result.add(review);
            }
        }
        return result;
    }

}