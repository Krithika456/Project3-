package application;

import java.util.List;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {

    public void provideFeedback(String asker, String feedback) {

        StudentInteraction.sendFeedback(asker, feedback);

    }
    private String userName;
    private String password;
    private List<String> role;

    /**
     * Constructor to initialize a new User object with userName, password, and role.
     *
     * @param userName the username of the user
     * @param password the user's password
     * @param role a list of roles assigned to the user
     */
    public User(String userName, String password, List<String> role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    private double weightage = 1.0; // Default weight

    // Getters & Setters
    public double getWeightage() { return weightage; }
    public void setWeightage(double weightage) { this.weightage = weightage; }

    /**
     * Returns the user's username.
     *
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's username.
     *
     * @param userName the new username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the user's password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the list of roles assigned to the user.
     *
     * @return the role list
     */
    public List<String> getRole() {
        return role;
    }

    /**
     * Sets the roles of the user.
     *
     * @param role the new list of roles
     */
    public void setRole(List<String> role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string containing the userName and roles
     */
    @Override
    public String toString() {
        return "User{" +
               "userName='" + userName + '\'' +
               ", role=" + role +
               '}';
    }
}
