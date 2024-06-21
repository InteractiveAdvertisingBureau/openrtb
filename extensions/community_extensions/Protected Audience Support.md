# Bid Request Values

## Object: InterestGroupAuctionSupport 

`BidRequest.imp.ext.igs`: This extension to "Object: Imp" allows sellers to signal Interest Group auction support for an Impression to buyers. 

<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>ae</code></td>
    <td>integer;<br>default 1</td>
    <td>Interest Group auction environment support for this impression:<br>
        0 = Interest Group auction not supported<br>
        1 = on-device-orchestrated Interest Group auction<br>
        3 = server-orchestrated Interest Group auction<br>
        <br>
        Note that this only indicates that the Interest Group auction is supported, not that it is guaranteed to execute. If no buyer chooses to participate in the Interest Group auction, then the Interest Group auction will be skipped and the winner of the OpenRTB (aka contextual) auction, if any, will serve instead.</td>
  </tr>
  <tr>
    <td><code>biddable</code></td>
    <td>integer;<br>default 0</td>
    <td>Indicates whether the buyer is allowed to participate in the Interest Group auction. Depending on account settings and other factors, a bidder might be disallowed from participating in an auction or submitting Interest Group bids, even though an Interest Group auction may ultimately decide the winning ad. The seller sets this. </br>
    Example: the publisher intends to enable Interest Group, but the seller has not onboarded this buyer for Interest Group auctions. Buyers should only expect sellers to honor corresponding Interest Group Intent signals when this field is 1.
    <br>
    <br>
    0 = no, the buyer is not allowed<br>
    1 = yes, the buyer is allowed
    </td>
  </tr>
<tr>
    <td><code>pbsmap</code></td>
    <td>integer;<br>default 0</td>
    <td>Indicates whether the seller can provide <code>igb.pbs</code> in <code>Object: InterestGroupAuctionBuyerSignals</code></br>
    where <br>
	0 = no; igb.pbs value is passthrough (DEFAULT)<br>
	1 = yes; igb.pbs will be provided in the structure indicated by the <code>buyer</code> attribute.
    </td>
  </tr>
</table>


# Bid Response Values

## Object: InterestGroupAuctionIntent

`BidResponse.ext.igi`: This extension to "Object: Bid" allows buyers and sellers to provide necessary signals in order to operate an Interest Group auction for a given ad slot. </br>
Must include at least one buyer (`igb`, in the bid response from the buyer to the seller) or at least one seller (`igs`, from the seller to the publisher) object, but not both.

<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>impid</code></td>
    <td>string; required*</td>
    <td>ID of the <code>Imp</code> object in the related bid request.  Used to link Interest Group buyer information to the specific ad slot.</br>
    *Only required in the bid response from the buyer to the seller</td>
  </tr>
  <tr>
    <td><code>igb</code></td>
    <td>array; required</td>
    <td>One or more <code>InterestGroupAuctionBuyer</code> objects. Required and mutually exclusive with <code>igs</code>.</td>
 </tr>
  <tr>
    <td><code>igs</code></td>
    <td>array; required</td>
    <td>One or more <code>InterestGroupAuctionSeller</code> objects. Required and mutually exclusive with <code>igb</code>.</td>
 </tr>	
</table>

## Object: InterestGroupAuctionBuyer 

`BidResponse.ext.igi.igb`: Information provided by the buyer and necessary for the seller to build the associated `config`.

