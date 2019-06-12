# OpenRTB SupplyChain object


## Abstract

As part of a broader effort to eliminate the ability to profit from invalid traffic, ad fraud, and counterfeit inventory in the open digital advertising ecosystem, the SupplyChain object enables buyers to see all parties who are selling or reselling a given bid request. This extension object can be used with OpenRTB 2.5 or OpenRTB 3.0.


## Introduction

Ads.txt has been extremely successful in allowing publishers and app makers to define who is authorized to sell a given set of impressions via the programmatic marketplace. Ads.txt does not however make any attempt at revealing or authorizing all parties that are part of the reselling of those impressions. This information can be important to buyers for any number of reasons including transparency of the supply chain, ensuring that all intermediaries are entities that the buyer wants to transact with and that inventory is purchased as directly as possible. The implementation should be as transparent as possible to buyers. It should enable them to easily understand who it is that is participating in the sale of any piece of inventory.


## Proposed implementation

The SupplyChain object is composed primarily of a set of nodes where each node represents a specific entity that participates in the selling of a bid request. The entire chain of nodes from beginning to end would represent all sellers who were paid for an individual bid request.


## Node definition

A node contains two required properties; the advertising system identifier (ASI) and the Publisher’s account ID (PubId). The advertising system identifier is the domain name of the advertising system. The publisher’s account id is used to identify the seller of the inventory; who the advertising system pays for this inventory. Both the advertising system identifier and the publisher’s account id should be the same values that are provided in ads.txt files. It is highly recommended but not required that a publisher account id uniquely identifies a single seller and is not used to aggregate inventory across multiple sellers. . It is expected that some consumers of the SupplyChain will require that the publisher account id uniquely identify the seller of the inventory.


## OpenRTB object: SupplyChain

This object represents both the links in the supply chain as well as an indicator whether or not the supply chain is complete.

The SupplyChain object should be included in the BidRequest.Source.ext.schain attribute in OpenRTB 2.5 or later. For OpenRTB 2.4 or prior, the BidRequest.ext.schain attribute should be used.

The SupplyChain object includes the following attributes:


<table>
  <tr>
   <td><strong>Attribute</strong></td>
   <td><strong>Type</strong></td>
   <td><strong>Description</strong></td>
  </tr>
  <tr>
   <td>complete</td>
   <td>Integer; required</td>
   <td>Flag indicating whether the chain contains all nodes leading back to the source of the inventory, where 0 = no, 1 = yes.</td>
  </tr>
  <tr>
   <td>nodes</td>
   <td>object array; required</td>
   <td>Array of SupplyChainNode objects in the order of the chain. The original source of the request is first and the final seller of the request last.</td>
  </tr>
</table>



## OpenRTB object: SupplyChainNode

This object is associated with a SupplyChain object as an array of nodes. These nodes define the identity of an entity participating in the supply chain of a bid request. The SupplyChainNode object contains the following attributes that are both required for the node to be valid.


<table>
  <tr>
   <td><strong>Attribute</strong></td>
   <td><strong>Type</strong></td>
   <td><strong>Description</strong></td>
  </tr>
  <tr>
   <td>asi</td>
   <td>String; required</td>
   <td>The canonical domain name of the
SSP, Exchange, Header Wrapper, etc system that bidders connect to. This may be the operational

domain of the system, if that is different than the parent corporate domain, to facilitate WHOIS and

reverse IP lookups to establish clear ownership of the delegate system. This should be the same value as used to identify sellers in an ads.txt file if one exists. Note: This is just the domain, not a full URL.</td>
  </tr>
  <tr>
   <td>pid</td>
   <td>String; required</td>
   <td>The identifier associated with the seller or reseller account within the advertising system. This must contain the same value used in

transactions (i.e. OpenRTB bid requests) in the field specified by the SSP/exchange. Typically, in OpenRTB, this is publisher.id. For OpenDirect it is typically the publisher’s organization ID.Should be limited to 64 characters in length.</td>
  </tr>
  <tr>
   <td>rid</td>
   <td>String; optional</td>
   <td>The OpenRTB RequestId of the request as issued by this seller.</td>
  </tr>
  <tr>
   <td>name</td>
   <td>String; optional</td>
   <td>The business name of the entity represented by this node. This value is optional and should NOT be included if it exists in the advertising system’s sellers.txt file.</td>
  </tr>
  <tr>
   <td>domain</td>
   <td>String; optional</td>
   <td>The business domain name of the entity represented by this node. This value is optional and should NOT be included if it exists in the advertising system’s sellers.txt file. Note: This is just the domain, not a full URL.</td>
  </tr>
</table>



## Implementation details

It is invalid for a reseller to copy the SupplyChain object from the previous seller to their request for that inventory without also inserting their information into the chain. If a reseller doesn’t insert themselves in the chain, their bid request should not include the SupplyChain object.

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
        "complete": 1,
        "nodes": [
          {
            "asi":"directseller.com",
            "pid":"00001",
            "rid":"BidRequest1"
          }
        ]     
      } 
    }
  }
}
```



## Sample resale of BidRequest1 (BidRequest2, seller = “reseller.com”):


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
        "complete": 1,
        "nodes": [
          {
            "asi":"directseller.com",
            "pid":"00001"
            "rid":"BidRequest1"
          },
          {
            "asi":"reseller.com",
            "pid":"aaaaa",
            "rid":"BidRequest2"
          }
        ]     
      } 
    }
  }
}
```


Valid, incomplete SupplyChain objects


## Sample originating bid request from advertising system that doesn’t support SupplyChain object. (BidRequest3, seller = “directseller.com”):


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



## Sample resale of BidRequest3 by advertising system that does support SupplyChain object. (BidRequest4, seller = “reseller.com”):


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
        "complete": 0,
        "nodes": [
          {
            "asi":"reseller.com",
            "pid":"aaaaa",
            "rid":"BidRequest4"
          }
        ]     
      } 
    }
  }
}

