package src;

import src.login.Login;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Creating all the ac data
        CreateACData.createACData();

        try {
            while (true) {
                System.out.println("Please choose an option:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        Login.register(scanner);
                        break;
                    case 2:
                        Login.login(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        File userDirectory = new File("src/resources/users/");

                        if (userDirectory.exists() && userDirectory.isDirectory()) {
                            File[] files = userDirectory.listFiles();
                            if (files != null) {
                                for (File file : files) {
                                    boolean deleted = file.delete();
                                    if (deleted) {
                                        System.out.println("Deleted file: " + file.getName());
                                    } else {
                                        System.out.println("Failed to delete file: " + file.getName());
                                    }
                                }
                            }
                        } else {
                            System.out.println("The specified path is not a directory or does not exist.");
                        }
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } finally {
            scanner.close();
        }
    }
}
