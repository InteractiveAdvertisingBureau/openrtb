### Global Privacy Control

Issue: [#98](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/98)

The goal is to support passing the Global Privacy Control signal to downstream participants in order to allow them to make decisions on how to interpret the privacy state of a user in regard to applicable regulations. 

Downstream consumers of this signal should use it as the primary signal where they believe it applies, since not all publishers may use the signal to change their interpretation of other privacy signals or may interpret it the same way as a downstream consumer. So, where the downstream consumer of OpenRTB signals sees both a USPAPI signal and a GPC signal on a request and considers the GPC signal to mean an opt-out in California, and where the USPAPI signal does not indicate such an opt-out, then the downstream consumer should consider the user opted-out.

When the creator of the OpenRTB object sees a GPC signal they must set this extension with that signal.

Request Changes

<table>
 <tr>
  <th>Content Object</th>
  <th>Type</th>
  <th>Example values</th>
  <th>Description</th>
 </tr>
 <tr>
  <td>content.ext.gpc</td>
  <td>integer</td>
  <td>1</td>
  <td>This value should exactly replicate the value in the Global Privacy Control signal setting that will be available at both the header and window level. Where GPC is set to 1 this should be set to 1 and where it is not present this property should not be present.</td>
 </tr>
</table>

Example Request

```
{
 "content":{
  "ext":{
   "gpc": 1
  }
 }
}
```
