package src;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;

import src.criptografia.repositorios.RepositorioChaves;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Calendar;

public class PDFSigner {

    public static void signPDF(String pdfPath, String password, String username) {
        System.out.println("Signing PDF...");
        
        String sanitizedUsername = username.replace(" ", "_");
        String keystorePath = "./src/resources/users/" + sanitizedUsername + ".p12";

        try {
            signDocument(pdfPath, keystorePath, password, username);
            System.out.println("PDF signed successfully at " + pdfPath);
        } catch (Exception e) {
            System.err.println("Error signing the PDF: " + e.getMessage());
        }
    }

    private static void signDocument(String pdfPath, String keystorePath, String password, String username) throws Exception {
        File pdfFile = new File(pdfPath);

        // Load keystore
        RepositorioChaves repositorioChaves = new RepositorioChaves(password.toCharArray(), username);
        repositorioChaves.abrir(keystorePath);

        // Get private key and certificate from keystore
        PrivateKey privateKey = repositorioChaves.pegarChavePrivada();
        X509Certificate certificate = repositorioChaves.pegarCertificado();

        // Load the PDF document using FileInputStream
        PDDocument document = Loader.loadPDF(pdfFile);

        PDPage page = document.getPage(0);  // Get the first page (or specific page) for the signature
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

        PDFont pdfFont=  new PDType1Font(FontName.HELVETICA_BOLD);

        // Add text to indicate where the signature is
        contentStream.beginText();
        contentStream.setFont(pdfFont, 12);
        contentStream.newLineAtOffset(100, 500);  // Adjust position based on where you want the signature text
        contentStream.showText("Signed By: " + certificate.getSubjectDN());
        contentStream.endText();
        contentStream.close();
    
        // 2. Create the signature dictionary
        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);
        signature.setName(username);
        signature.setLocation("Florianópolis, SC");
        signature.setReason("Testing");

        // Set the signing date
        signature.setSignDate(Calendar.getInstance());

       // Create SignatureOptions
        SignatureOptions signatureOptions = new SignatureOptions();
        signatureOptions.setPreferredSignatureSize(SignatureOptions.DEFAULT_SIGNATURE_SIZE * 2);

        // Create the signature interface for local signing
        SignatureInterface signatureInterface = new SignatureInterface() {
            @Override
            public byte[] sign(InputStream content) throws GeneralSecurityException {
                try {
                    byte[] contentBytes = content.readAllBytes();
                    Signature sig = Signature.getInstance("SHA256withRSA");
                    sig.initSign(privateKey);
                    sig.update(contentBytes);
                    return sig.sign();
                } catch (GeneralSecurityException e) {
                    // Directly throw GeneralSecurityException
                    throw e;
                } catch (Exception e) {
                    // Catch other exceptions and throw GeneralSecurityException
                    throw new GeneralSecurityException("Unexpected error signing the document: " + e.getMessage(), e);
                }
            }
        };

        // Register signature dictionary and sign interface
        document.addSignature(signature, signatureInterface, signatureOptions);

        // Write the signed document to a new file
        try (FileOutputStream fos = new FileOutputStream(pdfFile.getParent() + "/signed_" + pdfFile.getName())) {
            document.saveIncremental(fos);
        }

        document.close();
    }

    
}
