### Global Placement Id(gpid)

**Sponsor**: The Trade Desk

**Background**:
A global placement identifier (GPID) is a publisher-specified placement (tag) ID that is passed unchanged by all supply-side platforms (SSPs).
In programmatic auctions, the OpenRTB TagId property of the Impression object traditionally contains an exchange-specific placement ID. No two exchanges share the same set of placement IDs. Now with header bidding, however, buyers can buy inventory from different sources, but the challenge is that they have no way of knowing which placement is being transacted in a given auction. The solution is to use GPIDs—a publisher specifies a GPID, exchanges propagate it, and buyers know exactly which placement is being transacted via all auctions for the impression.

**Goal**:
The Global Placement ID (GPID) was an initiative in the Fall of 2021 led by the TradeDesk which aims to give buyers a way to identify a given ad slot on a page across SSPs and header bidding integrations. 

### Object: `Imp.ext.gpid` <a name="object"></a>
 <table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>gpid</code></td>
    <td>string</td>
    <td>Distinct, persistent id for each ad unit on a page</td>
  </tr>
</table>

### Implementation notes
While there is no single format for GPID syntax, it is important to keep GPIDs unique to each ad slot on the page, consistent in syntax, and informative, as placements are included in reporting and are used by sophisticated buyers for targeting.

<strong>General Guidelines</strong>
* Focus on the placement that is being sold.
* Avoid including any information that that's already in another part of the bid request. For example, there is no need to include domain, ad format, seller, if it's a mobile placement, and so on.
* Provide a descriptive, named placement rather than a numerical or alphanumerical one.
* Use delimiters, preferably forward slashes (/).
* Apply consistent formatting.

<strong>IMPORTANT:</strong> If you choose to include additional information in a GPID, make sure that the placement portion can be easily identified and extracted in a consistent manner for all placements transacted.

For ease of adoption, where applicable, a Google Ad Manager (GAM) or DoubleClick For Publishing (DFP) ad unit code alone can serve as an effective GPID.

* If the ad unit code alone is sufficient to uniquely identify a placement, use it as a GPID
* If the ad unit code alone is not sufficient to uniquely identify a placement, append the Div ID of the placement after its ad unit code to construct a GPID

### Getting to gpid using Ad Unit Code only

Some publishers have unique and consistent GPIDs within ad unit codes. In these cases, there is no need to include Div IDs in GPIDs, as ad unit codes suffice. Here are some GPID examples constructed using only ad unit codes.

  <table>
  <tr>
    <td><strong>Ad Unit Code Code&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Example gpid</strong></td>
  </tr>
  <tr>
    <td><code>123456/unique-placement-name</td>
    <td>123456/unique-placement-name</td>
  </tr>
</table>

#### Example Request - Ad Unit Code only

```
{
 "imp": [{
  "ext": {
   "gpid": "123456/unique-placement-name"
  }
 }]
}
```

### Using Ad Unit Codes and Div IDs as GPIDs

For details on how publishers typically pass Div IDs to their respective SSPs and exchanges, see the [Prebid documentation](https://docs.prebid.org/features/pbAdSlot.html). 

  <table>
  <tr>
    <td><strong>Ad Unit Code&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>DIV ID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>gpid</strong></td>
  </tr>
  <tr>
    <td><code>123456/example.com/homepage</code></td>
    <td>image_top</td>
    <td>123456/example.com/homepage/image_top</td>
  </tr>
</table>

#### Example Request - Ad Unit Codes and Div IDs

```
{
 "imp": [{
  "ext": {
   "gpid": "123456/example.com/homepage/image_top"
  }
 }]
}
```
