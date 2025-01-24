# Executive Summary
The Commerce Media Product Listing Ads use case is one that aligns with concepts already enabled by OpenRTB and native creative. However, it does have a few nuances that need acknowledgment within the programmatic ecosystem before trading commences. 

We’d highlight the following:
* The most critical component to the transaction is the understanding of the product feed that is in use. In the current use case: no creative is provided by the buyer - the creative is understood and surfaced by the platform based on feed-specific product codes provided in the bid response.
* Contextual requirements (allowed & blocked products and categories) are only understood through the feed.


While the current use case fits best with Native creative type, this option remains creative agnostic, so as not to prevent future iterations. This solution focuses on the fact that the bid request cannot be responded to appropriately without the buyer understanding the product feed. Therefore, <code>prodfeed</code> is the central component to this solution.

# Bid Request Object: Prodfeed
In an effort to ensure bidders understand the requirements to participate in this auction as early as possible, AND to remain creative agnostic, we propose placing key details about the product feed in a new <code>prodfeed</code> object within <code>bidrequest</code>. 

This gives a clear, high level signal that special treatment of the request is required, and may help future proof creative use cases beyond the native spec (for example audio creative in a grocery store limited to the allowed products). Other similar fields already live in the top level ‘bidrequest’ object, such as <code>acat</code>, <code>bcat</code>. 

<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>prodfeed</code></td>
    <td>object</td>
    <td>If understanding of a product feed or catalog is required to bid on this opportunity, then use this object to declare required details.</td>
  </tr>
</table>

## Object: prodfeed
Presence of this object indicates that the bid request can only be responded to appropriately if the buyer has an understanding of the product feed and product codes detailed in this object. 

<table>
  <tr>
    <td><strong>Attribute</strong></td>
    <td><strong>Type</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>id</code></td>
    <td>string</td>
    <td>An identifier uniquely identifying the product feed that pertains to the ad request.<br><br>
        If the feed owner maintains more than one product feed they may provide version specific details in this attribute.<br><br>
        It is expected that if a feedid is used, the use case has been  implemented by the buyer a priori to the request.
    </td>
  </tr>
   <tr>
    <td><code>domain</code></td>
    <td>string</td>
    <td>Canonical domain of the owner of the product feed related to the ad request.<br><br>
    Required if prodfeed parent object is present<br><br>
    It is expected that the product feed has been implemented by the buyer a priori to the request.
    </td>
  </tr>
   <tr>
    <td><code>aprod</code></td>
    <td>string, array</td>
    <td>Product codes from product feed that are eligible to serve. <br><br>
        Demand partners should only return products in their bid response included on this list. <br><br>
        Sellers should use aprod only for items that are not already contained in <code>aprodcat</code>
    </td>
  </tr>
   <tr>
    <td><code>bprod</code></td>
    <td>string, array</td>
    <td>Product codes from feeddomain or feedid product feed that are not are eligible to serve.<br><br>
        Demand partners should not return any product in their bid response included on this list.<br><br>
    </td>
  </tr>
   <tr>
    <td><code>aprodcat</code></td>
    <td>string, array</td>
    <td>Product categories from the product feed that are eligible to serve.</td>
    </td>
  </tr>
  <tr>
    <td><code>bprodcat</code></td>
    <td>string, array</td>
    <td>Product categories from the product feed that are not eligible to display. <br><br>
        Sellers should use <code>bprod</code> only for items that are not already contained in <code>bprodcat</code>
</td>
    </td>
  </tr>
   <tr>
    <td><code>ext</code></td>
    <td>object</td>
    <td>Placeholder for vendor specific extensions to this object.</td>
  </tr>
</table>



# Implementation Guidance
Bidding on PLAs (Product Listing Ads) differs from most ad placements in that the creative assets are provided by the publisher. The buyer need only provide a product ID to signal the product to be displayed. The native spec has been leveraged for this, we are currently suggesting data type '13': Product ID, although it is not an official update to the Native Spec at this time, we hope with adoption of this ext to promote it over time.

## Bid Request
### Prodfeed Object
At time of publication, no standard product feed specification exists, so attributes relating to products and categories are strings not IDs. Buyers must have some knowledge of the schema of any product feed they want to bid on <i>a priori</i> to the auction. 

