package main

import (
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
	"crypto/x509"
	"encoding/asn1"
	"encoding/base64"
	"encoding/pem"
	"errors"
	"fmt"
	"hash"
	"io/ioutil"
	"log"
	"math/big"
	"os"
	"strings"
)

func main() {
	privateKey, publicKey, err := loadKeys()
	if err != nil {
		log.Fatal(err)
	}
	msg := strings.Join([]string{
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
		"",                                     // ad.video player size?
	}, ":")
	signature, err := sign(privateKey, sha256.New(), msg)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("publisher signature: %s\n", base64.StdEncoding.EncodeToString(signature))
	verified, err := verify(publicKey, sha256.New(), msg, signature)
	if err != nil || !verified {
		fmt.Println("it didn't work :(")
	} else {
		fmt.Println("yay it worked!")
	}
}

func loadKeys() (privateKey *ecdsa.PrivateKey, publicKey *ecdsa.PublicKey, err error) {
	if len(os.Args) < 3 {
		return nil, nil, errors.New("invalid argument length")
	}
	b, err := ioutil.ReadFile(os.Args[1])
	if err != nil {
		return nil, nil, err
	}
	priv, err := pemToPrivateKey(b)
	if err != nil {
		return nil, nil, err
	}
	privateKey = priv.(*ecdsa.PrivateKey)
	b, err = ioutil.ReadFile(os.Args[2])
	if err != nil {
		return nil, nil, err
	}
	pub, err := pemToPublicKey(b)
	if err != nil {
		return nil, nil, err
	}
	publicKey = pub.(*ecdsa.PublicKey)
	return privateKey, publicKey, err
}

func sign(privateKey *ecdsa.PrivateKey, digest hash.Hash, msg string) ([]byte, error) {
	if _, err := digest.Write([]byte(msg)); err != nil {
		return nil, err
	}
	out, err := privateKey.Sign(rand.Reader, digest.Sum(nil), nil)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func verify(publicKey *ecdsa.PublicKey, digest hash.Hash, msg string, signature []byte) (bool, error) {
	if _, err := digest.Write([]byte(msg)); err != nil {
		return false, err
	}
	ecdsaSig := new(struct{ R, S *big.Int })
	if _, err := asn1.Unmarshal(signature, ecdsaSig); err != nil {
		return false, err
	}
	if !ecdsa.Verify(publicKey, digest.Sum(nil), ecdsaSig.R, ecdsaSig.S) {
		return false, fmt.Errorf("failed ECDSA signature validation")
	}
	return true, nil
}

func pemToPublicKey(raw []byte) (interface{}, error) {
	block, _ := pem.Decode(raw)
	if x509.IsEncryptedPEMBlock(block) {
		decrypted, err := x509.DecryptPEMBlock(block, []byte{})
		if err != nil {
			return nil, errors.New("failed decryption")
		}
		key, err := x509.ParsePKIXPublicKey(decrypted)
		if err != nil {
			return nil, err
		}
		return key, err
	}
	cert, err := x509.ParsePKIXPublicKey(block.Bytes)
	if err != nil {
		return nil, err
	}
	return cert, err
}

func pemToPrivateKey(raw []byte) (interface{}, error) {
	skipped := make([]string, 0)
	var derBlock *pem.Block
	for {
		derBlock, raw = pem.Decode(raw)
		if derBlock == nil {
			if len(skipped) == 0 {
				return nil, errors.New("failed to find any PEM data in key input")
			}
			if len(skipped) == 1 && skipped[0] == "CERTIFICATE" {
				return nil, errors.New("found a certificate rather than a key in the PEM for the private key")
			}
			return nil, fmt.Errorf("failed to find PEM block with type ending in \"PRIVATE KEY\" in key input after skipping PEM blocks of the following types: %v", skipped)
		}
		if derBlock.Type == "PRIVATE KEY" || strings.HasSuffix(derBlock.Type, " PRIVATE KEY") {
			break
		}
		skipped = append(skipped, derBlock.Type)
	}
	if key, err := x509.ParseECPrivateKey(derBlock.Bytes); err == nil {
		return key, nil
	}
	return nil, errors.New("failed to parse private key")
}