<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>origin </code></td>
    <td>string; required</td>
    <td>Origin of the Interest Group buyer to participate in the IG auction. See https://developer.mozilla.org/en-US/docs/Glossary/Origin.</td>
  </tr>
  <tr>
    <td><code>maxbid</code></td>
    <td>double</td>
    <td>Maximum Interest Group bid price expressed in CPM currency that a bidder agrees to pay if they win an in-browser Interest Group auction expressed expressed in the currency denoted by the <code>cur</code> attribute. Actual winning bid in the in-browser auction that determines the amount a bidder pays for the impression may be lower than this amount. This constraint reduces the risks from in-browser auction bids submitted in error or reported due to fraud and abuse.</td>
 </tr>
  <tr>
    <td><code>cur</code></td>
    <td>string;<br>default "USD"</td>
    <td>
    The buyer's currency signals, an object mapping string keys to Javascript numbers. If specified, the seller will add to its auction config <code>perBuyerCurrencies</code> attribute map, keyed by the Interest Group buyer origin. Indicates the currency in which Interest Group bids will be placed. The <code>maxbid</code> should always match the <code>cur</code> value.
    <br><br>
    Value must be a three digit ISO 4217 alpha currency code (e.g. "USD").
    </td>
  </tr>

  <tr>
    <td><code>pbs</code></td>
    <td>any valid JSON expression</td>
    <td>Buyer-specific signals ultimately provided to this buyer's <code>generateBid()</code> function as the <code>perBuyerSignals</code> argument. If specified, the seller will add to its auction config <code>perBuyerSignals</code> attribute map, keyed by the Interest Group buyer Origin. Per PA API spec, the value may be any valid JSON serializable value.</td>
  </tr>

  <tr>
    <td><code>ps</code></td>
    <td>object</td>
    <td>The buyer’s priority signals, an object mapping string keys to Javascript numbers. If specified, the seller will add to its auction config <code>perBuyerPrioritySignals</code> attribute map, keyed by the Interest Group buyer origin. See https://github.com/WICG/turtledove/blob/main/FLEDGE.md#35-filtering-and-prioritizing-interest-groups </td>
  </tr>
  <tr>
    <td><code>pbsmap</code></td>
    <td>integer;<br>default 0</td>
    <td>Indicates whether the buyer has opted into receiving <code>igb.pbs</code> in <code>Object: InterestGroupAuctionBuyerSignals</code></br>
    where <br>
	0 = no; igb.pbs value is passthrough (DEFAULT)<br>
	1 = yes; igb.pbs can be provided in InterestGroupAuctionBuyerSignals.buyer<br><br>
 The buyer agrees to receive <code>igb.pbs</code> in this format whether the seller provides it in <code>auctionConfig.perBuyerSignals</code> or via <code>directFromSellerSignals.perBuyerSignals</code>. 
    </td>
  </tr>
</table>


## Object: InterestGroupAuctionSeller

`BidResponse.ext.igi.igs`: Information provided by the seller and necessary to initiate an Interest Group component auction. </br>
Component seller auction configuration should be submitted to the top-level seller's on-page library for inclusion in the Interest Group auction.

<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
<tr>
    <td><code>impid</code></td>
    <td>string; required</td>
    <td>ID of the <code>Imp</code> object in the related bid request. Used to link Interest Group seller information to the specific ad slot.</td>
  </tr>
<tr>
    <td><code>config</code></td>
    <td>object; required</td>
    <td>Auction config for a component seller</td>
  </tr>
</table>

## Object: InterestGroupAuctionBuyerSignals
<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
<tr>
    <td><code>buyer</code></td>
    <td>Any JSON serializable value</td>
    <td>Value from the buyer, <code>OpenRTB BidResponse.ext.igi[].igb[].pbs.</code></td>
  </tr>
<tr>
    <td><code>requestnamespace</code></td>
    <td>object</td>
    <td>Namespace of the expected structure of the bid request. See Namespacing section in Implementation Guidance for additional details </td>
  </tr>
</table>

## List: Request Namespace
<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Definition</strong></td>
    <td><strong>Description</strong></td>
  </tr>
<tr>
    <td><code>1</code></td>
    <td><code>ortb2</code></td>
    <td>OpenRTB BidRequest</td>
    <td>A sparse OpenRTB BidRequest. If an ORTB object is present in both auctionSignals and this location, the fields in perBuyerSignals take precedence. </td>
  </tr>
<tr>
    <td><code>2</code></td>
    <td><code>Prebid</code></td>
    <td>Prebid BidRequest</td>
    <td>A sparse PreBid Bid Request. WHAT DO I PUT HERE</td>
  </tr>
<tr>
    <td><code>500+</code></td>
    <td>other</td>
    <td>Any programmatic bid request <b>not</b> structured as an OpenRTB request</td>
  </tr>
</table>


# Implementation Guidance

## Bid Request 

### Signaling Interest Group Auction Support 

Following extends the basic banner example to advertise Interest Group auction support.

```javascript
{
  "id": "80ce30c53c16e6ede735f123ef6e32361bfc7b22",
  "at": 1,
  "cur": [
    "USD"
  ],
  "imp": [
    {
      "id": "1",
      "bidfloor": 0.03,
      "banner": {
        "h": 250,
        "w": 300,
        "pos": 0
      },
      "ext": {
         "igs":{
            "ae": 1
         }
      }
   }],
  "site": {
    "id": "102855",
    "cat": [
      "IAB3-1"
    ],
    "domain": "www.example.com",
    "page": "http://www.example.com/1234.html",
    "publisher": {
      "id": "8953",
      "name": "example.com",
      "cat": [
        "IAB3-1"
      ],
      "domain": "example.com"
    },
    "user": {
      "id": "55816b39711f9b5acf3b90e313ed29e51665623f"
    }
  }
}
```


## Bid Response

### Bid Placed with Interest Group Auction Intent by the Buyer
	
