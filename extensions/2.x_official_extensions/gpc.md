# Global Privacy Control

Issue: [#98](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/98)

Sponsor: Magnite

## Objective

The purpose of this field is to support propagating the Global Privacy Control signal that originates in the HTTP headers from a web client, in order for all participants to have access to the same information on the privacy state of a user in regard to applicable regulations.

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
  <td>This value should exactly replicate the value in the Global Privacy Control signal setting received from the upstream request. Where that was an HTTP GET request, it is the contents of the `Sec-GPC` HTTP header. Where that was an OpenRTB request, it is the contents of the equivalent field.</td>
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