This creates a significant risk for very large bids. Best efforts should be made by implementers to minimize the number of products and/or product categories 

It is assumed that some knowledge of the product feed and/or the product IDs within that feed are understood by the buying system. At the very least, the Product ID (SKU, GTIN, etc.) as signaled by the <code>prodid</code> attribute must be known so the buyer can return a bid. 

It is strongly recommended that the buying system have some understanding of how specific products are categorized within a given product feed to enable support for<code>aprodcat</code> and <code>bprodcat</code> and avoid sending bids for all possible Product IDs. 

Where possible, bids requests that allow an entire category of items to be bid on should avoid sending individual product ids (<code>aprod</code>) that are already in the category associated with `aprodcat` and not send any Product IDs already within that category individually. 

To prevent bloat in the ad request, the number of product ids and/or categories should be reasonably constrained to the smallest possible number of values. For example, if a user is on a category section of a retail or commerce website or application, it is reasonable to send only the allowed product category (<code>aprodcat</code>) for the page where the user is.

### Native Markup Request Object

It is assumed that some knowledge of the product feed exists <i>a priori</i> to the bid request, but a mechanism is necessary to point to the Product Feed containing information about product being bid on is necessary. Requests for PLAs where the creative is provided by the publisher will require using a datatype of 13 in the <a href="https://github.com/InteractiveAdvertisingBureau/Native-Ads/blob/develop/OpenRTB-Native-Ads-Specification-Final-1.2.md#74-data-asset-types">Native Ads API Data Asset Type Object</a>. 

The <code>context</code> attribute will have a value of ‘3’ (Product context such as product listings, details, recommendations, reviews, or similar.)

## Bid Response

### Native Markup Response Object
PLAs will typically require the declaration of a product ID. 
The <code>link</code> object is required in the  <a href="https://github.com/InteractiveAdvertisingBureau/Native-Ads/blob/develop/OpenRTB-Native-Ads-Specification-Final-1.2.md#57-object-link-response">Native Ads API Response Object</a> When bidding useing a product feed using the Product Feed enumeration of 13, in the <a href="https://github.com/InteractiveAdvertisingBureau/Native-Ads/blob/develop/OpenRTB-Native-Ads-Specification-Final-1.2.md#74-data-asset-types">Native Ads API Data Asset Type Object</a> , it is acceptable to leave the ‘link.url’ field empty.

# Examples 

## Request:
````
{
	"id": "13423",
	"prodfeed": {
		"domain": "store.com",
		"bprod": ["sku1234", "sku1235"],
		"aprdodcat": ["abc"]
	},
    "site": {
        "id": "123456",
        "publisher": {
            "id": "seller123"
        },
        "cattax": "7",
	"cat": ["1123"],
        "domain": "store.com",
        "page": "https://store.com/search"
    },
    "cattax": "7",
    "bcat": ["1000", "1001", "1002"],
    "imp": [{
		    "id": "1",
            "tagid": "IYAwpstnsVuJQTpdnzJkhIWq",
            "native": {
                "request": {
                    "native": {
						"ver": "1.2",
						"context": 3,
						"plcmttype": 1,
                        "plcmtcnt": 1,
                        "assets": [{
                                "id": 1,
                                "required": 1,
								"data":{
									"type":13, 
									}
                            }
                        ],          
                    }
                }
            },
            "bidfloor": 0.9,
            "secure": 1
        }
    ],
    "tmax": 300,
    "at": 1
}
````

## Response:

````
{
    "id": "24711",
    "bidid": "6629493c000a958f04e60053",
    "seatbid": [{
            "bid": [{
                    "id": "102",
                    "price": 0.5131,
                    "impid": "1",
                    "burl": "http://example.com/billingnotice?p=${AUCTION_PRICE}&impid=1",
                    "adm": "\"native\":{\"link\":{\"url\":\"\"},\"assets\":[{\"id\":1,\"required\":1,\"data\":{\"value\":\"sku123\"}}],"eventtrackers":[{"event":1,"method":2,"url":"http:tracker.com/track}]}"
                },
                "adomain": ["brand.com"],
                "crid": "12345",
                "cid": "67890",
		"cattax": "7",
                "cat": ["1123"]
            }
        ],
        "seat": "12345"
    }
    ],
    "cur": "USD"
    }
````
