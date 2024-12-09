package src.criptografia.chaves;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class EscritorDeChaves {

    public static void escreveChaveEmDisco(Key chave, String nomeDoArquivo, String descricao)
            throws IOException {

        // Cria um objeto PemObject com a descrição e os bytes codificados da chave
        PemObject pemObject = new PemObject(descricao, chave.getEncoded());

        // Escreve o objeto Pem no arquivo especificado
        try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(nomeDoArquivo)))) {
            pemWriter.writeObject(pemObject);
        }
    }
}
