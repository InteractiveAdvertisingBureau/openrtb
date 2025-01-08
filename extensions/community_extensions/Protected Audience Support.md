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
</table>


# Bid Response Values

## Object: InterestGroupAuctionIntent

`BidResponse.ext.igi`: This extension to "Object: BidResponse" allows buyers and sellers to provide necessary signals in order to operate an Interest Group auction for a given ad slot. </br>
Must include at least a buyer object (`igb`, in the bid response from the buyer to the seller) or seller object (`igs`, from the seller to the publisher), but not both.

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
    <td><code>origin</code></td>
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
    <td><code>begid</code></td>
    <td>int</td>
    <td>The buyer’s experiment group ID, an integer between zero and 65535 (16 bits). If specified, the seller will add to its auction config <code>perBuyerExperimentGroupIds</code> attribute map, keyed by the Interest Group buyer origin. See https://github.com/WICG/turtledove/blob/main/FLEDGE.md#21-initiating-an-on-device-auction 
    <b>NOTE:</b> Assuming the auction is not run in parallel, the seller will provide the value via the <code>perBuyerExperimentGroupIds</code> auction configuration, provided the seller does not start the auction in parallel with OpenRTB requests.
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

# Non-OpenRTB Signaling
The objects coming from sellers or publishers are expected to be ‘namespaced.’ If OpenRTB is reused it is under the "ortb2" key.

<b>NOTES:</b> 
* If the publisher shares some vendor info that would be namespaced within the pub's windowHostname key, e.g. "prebid.org" or "vendor.com".
* If sellers are using directFromSellerSignals to communicate this information, they should follow the same naming convention.
* If ortb2 namespace is used, it is expected that the structured request matches OpenRTB object model and definitions. All deviations should be done as extensions and negotiated apriori between the parties wanting to send/receive non-standard signals.  

## Object: InterestGroupAuctionBuyerSignals
Early PAAPI auction provisioning practice was for buyers to supply their perBuyerSignals to seller partners as a passthrough value <code>BidResponse.ext.igbid[].igbuyer[].buyerdata</code> or some other means. The seller places this in the buyer’s key in <code>auctionConfig.perBuyerSignals</code> and PA then provides the value as the perBuyerSignals parameter to generateBid.  This does not afford a clean way for the seller to provide signals to a specific buyer, such as Deals.

To accommodate this, the community extensions support a more open perBuyerSignals handling. In an OpenRTB BidRequest sellers can signal their ability to provide and in the BidResponse buyers may opt in to receive an InterestGroupAuctionBuyerSignals object in the interest group auction. 

This object is a dictionary with support for some combination of the following enteries: 

 ```javascript
{
   let signalsFromMyOrigin        = perBuyerSignals[browserSignals.interestGroupOwner]; 
   let signalsFromComponentSeller = perBuyerSignals[browserSignals.seller];
   let signalsFromTopLevelSeller  = perBuyerSignals[browserSignals.topLevelSeller];
   let signalsFromPublisher       = perBuyerSignals[browserSignals.topWindowHostname];
}
```

The presence and placement of these entries determine the role of the <a href="https://developer.mozilla.org/en-US/docs/Glossary/Origin">Origin</a> listed. 

While the entities listed above and their associated <a href="https://developer.mozilla.org/en-US/docs/Glossary/Origin">Origins</a> are defined values established and recognized by the PA APIs, others may be included based on specific agreement between partners that have been negotiated <i>a priori</i>. In that case, the canonical domain of the parties should be used. 

See Namespacing section of implementation guidance for additional detail https://github.com/hillslatt/examplefork/edit/hillslatt-ig-support-ext/extensions/community_extensions/Protected%20Audience%20Support.md?pr=%2FInteractiveAdvertisingBureau%2Fopenrtb%2Fpull%2F175#namespacing

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
Where participants in the auction are using the OpenRTB object model, the <code>seller</code> atrribute should <b>always</b> always have a sub-attribute called .ortb2.

#### Namespacing in auctionSignals
The auctionSignals metadata may originate from diverse sources, so this map should be ‘namespaced’ by the providing entity. Where an entity is using OpenRTB objects, it should provide them in an attribute named ortb2. For example:

```jsonc
{
   ...
   "auctionSignals": {
      "prebid": {
         "ortb2": {
            ... // sparse OpenRTB Bid Request attributes
         }
      },
      "seller": {
         "ortb2": {
            ...
         }
      }
   }
   ...
}
```

### Name Spaced Multi-seller Auction 
```javascript
{
  'seller': 'https://www.example-toplevelseller.com',
  'componentAuctions': [
   {
     "seller": "https://www.example-ssp.com",
     "interestGroupBuyers": ["https://www.example-dsp.com"],
     "perBuyerSignals": {
       "https://www.example-dsp.com": {
         "https://www.example-toplevelseller.com": {}, /* top-level seller’s origin      */
         "https://www.example-ssp.com": {              /* seller’s origin                */
            "ortb2":{
               "imp":[
                  "pmp":{
                     "deals":[...]
                  }
               ]
            }
         },
         "https://www.example-dsp.com": igi.igb[].pbs, /* buyer’s origin                 */
         "www.example-publisher.com": {},              /* publisher host without scheme  */
       }
     },
     ...
   },
   ...
   ]
}
```

**Within generateBid**
generateBid(interestGroup, auctionSignals, perBuyerSignals, trustedBiddingSignals, browserSignals, directFromSellerSignals) 
```javascript
{
   let signalsFromMyOrigin        = perBuyerSignals[browserSignals.interestGroupOwner]; 
   let signalsFromComponentSeller = perBuyerSignals[browserSignals.seller];
   let signalsFromTopLevelSeller  = perBuyerSignals[browserSignals.topLevelSeller];
   let signalsFromPublisher       = perBuyerSignals[browserSignals.topWindowHostname];
}
```

### Name Spaced Single-seller Auction  
```javascript
 {
  'seller': 'https://www.example-seller.com',
  "interestGroupBuyers": ["https://www.example-dsp.com"],
  "perBuyerSignals": {
    "https://www.example-dsp.com": {
      "https://www.example-seller.com": {},         /* single-seller’s origin         */
      "https://www.example-dsp.com": igi.igb[].pbs, /* buyer’s origin                 */
      "www.example-publisher.com": {},              /* publisher host without scheme  */
    }
  },
  ...
}
```

**Within generateBid**
```javascript
generateBid(interestGroup, auctionSignals, perBuyerSignals, trustedBiddingSignals, browserSignals, directFromSellerSignals) 
{
   let signalsFromMyOrigin        = perBuyerSignals[browserSignals.interestGroupOwner]; 
   let signalsFromSingleSeller    = perBuyerSignals[browserSignals.seller];
   let signalsFromPublisher       = perBuyerSignals[browserSignals.topWindowHostname];
}
```
