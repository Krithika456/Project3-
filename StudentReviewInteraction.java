
package application;

import java.util.List;
import java.util.Scanner;

public class StudentReviewInteraction {

    // Chat-related classes inside this file
    static class Message {
        private String sender;
        private String text;

        public Message(String sender, String text) {
            this.sender = sender;
            this.text = text;
        }

        @Override
		public String toString() {
            return sender + ": " + text;
        }
    }

    static class ChatSession {
        private String student;
        private String reviewer;
        private List<Message> messages = new java.util.ArrayList<>();

        public ChatSession(String student, String reviewer) {
            this.student = student;
            this.reviewer = reviewer;
        }

        public void sendMessage(String sender, String text) {
            messages.add(new Message(sender, text));
        }

        public void showChatHistory() {
            System.out.println("--- Chat between " + student + " and " + reviewer + " ---");
            for (Message m : messages) {
                System.out.println(m);
            }
            System.out.println("-----------------------------------");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ReviewList reviewList = new ReviewList();
        // Example: adding some reviews manually
        reviewList.addReview(new Review(1, "Reviewer1", "Good answer but needs sources.", false, 101));
        reviewList.addReview(new Review(2, "Reviewer2", "Very clear explanation!", false, 101));
        reviewList.addReview(new Review(3, "Reviewer3", "Needs more examples.", false, 102));

        int idOfAnswer = 101; // Assume student found answer with id 101

        List<Review> specificReviews = reviewList.getSpecificReviews(idOfAnswer);

        if (specificReviews.isEmpty()) {
            System.out.println("No reviews found for this answer.");
        } else {
            System.out.println("Reviews for the answer:");
            for (int i = 0; i < specificReviews.size(); i++) {
                System.out.println((i + 1) + ". " + specificReviews.get(i).getReviewerName() + ": " + specificReviews.get(i).getReviewText());
            }

            System.out.print("\nSelect a reviewer to start private chat (enter number, or 0 to exit): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline

            if (choice > 0 && choice <= specificReviews.size()) {
                Review selectedReview = specificReviews.get(choice - 1);
                ChatSession chat = new ChatSession("Student", selectedReview.getReviewerName());

                boolean chatting = true;
                while (chatting) {
                    System.out.print("You: ");
                    String message = scanner.nextLine();
                    chat.sendMessage("Student", message);

                    if (message.equalsIgnoreCase("bye")) {
                        chatting = false;
                    } else {
                        chat.sendMessage(selectedReview.getReviewerName(), "Thanks for reaching out!");
                    }
                }

                chat.showChatHistory();
            }
        }

        scanner.close();
    }
}
