package src.login;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

import src.User;
import src.criptografia.certificados.GeradorDeCertificados;
import src.criptografia.chaves.GeradorDeChaves;
import src.criptografia.repositorios.GeradorDeRepositorios;
import src.criptografia.repositorios.RepositorioChaves;

public class UserDataCreator {

    private UserDirectoryCreator userDirectoryCreator;

    public UserDataCreator() {
        this.userDirectoryCreator = new UserDirectoryCreator(); 
    }

    public User createUser(String username, String password, String basePath) {
       
        userDirectoryCreator.createUserDirectory(username, basePath);
        String sanitizedUsername = username.replace(" ", "_");
        String keyRepositoryPath = "./"+basePath + sanitizedUsername+".p12";

        try{
            
            GeradorDeChaves geradorDeChaves;
            GeradorDeCertificados geradorDeCertificados = new GeradorDeCertificados();

            geradorDeChaves = new GeradorDeChaves("EC");

            // Gera um par de chaves para o User
            KeyPair conjuntoChaves256 = geradorDeChaves.gerarParDeChaves(256);

            int hash = username.hashCode();
            int serialNumber = Math.abs(hash) % 1000000;

            // Gerando a estrutura do certificado do usuário
            TBSCertificate tbsCertificateUser = geradorDeCertificados.gerarEstruturaCertificado(conjuntoChaves256.getPublic(),
                    serialNumber,
                    "CN=" + username,
                    "CN=AC-RAIZ",
                    7 /*dias*/);

            // Gerando o valor da assinatura do certificado do usuário
            RepositorioChaves repositorioChaves = new RepositorioChaves("senha".toCharArray(), "AC-RAIZ");
            repositorioChaves.abrir("./src/resources/repositorioAC.p12");
            PrivateKey privateKeyAC = repositorioChaves.pegarChavePrivada();
            DERBitString derBitStringUser = geradorDeCertificados.geraValorDaAssinaturaCertificado(tbsCertificateUser, privateKeyAC);

            // Gerando o certificado do usuário
            DefaultSignatureAlgorithmIdentifierFinder finder = new DefaultSignatureAlgorithmIdentifierFinder();
            X509Certificate certificadoUser = geradorDeCertificados.gerarCertificado(tbsCertificateUser,
                    finder.find("SHA256withECDSA"),
                    derBitStringUser);
            
            GeradorDeRepositorios.gerarPkcs12(conjuntoChaves256.getPrivate(), certificadoUser, keyRepositoryPath, username, password.toCharArray());
            System.out.println("Repositório do usuário gerado com sucesso.");
            
            return new User(username, password, keyRepositoryPath);
        } catch (IOException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }     

        return new User(username, password, keyRepositoryPath);
    }
}
