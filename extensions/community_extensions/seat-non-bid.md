# Seat Non Bid Response

Sponsors: Xandr, Magnite, CafeMedia, Media&#46;net

## Overview

There’s an ongoing effort in the industry for exchanges to provide publishers with insights into why seats do not bid. Insights include reasons why the exchange did not request a bid from a seat, why a seat did not bid, and why a bid was considered invalid. Publishers want to use this information to learn how to improve performance and increase efficiency.

This proposal introduces an extension on the `BidResponse` object to enable each seat to provide a reason for not bidding, or for the exchange to provide a reason for not requesting a bid or rejecting a bid from a particular seat.

## Why Something New

The OpenRTB 2.x `BidResponse` object defines the `nbr` field to provide one reason for not bidding. There's no structure defined to convey granular information when some seats bid and others do not.

The `Bid` object cannot be extended for non bid scenarios since it constitutes an offer to buy an impression and requires a price. Similarly, the `SeatBid` object cannot be extended as it requires at least one `Bid`.

## Considerations

Exchanges and publishers who do not wish to emit or act upon these insights may choose to ignore this extension. Exchanges may provide an option to publishers for  including this level of detail.

There are many reasons for a non bid scenario and it is understood not all can be included in a standardized enumeration. Exchanges may use 500+ values to define their own reason codes as appropriate.

## Specification

### Example Bid Response

```
{
    "id": "1234567890",
    "ext": {
        "seatnonbid": [{
            "seat": "512",
            "nonbid": [{
                "impid": "102",
                "statuscode": 301
            }]
        }]
    }
}
```

### Object: BidResponse

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>ext.seatnonbid</code>
      </td>
      <td>
        object array
      </td>
      <td>
        Optional array of <code>SeatNonBid</code> objects.
      </td>
    </tr>
  </tbody>
</table>

### Object: SeatNonBid

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>nonbid</code>
      </td>
      <td>
        object array; required
      </td>
      <td>
        Array of 1+ <code>NonBid</code> objects each related to an impression. Multiple non bids may relate to the same
        impression.
      </td>
    </tr>
    <tr>
      <td>
        <code>seat</code>
      </td>
      <td>
        string
      </td>
      <td>
        ID of the buyer seat (e.g., advertiser, agency) on whose behalf this bid is made.
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        object
      </td>
      <td>
        Placeholder for future extensions.
      </td>
    </tr>
  </tbody>
</table>

### Object: NonBid

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>impid</code>
      </td>
      <td>
        string; required
      </td>
      <td>
        ID of the <code>Imp</code> object in the related bid request.
      </td>
    </tr>
    <tr>
      <td>
        <code>statuscode</code>
      </td>
      <td>
        integer; required
      </td>
      <td>
        Reason for non bid. Refer to the Non Bid Status Codes list in this document.
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        object
      </td>
      <td>
        Placeholder for future extensions.
      </td>
    </tr>
  </tbody>
</table>

### List: Non Bid Status Codes

