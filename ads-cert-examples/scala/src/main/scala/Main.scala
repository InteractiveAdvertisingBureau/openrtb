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

  private var privateKey: PrivateKey = _
  private var publicKey: PublicKey = _

  Security.addProvider(new BouncyCastleProvider())

  def main(args: Array[String]): Unit = {
    loadKeys(args)
    val message = Stream(
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
    ).map(Option(_).getOrElse("")).toList.mkString(":")
    for {
      signature <- sign(Signature.getInstance("SHA256withECDSA"), message)
    } yield {
      printf("publisher signature: %s\n", Base64.encodeBase64String(signature))
      Try {
        verify(Signature.getInstance("SHA256withECDSA"), message, signature)
      } match {
        case Success(b) => b.getOrElse(false) match {
          case true => println("yay it worked!")
          case false => println("it didn't work :(")
        }
        case Failure(e) => throw e
      }
    }
  }

  private def loadKeys(args: Array[String]): Try[_] = {
    if (args.length < 2) {
      Failure(new IllegalArgumentException("invalid argument length"))
    }
    val privateParser = new PEMParser(new InputStreamReader(new FileInputStream(args(0))))
    val oid = privateParser.readObject.asInstanceOf[ASN1ObjectIdentifier]
    val spec = ECNamedCurveTable.getByOID(oid)
    if (spec == null) Failure(new RuntimeException("spec not found for named curve"))
    val pemPair = privateParser.readObject.asInstanceOf[PEMKeyPair]
    val pair = new JcaPEMKeyConverter().setProvider("BC").getKeyPair(pemPair)
    privateKey = pair.getPrivate
    // note: you can just use:
    // publicKey = pair.getPublic();
    // however, for this example I want to be able to specify an incorrect public key
    // load public key
    val publicParser = new PEMParser(new InputStreamReader(new FileInputStream(args(1))))
    val publicObj = publicParser.readPemObject
    publicKey = KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(publicObj.getContent))
    Success(())
  }

  private def sign(digest: Signature, message: String): Try[Array[Byte]] = {
    digest.initSign(privateKey)
    digest.update(message.getBytes(StandardCharsets.UTF_8))
    Success(digest.sign)
  }

  private def verify(digest: Signature, message: String, signature: Array[Byte]): Try[Boolean] = {
    digest.initVerify(publicKey)
    digest.update(message.getBytes(StandardCharsets.UTF_8))
    Success(digest.verify(signature))
  }
}
