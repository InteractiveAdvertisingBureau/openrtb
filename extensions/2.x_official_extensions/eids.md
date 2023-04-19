### Extended Identifiers(eids)

**Issue**: [#27](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/27)

**Sponsor**: PubMatic

**Goal**:

The goal is to support a standard protocol for multiple third party identity provider to the ecosystem for 2.x.
This is same object which is part of native user object in [oRTB 3.0 extended identifiers](https://github.com/InteractiveAdvertisingBureau/AdCOM/blob/master/AdCOM%20v1.0%20FINAL.md#object_eids)

**Requested Changes**:

Addition to **User Extension** Object

### Object:  Extended Identifiers <a name="object_eids"></a>

Extended identifiers support in the OpenRTB specification allows buyers to use audience data in real-time bidding.  The exchange should ensure that business agreements allow for the sending of this data.  Note, it is assumed that exchanges and DSPs will collaborate with the appropriate regulatory agencies and ID vendor(s) to ensure compliance.

<table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td><code>source</code></td>
    <td>string</td>
    <td>Source or technology provider responsible for the set of included IDs.  Expressed as a top-level domain.</td>
  </tr>
  <tr>
    <td><code>uids</code></td>
    <td>object&nbsp;array</td>
    <td>Array of extended ID UID objects from the given <code>source</code>.  Refer to <a href="#object_eid_uids">Object: Extended Identifier UIDs</a>.</td>
  </tr>
  <tr>
    <td><code>ext</code></td>
    <td>object</td>
    <td>Optional vendor-specific extensions.</td>
  </tr>
</table>

### Object:  Extended Identifier UIDs <a name="object_eid_uids"></a>

<table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td><code>id</code></td>
    <td>string</td>
    <td>Cookie or platform-native identifier.</td>
  </tr>
  <tr>
    <td><code>atype</code></td>
    <td>integer</td>
    <td>Type of user agent the match is from.  It is highly recommended to set this, as many DSPs separate app-native IDs from browser-based IDs and require a type value for ID resolution.  Refer to <a href="#list_agenttypes">List: Agent Types</a>.</td>
  </tr>
  <tr>
    <td><code>ext</code></td>
    <td>object</td>
    <td>Optional vendor-specific extensions.</td>
  </tr>
</table>

### List:  Agent Types <a name="list_agenttypes"></a>

This list identifies the user agent types a user identifier is from.

<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td>1</td>
    <td>An ID which is tied to a specific web browser or device (cookie-based, probabilistic, or other).</td>
  </tr>
  <tr>
    <td>2</td>
    <td>In-app impressions, which will typically contain a type of device ID (or rather, the privacy-compliant versions of device IDs).</td>
  </tr>
  <tr>
    <td>3</td>
    <td>A person-based ID, i.e., that is the same across devices.</td>
  </tr>
  <tr>
    <td>500+</td>
    <td>Vendor-specific codes.</td>
  </tr>
</table>

#### Approved vendor-specific codes
<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td>571187</td>
    <td>A PAIR Id, which is an encrypted ID that can only be processed by one advertiser, usually created through cleanrooms.</td>
  </tr>
</table>


Example Request

```
{
  "user": {
    "id": "aaa",
    "buyerid": "xxx",
    "ext": {
      "eids": [
        {
          "source": "x-device-vendor-x.com",
          "uids": [
            {
              "id": "yyy",
              "atype": 1
            },
            {
              "id": "zzz",
              "atype": 1
            },
            {
              "id": "DB700403-9A24-4A4B-A8D5-8A0B4BE777D2",
              "atype": 2
            }
          ]
        },
        {
          "source": "digitru.st",
          "uids": [
            {
              "id": "IPl4zj44RhezVyNE83bYfoz59H5GCIQrfCdAVyN51zcsme0faoHqfLBijMQEazqGewXTZsMwMfZqwne8x4eAVeNmAx7iPpk7bpGYp71GUZyysbEEReDYEMJ2hNSldGTT9UExI62HvXuBM16X121r0i8Tko2Bps84tQFWb4lJeun/nRzYwj3ehUGjkE3UOxvHoplNqA43n25pRjgUkUVTRgrpTVLv5Vl4PJ4ir7twHLLow539N3ETb0cHt8GVweHBc2dGmqUTNQxGUZxBA21omEmotXpfqRKrHUo4fm/O9NFgYRN6W8LaCy3USy8dPQ==",
              "atype": 1,
              "ext": {
                "keyv": 4
              }
            }
          ]
        },
        {
          "source": "stat-id-vendor-z.org",
          "uids": [
            {
              "id": "0db20294a3908612bc7e732c2022095391074cf3",
              "atype": 1,
              "ext": {
                "confidence": 0.75
              }
            }
          ]
        }
      ]
    }
  }
}
```
