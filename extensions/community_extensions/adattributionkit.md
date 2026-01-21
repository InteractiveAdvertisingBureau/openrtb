# Apple AdAttributionKit (AAK)

**Status:** Draft (Community Extension Proposal)  
**Related:** OpenRTB 2.x  Extensions Mechanism  
**Version:** 0.1 (Draft)

Sponsors: Liftoff, Dataseat, TBD

Document version support: [AdAttributionKit][1] versions 1.0. Support for newer versions will be brought up for consideration within the IAB TL Programmatic working group subcommittee.

## 1. Overview

This proposal defines a standardized way in OpenRTB to:

1. Signal **publisher eligibility constraints** (which ad networks are allowed/installed for AAK attribution in the publisher app).
2. Allow bidders to return **AdAttributionKit attribution materials** (e.g., a signed impression payload) required for publisher-side registration.
3. Support both **install** and **reengagement** flows (where applicable), aligned with AdAttributionKit conversion types.   

> Note: Apple indicates AdAttributionKit supports **JWS formatted impressions and postbacks**. 

---

## 2. Use Cases

### 2.1 App-install attribution via OpenRTB
A DSP bids with creatives that are eligible for AdAttributionKit attribution. The exchange/publisher app needs sufficient information to register the impression/click for attribution and later receive postbacks.

### 2.2 Reengagement campaigns (retargeting)
AdAttributionKit supports a conversion type representing reengagement (“re-engagement”).   
The bidder may need to indicate that the ad is eligible for reengagement measurement and provide the appropriate destination URL inputs.

---

## 3. Extension Name and Placement

### 3.1 Extension Key
`adattributionkit` (AdAttributionKit Network)

### 3.2 Object Placement (OpenRTB 2.x)
- **BidRequest.Imp.ext.adattributionkit** — publisher signals eligibility / constraints and capabilities
- **BidResponse.SeatBid.Bid.ext.adattributionkit** — bidder returns AdAttributionKit materials

---

## 4. Bid Request

### Object: `BidRequest.imp[].ext.adattributionkit`

When traffic is eligible for AdAttributionKit, SSPs should include a new `adattributionkit` object under `BidRequest.imp.ext`. This object informs DSPs that they can respond with AAK data for attribution.

The object is only present if both the SSP SDK version and the OS version (iOS 17.4+) support AdAttributionKit.

| Attribute | Type | Description |
|-----------|------|-------------|
| version | string; required | Version of AdAttributionKit supported (e.g., "1.0"). Dependent on both the OS version and the SDK version. |
| sourceapp | string; required | The App Store ID of the publisher's app. |
| skadnetids | array of strings; required | A subset of `SKAdNetworkItem` entries in the publisher app's `Info.plist` that are relevant to the bid request. These are the AdNetwork IDs that the DSP can use for attribution. |
| ext | object; optional | Placeholder for exchange-specific extensions to OpenRTB. |
| ext.sko | integer; optional | Indicates whether SKOverlay is available. `1` = available, `0` = not available. |

### Example Bid Request

```json
{
  "imp": [
    {
      "ext": {
        "adattributionkit": {
          "version": "1.0",
          "sourceapp": "123123123",
          "skadnetids": [
            "m8dbw4sv7c.skadnetwork",
            "m2jqnlggk3.adattributionkit"
          ],
          "ext": {
            "sko": 1
          }
        }
      }
    }
  ]
}
```

---

## 5. Bid Response

### Object: `BidResponse.seatbid[].bid[].ext.adattributionkit`

If the bid request indicated AAK support, DSPs can return AAK attribution data using a custom extension field under `BidResponse.seatbid[].bid[].ext.adattributionkit`.

| Attribute | Type | Description |
|-----------|------|-------------|
| jwt | string; required | Signed compact JWS object to be used on device for AAK implementation. This contains the signed attribution data. |
| version | string; required | Version of AdAttributionKit (e.g., "1.0"). |
| itunesitem | string; required | The App Store ID of the advertised app. |
| cpp | string; optional | The Custom Product Page ID (PPID) for the advertised app. |
| reengagementurl | string; optional | The re-engagement URL for Custom Click attribution. Only supported on iOS 18+. |

### Example Bid Response

