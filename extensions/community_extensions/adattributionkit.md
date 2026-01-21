# Apple AdAttributionKit (AAK)

**Status:** Draft (Community Extension Proposal)
**Related:** OpenRTB 2.x Extensions Mechanism
**Version:** 0.1

Sponsors: Liftoff, Dataseat, TBD

Document version support: [AdAttributionKit][1] versions 1.0. Support for newer versions will be brought up for consideration within the IAB TL Programmatic working group subcommittee.

## 1. Overview

AdAttributionKit (AAK) is Apple's privacy-preserving attribution framework introduced in iOS 17.4+ as the successor to SKAdNetwork. Like SKAdNetwork, it uses ad network identifiers (`.skadnetwork` suffix) to establish trust between publishers and advertisers while providing cryptographically signed attribution data.

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
| sourceapp | string; required | The iTunes item identifier (numeric App Store ID) of the publisher's app (e.g., "123456789"). |
| skadnetids | array of strings; required | A subset of ad network identifiers from the publisher app's `Info.plist` `SKAdNetworkItems` array. AdAttributionKit reuses the same ad network IDs as SKAdNetwork (with `.skadnetwork` suffix). These identifiers indicate which ad networks the DSP can use for attribution. |
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

## 7. AdAttributionKit Support Flow

### 7.1 Bid Request Flow
1. SSP SDK retrieves the `SKAdNetworkItems` from the publisher app's `Info.plist`
2. SDK makes ad request to ad server including the ad network identifiers
3. SSP determines from `Info.plist` which DSPs have AdAttributionKit capabilities. Bid request to eligible DSPs includes the `imp.ext.adattributionkit` object, defined above
4. DSP responds, including `BidResponse.seatbid.bid.ext.adattributionkit` if the campaign requires AdAttributionKit support
5. Ad response to SDK includes `adattributionkit` object

### 7.2 Attribution Registration (Publisher SDK)
When the impression is shown, the SDK creates an `AppImpression` object using the JWT from the bid response ([Refer to Apple AdAttributionKit AppImpression][3]) and registers it using one of the following methods:

**Click Attribution:**
- Load `SKStoreProductViewController` with the `AppImpression` object ([Refer to Apple SKStoreProductViewController][4])
- Configure `SKOverlay.AppConfiguration` with the `AppImpression` object ([Refer to Apple SKOverlay.AppConfiguration][5])
- Call `handleTap(reengagementURL:)` with the `reengagementurl` for Custom Click attribution (iOS 18+) ([Refer to Apple AppImpression][8])

**View-Through Attribution:**
- Call `beginView()` when the impression becomes visible ([Refer to Apple AppImpression][6])
- Call `endView()` when the impression is no longer visible ([Refer to Apple AppImpression][7])

If valid, Apple will consider the app for install or reengagement attribution based on the impression data.

### 7.3 Conversion Registration (Advertiser App)
1. Target app must register the user for AdAttributionKit attribution on app launch
2. (Optional) Target app can provide an additional 6 bits of conversion value information

### 7.4 Postback
If AdAttributionKit determines that the attributed impression/click led to the install or reengagement, Apple will send a postback to the DSP's registered endpoint with the IDs of the source app, target app, campaign, and conversion value (if provided by the target app).

---

## 8. Change Log

| Version | Date | Description |
|---------|------|-------------|
| 0.1 | 2025-01 | Initial draft for community review |


[1]: https://developer.apple.com/documentation/AdAttributionKit
[2]: https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/OpenRTB%20v3.0%20FINAL.md#list--loss-reason-codes-
[3]: https://developer.apple.com/documentation/adattributionkit/appimpression/
[4]: https://developer.apple.com/documentation/storekit/skstoreproductviewcontroller/loadproduct%28parameters:impression:%29
[5]: https://developer.apple.com/documentation/storekit/skoverlay/appconfiguration/appimpression
[6]: https://developer.apple.com/documentation/adattributionkit/appimpression/beginview%28%29
[7]: https://developer.apple.com/documentation/adattributionkit/appimpression/endview%28%29
[8]: https://developer.apple.com/documentation/adattributionkit/appimpression/handletap(reengagementurl:)