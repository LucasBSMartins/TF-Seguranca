package src;

public class User {
    private String username;
    private String password;
    private String keyRepositoryPath; // Variable for the repository path

    // Constructor
    public User(String username, String password, String keyRepositoryPath) {
        this.username = username;
        this.password = password;
        this.keyRepositoryPath = keyRepositoryPath; // Initialize the repository path
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyRepositoryPath() {
        return keyRepositoryPath; // Return the repository path
    }

    public void setKeyRepositoryPath(String keyRepositoryPath) {
        this.keyRepositoryPath = keyRepositoryPath; // Set the repository path
    }

    // Check if the current user's credentials match the given ones
    public boolean validateCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    // Display user information (for testing purposes)
    public void displayUserInfo() {
        System.out.println("Username: " + username);
        System.out.println("Key Repository Path: " + keyRepositoryPath); // Display the repository path
    }
}