```json
{
  "seatbid": [
    {
      "bid": [
        {
          "ext": {
            "adattributionkit": {
              "jwt": "eyJhbGciOiJFUzI1NiIsImtpZCI6ImZha2Uua2V5In0.eyJpbXByZXNzaW9uLXR5cGUiOiJhcHAtaW1wcmVzc2lvbiIsImFkLW5ldHdvcmstaWRlbnRpZmllciI6Im15ZHNwLmFkYXR0cmlidXRpb25raXQiLCJwdWJsaXNoZXItaXRlbS1pZGVudGlmaWVyIjowLCJzb3VyY2UtaWRlbnRpZmllciI6MTIzNCwidGltZXN0YW1wIjoxNzAwMDAwMDAwfQ.signature",
              "version": "1.0",
              "itunesitem": "12345678",
              "cpp": "d7db643c-f84f-41d5-b2b3-fce30bf73640",
              "reengagementurl": "https://app.com/re"
            }
          }
        }
      ]
    }
  ]
}
```

---

## 6. Loss Reason Code

Bid responses that contain invalid or malformed AdAttributionKit extensions may be rejected. This rejection can be communicated in loss notifications (lurl) using [Loss Reason Code][2] `216`.

<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td>216</td>
    <td>Creative Filtered - Invalid AdAttributionKit</td>
  </tr>
</table>

## 7.  AdAttributionKit Support Flow

1. SSP SDK retrieves the SKAdNetworkItems from the publisher app’s Info.plist
2. SDK makes ad request to ad server including SKAdNetworkItems
3. SSP determines from Info.plist which DSPs have AdAttributionKit capabilities. Bid request to eligible DSPs includes the imp.ext.adattributionkit object, defined above
4. DSP responds, including `BidResponse.seatbid.bid.ext.adattributionkit` if the campaign requires AdAttributionKit support
5. Ad response to SDK includes `adattributionkit` object
6. If the impression is shown and the user clicks, SSP does
- SDK creates AppImpression object with jwt ([doc][3])
- SDK loads SKStoreProductViewController with the AppImpression object ([doc][4])
- SDK uses the AppImpression object to SKOverlay.AppConfiguration ([doc][5])
- SDK uses the AppImpression object to begin and end View-Though attribution (begin: [doc][6], end: [doc][7])
- SDK uses the AppImpression object to call handleTap([reengagementURL:][8]) with reengagementurl for Custom Click attribution

  If valid, Apple will consider the app for install/Reengagement attribution

7. Target app must register that user for AdAttributionKit attribution on app launch.
8. (Optional). Target app can choose to provide an additional 6 bits of conversion value information.
9. If AdAttributionKit determines that the DSP’s click led to the install, Apple will send a postback to the DSP’s registered endpoint with the ids of the source app, target app and campaign, and conversion value if provided by the target app.

---

## 8. Change Log

| Version | Date | Description |
|---------|------|-------------|
| 1.0 | TBD | Initial release |


[1]: https://developer.apple.com/documentation/AdAttributionKit
[2]: https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/OpenRTB%20v3.0%20FINAL.md#list--loss-reason-codes-
[3]: https://developer.apple.com/documentation/adattributionkit/appimpression/
[4]: https://developer.apple.com/documentation/storekit/skstoreproductviewcontroller/loadproduct%28parameters:impression:%29
[5]: https://developer.apple.com/documentation/storekit/skoverlay/appconfiguration/appimpression
[6]: https://developer.apple.com/documentation/adattributionkit/appimpression/beginview%28%29
[7]: https://developer.apple.com/documentation/adattributionkit/appimpression/endview%28%29
[8]: https://developer.apple.com/documentation/adattributionkit/appimpression/handletap(reengagementurl:)
[9]: https://github.com/InteractiveAdvertisingBureau/openrtb/blob/main/extensions/community_extensions/skadnetwork.md#iabtl-managed-skadnetwork-id-list

[10]: https://developer.apple.com/documentation/uikit/uidevice/identifierforvendor
[11]: https://developer.apple.com/documentation/apptrackingtransparency/attrackingmanager/authorizationstatus
[12]: https://github.com/InteractiveAdvertisingBureau/openrtb/blob/main/extensions/community_extensions/skadnetwork.md#skadnetwork-id-lists-for-app-developers