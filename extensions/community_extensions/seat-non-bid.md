# Seat Non Bid Response

Sponsors: Xandr, Magnite, CafeMedia, Media&#46;net

## Overview

There’s an ongoing effort in the industry for exchanges to provide publishers with insights into why bidders do not bid. Insights include reasons why the exchange did not send a bid request to the bidder, why the bidder did not respond with a bid, and why a bid was considered invalid. Publishers want to use this information to learn how to improve performance and increase efficiency.

There is no location in the OpenRTB 2.x BidResponse object to convey these per-bidder scenarios to the publisher.

OpenRTB 2.x defines one object for seat specific responses. The `SeatBid` object and `Bid` sub-object constitute an offer to buy an impression and require at least one bid with a price. When no bid is placed, the `SeatBid` and `Bid` objects cannot be present in the BidResponse and thus cannot be extended with further information.

The Seat Non Bid is an extension of the OpenRTB 2.x BidResponse object which enables exchanges to specify a new seat specific response object to convey the reasons why an exchange was unable to solicit a bid. Publishers are able to act on this telemetry by automated analysis to optimize performance or to manually investigate bidding issues.

## Considerations

Exchanges and Publishers who do not wish to emit or act upon these insights may choose to  ignore this extension with no action required.

There are many reasons for a no bid scenario and it is understood not all would be included in a standardized enumeration. Exchanges may use 500+ values to define their own reason codes as appropriate.

## Specification

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
      <td>500+</td>
      <td>Vendor-specific codes.</td>
    </tr>
  </tbody>
</table>

If a bid was received with a `nbr` populated, set the status code to align with the `nbr` value. Exchanges are encouraged to provide as much detail as possible, but it is acceptable to use the general codes (0, 100, 200, 300) when details aren't known.

#### Guidance

Non Bid Status Code values are purposefully divided into the following ranges to assist with higher level classification:

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
      <td>Exchange and/or seat may need to investigate.</td>
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