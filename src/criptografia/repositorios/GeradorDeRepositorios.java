package src.criptografia.repositorios;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class GeradorDeRepositorios {

    public static void gerarPkcs12(PrivateKey chavePrivada, X509Certificate certificado,
                                   String caminhoPkcs12, String alias, char[] senha) {
        try {
            // Instanciando o KeyStore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            // Carregando o KeyStore com a senha
            keyStore.load(null, senha);

            // Criando um array de certificados contendo apenas um certificado
            X509Certificate[] certificateChain = new X509Certificate[1];
            certificateChain[0] = certificado;

            // Definindo uma entrada de chave no KeyStore com o alias, a chave privada, a senha e a cadeia de certificados
            keyStore.setKeyEntry(alias, chavePrivada, senha, certificateChain);

            // Armazenando o KeyStore no FileOutputStream, protegido com a mesma senha utilizada para carregar o KeyStore
            FileOutputStream fos = new FileOutputStream(caminhoPkcs12);
            keyStore.store(fos, senha);

        } catch(KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
    }
}