Following extends the Ad Served On Win Notice example to demonstrate a bidder placing a bid into the standard OpenRTB auction and also signaling intent to the seller for one IG owner/buyer to participate in potential on-device auction.


```json
{
  "id": "1234567890",
  "bidid": "abc1123",
  "cur": "USD",
  "seatbid": [
    {
      "seat": "512",
      "bid": [
        {
          "id": "1",
          "impid": "102",
          "price": 9.43,
          "nurl": "http://adserver.com/winnotice?impid=102",
          "iurl": "http://adserver.com/pathtosampleimage",
          "adomain": [
            "advertiserdomain.com"
          ],
          "cid": "campaign111",
          "crid": "creative112",
          "attr": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            12
          ]
        }
      ]
    }
  ],
 "ext": {
    "igi":[{
      "impid": "1",
      "igb":[{   	 
        "origin": "https://paapi.dsp.com",
        "pbs": "{\"key\": \"value\"}"
      }]
    }]
  }
}
```

### No Bid with Interest Group Auction Intent by the Buyer

Following is an example where a bidder places no bid in the standard OpenRTB auction, but does signal intent to participate in potential on-device IG auction.

```json
{
  "id": "1234567890", 
  "seatbid": [],
  "ext": {
    "igi":[{
        "impid": "1",
        "igb":[{   	 
          "origin": "https://paapi.dsp.com",
          "pbs": "{\"key\": \"value\"}"
        }]
    }]
  }
}
```

### Auction Config Placed with Interest Group Auction Intent by the Seller
	
Following extends the Ad Served On Win Notice example to demonstrate a seller placing an auction config into the standard OpenRTB auction and also signaling intent to the publisher to participate in potential on-device auction.


```json
{
  "id": "1234567890",
  "bidid": "abc1123",
  "cur": "USD",
  "seatbid": [
    {
      "seat": "512",
      "bid": [
        {
          "id": "1",
          "impid": "102",
          "price": 9.43,
          "nurl": "http://adserver.com/winnotice?impid=102",
          "iurl": "http://adserver.com/pathtosampleimage",
          "adomain": [
            "advertiserdomain.com"
          ],
          "cid": "campaign111",
          "crid": "creative112",
          "attr": [
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            12
          ]
        }
      ]
    }
  ],
  "ext": {
    "igi":[{
      "igs":[{
        "impid": "1",
        "config": {
          "key": "value"
        }
      }]
    }]
  }
}
```

### Namespacing
#### Namespacing in auctionSignals
The auctionSignals metadata may originate from diverse sources, so this map should be ‘namespaced’ by the providing entity. Where an entity is using OpenRTB objects, it should provide them in an attribute named ortb2. For example:

```json
{
   ...
   "auctionSignals": {
      "prebid": {
         "ortb2": {
            "sparse OpenRTB Request"
         }
      },
      "seller": {
         "ortb2": {
            ...
         }
      }
    "other entities providing metadata, as needed"
   }
   ...
}
```

#### Namespacing in perBuyerSignals
Early PAAPI auction provisioning practice was for buyers to supply their <code>perBuyerSignals</code> to seller partners as a passthrough value in <code>BidResponse.ext.igbid[].igbuyer[].buyerdata</code> or some other means. The seller places this in the buyer’s key in <code>auctionConfig.perBuyerSignals</code> and Protected Audience then provides the value as the <code>perBuyerSignals</code> parameter to <code>generateBid</code>. This does not afford a clean way for the seller to provide signals to a specific buyer, such as Deals.

To accommodate this, the community extensions support a more open <code>perBuyerSignals</code> handling. In an OpenRTB BidRequest sellers can signal their ability to provide and in the BidResponse buyers may opt in to receive an <code>InterestGroupAuctionBuyerSignals</code> object in the interest group auction. 

If buyer "https://dsp.example"’s igb.pbsmap is missing or 0, then the seller/SSP provides perBuyerSignals as:

```
"perBuyerSignals": {
"https://dsp.example": ... // BidResponse.ext.igi[].igb[].pbs
}
```

If the buyer opts into perBuyerSignals namespacing by returning pbsmap = 1, then the seller/SSP provides perBuyerSignalls as:

```
"perBuyerSignals": {
"https://dsp.example": {
        "buyer": …  // BidResponse.ext.igi[].igb[].pbs,
        "seller": {
           "ortb2":{
                /* sparse ORTB value */
           }
        }
     }
}

{
   "buyer": ...,   /* buyer’s pbs value as provided to the seller/publisher in BidResponse.ext.igi[].igb[].pbs */ 
   "seller": {
      "ortb2": {
         ...       /* buyer-specific BidRequest object values */ 
      } 
   }
}
```
