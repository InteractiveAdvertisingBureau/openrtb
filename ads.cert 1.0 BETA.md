![](https://drive.google.com/uc?id=1MStOYYaZDqrvuOwlmecX0iayL0Jt_eAN)
# Ads.cert v1.0: Signed Bid Requests

## BETA DRAFT

**July 24, 2018**



**Executive Summary**

"Ads.cert: Signed Bid Requests" is part of the [OpenRTB 3.0 beta release](https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/OpenRTB%203.0%20BETA.md ). Public comments are open until September 2018. Feedback can be sent to [openmedia@iabtechlab.com](mailto:openmedia@iabtechlab.com) or contributed to the IAB Tech Lab OpenRTB working group.

The beta period will allow for public feedback and continued refinement of the ads.cert specification. As one of many components to fight fraud, it is important that the ads.cert specification and expected business practices are clear. This beta phase will allow the OpenRTB working group to finalize specifications that meet industry needs.

A major component of OpenRTB 3.0 is the shift to an authenticated supply chain. This move to standardize cryptographically signed bid requests is the next step needed in OpenRTB to ensure security and trust in the supply path, to be used in conjunction with ads.txt.


At a high level, the principle of signed bid requests is that Publishers (ie publisher software) should add a signature at the origination of the real time bidding transaction. This provides a traceable path to verify critical data of the inventory such as domain, IP, format, etc. 

Publishers will benefit from this anti-fraud measure in knowing that their inventory is securely passed for sale.  Advertisers and buyers will benefit from this by reviewing the supply authentication to gain confidence in where the inventory is coming from in the real time bidding transaction.

Signed bid requests complements the recent ads.txt protocol. Ads.txt and the data within it should be used to validate who the authorized sellers are for a source of inventory.  Publisher signatures allow a buyer to validate some aspects of the bid request and know that it's trusted from the publisher and key elements of the bid request are unmodified. Together, these technologies are a powerful combination in fighting fraud to allow buyers to check for authenticity and authorization of the sales channel.

After public comment period, the OpenRTB working group will incorporate feedback into a final version of this proposal for industry adoption in conjunction with [OpenRTB 3.0 ](http://iabtechlab.com/openrtb)and [ads.txt](http://iabtechlab.com/ads-txt). As of July publication, there is not a set date for expected final spec and industry adoption. There will be clear communication to the industry for the next steps of ads.cert specification.

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
[3.2.1 SIGNATURE DIGEST FIELDS](#digestfields)  
[3.3 MESSAGE SERIALIZATION](#serialization)  
[4 KEYS AND SIGNATURES](#keysandsignatures)  
[4.1 KEY GENERATION](#keygeneration)  
[4.2 ADS-CERT.TXT FILE DISTRIBUTION](#filedistribution)  
[4.3 ADS-CERT.TXT FILE CONTENT](#filecontent)   
[4.4 PUBLIC KEY DISTRIBUTION](#keydistribution)  
[4.5 PRIVATE KEY FILE](#privatekeyfile)  
[4.6 KEY CERTIFICATE EXPIRATION](#keycertificateexpiration)  
[4.7 END-TO-END EXAMPLE](#example)  
[4.7.1 Publisher publishing ads-cert.txt file](#publisherpublishing)  
[4.7.2 Generating the signing keys](#generatingsigningkeys)  
[4.7.3 Crawling the key management endpoint](#crawlingkeymanagement)  
[4.7.4 Generating a bid request signature](#generatingbidrequestsignature)  
[5 IMPLEMENTATION RECOMMENDATIONS](#implementationrecommendations)  
[6 REFERENCES](#references)  


# INTRODUCTION <a name="intro"></a>

For brevity, we'll assume readers are already familiar with the problem of fraud in ad tech and its vast scale [1][2][3].  Fraud can come in various forms; here we are concentrating on the form wherein ad inventory is being offered to buyers with a false label during the real-time bidding process.  Typically the domain of the webpage (or the bundleID of the mobile app) has been falsified to look like a site that is more valuable than the actual impression available.  A variety of other fields are subject to falsification such as the IP address (to make the inventory appear to be in a more desirable geography), the TID (to make it appear to be a unique impression), the device ID (to correspond to IDs known to have dense/high bids in the past), etc.

## 1.1 APPROACH <a name="approach"></a>

The approach taken here is inspired by the "Domain Keys Identified Mail" or DKIM standard [17].  That standard defines a mechanism where the sending domain of an email system can cryptographically sign an outbound email message with a private key.  Final recipient and intermediary servers can use the domain's public key to then validate the message.  This allows several features and effects:

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

The primary incremental improvement between the ads.txt protected environment of today versus including Signed Bid Requests is the increased assurance to the publisher that inventory isn't being offered on exchanges via IDs contained within the ads.txt file but without the publisher's knowledge.  This companion to ads.txt helps ensure that supply chain intermediaries cannot easily outright manufacture bid requests, distort/reformat bid requests, or replay bid requests in order to inflate volumes.

Not every misrepresentation use case is defended against in the current version of Signed Bid Requests.  As it evolves and broadens, this spec should be fairly effective in addressing the following types of supply chain fraud, all of which are currently possible for intermediaries to introduce within the supply chain:

* App/Domain fraud - making the impression appear to be from a more valuable domain/app

* IP fraud - making the impression appear to be from a more valuable IP subnet (for example, not from a data center or a different country)

* Device ID fraud - making the impression appear to come from a more valuable user

* Consent fraud - granting GDPR-style consent to entities that didn't originally have it, or claiming it is not subject to such regulation

* Basic ad format fraud - for example making a display request look like a video request

Although not an exhaustive list, the following types of fraud would not be addressed due to limitations of the mechanism or avoidance of scope-creep in the initial version:

* More subtle format fraud, such as changing the size of a video unit

* Misdirection of impressions away from the publisher's offered placement and onto another location

Future revisions will broaden the scope of use cases addressed.

# 3 MESSAGE SIGNING BUSINESS LOGIC <a name="logic"></a>

## 3.1 RULES OF SIGNATURES <a name="rules"></a>

There are a few rules for using digital signatures for this purpose.  Below we use the term message to mean an OpenRTB bid request.

1) The originating publisher ("Request Originator") of a message must be the entity that signs the message or delegates this authority to a singular primary system.

2) The message needs to have something unique (a random message identifier) to the message to prevent reuse of the message and signature by a downstream system (TID is used for this purpose).  A timestamp is also included in the signed elements to limit the lookback duration that needs to be considered when de-duplicating requests with a given TID.

3) Any downstream consumer of the message that wants to authenticate it needs access to the fields from the message that were signed, the signature (ps) and the public key of the source of the message.

4) No intermediary/downstream system can make changes in the signed fields 

## 3.2 SIGNATURE BLOCK <a name="block"></a>

We are beginning with a solution for 3.0 to address the key problem in a simple way.  We will allow a single signature generated at the 'root system level'.  This root system could be a publisher's CMS, their main ad server or the controlling header bidder wrapper.   A small set of essential fields will be signed.  While there are certain use cases for additional or more comprehensive signatures, we believe this balances the security versus complexity concerns in an appropriate way for an initial release.  The design also allows for the addition of new signatures and types of signatures in the future, such as exchange signatures, arrays of signatures, etc.

Specifically we will add two new fields in OpenRTB - one called ps, for publisher signature, and one calls pcv, for publisher certificate version - to the Ad Specification object in the AdCOM spec on the request side to contain the signature string that results from signing the fields listed below and the version of the certificate used (for when the publisher issues a new public certificate - which will be done periodically or when there is a security compromise).  Each field value should be concatenated, with a delimiter, to produce the signature digest.  An example is shown below.

The pcv value consists of the key management endpoint identifier, followed by a colon (:), followed by the version string of the key hosted on that endpoint, followed by another colon, and then followed by a timestamp string.  Refer to sections 4.3 and 4.4 for more details on the key management host identifier and key version string.

The timestamp string is necessary to limit the time window evaluated for deduplicating signatures.  Timestamps must be specified in the format yymmddHHMM.  Note that granularity is at the minute level and not more fine-grained.  Dates/times must be specified in Universal Time Coordinated (UTC) and use a 24-hour format (e.g. 13:26 for 1:36pm).  For example, the timestamp string `1807241326` would be used to represent the time 2018-07-24 13:26 UTC.

If a value is not available, null, zero length, or inapplicable for any of these fields an empty string is used.  The publisher signature field should be base64 encoded before placing within the JSON object.

### 3.2.1 SIGNATURE DIGEST FIELDS <a name="digestfields"></a>

<table>
  <tr>
    <td>Level</td>
    <td>Spec</td>
    <td>Object</td>
    <td>Field</td>
    <td>Example Value</td>
  </tr>
  <tr>
    <td>1</td>
    <td>Transaction</td>
    <td>Source</td>
    <td>tid</td>
    <td>ABC7E92FBD6A</td>
  </tr>
  <tr>
    <td>1</td>
    <td>AdCOM</td>
    <td>Ad</td>
    <td>pcv</td>
    <td>a:180720yz:1807241326</td>
  </tr>
  <tr>
    <td>1</td>
    <td>AdCOM</td>
    <td>Site</td>
    <td>domain</td>
    <td>newsite.com</td>
  </tr>
  <tr>
    <td>1</td>
    <td>AdCOM</td>
    <td>App</td>
    <td>bundle [note 1]</td>
    <td></td>
  </tr>
  <tr>
    <td>2</td>
    <td>AdCOM</td>
    <td>User</td>
    <td>consent</td>
    <td></td>
  </tr>
  <tr>
    <td>2</td>
    <td>AdCOM</td>
    <td>Placement</td>
    <td>String consisting of one or more {v,d,a} for video / display / audio sub-objects of the placement present</td>
    <td>vd</td>
  </tr>
  <tr>
    <td>TBD</td>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ip</td>
    <td>192.168.1.1</td>
  </tr>
  <tr>
    <td>TBD</td>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ipv6</td>
    <td></td>
  </tr>
  <tr>
    <td>TBD</td>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ifa</td>
    <td></td>
  </tr>
  <tr>
    <td>TBD</td>
    <td>AdCOM</td>
    <td>Device</td>
    <td>ua</td>
    <td></td>
  </tr>
  <tr>
    <td>TBD</td>
    <td>AdCOM</td>
    <td>Ad.Video</td>
    <td>Player size (field?)</td>
    <td></td>
  </tr>
</table>


*Note 1: presently in-app signatures are not fully supported due to the lack of a reliable method to retrieve the public key since there is no known domain.  The issue is similar for ads.txt, and a solution is [under discussion](iabtechlab.com/ads-txt) in that context. App bundle and ifa are included in this spec with the goal that mobile apps can be supported by version 1 signatures.  Regardless of the availability of that solution, the app-specific fields can still be included in the signature.*

Things considered or under consideration for signature:

* Universal audience/user identifiers

    * Middlemen will have incentive to replace IDs with ones with historically high monetization

    * However, no proposal is accepted yet - if and when one is, it can be added to the signature block

* Question: Should we even attempt to design in IP and idfa given that GDPR will have unknown effects on the ability for that to be used across GEO borders or be truncated due to privacy. Current answer: IP can be anonymized at signature time and maintained in that state through the chain.

## 3.3 MESSAGE SERIALIZATION <a name="serialization"></a>

  Each message to be signed must be canonically generated and serialized. Each message is a non-empty set of elements, in URL query string format.  The key names should be alphabetized to avoid ambiguity in string serialization. Refer to [5][6] for URL query formatting and encoding.  Note that missing values should be included as a variable with an empty string as the value.

# 4 KEYS AND SIGNATURES <a name="keysandsignatures"></a>

We specify that the Elliptic Curve Digital Signature Algorithm (ECDSA) [1] be used which utilizes public and private keys.  The ECDSA creates a short signature overhead along with similar computational costs between creating and verifying signatures.  We specify FIPS PUB 186-4 Digital Signature Standard NIST Curve P-256 which is widely available in cryptography packages.  This is specified as the named curve "prime256v1" in cryptographic libraries.  Until this spec changes this is the only supported EC algorithm.  We specify 256-bit keys based on results from benchmarking performance on various platforms and industry recommendations.  Modern processors contain instruction sets optimized for values at this size, leading to best performance for signing and signature verification operations.

Use SHA-256 as the digest algorithm.

## 4.1 KEY GENERATION <a name="keygeneration"></a>

This section illustrates the OpenSSL commands used to generate the necessary public/private key pair for a publisher.  Additional diagnostic commands are shown for informational purposes to help illustrate the makeup and purpose of the key's data.  Implementers should choose a secure cryptography library that supports the same cryptographic standards, as multiple options are available.  We strongly recommend that implementers do NOT use manual commands such as these to generate keys for production use.  Instead, these commands illustrate the general process that key management software needs to implement so that generation and rotation of these keys gets managed automatically.

This example command generates a unique private key and stores the key in the file "private_key.pem".  This private key file must be protected from disclosure to others, as it contains the secret used to certify legitimate ad inventory.

`$ openssl ecparam -name prime256v1 -genkey -out private_key.pem`

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

Notice that part of the encoded key data includes an "EC parameters" section.  This data specifies the specific elliptic curve algorithm, "prime256v1", used in conjunction with the key.  The following command shows how to decode that parameter.  Notice that this parameter indicates the "prime256v1" parameter used on the original command that generated the key.

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
```

```
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

## 4.2 ADS-CERT.TXT FILE DISTRIBUTION <a name="filedistribution"></a>

Using a similar simple design as the ads.txt specification, instructions for obtaining public keys are also distributed using plain text files hosted on a common website path.

Publisher participation in this scheme is declared for a domain through the presence of an ads-cert.txt file at the root of the domain (e.g. example.com/ads-cert.txt) which points signature verifiers to the location where versioned public key files can be found.  This file should be deployed in the same manner and constraints as defined for the ads.txt specification.

Due to the technical complexity of generating signatures and managing keys, publishers should consolidate key management to a core server type or outsource this function to a trusted third-party.  We expect that software packages and hosted solutions will be created which specialize in managing this function.  The purpose of the ads-cert.txt file is to define these delegate systems for locating public key versions.

The ads-cert.txt file must be accessible via HTTPS from the website that the instructions are to be applied to under a standard relative path on the server host: "/ads-cert.txt" and the HTTP request header containing "Content-Type: text/plain".  HTTP can be intercepted and changed by an attacker, so it should not be trusted for distribution of security key locations.

For the purposes of this document the "root domain" is defined as the "public suffix" plus one string in the name. Crawlers should incorporate Public Suffix list [16] to derive the root domain.

For convenience we will refer to this resource as the "/ads-cert.txt file", though the resource need in fact not originate from a file-system.

 

If the server response indicates an HTTP/HTTPS redirect (301, 302, 307 status codes), the crawling system should follow the redirect and consume the data as authoritative for the source of the redirect, if and only if the redirect is within scope of the original root domain as defined above.  Multiple redirects are valid as long as each redirect location remains within the original root domain.  For example an HTTP to HTTPS redirect within the same root domain is valid.  Any redirect off the root domain should be ignored.  Accepting a redirect off the root domain is insecure for key distribution purposes.

If the server response indicates Success (HTTP 2xx Status Code,) the crawling system must read the content, parse it, and utilize the contained instructions for validating digital signatures.  Validation of the content returned should be done and only valid PEM format certificates should be stored and used.

If the server response indicates the resource is absent (HTTP 404) or forbidden (HTTP 403) or any other HTTP error the advertising system should interpret the response that no public key is available for use.

It is advised to use the HTTP header "Content-Type: text/plain; charset=utf-8" to signal UTF8 support.

## 4.3 ADS-CERT.TXT FILE CONTENT <a name="filecontent"></a>

As with ads.txt, we expect that the process for setting up an ads-cert.txt file will be somewhat manual, but edits to this file should be infrequent since key management complexity is offloaded to another server.

To reduce ambiguity, the first line of this file must contain exactly:

`# begin ads-cert.txt`

The file specifies the key management endpoint.  While most publishers are expected to only need one key management endpoint (or a very limited number), the file format allows for 26 entries (ASCII a-z) to specify multiple endpoints.

Empty lines are permitted in the file.  Lines with comments are permitted but must contain a hash symbol (#) as the first character.

`# This is an example comment.`

Key management endpoints are specified by URL base path:

`keyhost_x=https://www.example.com/path/to/key/management`

This directory will then contain files with an index and versions of the keys. The "x" designator indicates that this is the "x" keyhost endpoint and will correspond to the value transmitted with bid requests, explained later.

For example, the key management endpoint for a fictitious "Bisque Publishing Group" portfolio of sites which manages its own keys could use:

`keyhost_a=https://www.bisquepublishing.com/ads_keys`

Future revisions of this format may provide options for defining options or restrictions associated with an endpoint.

The endpoint must be a fully qualified HTTPS URL hosted using SSL. Crawlers must not accept endpoints hosted over non-SSL connections.

## 4.4 PUBLIC KEY DISTRIBUTION <a name="keydistribution"></a>

  The public key is encoded as a formatted plain text object, described here. Note that the public key must be secured from tampering yet accessible to be read by outside systems to validate the messages against the signatures.  This scheme has been designed to allow deployment as plain files hosted by any secure web server.  We do not anticipate that any specialized software solution will be needed to host this key management endpoint: specialized software will only be needed to manage generating the files and uploading them to the appropriate hosting location.

Two artifact types are expected to be published at the key management endpoint:

* An adskeyindex.txt file listing each of the current, future, and recent past key identifiers, and

* Files containing the individual public keys to be used for verification.

Keys are published in a file with the pattern `adspublickey-YYMMDDXX.txt` where `YYMMDDXX` represents the key version string. The version string may contain between zero and two characters after the date to indicate additional distinguishing information, although we anticipate that this should not be frequently needed, and implementers are encouraged to minimize the number of active versions in use.  Platforms performing key management services on behalf of multiple clients should use a unique key management path for each logical client.

For example, the `180720yz` key identifier would correspond to a file `adspublickey-180720yz.txt` published at the path:

`https://www.bisquepublishing.com/ads_keys/adspublickey-180720yz.txt`

The adskeyindex.txt file lists each key version string that a crawler should expect to find in the key management endpoint path.

For disambiguation, this file must begin with the following line:

`# begin adskeyindex.txt`

The file may contain comment lines using the same format as described in section 4.3.

The file should then contain one line per key identifier found at this path.  Some examples:

`180720yz`    
`180730k`  
`180802`  

If a platform needs to revoke a key, the key file should be unpublished from the original path, and a revocation entry should be included in the adskeyindex.txt file.  Specify as one line "revoke:" followed by the key version string to revoke.  Repeat as one entry per line for each key to revoke.  For example:

`revoke:180720yz`  
`revoke:180722ab`

The contents of a public key file associated with an individual version string should be treated as immutable and not be updated for the life of the version string's use.  Crawlers should expect to aggressively cache the contents of these files. Key management endpoints must only be hosted using HTTPS.

## 4.5 PRIVATE KEY FILE <a name="privatekeyfile"></a>

Protect your private key file as you would for SSL.  Do not post to a public location or share the key with any system you do not trust.  Note that the private key must be secure and inaccessible to outside systems yet accessible to the systems that generate and sign messages.  Should the security of the private key become compromised, the implementer is advised to obtain a new private/public key and increment the version.

## 4.6 KEY CERTIFICATE EXPIRATION <a name="keycertificateexpiration"></a>

We expect key management endpoints to use fully automated software solutions for key management and rotation.  It is recommended that the keys change every 90 days and not more frequently than every 60 days, although these are not hard limits.  We recommend publishing new public keys and version strings into the adskeyindex.txt file at least 14 days prior to its use so that crawlers have adequate time to index the new files.  It may be wise, as a matter of policy, to change the key whenever the publisher changes any vendor (e.g. hosting platform) or personnel in their ad serving stack granted access to the private key data.  We recommend configuring automation to monitor the freshness and correct functioning of the key management solution.

## 4.7 END-TO-END EXAMPLE <a name="example"></a>

Here is a sample end-to-end walkthrough that can be used as a test case for verifying proper function of the signing and verification logic in an implementation.

### 4.7.1 Publisher publishing ads-cert.txt file <a name="publisherpublishing"></a>

Example.com, part of the fictitious "Bisque Publishing Group," publishes an ads-cert.txt file at `https://example.com/ads-cert.txt` containing the following content:

```
# begin ads-cert.txt  
# This is an example comment.  
keyhost_a=https://www.bisquepublishing.com/ads_keys  
```

A buyer may crawl this file and find that the "a" partition for example.com points to https://www.bisquepublishing.com/ads_keys as the key distribution point.

### 4.7.2 Generating the signing keys <a name="generatingsigningkeys"></a>

Normally a software API would be used by an implementer to generate keys, but the following OpenSSL commands simulate this process.  We will generate a private key for use by the publisher's signing mechanism and derive the public key to be distributed to buyers:

```
$ openssl ecparam -name prime256v1 -genkey -out private_180720yz.pem  
$ cat private_180720yz.pem   
-----BEGIN EC PARAMETERS-----  
BggqhkjOPQMBBw==  
-----END EC PARAMETERS-----  
-----BEGIN EC PRIVATE KEY-----  
MHcCAQEEINhNtLN4yxcsuTEVTGC2F0ZO2WHrsq/5ZaYCVWA1ICinoAoGCCqGSM49  
AwEHoUQDQgAE96ZDx1/52MPrkYAGcVZduHwJc5GH26vx9KX8MQFQ1/0KcTe0VDc9  
1YgZ0AtXGgtk7aF8sf+aJa/BuMivzutBdw==  
-----END EC PRIVATE KEY-----  
```

```
$ openssl ec -in private_180720yz.pem -pubout \  
      -out adspublickey-180720yz.txt  
$ cat adspublickey-180720yz.txt   
-----BEGIN PUBLIC KEY-----  
MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE96ZDx1/52MPrkYAGcVZduHwJc5GH  
26vx9KX8MQFQ1/0KcTe0VDc91YgZ0AtXGgtk7aF8sf+aJa/BuMivzutBdw==  
-----END PUBLIC KEY-----
```

The file names used here simulate one way that the implementer might organize the key artifacts.  The private key file would be stored securely for use by the signing software, and the public key would be uploaded to the key management endpoint web server.  An entry for the key "180720yz" would also be added to the key distribution endpoint's adskeyindex.txt file.

### 4.7.3 Crawling the key management endpoint <a name="crawlingkeymanagement"></a>

The buyer crawls https://www.bisquepublishing.com/ads_keys/adskeyindex.txt and finds the following file content:

```
# begin adskeyindex.txt  
180720yz  
180802  
  
# Revoked due to changing hosting provider  
revoke:180722ab  
```


This file would prompt the buyer to download two public key files:

`https://www.bisquepublishing.com/ads_keys/adspublickey-180720yz.txt`  
`https://www.bisquepublishing.com/ads_keys/adspublickey-180802.txt`

If the buyer previously crawled the public key with the ID 180722ab, then the "revoke" directive will inform the buyer to no longer purchase bid requests using that key.

### 4.7.4 Generating a bid request signature <a name="generatingbidrequestsignature"></a>

For an individual bid request to an individual exchange, the signature signing software will accept a string containing the plaintext of the signature message.  The publisher' signature management software will concatenate a URL encoded querystring style set of key=value pairs representing the signed message and use the integrated cryptographic library to hash+sign the message.

`bundle=&domain=example.com&pcv=a:180720yz:1807241326&tid=ABC7E92FBD6A`

*This beta release of the Signed Bid Request specification will be revised in a future revision to include a full compatibility test message set so that implementers can verify correct public key interpretation and signature verification.  Currently the above protocol walkthrough helps illustrate the process to solicit public comment.*

# 5 IMPLEMENTATION RECOMMENDATIONS <a name="implementationrecommendations"></a>

Signed Bid Requests are an incremental improvement over the current state, narrowly targeted at creating authentic and validatable bid requests. It is not a fix-all. Further enhancements are being developed to add additional security to the supply chain.  Implementers should continue to use caution and diligence even when transacting with Signed Bid Requests.

Publishers should first determine if their inventory should be signed (e.g. video inventory or premium inventory likely to be spoofed). Then publishers should identify a trusted "signing provider" to sign bid requests. Some technically advanced publishers may prefer to implement and sign their bid requests via code in their content production systems or other server code. 

Here a signing provider is a system that the publisher delegates as a trusted partner.  Partners that have a singular function for publishers, like ad servers or header wrapper providers like prebid.js, and prebid-server are expected to implement as they are primary systems that initiate calls to SSPs.

It is recommended that implementers limit the the production and relay of signatures in to the initial SSP be server-to-server.  Caution must be used when exposing signing logic and functions in JavaScript due to the security issues of in-browser code.  Implementers need to avoid creating server endpoints invoked directly by JavaScript which could generate signatures by circumventing a publisher's other controls/analytics.  Implementations provided as a hosted solution may need to secure these endpoints with additional, custom means.

It is recommended that implementers expose analytics on the count of signed bid requests that we transmitted to any buyer for inspection by the publisher.  This would seller/publishers to be aware of the volume of available inventory on the market with under their name.  It may also help identify cases where unauthorized parties could be attempting to manipulate the signing endpoint to generate illegitimate signatures for improper use.  Implementers will need to remain vigilant regarding the new security surface area created from deploying additional software.

Implementers should not pause bidding behavior in real-time to fetch certificates that are uncached.  Systems should instead create a list of to be fetched certificates and delegate this to the data crawler.   Crawlers should be monitoring the files frequently to retrieve new versions to not interrupt bidding.  Ideally, public keys will be published and crawled in advance of their initial active date.

# 6 REFERENCES <a name="references"></a>

1. [https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm](https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm)

2. [https://en.wikipedia.org/wiki/Digital_Signature_Algorithm](https://en.wikipedia.org/wiki/Digital_Signature_Algorithm)

3. [https://techcrunch.com/2016/01/06/the-8-2-billion-adtech-fraud-problem-that-everyone-is-ignoring/](https://techcrunch.com/2016/01/06/the-8-2-billion-adtech-fraud-problem-that-everyone-is-ignoring/) 

4. Bruce Schneier, Applied Cryptography: Protocols, Algorithms, and Source Code in C, John Wiley & Sons, Inc. New York, NY, USA, 1993

5. [http://tools.ietf.org/html/rfc3986](http://tools.ietf.org/html/rfc3986)

6. [https://en.wikipedia.org/wiki/Query_string](https://en.wikipedia.org/wiki/Query_string)
