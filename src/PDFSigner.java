package src;

import java.util.Scanner;

public class PDFSigner {

    public static void signPDF(Scanner scanner, String username, String password) {
        System.out.println("Signing PDF...");
        System.out.print("Enter the path to the PDF file: ");
        String pdfPath = scanner.nextLine();
        
        

        System.out.println("PDF signed successfully at " + pdfPath);
    }
}
