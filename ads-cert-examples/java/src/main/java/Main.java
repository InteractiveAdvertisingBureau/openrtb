import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;

import org.apache.commons.codec.binary.Base64;

public class Main {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        try {
            loadKeys(args);
            String message = String.join(":",
                    Stream.of(
                            "2824f5d3-a2dc-49ec-8358-dbe92bd01ec2", // source.tid
                            "a:180720yz:1807241326",                // ad.pcv
                            "newsite.com",                          // site.domain
                            "",                                     // app.bundle
                            "",                                     // user.consent
                            "vd",                                   // placement
                            "192.168.1.1",                          // device.ip
                            "",                                     // device.ipv6
                            "",                                     // device.ifa
                            "",                                     // device.ua
                            ""                                      // ad.video player size?
                    ).map(v -> Optional.ofNullable(v).orElse("")).collect(Collectors.toList()));
            byte[] signature = sign(Signature.getInstance("SHA256withECDSA"), message);
            System.out.printf("publisher signature: %s\n", Base64.encodeBase64String(signature));
            if (verify(Signature.getInstance("SHA256withECDSA"), message, signature)) {
                System.out.println("yay it worked!");
            } else {
                System.out.println("it didn't work :(");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadKeys(String[] args) throws RuntimeException {
        if (args.length < 2) {
            throw new RuntimeException("invalid argument length");
        }
        try {
            // load private key
            PEMParser privateParser = new PEMParser(new InputStreamReader(new FileInputStream(args[0])));
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) privateParser.readObject();
            X9ECParameters spec = ECNamedCurveTable.getByOID(oid);
            if (spec == null) {
                throw new RuntimeException("spec not found for named curve");
            }
            PEMKeyPair pemPair = (PEMKeyPair) privateParser.readObject();
            KeyPair pair = new JcaPEMKeyConverter().setProvider("BC").getKeyPair(pemPair);
            privateKey = pair.getPrivate();
            // note: you can just use:
            // publicKey = pair.getPublic();
            // however, for this example I want to be able to specify an incorrect public key
            // load public key
            PEMParser publicParser = new PEMParser(new InputStreamReader(new FileInputStream(args[1])));
            PemObject publicObj = publicParser.readPemObject();
            publicKey = KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(publicObj.getContent()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] sign(Signature digest, String message) throws RuntimeException {
        try {
            digest.initSign(privateKey);
            digest.update(message.getBytes("UTF-8"));
            return digest.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean verify(Signature digest, String message, byte[] signature) throws RuntimeException {
        try {
            digest.initVerify(publicKey);
            digest.update(message.getBytes("UTF-8"));
            return digest.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
