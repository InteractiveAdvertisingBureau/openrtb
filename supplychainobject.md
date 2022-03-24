# OpenRTB SupplyChain object


## Abstract

As part of a broader effort to eliminate the ability to profit from invalid traffic, ad fraud, and counterfeit inventory in the open digital advertising ecosystem, the SupplyChain object enables buyers to see all parties who are selling or reselling a given bid request. This extension object can be used with OpenRTB 2.5. It was officially added to the source object for OpenRTB 2.6 and 3.0.


## Introduction

Ads.txt has been extremely successful in allowing publishers and app makers to define who is authorized to sell a given set of impressions via the programmatic marketplace. Ads.txt does not however make any attempt at revealing or authorizing all parties that are part of the transacting of those impressions. This information can be important to buyers for a number of reasons including transparency of the supply chain, ensuring that all intermediaries are entities with which the buyer wants to transact and that inventory is purchased as directly as possible. The implementation should be as transparent as possible to buyers. It should enable them to easily understand who it is that is participating in the sale of any piece of inventory.


## Implementation

The SupplyChain object is composed primarily of a set of nodes where each node represents a specific entity that participates in the transacting of inventory. The entire chain of nodes from beginning to end represents all entities who are involved in the direct flow of payment for  inventory. Future versions of the specification may also include entities who are involved in the transaction but are not involved in payment.


## Node Definition

A node contains two required properties; the advertising system identifier (asi) and the seller ID (sid). The advertising system identifier is the domain name of the advertising system. The seller ID is used to identify the seller of the inventory; who the advertising system pays for this inventory. Both the advertising system identifier and the seller ID should be the same values that are provided in ads.txt files. It is invalid for a Seller ID to represent multiple entities. Every Seller ID must map to only a single entity that is paid for inventory transacted with that Seller ID. It is valid for a selling entity to have multiple Seller IDs within an advertising system. 


## OpenRTB Object: SupplyChain

This object represents both the links in the supply chain as well as an indicator whether or not the supply chain is complete.

The SupplyChain object should be included in the BidRequest.Source.schain attribute in OpenRTB 2.6 or later, and BidRequest.Source.ext.schain attribute in OpenRTB 2.5. For OpenRTB 2.4 or prior, the BidRequest.ext.schain attribute should be used.

The SupplyChain object includes the following attributes:


<table>
  <tr>
   <td><strong>Attribute</strong></td>
   <td><strong>Type</strong></td>
   <td><strong>Description</strong></td>
  </tr>
  <tr>
   <td>complete</td>
   <td>integer; required</td>
   <td>Flag indicating whether the chain contains all nodes involved in the transaction leading back to the owner of the site, app or other medium  of the inventory, where 0 = no, 1 = yes.</td>
  </tr>
  <tr>
   <td>nodes</td>
   <td>object array; required</td>
   <td>Array of SupplyChainNode objects in the order of the chain. In a complete supply chain, the first node represents the initial advertising system and seller ID involved in the transaction, i.e. the owner of the site, app, or other medium. In an incomplete supply chain, it represents the first known node. The last node represents the entity sending this bid request.</td>
  </tr>
  <tr>
   <td>ver</td>
   <td>string; required</td>
   <td>Version of the supply chain specification in use, in the format of “major.minor”. For example, for version 1.0 of the spec, use the string “1.0”. </td>
  </tr>
  <tr>
   <td>ext</td>
   <td>object; optional</td>
   <td>Placeholder for advertising-system specific extensions to this object. </td>
  </tr>
</table>


## OpenRTB Object: SupplyChainNode

This object is associated with a SupplyChain object as an array of nodes. These nodes define the identity of an entity participating in the supply chain of a bid request. The SupplyChainNode object contains the following attributes:


<table>
  <tr>
   <td><strong>Attribute</strong></td>
   <td><strong>Type</strong></td>
   <td><strong>Description</strong></td>
  </tr>
  <tr>
   <td>asi</td>
   <td>String; required</td>
   <td>The canonical domain name of the SSP, Exchange, Header Wrapper, etc system that bidders connect to. This may be the operational domain of the system, if that is different than the parent corporate domain, to facilitate WHOIS and reverse IP lookups to establish clear ownership of the delegate system. This should be the same value as used to identify sellers in an ads.txt file if one exists.
