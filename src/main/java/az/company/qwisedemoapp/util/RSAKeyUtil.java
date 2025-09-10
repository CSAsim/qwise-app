package az.company.qwisedemoapp.util;

import az.company.qwisedemoapp.exception.NotFoundException;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAKeyUtil {

    private RSAKeyUtil() {}

    public static PrivateKey getPrivateKey(String filename) throws Exception {
        try (InputStream is = RSAKeyUtil.class.getClassLoader().getResourceAsStream(filename)) {
            if(is == null) {
                throw new NotFoundException("Private key not found in resource: " + filename);
            }
            String key = new String(is.readAllBytes())
                    .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }

    public static PublicKey getPublicKey(String filename) throws Exception {
        try (InputStream is = RSAKeyUtil.class.getClassLoader().getResourceAsStream(filename)) {
            if(is == null) {
                throw new NotFoundException("Public key not found in resource: " + filename);
            }
            String key = new String(is.readAllBytes())
                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        }
    }
}
