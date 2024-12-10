package src.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//import PDFSigner;
import src.User;

public class Login {
    // A map to store users with username as the key
    private static Map<String, User> users = new HashMap<>();

    // Simulate user login
    public static void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.validateCredentials(username, password)) {
            System.out.println("Login successful!");

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Sign a file");
                System.out.println("2. Log Out");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        //PDFSigner.signPDF(pathRepository,,username, password); 
                        break;
                    case 2:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }


    // Simulate user registration
    public static void register(Scanner scanner) {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        // Assuming userDataCreator is an instance of UserDataCreator
        UserDataCreator userDataCreator = new UserDataCreator();

        // Registration logic
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
        } else {
            // Generate user data and create a new user
            User newUser = userDataCreator.createUser(username, password, "src/resources/users/");
            users.put(username, newUser);
            System.out.println("Registration successful!");
        }
    }
}
