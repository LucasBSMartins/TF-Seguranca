package src.criptografia.assinatura;

import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.cert.jcajce.JcaCertStore;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class GeradorDeAssinatura {

    private X509Certificate certificado;
    private PrivateKey chavePrivada;
    private CMSSignedDataGenerator geradorAssinaturaCms;

    public GeradorDeAssinatura() {
        this.geradorAssinaturaCms = new CMSSignedDataGenerator();
    }

    public void informaAssinante(X509Certificate certificado,
                                 PrivateKey chavePrivada) {
        this.certificado = certificado;
        this.chavePrivada = chavePrivada;
    }

    public CMSSignedData assinar(String caminhoDocumento) throws CMSException, CertificateEncodingException {

        // Cria uma lista contendo o certificado usado para assinar
        List<X509Certificate> certList = new ArrayList<>();
        certList.add(this.certificado);

        CMSTypedData typedData = this.preparaDadosParaAssinar(caminhoDocumento);

        SignerInfoGenerator sig = this.preparaInformacoesAssinante(this.chavePrivada, this.certificado);

        // Adiciona as informações do assinante e os certificados à estrutura da assinatura CMS
        this.geradorAssinaturaCms.addSignerInfoGenerator(sig);
        this.geradorAssinaturaCms.addCertificates(new JcaCertStore(certList));

        // Gera a assinatura CMS,
        return geradorAssinaturaCms.generate(typedData, true);
    }

    private CMSTypedData preparaDadosParaAssinar(String caminhoDocumento) {
        try(FileInputStream arquivoEntrada = new FileInputStream(caminhoDocumento)) {

            // Lê o documento para uma byte array
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] bytes = new byte[arquivoEntrada.available()];
            while ((nRead = arquivoEntrada.read(bytes, 0, bytes.length)) != -1) {
                buffer.write(bytes, 0, nRead);
            }

            // Retorna os dados no formato CMSTypedData.
            return new CMSProcessableByteArray(bytes);
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    private SignerInfoGenerator preparaInformacoesAssinante(PrivateKey chavePrivada,
                                                                Certificate certificado) {
        try {
            ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withECDSA").build(chavePrivada);

            // Retorna a estrutura com informações do assinante.
            return new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().build())
                    .build(contentSigner, (X509Certificate) certificado);
        } catch (OperatorCreationException | CertificateEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void escreveAssinatura(OutputStream arquivo, CMSSignedData assinatura) {
        try {
            arquivo.write(assinatura.getEncoded());
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

