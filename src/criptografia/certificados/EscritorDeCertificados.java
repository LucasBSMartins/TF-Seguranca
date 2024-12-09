package src.criptografia.certificados;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EscritorDeCertificados {
    public static void escreveCertificado(String nomeArquivo,
                                          byte[] certificadoCodificado,
                                          String descricao) throws IOException {

        PemObject pemObject = new PemObject(descricao, certificadoCodificado);

        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(nomeArquivo)))) {
            pemWriter.writeObject(pemObject);
        }
    }
}
