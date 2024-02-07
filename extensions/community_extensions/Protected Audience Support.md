# Bid Request Values

## Object: InterestGroupAuctionSupport 

This object allows sellers to signal interest group auction support for an Imp. 

<table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>ae</code></td>
    <td>integer;<br>default 1</td>
    <td>Interest Group auction environment support for this impression: <br>
1 = on-device orchestrated auction<br>
2 = server-orchestrated auction<br>
<br>
Note that this only indicates that the interest group auction is supported, not that it is guaranteed to execute. If no buyer chooses to participate in the interest group auction, then the interest group auction will be skipped and the winner of the contextual auction, if any, will serve instead.</td>
  </tr>
  <tr>
    <td><code>biddable</code></td>
    <td>integer;<br>default 0</td>
    <td>Indicates whether the bidder is allowed to participate in the interest group auction. Depending on account settings and other factors, a bidder might be disallowed from participating in an auction or submitting interest group bids, even though an interest group auction may ultimately decide the winning ad. The seller sets this. Example, the publisher intends to enable IG, but the seller (SSP) has not onboarded this buyer for IG auctions. Buyers should only expect sellers to honor corresponding Interest Group Intent signals when this field is 1.
    <br>
    <br>
    0 = no, 1 = yes
    </td>
  </tr>
</table>


# Bid Response Values

## Object: InterestGroupAuctionIntent
Information to signal participation in a potential interest group auction for a given ad slot. Must include at least one buyer (`igb`) or at least one seller (`iqs`) object, but not both.

<table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>impid</code></td>
    <td>string; required</td>
    <td>ID of the `Imp` object in the related bid request.  Used to link interest group buyer information to the specific ad slot.</td>
  </tr>
  <tr>
    <td><code>igb</code></td>
    <td>array; required</td>
    <td>One or more `InterestGroupAuctionBuyer` objects. Required and mutually exclusive with `igs`.</td>
 </tr>
  <tr>
    <td><code>igs</code></td>
    <td>array; required</td>
    <td>One or more `InterestGroupAuctionSeller` objects. Required and mutually exclusive with `igb`.</td>
 </tr>	
</table>

## Object: InterestGroupAuctionBuyer 

Information for an interest group auction buyer.

<table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>origin </code></td>
    <td>string; required</td>
    <td>Origin of the interest group buyer to participate in the IG auction. See https://developer.mozilla.org/en-US/docs/Glossary/Origin.</td>
  </tr>
  <tr>
    <td><code>maxbid</code></td>
    <td>double</td>
    <td>Maximum interest group bid price expressed in CPM currency that a bidder agrees to pay if they win an in-browser interest group auction expressed expressed in the currency denoted by the `cur` attribute. Actual winning bid in the in-browser auction that determines the amount a bidder pays for the impression may be lower than this amount. This constraint allows to reduce the risks from in-browser auction bids submitted in error or reported due to fraud and abuse.</td>
 </tr>
  <tr>
    <td><code>cur</code></td>
    <td>string;<br>default "USD"</td>
    <td>
    The buyer’s currency signals, an object mapping string keys to Javascript numbers. If specified, the seller will add to its auction config `perBuyerCurrencies` attribute map, keyed by the interest group buyer origin. Indicates the currency in which interest group bids will be placed. The `maxbid` should always match the `cur` value.
    <br><br>
    Value must be a three digit ISO 4217 alpha currency code (e.g. "USD").
    </td>
  </tr>

  <tr>
    <td><code>pbs</code></td>
    <td>any valid JSON expression</td>
    <td>Buyer-specific signals ultimately provided to this buyer's <code>generateBid()</code>code> function as the <code>perBuyerSignals</code>code> argument. If specified, the seller will add to its auction config <code>perBuyerSignals</code>code> attribute map, keyed by the interest group buyer Origin. Per PA API spec, the value may be any valid JSON serializable value.</td>
  </tr>

  <tr>
    <td><code>ps</code></td>
    <td>object</td>
    <td>The buyer’s priority signals, an object mapping string keys to Javascript numbers. If specified, the seller will add to its auction config <code>perBuyerPrioritySignals</code>code> attribute map, keyed by the interest group buyer origin. See https://github.com/WICG/turtledove/blob/main/FLEDGE.md#35-filtering-and-prioritizing-interest-groups </td>
  </tr>
</table>


  ## Object: InterestGroupAuctionSeller
Information for an interest group auction component seller. Component seller auction configuration should be submitted to the top-level seller on-page library for inclusion in the interest group auction.

<table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Description</strong></td>
  </tr>
<tr>
    <td><code>impid</code></td>
    <td>string; required</td>
    <td>ID of the Imp object in the related bid request. Used to link interest group seller information to the specific ad slot.</td>
  </tr>
<tr>
    <td><code>config</code></td>
    <td>object; required</td>
    <td>Auction config for a component seller</td>
  </tr>
</table>
