package src;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;

import src.criptografia.certificados.GeradorDeCertificados;
import src.criptografia.chaves.GeradorDeChaves;
import src.criptografia.repositorios.GeradorDeRepositorios;

public class CreateACData {

    public static void createACData() {
        try{
            GeradorDeChaves geradorDeChaves;
            geradorDeChaves = new GeradorDeChaves("EC");
            
            //key pair
            KeyPair conjuntoChaves521 = geradorDeChaves.gerarParDeChaves(521);
            System.out.println("Chaves da AC-Raiz gerada com sucesso.");

            //certificate
            GeradorDeCertificados geradorDeCertificados = new GeradorDeCertificados();
            
            // Gerando a estrutura do certificado da AC-raiz
            TBSCertificate tbsCertificateAC = geradorDeCertificados.gerarEstruturaCertificado(conjuntoChaves521.getPublic(),
                1,
                "CN=AC-RAIZ",
                "CN=AC-RAIZ",
                7 /*dias*/);
            
            // Gerando o valor da assinatura do certificado da AC-raiz
            DERBitString derBitStringAC = geradorDeCertificados.geraValorDaAssinaturaCertificado(tbsCertificateAC, conjuntoChaves521.getPrivate());

            // Gerando o certificado da AC-raiz
            DefaultSignatureAlgorithmIdentifierFinder finder = new DefaultSignatureAlgorithmIdentifierFinder();
            X509Certificate certificadoAC = geradorDeCertificados.gerarCertificado(tbsCertificateAC,
                    finder.find("SHA256withECDSA"),
                    derBitStringAC);

            String caminhoPcks12AC = "./src/resources/repositorioAC.p12";
            String aliasAC = "AC-RAIZ";

            // Gera o arquivo PKCS#12 para o repositório da AC
            GeradorDeRepositorios.gerarPkcs12(conjuntoChaves521.getPrivate(), certificadoAC, caminhoPcks12AC, aliasAC, "senha".toCharArray());
            System.out.println("Repositório da AC-Raiz gerado com sucesso.");
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
