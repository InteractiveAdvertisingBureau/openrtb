import java.io.{FileInputStream, InputStreamReader}
import java.nio.charset.StandardCharsets
import java.security.spec.X509EncodedKeySpec
import java.security._

import org.apache.commons.codec.binary.Base64

import org.bouncycastle.asn1.ASN1ObjectIdentifier
import org.bouncycastle.asn1.x9.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}

import scala.util.{Failure, Success, Try}


object Main {

  Security.addProvider(new BouncyCastleProvider())

  def main(args: Array[String]): Unit = {

    val message = Seq(
      "2824f5d3-a2dc-49ec-8358-dbe92bd01ec2", // source.tid
      "a:180720yz:1807241326", // ad.pcv
      "newsite.com", // site.domain
      "", // app.bundle
      "", // user.consent
      "vd", // placement
      "192.168.1.1", // device.ip
      "", // device.ipv6
      "", // device.ifa
      "", // device.ua
      "" // ad.video player size?
    ).mkString(":")

    val tryResult = for {
      (privateKey, publicKey) <- loadKeys(args)
      signature <- sign(privateKey, Signature.getInstance("SHA256withECDSA"), message)
      _ <- Try(println(s"publisher signature: ${Base64.encodeBase64String(signature)}"))
      result <- verify(publicKey, Signature.getInstance("SHA256withECDSA"), message, signature)
    } yield result

    tryResult match {
      case Success(true) => println("yay it worked!")
      case Success(false) => println("it didn't work :(")
      case Failure(e) => println(e)
    }
  }

  private def loadKeys(args: Array[String]): Try[(PrivateKey, PublicKey)] = {
    if (args.length < 2) {
      throw new IllegalArgumentException("invalid argument length")
    }
    val privateParser = new PEMParser(new InputStreamReader(new FileInputStream(args(0))))
    val oid = privateParser.readObject match {
      case o: ASN1ObjectIdentifier => o
      case _ => throw new ClassCastException
    }
    val spec = Option(ECNamedCurveTable.getByOID(oid))
    if (spec.isEmpty) throw new RuntimeException("spec not found for named curve")
    val pemPair = privateParser.readObject match {
      case p: PEMKeyPair => p
      case _ => throw new ClassCastException
    }
    val pair = new JcaPEMKeyConverter().setProvider("BC").getKeyPair(pemPair)
    val privateKey = pair.getPrivate
    // note: you can just use:
    // publicKey = pair.getPublic();
    // however, for this example I want to be able to specify an incorrect public key
    // load public key
    val publicParser = new PEMParser(new InputStreamReader(new FileInputStream(args(1))))
    val publicObj = publicParser.readPemObject
    val publicKey = KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(publicObj.getContent))
    Success((privateKey, publicKey))
  }

  private def sign(privateKey: PrivateKey, digest: Signature, message: String): Try[Array[Byte]] = {
    digest.initSign(privateKey)
    digest.update(message.getBytes(StandardCharsets.UTF_8))
    Success(digest.sign)
  }

  private def verify(publicKey: PublicKey, digest: Signature, message: String, signature: Array[Byte]): Try[Boolean] = {
    digest.initVerify(publicKey)
    digest.update(message.getBytes(StandardCharsets.UTF_8))
    Success(digest.verify(signature))
  }
}
