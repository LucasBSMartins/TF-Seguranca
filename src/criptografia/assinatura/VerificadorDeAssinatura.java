package src.criptografia.assinatura;

import org.bouncycastle.cms.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.*;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import java.security.cert.X509Certificate;


public class VerificadorDeAssinatura {

    public boolean verificarAssinatura(X509Certificate certificado,
                                        CMSSignedData assinatura) throws OperatorCreationException, CMSException {

        // Gera o verificador de informações de assinatura a partir do certificado do assinante
        SignerInformationVerifier signerInformationVerifier = this.geraVerificadorInformacoesAssinatura(certificado);

        // Pega as informações da assinatura dentro do CMS
        SignerInformation signerInformation = this.pegaInformacoesAssinatura(assinatura);

        // Verifica a integridade da assinatura utilizando o verificador de informações de assinatura
        return signerInformation.verify(signerInformationVerifier);
    }


    private SignerInformationVerifier geraVerificadorInformacoesAssinatura(X509Certificate certificado) throws OperatorCreationException {

        ContentVerifierProvider contentVerifierProvider = new JcaContentVerifierProviderBuilder().setProvider(new BouncyCastleProvider()).build(certificado);

        DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder().setProvider(new BouncyCastleProvider()).build();

        SignatureAlgorithmIdentifierFinder signatureAlgorithmIdentifierFinder = new DefaultSignatureAlgorithmIdentifierFinder();

        CMSSignatureAlgorithmNameGenerator signatureAlgorithmNameGenerator = new DefaultCMSSignatureAlgorithmNameGenerator();

        return new SignerInformationVerifier(signatureAlgorithmNameGenerator,
                                            signatureAlgorithmIdentifierFinder,
                                            contentVerifierProvider,
                                            digestCalculatorProvider);
    }

    private SignerInformation pegaInformacoesAssinatura(CMSSignedData assinatura) {
        // Retorna as informações da primeira assinatura encontrada dentro do CMS
        return  assinatura.getSignerInfos().getSigners().iterator().next();
    }

}