</td>
  </tr>
  <tr>
   <td>sid</td>
   <td>string; required</td>
   <td>The identifier associated with the seller or reseller account within the advertising system. This must contain the same value used in transactions (i.e. OpenRTB bid requests) in the field specified by the SSP/exchange. Typically, in OpenRTB, this is publisher.id. For OpenDirect it is typically the publisher’s organization ID.Should be limited to 64 characters in length.
</td>
  </tr>
  <tr>
   <td>rid</td>
   <td>String; optional</td>
   <td>The OpenRTB RequestId of the request as issued by this seller.</td>
  </tr>
  <tr>
   <td>name</td>
   <td>String; optional</td>
   <td>The name of the company (the legal entity) that is paid for inventory transacted under the given seller_id. This value is optional and should NOT be included if it exists in the advertising system’s sellers.json file.</td>
  </tr>
  <tr>
   <td>domain</td>
   <td>string; optional</td>
   <td>The business domain name of the entity represented by this node. This value is optional and should NOT be included if it exists in the advertising system’s sellers.json file.</td>
  </tr>
  <tr>
   <td>hp</td>
   <td>integer; required</td>
   <td>Indicates whether this node will be involved in the flow of payment for the inventory. When set to 1, the advertising system in the asi field pays the seller in the sid field, who is responsible for paying the previous node in the chain. When set to 0, this node is not involved in the flow of payment for the inventory. For version 1.0 of SupplyChain, this property should always be 1. It is explicitly required to be included as it is expected that future versions of the specification will introduce non-payment handling nodes. Implementers should ensure that they support this field and propagate it onwards when constructing SupplyChain objects in bid requests sent to a downstream advertising system.
   </td>
  </tr>
  <tr>
   <td>ext</td>
   <td>object; optional</td>
   <td>Placeholder for advertising-system specific extensions to this object.
   </td>
  </tr>
</table>

