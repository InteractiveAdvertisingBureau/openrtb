# Global Privacy Control

Issue: [#98](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/98)

Sponsor: Magnite

## Objective

The purpose of this field is to support propagating the Global Privacy Control (GPC) signal that originates in the HTTP headers from a web client, in order for all participants to have access to the same information on the privacy state of a user in regard to applicable regulations.

## Request Changes

<table>
 <tr>
  <th>Content Object</th>
  <th>Type</th>
  <th>Example values</th>
  <th>Description</th>
 </tr>
 <tr>
  <td>regs.ext.gpc</td>
  <td>string</td>
  <td>1</td>
  <td>This is to be populated the value of the `Sec-GPC` HTTP header from the user agent where the ad will be shown. Vendors with access to that header must populate this field with that value. Vendors receiving an OpenRTB request must populate this with the value of the same field from the request. No other usage of this field is valid. E.g., vendors are not to copy the GPC signal from the GPP string to this field.</td>
 </tr>
</table>

## Example Request

```
{
 "regs": {
  "ext": {
   "gpc": "1"
  }
 }
}
```
