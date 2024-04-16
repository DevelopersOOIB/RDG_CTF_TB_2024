package ru.dvfu.demo.security.jwt;

import java.nio.charset.StandardCharsets;

class JwtSignature {

    private final byte[] key;

    public JwtSignature(String key) {
        this.key = key.getBytes(StandardCharsets.UTF_8);
    }

    public boolean validateSign(String sourceText, String signText) {
        byte[] sourceArray = sourceText.getBytes(StandardCharsets.UTF_8);

        makeRearrangement(sourceArray);
        makeXor(sourceArray);

        String signSource = new String(sourceArray, StandardCharsets.UTF_8);
        return signText.equals(signSource);
    }

    public byte[] makeSign(String text) {
        byte[] sourceArray = text.getBytes(StandardCharsets.UTF_8);

        makeRearrangement(sourceArray);
        makeXor(sourceArray);

        return sourceArray;
    }

    private void makeRearrangement(byte[] sourceBytes) {
        for (int i = 0; i <sourceBytes.length - 2; i += 3 ) {
            byte temp = sourceBytes[i];
            sourceBytes[i] = sourceBytes[i + 2];
            sourceBytes[i + 2] = sourceBytes[i + 1];
            sourceBytes[i + 1] = temp;
        }
    }

    private void makeXor(byte[] sourceBytes) {
        for (int i = 0; i < sourceBytes.length; i++) {
            byte encodeByte = (byte) (sourceBytes[i] ^ key[i % key.length]);
            sourceBytes[i] = encodeByte;
        }
    }
}
