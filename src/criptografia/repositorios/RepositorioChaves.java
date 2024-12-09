package src.criptografia.repositorios;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class RepositorioChaves {

    private KeyStore repositorio;
    private char[] senha;
    private String alias;

    public RepositorioChaves(char[] senha, String alias) {
        try {
            this.repositorio = KeyStore.getInstance("PKCS12");
            this.senha = senha;
            this.alias = alias;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void abrir(String caminhoRepositorio) throws IOException, CertificateException, NoSuchAlgorithmException {
        this.repositorio.load(new FileInputStream(caminhoRepositorio), this.senha);
    }

    public PrivateKey pegarChavePrivada() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        return (PrivateKey) this.repositorio.getKey(this.alias, this.senha);
    }


    public X509Certificate pegarCertificado() throws KeyStoreException {
        return (X509Certificate) this.repositorio.getCertificate(this.alias);
    }
}
