# Per-Impression Transactions IDs for Multi-Impression Bid Requests

Issue: [#104](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/104)

Sponsor: Index Exchange

## Background

OpenRTB bid requests support an array of impression objects, which may correspond to multiple discrete transactions, or all relate to 1 transaction. However, the 2.X spec only supports 1 transaction ID per request via the source.tid field. This community extension allows for specifying a transaction ID per item in the impression array, so that multiple transaction IDs can be transmitted in a single bid request.

## Request Changes

<table>
 <tr>
  <th>Imp Object</th>
  <th>Type</th>
  <th>Example values</th>
  <th>Description</th>
 </tr>
 <tr>
  <td>imp.ext.tid</td>
  <td>string</td>
  <td>e8348715-9221-4483-8615-3471ec0fb77b</td>
  <td>A string representing a transaction ID, which is expected to be common across all participants in
this bid request (e.g., potentially multiple exchanges).</td>
 </tr>
</table>

## How To Use This Field

### If source.tid IS provided and imp.ext.tid is NOT
It should be assumed that all items in the imp array belong to the same transaction (i.e. buyers should only expect 1 billing (impression) notification to be fired though there's an array of imp objects).

### If source.tid is NOT provided and imp.ext.tid IS
The seller may either set the same imp.ext.tid across all items in the impression array (which is functionally equivalent to the above case).

OR

The seller may set a different imp.ext.tid for some/all items in the impression array, which indicates that multiple transactions are being transmitted in this 1 bid request. In this case, buyers may expect more than 1 billing (impression) notification to be fired, if more than 1 of their bids is accepted, and creatives returned to the client device.

### If BOTH source.tid and imp.ext.tid are provided
imp.ext.tid should override, with source.tid used as a fallback for any items in the impression array that don't specify a imp.ext.tid


## Example Request

```
{
  "imp": [
    {
      "id": 1,
      "banner": {
        "w": 300,
        "h": 250,
        "ext": {
          "tid": "e8348715-9221-4483-8615-3471ec0fb77b"
        }
      }
    },
    {
      "id": 2,
      "banner": {
        "w": 250,
        "h": 250,
        "ext": {
          "tid": "678b925c-1abd-4651-abe2-806018eeffa9"
        }
      }
    }
  ]
  ...
}
```
