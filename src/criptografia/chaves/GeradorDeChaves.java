package src.criptografia.chaves;

import java.security.*;

public class GeradorDeChaves {

    private String algoritmo;
    private KeyPairGenerator generator;

    public GeradorDeChaves(String algoritmo)    throws NoSuchAlgorithmException {
        this.algoritmo = algoritmo;
        this.generator = KeyPairGenerator.getInstance(this.algoritmo);
    }

    public KeyPair gerarParDeChaves(int tamanhoDaChave) {
        this.generator.initialize(tamanhoDaChave);
        return this.generator.generateKeyPair();
    }
}