Note that the asi and domain fields are to be populated with only the canonical domains of the advertising system or seller, and these should be the same domains used in OpenRTB Site .domain, sellers.json, ads.txt for reconciliation.  Full URLs (or even schemas, i.e. http:// or https://) should not be used.  For the purposes of this document the “root domain” is defined as the “public suffix” plus one string in the name. Implementers should incorporate Public Suffix list [16] to derive the root domain.

Valid examples:
example.com
example.co.uk

Invalid examples:
http://example.com
https://example.com/about-us.html


## Implementation Details

It is invalid for a reseller to copy the SupplyChain object from the previous seller to their request for that inventory without also inserting their node into the chain. If a reseller doesn’t insert themselves in the chain, their bid request should not include the SupplyChain object.

If a seller is reselling inventory that didn’t previously contain a SupplyChain object, they should create the SupplyChain object themselves, mark the “complete” attribute with a value of 0 and insert their node into the “nodes” array.

If a seller is reselling inventory that has a SupplyChain object, the reseller should copy the existing object, keeping the original value of the “complete” and append their node to the end of the “nodes” array.

If this is the originating bid request for this inventory, the SupplyChain object should be created with the “complete” attribute set to 1 and their information being the only node in the “nodes” array.


## Examples:

## Valid, complete SupplyChain objects

### Sample originating bid request. (BidRequest1, seller = “directseller.com”):


```
"bidrequest" : {
  "id": "BidRequest1",
  "app": {
    "publisher": {
      "id": "00001"
    }
  }
  "source": {
    "ext": {
      "schain": {
        "ver":"1.0",
        "complete": 1,
        "nodes": [
          {
            "asi":"directseller.com",
            "sid":"00001",
            "rid":"BidRequest1",
            "hp":1
          }
        ]     
      } 
    }
  }
}
```

### Sample resale of BidRequest1 (BidRequest2, seller = “reseller.com”):

```
"bidrequest" : {
  "id": "BidRequest2",
  "app": {
    "publisher": {
      "id": "aaaaa"
    }
  }
  "source": {
    "ext": {
      "schain": {
        "ver":"1.0",
        "complete": 1,
        "nodes": [
          {
            "asi":"directseller.com",
            "sid":"00001"
            "rid":"BidRequest1",
            "hp":1
          },
          {
            "asi":"reseller.com",
            "sid":"aaaaa",
            "rid":"BidRequest2",
            "hp":1
          }
        ]     
      } 
    }
  }
}

```

## Valid, incomplete SupplyChain objects

### Sample originating bid request from advertising system that doesn’t support SupplyChain object. (BidRequest3, seller = “directseller.com”):

```
"bidrequest" : {
  "id": "BidRequest3",
  "app": {
    "publisher": {
      "id": "00001"
    }
  }
  "source": {
    "ext": {
    }
  }
}
```

### Sample resale of BidRequest3 by advertising system that does support SupplyChain object. (BidRequest4, seller = “reseller.com”):


```
"bidrequest" : {
  "id": "BidRequest4",
  "app": {
    "publisher": {
      "id": "aaaaa"
    }
  }
  "source": {
    "ext": {
      "schain": {
        "ver":"1.0",
        "complete": 0,
        "nodes": [
          {
            "asi":"reseller.com",
            "sid":"aaaaa",
            "rid":"BidRequest4",
            "hp":1
          }
        ]     
      } 
    }
  }
}
```
## SupplyChain for Non-OpenRTB Requests
As the documentation above provides guidance only for transactions made via OpenRTB protocol, this section describes a standard way to communicate SupplyChain information that via a tag rather than OpenRTB. This situation most commonly occurs when an advertising system provides a tag that can be inserted into an ad server, video player, SSAI vendor, etc. to initiate an ad request to an advertising system.

**Goals of the serialization:**
* Support all properties of SupplyChain object
* Minimize the need for URL encoding of the serialized data
* Support forward compatibility for future changes to SupplyChain

**Usage Scenarios:**
This appendix outlines the methodology defining a well-structured string containing SupplyChain data that can be received via industry standard ‘key value pairs’. 

If the receiving advertising system is handling a tag-based request and forming an outbound OpenRTB request to another adverting system then the following procedure is specified.  First it should parse the SupplyChain data received as described below, create and fill the SupplyChain object with the data received.  Finally the advertising system should append its own information to the array in the object.

If the receiving advertising system is handling a tag-based request and forming an outbound tag-based request to another advertising system then the following procedure is specified:  It should append a node for itself to the pre-existing string, without altering any preceding information in the received string.

If the receiving advertising system is handling an OpenRTB request and forming an outbound tag-based request to another advertising system then the following procedure is specified:  It should parse and serialize the data within the received SupplyChain object and serialize as specified below. It should form a spec compliant string for itself then append it to the prior string.

**Receiving SupplyChain into an advertising system via tags or URLs**
Advertising systems should support a parameter in their ad tags or VAST URLs to accept a string serialized SupplyChain. It is recommended that this parameter be “schain”.  

For example, an ad server may have a display ad tag format like this:

```
<script src="https://ads.exchange1.com/srv?pid=194&sz=300x250&plid=2842181&schain=[SUPPLYCHAIN GOES HERE]"></script>
```

or a VAST URL format like:

```
https://ads.exchange1.com/srv?pid=194&sz=v&plid=2842185&schain=[SUPPLYCHAIN GOES HERE]
```

**Sending SupplyChain to other tags or URLs**
An advertising system may have a need to pass on a SupplyChain object to another ad tag or VAST URL. This may occur regardless of whether the SupplyChain information is received by the advertising system through OpenRTB or via a string serialized SupplyChain as described above. For this purpose, it is recommended that advertising systems support a macro (for example, $SCHAIN) to output a string serialized SupplyChain. The output of this macro must be a string serialized SupplyChain with the advertising system’s node appended.

### Serialization of an OpenRTB SupplyChain object into a URL parameter
Suggested URL parameter: schain

### Format of serialization
The serialization is composed of two items; the SupplyChainObject properties and the SupplyChainNode array. These two items are separated by a bang (“!”) character.

```{SupplyChainObject}!{SupplyChainNode array}```

### SupplyChainObject properties
There are two properties in a SupplyChain object; version and complete. These two values must be included at the beginning of the serialized value and must be separated by a comma (“,”).

```ver,complete```

### Array of SupplyChainNode properties
Following the SupplyChainObject properties, every node in the SupplyChain must be included. Properties of a SupplyChainNode object must be separated by a comma (“,”) and if there is more than one node, each must be separated by a bang (“!”) character. 

The order of properties is as follows:

```asi,sid,hp,rid,name,domain,ext```

The contents of the ext property are exchange specific, no attempt is made in this document to specify the method of serialization of values for this object.

Optional SupplyChainNode property values can be omitted and trailing separators can be optionally excluded. 

Example:
```exampleexchange.com,12345,1,,,```

or

```exampleexchange.com,12345,1```

If the values in any of the properties require URL encoding (See RFC 3986 or Wikipedia post) or contain a comma or bang character, they should be URL encoded. The comma that is used to separate properties should not be encoded. 

Example:
```exampleexchange.com,123%2CB,1,,,```

This represents an sid of “123,B” on exampleexchange.com, which handles payments.

### Examples

### Single Hop - Chain Complete

**SupplyChain**
``` 
"schain" : {
    "ver":"1.0",
    "complete":1,
    "nodes":[
        {
            "asi":"exchange1.com",
            "sid":"1234",
            "hp":1,
            "rid":"bid-request-1",
            "name":"publisher",
            "domain":"publisher.com"
        }
    ]
}
``` 

**Serialized Value**
``` 
1.0,1!exchange1.com,1234,1,bid-request-1,publisher,publisher.com
``` 

### Single Hop - Chain Complete, optional fields missing

**SupplyChain**
```
"schain" : {
    "ver":"1.0",
    "complete":1,
    "nodes" : [
        {
            "asi":"exchange1.com",
            "sid":"1234",
            "hp":1
        }
    ]
}
```

**Serialized Value**
```1.0,1!exchange1.com,1234,1,,,```

### Multiple Hops - With all properties supplied

**SupplyChain**
```
"schain" : {
    "ver": "1.0",
    "complete" : 1,
    "nodes" : [
        {
            "asi":"exchange1.com",
            "sid":"1234",
            "hp":1,
            "rid":"bid-request-1",
            "name":"publisher",
            "domain":"publisher.com"
        },
        {
            "asi":"exchange2.com",
            "sid":"abcd",
            "hp":1,
            "rid":"bid-request-2",
            "name":"intermediary",
            "domain":"intermediary.com"
        }
    ]
}
```
**Serialized Value**
```1.0,1!exchange1.com,1234,1,bid-request-1,publisher,publisher.com!exchange2.com,abcd,1,bid-request2,intermediary,intermediary.com```

### Multiple Hops - Chain Complete, optional fields missing

**SupplyChain**
```
"schain" : {
    "ver":"1.0",
    "complete":1,
    "nodes":[
        {
            "asi":"exchange1.com",
            "sid":"1234",
            "hp":1
        },
        {
            "asi":"exchange2.com",
            "sid":"abcd",
            "hp":1
        }
    ]
}
```

**Serialized Value**
```1.0,1!exchange1.com,1234,1,,,!exchange2.com,abcd,1,,,```

### Multiple Hops Expected - Chain Incomplete

**SupplyChain**
```
"schain" : {
    "ver":"1.0",
    "complete" :0,
    "nodes":[
        {
            "asi":"exchange2.com",
            "sid":"abcd",
            "hp":1
        }
    ]
}
```

**Serialized Value**
```1.0,0!exchange2.com,abcd,1,,,```

### Single Hop - Chain Complete, encoded values

**SupplyChain**
```
"schain" : {
    "ver":"1.0",
    "complete":1,
    "nodes":[
        {
            "asi":"exchange1.com",
            "sid":"1234!abcd",
            "hp":1,
            "rid":"bid-request-1",
            "name":"publisher, Inc.",
            "domain":"publisher.com"
        }
    ]
}
```

**Serialized Value**
```1.0,1!exchange1.com,1234%21abcd,1,bid-request-1,publisher%2c%20Inc.,publisher.com```