<table>
  <thead>
    <tr>
      <th>Value</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>0</td>
      <td>No Bid - General</td>
    </tr>
    <tr>
      <td>1</td>
      <td>No Bid - Internal Technical Error</td>
    </tr>
    <tr>
      <td>2</td>
      <td>No Bid - Invalid Request</td>
    </tr>
    <tr>
      <td>3</td>
      <td>No Bid - Known Web Crawler</td>
    </tr>
    <tr>
      <td>4</td>
      <td>No Bid - Suspected Non-Human Traffic</td>
    </tr>
    <tr>
      <td>5</td>
      <td>No Bid - Cloud, Data Center, or Proxy IP</td>
    </tr>
    <tr>
      <td>6</td>
      <td>No Bid - Unsupported Device</td>
    </tr>
    <tr>
      <td>7</td>
      <td>No Bid - Blocked Publisher or Site</td>
    </tr>
    <tr>
      <td>8</td>
      <td>No Bid - Unmatched User</td>
    </tr>
    <tr>
      <td>9</td>
      <td>No Bid - Daily User Cap Met</td>
    </tr>
    <tr>
      <td>10</td>
      <td>No Bid - Daily Domain Cap Met</td>
    </tr>
    <tr>
      <td>11</td>
      <td>No Bid - Ads.txt Authorization Unavailable</td>
    </tr>
    <tr>
      <td>12</td>
      <td>No Bid - Ads.txt Authorization Violation</td>
    </tr>
    <tr>
      <td>13</td>
      <td>No Bid - Ads.cert Authentication Unavailable</td>
    </tr>
    <tr>
      <td>14</td>
      <td>No Bid - Ads.cert Authentication Violation</td>
    </tr>
    <tr>
      <td>15</td>
      <td>No Bid - Insufficient Auction Time</td>
    </tr>
    <tr>
      <td>16</td>
      <td>No Bid - Incomplete SupplyChain</td>
    </tr>
    <tr>
      <td>17</td>
      <td>No Bid - Blocked SupplyChain Node</td>
    </tr>
    <tr>
      <td>100</td>
      <td>Error - General</td>
    </tr>
    <tr>
      <td>101</td>
      <td>Error - Timeout</td>
    </tr>
    <tr>
      <td>102</td>
      <td>Error - Invalid Bid Response</td>
    </tr>
    <tr>
      <td>103</td>
      <td>Error - Bidder Unreachable</td>
    </tr>
    <tr>
      <td>200</td>
      <td>Request Blocked - General</td>
    </tr>
    <tr>
      <td>201</td>
      <td>Request Blocked - Unsupported Channel (app/site/dooh)</td>
    </tr>
    <tr>
      <td>202</td>
      <td>Request Blocked - Unsupported Media Type (banner/video/native/audio)</td>
    </tr>
    <tr>
      <td>203</td>
      <td>Request Blocked - Optimized</td>
    </tr>
    <tr>
      <td>204</td>
      <td>Request Blocked - Privacy</td>
    </tr>
    <tr>
      <td>300</td>
      <td>Response Rejected - General</td>
    </tr>
    <tr>
      <td>301</td>
      <td>Response Rejected - Below Floor</td>
    </tr>
    <tr>
      <td>302</td>
      <td>Response Rejected - Duplicate</td>
    </tr>
    <tr>
      <td>303</td>
      <td>Response Rejected - Category Mapping Invalid</td>
    </tr>
    <tr>
      <td>304</td>
      <td>Response Rejected - Below Deal Floor</td>
    </tr>
    <tr>
      <td>350</td>
      <td>Response Rejected - Invalid Creative</td>
    </tr>
    <tr>
      <td>351</td>
      <td>Response Rejected - Invalid Creative (Size Not Allowed)</td>
    </tr>
    <tr>
      <td>352</td>
      <td>Response Rejected - Invalid Creative (Not Secure)</td>
    </tr>
    <tr>
      <td>353</td>
      <td>Response Rejected - Invalid Creative (Incorrect Format)</td>
    </tr>
    <tr>
      <td>354</td>
      <td>Response Rejected - Invalid Creative (Malware)</td>
    </tr>
    <tr>
      <td>355</td>
      <td>Response Rejected - Invalid Creative (Advertiser Exclusions)</td>
    </tr>
    <tr>
      <td>356</td>
      <td>Response Rejected - Invalid Creative (Advertiser Blocked)</td>
    </tr>
     <tr>
      <td>357</td>
      <td>Response Rejected - Invalid Creative (Category Exclusion)</td>
    </tr>
    <tr>
      <td>500+</td>
      <td>Vendor-specific codes.</td>
    </tr>
  </tbody>
</table>

*Advertiser Exclusions vs Advertiser Blocked*:  "Exclusion" refers to scenarios of competitive separation, while "Blocked" refers to publisher driven block lists.

## Non Bid Status Codes Guidance

- Exchanges are encouraged to provide as much detail as possible, but it is acceptable to use the general codes (0, 100, 200, 300) when details aren't known.

- The values 1-17 intentionally overlap with the OpenRTB 3.0 [No-Bid Reason Codes](https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/OpenRTB%20v3.0%20FINAL.md#list--no-bid-reason-codes-) such that seats which provide a `nbr` can  easily mapping to a `statuscode`. 


Non Bid Status Code values are purposefully organized into the following ranges to assist with high level classification:

<table>
  <thead>
    <tr>
      <th>Range&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
      <th>Description&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
      <th>Interpretation</th>
      <th>Action</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>0-99</td>
      <td>No Bid</td>
      <td>Auction ran successfully without demand for the impression.</td>
      <td>Seats with low demand may be re-evaluated or optimized.</td>
    </tr>
    <tr>
      <td>100-199</td>
      <td>Error</td>
      <td>Technical problem occurred during the auction.</td>
      <td>Seat may need investigation to determine root cause.</td>
    </tr>
    <tr>
      <td>200-299</td>
      <td>Request Rejected</td>
      <td>Impression was explicitly not sent to the seat.</td>
      <td>Bid Request should be re-evaluated for unsupported impressions. May be expected due to exchange configuration.
      </td>
    </tr>
    <tr>
      <td>300-399</td>
      <td>Response Rejected</td>
      <td>Seat responded with a bid that was rejected by the exchange.</td>
      <td>Seat may need to resolve.</td>
    </tr>
  </tbody>
</table>