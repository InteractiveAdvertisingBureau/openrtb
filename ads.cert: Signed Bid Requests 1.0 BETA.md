![](https://drive.google.com/uc?id=1MStOYYaZDqrvuOwlmecX0iayL0Jt_eAN)
# Ads.cert v1.0: Signed Bid Requests

## Updated BETA DRAFT

**November 2018**



**Executive Summary**

“Ads.cert: Signed Bid Requests” is part of the OpenRTB 3.0 updates of 2018. This document is a revised Beta version, enhanced from the beta version initially published. Feedback or questions on how to participate in beta implementation can be sent to openmedia@iabtechlab.com or contributed through the IAB Tech Lab OpenRTB working group.

The beta period will allow for continued refinement of the ads.cert specification through implementations. The specification is being left in beta pending real-world implementations.  As one of many components to fight fraud, it is important that the ads.cert: signed bid requests specification and expected business practices are clear. 

A major component of OpenRTB 3.0 is the shift to an authenticated supply chain. This move to standardize cryptographically signed bid requests is the next step needed in OpenRTB to ensure security and trust in the supply path, to be used in conjunction with ads.txt.

At a high level, the principle of signed bid requests is that Publishers (ie publisher software) should add a signature at the origination of the real time bidding transaction. This provides a traceable path to verify critical data of the inventory such as domain, IP, format, etc. 

Publishers will benefit from this anti-fraud measure in knowing that their inventory is securely passed for sale.  Advertisers and buyers will benefit from this by reviewing the supply authentication to gain confidence in where the inventory is coming from in the real time bidding transaction.

Signed bid requests complements the ads.txt protocol. Ads.txt and the data within it should be used to validate who the authorized sellers are for a source of inventory.  Publisher signatures allow a buyer to validate some aspects of the bid request and know that it’s trusted from the publisher and key elements of the bid request are unmodified. Together, these technologies are a powerful combination in fighting fraud to allow buyers to check for authenticity and authorization of the sales channel.

Ads.txt and ads.cert are resources hosted on the web. App developers need a way to point to a URL where they host these files. Future app support for ads.cert can similarly follow app support for ads.txt. More information on mobile app support is available at iabtechlab.com/ads-txt.

Full adoption of OpenRTB 3.0 and AdCOM 1.0 is expected to arrive on production implementations within early 2019. Beta implementations of ads.cert: signed bid requests are also expected within this time frame. After further beta implementation and signs of industry adoption, the OpenRTB working group will take the appropriate steps to finalize the specification for full industry adoption. 

**About IAB Tech Lab**

The IAB Technology Laboratory (Tech Lab) is a non-profit research and development consortium that produces and provides standards, software, and services to drive growth of an effective and sustainable global digital media ecosystem.  Comprised of digital publishers and ad technology firms as well as marketers, agencies, and other companies with interests in the interactive marketing arena, IAB Tech Lab aims to enable brand and media growth via a transparent, safe, effective supply chain, simpler and more consistent measurement, and better advertising experiences for consumers, with a focus on mobile and TV/digital video channel enablement.  The IAB Tech Lab portfolio includes the DigiTrust real-time standardized identity service designed to improve the digital experience for consumers, publishers, advertisers, and third-party platforms.  Board members include AppNexus, ExtremeReach, Google, GroupM, Hearst Digital Media, Integral Ad Science, Index Exchange, LinkedIn, MediaMath, Microsoft, Moat, Pandora, PubMatic, Quantcast, Telaria, The Trade Desk, and Yahoo! Japan. Established in 2014, the IAB Tech Lab is headquartered in New York City with an office in San Francisco and representation in Seattle and London.

Learn more about IAB Tech Lab here: [www.iabtechlab.com](https://www.iabtechlab.com) 

**Major Contributing Authors**

Neal Richter, Rakuten Marketing; Curt Larson, Sharethrough; Sam Tingleff, IAB Tech Lab; Curtis Light, Google


**IAB Tech Lab Contact**

Jennifer Derke, Director of Product, Programmatic & Data, IAB Tech Lab

[openrtb@iabtechlab.com](mailto:openrtb@iabtechlab.com)

**IAB Tech Lab OpenRTB Working Group Members**

[https://iabtechlab.com/working-groups/openrtb-working-group/](https://iabtechlab.com/working-groups/openrtb-working-group/) 

## TABLE OF CONTENTS

[INTRODUCTION](#intro)  
[1.1 APPROACH](#approach)  
[1.2 LANGUAGE AND TERMINOLOGY](#language)  
[2 SUPPORTED USE CASES](#supported)  
[3 MESSAGE SIGNING BUSINESS LOGIC](#logic)  
[3.1 RULES OF SIGNATURES](#rules)  
[3.2 SIGNATURE BLOCK](#block)  
[3.2.1 SIGNATURE FIELDS](#digestfields)  
[3.3 MESSAGE SERIALIZATION](#serialization)  
[3.4 SUGGESTED WORKFLOW](#suggestedworkflow)     
[3.5 SECURING THE SIGNING SERVICE](#securing)    
[4 KEYS AND SIGNATURES](#keysandsignatures)  
[4.1 PUBLIC KEY DISTRIBUTION](#publickeydistribution)  
[4.2 ADS-CERT.TXT FILE CONTENT](#filecontent)    
[4.3 PRIVATE KEY FILE](#privatekeyfile)    
[4.4 KEY CERTIFICATE EXPIRATION](#keycertificateexpiration)  
[4.5 KEY GENERATION](#keygeneration)    
[5 IMPLEMENTATION RECOMMENDATIONS](#implementationrecommendations)  
[6 LIMITATIONS AND ABUSE VECTORS](#limitations)   
[7 REFERENCES](#references)  


# INTRODUCTION <a name="intro"></a>

For brevity, we'll assume readers are already familiar with the problem of fraud in ad tech and its vast scale [1][2][3].  Fraud can come in various forms; here we are concentrating on the form wherein ad inventory is being offered to buyers with a false label during the real-time bidding process.  Typically the domain of the webpage (or the bundleID of the mobile app) has been falsified to look like a site that is more valuable than the actual impression available.  A variety of other fields are subject to falsification such as the IP address (to make the inventory appear to be in a more desirable geography), the TID (to make it appear to be a unique impression), the device ID (to correspond to IDs known to have dense/high bids in the past), etc.

## 1.1 APPROACH <a name="approach"></a>

The approach taken here is inspired by the "Domain Keys Identified Mail" or DKIM standard [17].  That standard defines a mechanism where the sending domain of an email system can cryptographically sign an outbound email message with a private key.  Final recipient and intermediary servers can use the domain’s public key to then validate the message.  This allows several features and effects:

* No attempt is made to include encryption of the message as part of the mechanism, instead a digital signature of a cleartext message is used;

* The signed aspects of the message can be proven (within the bounds of the signature's security) to be from the declared sending domain by the recipient or intermediary servers;

* Any alterations of the signed elements can be detected by the recipient or intermediary servers;

* There is no dependency on public and private key pairs being issued by well-known, trusted certificate authorities;

* There is no dependency on the deployment of any new protocols or services for public key distribution or revocation;


## 1.2 LANGUAGE AND TERMINOLOGY <a name="language"></a>

 Requirements Notation:

 The keywords "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this document are to be interpreted as described in [RFC2119].

"Authentic Inventory" denotes that the inventory as declared in the OpenRTB or other RTB protocol's bid request can be validated as coming from that source.

A "Request Originator" is the root originating system of the ad inventory.

An "Intermediary" is a system that is involved as a third party between a seller end-point and one or more buyer end-points during the bidding process of the transaction.

A "Signer" is the agent system that signs a message.

A "Request Buyer" is any system that may make an offer in response to the bid request and may optionally authenticate it.

# 2 SUPPORTED USE CASES <a name="supported"></a>

The primary incremental improvement between the ads.txt protected environment of today versus including Signed Bid Requests is the increased assurance to the publisher that inventory isn’t being offered on exchanges via IDs contained within the ads.txt file but without the publisher’s knowledge.  This companion to ads.txt helps ensure that supply chain intermediaries cannot easily outright manufacture bid requests, distort/reformat bid requests, or replay bid requests in order to inflate volumes.

Not every misrepresentation use case is defended against in the current version of Signed Bid Requests.  As it evolves and broadens, this spec should be fairly effective in addressing the following types of supply chain fraud, all of which are currently possible for intermediaries to introduce within the supply chain:

* App/Domain fraud - making the impression appear to be from a more valuable domain/app

* IP fraud - making the impression appear to be from a more valuable IP subnet (for example, not from a data center or a different country)

* Device ID fraud - making the impression appear to come from a more valuable user

* Consent fraud - granting GDPR-style consent to entities that didn't originally have it, or claiming it is not subject to such regulation

* Basic ad format fraud - for example making a display request look like a video request

Although not an exhaustive list, the following types of fraud would not be addressed due to limitations of the mechanism or avoidance of scope-creep in the initial version:

* More subtle format fraud, such as changing the size of a display ad unit

* Misdirection of impressions away from the publisher's offered placement and onto another location

* Non-human traffic/IVT

* Improper double-sale of an impression opportunity to multiple bidders


Future revisions will broaden the scope of use cases addressed.

# 3 MESSAGE SIGNING BUSINESS LOGIC <a name="logic"></a>

## 3.1 RULES OF SIGNATURES <a name="rules"></a>

There are a few rules for using digital signatures for this purpose.  Below we use the term message to mean an OpenRTB bid request.

1) The originating publisher ("Request Originator") of a message must be the entity that signs the message or delegates this authority to a singular primary system.

2) The message needs to have something unique (a random message identifier) to the message to prevent reuse of the message and signature by a downstream system (TID and/or timestamp are used for this purpose).  A timestamp is also included in the signed elements to limit the lookback duration that needs to be considered when de-duplicating requests with a given TID.

3) Any downstream consumer of the message that wants to authenticate it needs access to the fields from the message that were signed,  the signature (ds) and the public key of the source of the message.

4) No intermediary/downstream system can make changes in the signed fields 

## 3.2 SIGNATURE BLOCK <a name="block"></a>

We are beginning with a solution for 3.0 to address the key problem in a simple way.  We will allow a single signature generated at the ‘root system level’.  This root system could be a publisher’s CMS, their main ad server or the controlling header bidder wrapper.   A small set of essential fields will be signed.  While there are certain use cases for additional or more comprehensive signatures, we believe this balances the security versus complexity concerns in an appropriate way for an initial release.  The design also allows for the addition of new signatures and types of signatures in the future, such as exchange signatures, arrays of signatures, etc.

We have added four new fields in OpenRTB’s source object:
* Ds - digital signature - contains the actual signature 
* Cert -  filename of the certificate used 
* Dsmap - the fields that were included in the signature, in order
* Digest - the digest used for signature (generally only used in debug mode)


### 3.2.1 SIGNATURE DIGEST FIELDS <a name="digestfields"></a>

<table>
  <tr>
    <td>Spec</td>
    <td>Object</td>
    <td>Field/Param Name</td>
    <td>Example Value</td>
    <td>Comment</td>
  </tr>
  <tr>
    <td>OpenRTB</td>
    <td>Source</td>
    <td>tid</td>
    <td>ABC7E92FBD6A</td>
    <td></td>
  </tr>
  <tr>
    <td>OpenRTB</td>
    <td>Source</td>
    <td>ts</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>OpenRTB</td>
    <td>Source</td>
    <td>cert</td>
    <td>ads-cert.1.txt</td>
    <td>Name of the certificate file</td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>Site</td>
    <td>domain</td>
    <td>newsite.com</td>
    <td></td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>App</td>
    <td>bundle</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>User</td>
    <td>consent</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>-</td>
    <td>ft</td>
    <td>vd</td>
    <td>String consisting of one or more {v,d,a} for video / display / audio sub-objects of the placement present </td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ip</td>
    <td>192.168.1.1</td>
    <td>Note: if truncations/anonymization will happen, signer must do that at the time of signature or omit component</td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ipv6</td>
    <td></td>
    <td>Note: if truncations/anonymization will happen, signer must do that at the time of signature or omit component</td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ifa</td>
    <td></td>
    <td>Note: if truncations/anonymization will happen, signer must do that at the time of signature or omit component</td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ua</td>
    <td></td>
    <td>Note: if truncations/anonymization will happen, signer must do that at the time of signature or omit component</td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>VideoPlacement</td>
    <td>w</td>
    <td>480 </td>
    <td></td>
  </tr>
  <tr>
    <td>AdCOM</td>
    <td>VideoPlacement</td>
    <td>h</td>
    <td>360</td>
    <td></td>
  </tr>
</table>


* Note: presently in-app signatures are not fully supported due to the lack of a reliable method to retrieve the public key since there is no known domain.  The issue is similar for ads.txt, and a solution is under discussion in that context. App bundle and ifa are included in this spec with the goal that mobile apps can be supported.  Regardless of the availability of that solution, the app-specific fields can still be included in the signature.*


If a value is not available, null, zero length, or inapplicable for any of these fields an empty string is used or the element can be excluded from the signature and the map.  The publisher signature field should be base64 encoded before placing within the JSON object. 

## 3.3 MESSAGE SERIALIZATION <a name="serialization"></a>

Each message to be signed must be canonically generated and serialized. Each message is a non-empty set of elements, in URL query string format.  The key names should be alphabetized to avoid ambiguity in string serialization. Note that missing values should NOT be included as a variable.

Example: domain=newsite.com&ft=d&tid=ABC7E92FBD6A

Note that the digest is used to generate the signature, but the digest itself will generally only be included in in the bid request when in debug mode.  However, the dsmap field should always be present to allow the signature inspector to validate the signature.  The dsmap format is the same as the digest format, but excludes the values.

Example: domain=&ft=&tid=

This allows the signature validator to substitute the actual values elsewhere in the bid request for the values and to then validate the signature.

## 3.4 SUGGESTED WORKFLOW <a name="suggestedworkflow"></a>
The following workflow illustrates the expected use case in today’s RTB-centric workflow.  Other workflows may be innovated or apply in other contexts.

![](https://drive.google.com/uc?id=1CY30AKRpiY4ys7Fqme2HVFcGwayTj6_T)


## 3.5 SECURING THE SIGNING SERVICE <a name="securing"></a>

For bid request signatures to be a trustworthy and reliable signal for bid request authenticity, the signing solution must restrict signing requests to only authorized channels and/or make automated detection/filtration of such attempts automatic.  There are several methods to reduce the chance of this fraud, and the choice of what to use is ultimately up to the vendors who implement this system.  Possible approaches:

Intrusion detection
* Reconciliation of requested signature volume versus impression traffic


Intrusion prevention
* Signing service is not externalized and only available as a function call inside a server such as an ad server or header-bidding server - probably the best answer but not feasible in many use cases.
* Domain restrictions on what domain headers are allowed to request
* IP filters to exclude known IPs from data centers to request signatures
* Only allowing HTTPS requests
* A tighter impression-level connection between ad server and signing service so signing service only signs requests that came from the ad server and only signs each impression once - eg a signing token is issues from the content management system or ad server and this can only be used once by the signing service
* Client-side signals about the page or app requesting the ad, such as IFRAME signals (ancestorOrigins) and the Origin/Referer HTTP request header
* Publisher use of Content Security Policy HTTP response headers to prevent unauthorized iframing of site content


# 4 KEYS AND SIGNATURES <a name="keysandsignatures"></a>

We specify that the Elliptic Curve Digital Signature Algorithm (ECDSA) [1] be used which utilizes public and private keys.  The ECDSA creates a short signature overhead along with similar computational costs between creating and verifying signatures.  We specify FIPS PUB 186-4 Digital Signature Standard NIST Curve P-256 which is widely available in cryptography packages.  This is specified as the named curve “prime256v1” in cryptographic libraries.  Until this spec changes this is the only supported EC algorithm.  We specify 256-bit keys based on results from benchmarking performance on various platforms and industry recommendations.  Modern processors contain instruction sets optimized for values at this size, leading to best performance for signing and signature verification operations.

Use SHA-256 as the digest algorithm.

## 4.1 PUBLIC KEY DISTRIBUTION <a name="publickeydistribution"></a>

Using a similar simple design as the ads.txt specification, instructions for obtaining public keys are also distributed using plain text files hosted on a common website path.

Publisher participation in this scheme is declared for a domain through the presence of an ads-cert.$v.txt (where v is a version integer) file at the root of the domain (e.g. example.com/ads-cert.1.txt) which points signature verifiers to the default public certificate.  This file should be deployed in the same manner and constraints as defined for the ads.txt specification.  As the filename is transmitted in the bid request (in the cert field) you could technically use any filename.

Due to the technical complexity of generating signatures and managing keys, publishers may outsource this function to a trusted third-party.  We expect that software packages and hosted solutions will be created which specialize in managing this function.  

The ads-cert.txt file must be accessible via HTTPS from the website that the instructions are to be applied to under a standard relative path on the server host: "/ads-cert.$v.txt" and the HTTPS request header containing "Content-Type: text/plain".  Since plain HTTP can be intercepted and changed by an attacker, it should not be trusted for distribution of key files.

For the purposes of this document the “root domain” is defined as the “public suffix” plus one string in the name. Crawlers should incorporate Public Suffix list [16] to derive the root domain.

For convenience we will refer to this resource as the "/ads-cert.txt file", though the resource need in fact not originate from a file-system and may have a different name.
 
If the server response indicates an HTTPS redirect (301, 302, 307 status codes), the crawling system should follow the redirect and consume the data as authoritative for the source of the redirect, if and only if the redirect is within scope of the original root domain as defined above.  Multiple redirects are valid as long as each redirect location remains within the original root domain.  Any redirect off the root domain should be ignored.  Accepting a redirect off the root domain is insecure for key distribution purposes.  If an outsourced key management solution is used, it’s required that a subdomain be used via a CNAME record.

If the server response indicates Success (HTTPS 2xx Status Code,) the crawling system must read the content, parse it, and utilize the contained instructions for validating digital signatures.  Validation of the content returned should be done and only valid PEM format certificates should be stored and used.

If the server response indicates the resource is absent (HTTP 404) or forbidden (HTTP 403) or any other HTTP error the advertising system should interpret the response that no public key is available for use.

It is advised to use the HTTP header "Content-Type: text/plain; charset=utf-8" to signal UTF8 support.

Best practice is to use an integer number for the version if you have multiple certificates published.  
Examples would be:


<code>https://example.com/ads-cert.1.txt</code>     
<code>https://example.com/ads-cert.2.txt</code>



## 4.2 ADS-CERT.TXT FILE CONTENT <a name="filecontent"></a>

The public key is encoded as a formatted plain text object, described here. Note that the public key must be secured from tampering yet accessible to be read by outside systems to validate the messages against the signatures.  This scheme has been designed to allow deployment as plain files hosted by any secure web server.  We do not anticipate that any specialized software solution will be needed to host this key management endpoint: specialized software will only be needed to manage generating the files and uploading them to the appropriate hosting location.

As with ads.txt, we expect that the process for setting up an ads-cert.txt file will be somewhat manual initially, yet edits to this file over time should be infrequent since key management complexity is offloaded to another server publishing new certificate files.

Empty lines are permitted in the file.  Lines with comments are permitted but must contain a hash symbol (#) as the first character.

<code># This is an example comment.</code>

The contents of the file are defined by the RFCs 1421 through 1424 and commonly implemented by systems such as OpenSSL.  Please see Section 4.6 Key Generation for an example file and how to create a properly formatted keyfile. 

## 4.3 PRIVATE KEY FILE <a name="privatekeyfile"></a>

Protect your private key file as you would for SSL.  Do not post to a public location or share the key with any system you do not trust.  Note that the private key must be secure and inaccessible to outside systems yet accessible to the systems that generate and sign messages.  Should the security of the private key become compromised, the implementer is advised to obtain a new private/public key and increment the version. 


## 4.4 KEY CERTIFICATE EXPIRATION <a name="keycertificateexpiration"></a>

We recommend that implementers use automated software solutions for key management and key rotation.  It is recommended that the keys change every 90 days and not more frequently than every 60 days, although these are not hard limits. It may be wise, as a matter of policy, to change the key whenever the publisher changes any vendor (e.g. hosting platform) or personnel in their ad serving stack granted access to the private key data.  When the key is changed the cert field in OpenRTB will alert signature inspectors to use a new key.  We recommend configuring automation to monitor the freshness and availability of the certificate files.

## 4.5 KEY GENERATION <a name="keygeneration"></a>

This section illustrates the OpenSSL commands used to generate the necessary public/private key pair for a publisher.  Additional diagnostic commands are shown for informational purposes to help illustrate the makeup and purpose of the key’s data.  Implementers should choose a secure cryptography library that supports the same cryptographic standards, as multiple options are available.  We strongly recommend that implementers do NOT use manual commands such as these to generate keys for production use.  Instead, these commands illustrate the general process that key management software needs to implement so that generation and rotation of these keys gets managed automatically.

This example command generates a unique private key and stores the key in the file “private_key.pem”.  This private key file must be protected from disclosure to others, as it contains the secret used to certify legitimate ad inventory.

<code>$ openssl ecparam -name prime256v1 -genkey -out private_key.pem</code>

For illustrative purposes, this shows a sample of a generated key.  (Do not use this sample for production purposes, as it is not confidential after being published in this document.)

```
$ cat private_key.pem 
-----BEGIN EC PARAMETERS-----
BggqhkjOPQMBBw==
-----END EC PARAMETERS-----
-----BEGIN EC PRIVATE KEY-----
MHcCAQEEINhNtLN4yxcsuTEVTGC2F0ZO2WHrsq/5ZaYCVWA1ICinoAoGCCqGSM49
AwEHoUQDQgAE96ZDx1/52MPrkYAGcVZduHwJc5GH26vx9KX8MQFQ1/0KcTe0VDc9
1YgZ0AtXGgtk7aF8sf+aJa/BuMivzutBdw==
-----END EC PRIVATE KEY-----
```


For debugging, a more readable version of the private key can be viewed by using the openssl diagnostics mode which outputs a text representation of the key.

```
$ openssl ec -in private_key.pem -text -noout
read EC key
Private-Key: (256 bit)
priv:
    d8:4d:b4:b3:78:cb:17:2c:b9:31:15:4c:60:b6:17:
    46:4e:d9:61:eb:b2:af:f9:65:a6:02:55:60:35:20:
    28:a7
pub:
    04:f7:a6:43:c7:5f:f9:d8:c3:eb:91:80:06:71:56:
    5d:b8:7c:09:73:91:87:db:ab:f1:f4:a5:fc:31:01:
    50:d7:fd:0a:71:37:b4:54:37:3d:d5:88:19:d0:0b:
    57:1a:0b:64:ed:a1:7c:b1:ff:9a:25:af:c1:b8:c8:
    af:ce:eb:41:77
ASN1 OID: prime256v1
NIST CURVE: P-256
```

Notice that part of the encoded key data includes an “EC parameters” section.  This data specifies the specific elliptic curve algorithm, “prime256v1”, used in conjunction with the key.  The following command shows how to decode that parameter.  Notice that this parameter indicates the “prime256v1” parameter used on the original command that generated the key.

This example command shows the how the EC parameters string can be decoded to reveal the specific cryptographic algorithm used. 
```
$ echo "BggqhkjOPQMBBw==" | openssl base64 -d | openssl asn1parse -inform DER
    0:d=0  hl=2 l=   8 prim: OBJECT            :prime256v1
```

The private key consists of an encoded 32-byte (256-bit) number, and the public key consists of two 32-byte numbers.  A more-readable representation of these bytes is below:

Private key data:

```
d8:4d:b4:b3:78:cb:17:2c:
b9:31:15:4c:60:b6:17:46:
4e:d9:61:eb:b2:af:f9:65:
a6:02:55:60:35:20:28:a7
```

Public key data:
```
04: (format prefix)

(x value)
f7:a6:43:c7:5f:f9:d8:c3:
Eb:91:80:06:71:56:5d:b8:
7c:09:73:91:87:db:ab:f1:
F4:a5:fc:31:01:50:d7:fd:

(y value)
0a:71:37:b4:54:37:3d:d5:
88:19:d0:0b:57:1a:0b:64:
Ed:a1:7c:b1:ff:9a:25:af:
C1:b8:c8:af:ce:eb:41:77
```

The private key file is then used to create and export the corresponding public key to a file:
```
$ openssl ec -in private_key.pem -pubout -out public_key.pem
read EC key
writing EC key

$ cat public_key.pem 
-----BEGIN PUBLIC KEY-----
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE96ZDx1/52MPrkYAGcVZduHwJc5GH
26vx9KX8MQFQ1/0KcTe0VDc91YgZ0AtXGgtk7aF8sf+aJa/BuMivzutBdw==
-----END PUBLIC KEY-----
```

This public key file contains the same information as in the private key file (excluding the actual private key value).
```
$ openssl ec -in public_key.pem -pubin -text -noout
read EC key
Public-Key: (256 bit)
pub:
    04:f7:a6:43:c7:5f:f9:d8:c3:eb:91:80:06:71:56:
    5d:b8:7c:09:73:91:87:db:ab:f1:f4:a5:fc:31:01:
    50:d7:fd:0a:71:37:b4:54:37:3d:d5:88:19:d0:0b:
    57:1a:0b:64:ed:a1:7c:b1:ff:9a:25:af:c1:b8:c8:
    af:ce:eb:41:77
ASN1 OID: prime256v1
NIST CURVE: P-256
```

Technical details of this encryption and signature algorithms are beyond the scope of this document.  If seeking further details about encryption algorithms, the reader should refer to the documentation of a specific cryptographic library or textbook such as [4] for more details.

Writing secure code involving cryptography is difficult.  Implementers should use one of the widely-available cryptography libraries (OpenSSL, Bouncy Castle, libsodium, etc) for all key generation and signing, as these libraries have been thoroughly peer-reviewed and vetted for security.  A mistake as simple as using a poor quality random number generator when signing bid requests can result in an attacker being able to guess the private key used for signing.  This type of error has been the source of many high-profile security compromises.  Using a leading off-the-shelf cryptography library helps avoid these errors.



# 5 IMPLEMENTATION RECOMMENDATIONS <a name="implementationrecommendations"></a>

Signed Bid Requests are an incremental improvement over the current state, narrowly targeted at creating authentic and validatable bid requests. It is not a fix-all. It should be used in combination with ads.txt - it does not replace ads.txt.  Further enhancements are being developed to add additional security to the supply chain.  Implementers should continue to use caution and diligence even when transacting with Signed Bid Requests.

Publishers should first determine if their inventory should be signed (e.g. video inventory or premium inventory likely to be spoofed). Then publishers should identify a trusted “signing provider” to sign bid requests. Some technically advanced publishers may prefer to implement and sign their bid requests via code in their content management systems or other server code. 

Here a signing provider is a system that the publisher delegates as a trusted partner.  Partners that have a singular function for publishers, like ad servers or header wrapper providers like prebid.js, and prebid-server are expected to implement as they are the primary systems that initiate calls to SSPs.

It is recommended that implementers limit the the production and relay of signatures in to the initial SSP be server-to-server.  Caution must be used when exposing signing logic and functions in JavaScript due to the security issues of in-browser code.  Implementers need to avoid creating server endpoints invoked directly by JavaScript which could generate signatures by circumventing a publisher’s other controls/analytics.  Implementations provided as a hosted solution may need to secure these endpoints with additional, custom means.  See section 3.5.

Implementers should not pause bidding behavior in real-time to fetch certificates that are uncached. Systems should instead create a list of to be fetched certificates and delegate this to the data crawler. Crawlers should be monitoring the files frequently to retrieve new versions to not interrupt bidding. Ideally, public keys will be published and crawled in advance of their initial active date.

# 6 LIMITATIONS AND ABUSE VECTORS <a name="limitations"></a>
This spec doesn’t address all possible scenarios.  The use cases section describes the unsupported use cases.  Further, how to secure the signing service is discussed in section 3.5.  Beyond those two concerns, there are other possible scenarios to be aware of.

Impression replay - an SSP, exchange, or buyer on an exchange rebroadcasting an identical copy of a bid request and redirecting the response to a less valuable impression
* Buyers can limit buying to within a window of the timestamp to limit such abuse
* Buyers can look (likely offline after the fact) for re-use of the same TID from the same exchange to help detect such rebroadcast

Compelled request signing - a publisher’s website or ad server could be invoked through illegitimate means (e.g. an unauthorized site iframing a publisher’s website or ad tag code), causing illegitimate visits to the publisher’s page for the purpose of generating signed bid requests.  This could be combined with the “impression reply” scenario to generate signatures that appear genuine (since the fraudulent actor would not get paid for the compelled request itself).
* Buyers can inspect client-side impression signals such as window.location.ancestorOrigins (Chrome and Safari only) to detect potential unexpected iframe rendering of publisher website content or ad tags
* Publisher ad server implementations can screen requests by inspecting any ancestorOrigins information collected by client-side scripting code
* Use of XMLHttpRequest (XHR) HTTP POST methods to send bid requests from client to ad server will typically expose the URL of the resource generating that request in the Origin and Referer HTTP request headers.  Signer implementations might use this information to ensure that the client-side scripts initiating these requests originate from legitimate pages.
* Publishers can deploy Content Security Policy HTTP response headers to block unauthorized site iframing.

Adopting this specification may lessen some risks of buying illegitimate or unauthorized impressions, but additional mechanisms will be needed to address these gaps.  These solutions will be considered for future iterations of this spec.




# 7 REFERENCES <a name="references"></a>

1. [https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm](https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm)

2. [https://en.wikipedia.org/wiki/Digital_Signature_Algorithm](https://en.wikipedia.org/wiki/Digital_Signature_Algorithm)

3. [https://techcrunch.com/2016/01/06/the-8-2-billion-adtech-fraud-problem-that-everyone-is-ignoring/](https://techcrunch.com/2016/01/06/the-8-2-billion-adtech-fraud-problem-that-everyone-is-ignoring/) 

4. Bruce Schneier, Applied Cryptography: Protocols, Algorithms, and Source Code in C, John Wiley & Sons, Inc. New York, NY, USA, 1993

5. [http://tools.ietf.org/html/rfc3986](http://tools.ietf.org/html/rfc3986)

6. [https://en.wikipedia.org/wiki/Query_string](https://en.wikipedia.org/wiki/Query_string)

7. [https://tools.ietf.org/html/rfc1421](https://tools.ietf.org/html/rfc1421)

8. [https://tools.ietf.org/html/rfc1422](https://tools.ietf.org/html/rfc1422)

9. [https://tools.ietf.org/html/rfc1423](https://tools.ietf.org/html/rfc1423)

10. [https://tools.ietf.org/html/rfc1424](https://tools.ietf.org/html/rfc1424)

11. [https://www.openssl.org/](https://www.openssl.org/)
