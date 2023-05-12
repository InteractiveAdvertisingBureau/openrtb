# IFA_TYPE
**Sponsors:** 

Basis Technologies<br />
Global<br />
Unruly<br />
Index Exchange<br />

## Overview
The `ifa_type` field was originally introduced by IAB Tech Lab in:<br />
[Guidelines for Identifier for Advertising (IFA) on CTV/OTT platforms](https://iabtechlab.com/wp-content/uploads/2018/12/OTT-IFA-guidelines.final_Dec2018.pdf)
(Dec. 2018, updated Sept. 2020).

It has been in use by a number of companies, and is frequently seen in OpenRTB requests. However, the need for such a field has evolved beyond CTV. 
An updated [proposal](https://docs.google.com/document/d/1ko5l88-sS-7HC7TZJW_BwvCAk9f7gzLsFXRp1QjHimM/edit) was presented to the Supply Chain Group in 2020. Since its original introduction in 2018, `ifa_type` has yet to be formally incorporated into OpenRTB. 

**Request Change**

This extension provides a means to indicate the source of the ID passed in `device.ifa`, with the goal of formally adopting previous proposals for an attribute that has already been in use by the community for many years.

## Specification
### Bid Request
#### Object: `device.ext` <a name="object_deviceext"></a>
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
        <code>ifa_type</code>
      </td>
      <td>
        string
      </td>
      <td>
        Declared source of the device.ifa provided in the bid request. Refer to <a href="">List: IFA Types</a> for values.
      </td>
    </tr>
  </tbody>
</table>

### List:  IFA Types <a name="list_ifatypes"></a>

This list identifies the source type of the IFA.

<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td>dpid</td>
    <td>Device provided ID</td>
  </tr>
  <tr>
    <td>ppid</td>
    <td>Publisher provided ID</td>
  </tr>
  <tr>
    <td>sspid</td>
    <td>SSP provided ID</td>
  </tr>
  <tr>
    <td>sessionid<sup>1</sup></td>
    <td>Session ID / Synthetic ID</td>
  </tr>
</table>

*<sup>1</sup>sessionids are defined as IFAs with a more limited life span than standard user/device identifiers - and are intended for use within shorter time intervals.*

**NOTE:** The following values from the original ifa_type proposal shall be considered deprecated:

rida - Roku id<br />
aaid - Android id<br />
idfa - Apple id<br />
afai - Amazon Fire id<br />
msai - Microsoft id

Implementers who wish to know this can infer it. When ifa_type = dpid, look to device information to then determine if it is a RIDA, IDFA, etc.

### Example
```
"device": {
	"devicetype": 3,
	"ifa": "75a6ab8d-4698-1234-1234-446fa01b5f62",
	"ext": {
    	"ifa_type": "dpid"
	}
...
```

