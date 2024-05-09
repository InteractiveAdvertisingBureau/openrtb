# SKAdNetwork

Sponsors: MoPub, Chartboost, PubMatic, Digital Turbine (formerly Fyber), Magnite (formerly Rubicon Project)

Document verison support: SKAdNetwork versions 2.0 to 4.0. Support for newer versions will be brought up for consideration within the IAB TL Programmatic working group subcommittee.

## Overview

The IAB Tech Lab has introduced technical specifications aimed at adapting Apple’s [SKAdNetwork][1], a method for validating advertiser app installations, for programmatic ad buying. The OpenRTB object extensions, APIs and file formats described in this document together enable the advertising ecosystem to communicate and manage the information needed to use the SKAdNetwork capabilities in iOS 14 and above.

The following are the updates provided in this document
1. A [SKAdNetwork extension][10] to support programmatic buying
   * Bid Request extension (`BidRequest.imp.ext.skadn`)
   * Bid Response extension (`BidResponse.seatbid.bid.ext.skadn`)
2. A [Device extension][11] (`BidRequest.device.ext`) to support IDFV and authorization status
3. [IABTL managed SKAdNetwork list][15]
4. Guidance for app developers to help [manage their Info.plists][12] and work with various SDKs.

## SKAdNetwork Extension

### Participant responsibilities

The responsibilities of each participant when using the SKAdNetwork specifications are as follows.

#### IABTL responsibilities are to:
1. Organize SKAdNetwork IDs as well as define an automated self-serve registration process
    - Will not validate the ID with Apple, but will verify that the domain matches the domain of the verified email address of the submitter. Will also provide an offline/non-automated process in case the email domain is different.
    - IABTL will lowercase all received SKAdNetwork IDs upon appending to the master list
2. Perform releases in batches at "x" cadence to ensure as many partners publishers have the most up-to-date lists
    - List should be in both JSON and XML formats to allow publishers to build to the IABTL list as well as other lists
3. Assign a permanent ID for each registered `SKAdNetwork ID`
    - Each registrant may have more than one `SKAdNetwork ID`. In this scenario, each `SKAdNetwork ID` will be assigned its own unique IABTL ID
4. Provide a tool for publishers to build their `Info.plist` files and express IABTL signaling from various URLs and / or raw SKAdNetwork ID (Tool available at https://tools.iabtechlab.com/skadnetwork)


#### SSP/SDK responsibilities are to:

1. Provide publishers with access to their buying entities SKAdNetwork IDs through a publicly hosted lists on their own business domain
2. Support OpenRTB extension objects: `BidRequest.imp.ext.skadn` & `BidResponse.imp.ext.skadn`
3. Provide signed ads to the source app by calling `loadProduct()` with the appropriate data returned on the bid response

#### DSP/intermediary/buying entities responsibilities are to:

1. Provide SKAdNetwork IDs to each supply partner and register on the IAB Tech Lab list at https://tools.iabtechlab.com/skadnetwork
2. Support OpenRTB extension objects: `BidRequest.imp.ext.skadn` & `BidResponse.imp.ext.skadn`
3. Determine if their entity is eligible for attribution postbacks
4. Return all necessary signed parameters to SSP/SDK to facilitate ad signatures and receive install validation postbacks at endpoint established during SKAdNetwork registration with Apple

#### Publishers/source app’s responsibilities are to:

1. Add the ad network’s ID to its Info.plist in all lower case characters.
2. Update Info.plist with new entries added to the IAB Tech Lab / SSP / SDK publicly hosted lists when publishing new app versions to the App Store
3. Supply the supported `versions`, raw `skadnetids`, IABTL `max` and / or `excl` to the SSP / SDK on the device at runtime

### Regulatory Guidance

OpenRTB implementations will need to ensure compliance on every transaction with all applicable regional legislation.

### Bid Request

#### Object: `BidRequest.imp.ext.skadn`

If a DSP has at least one SKAdNetworkItem in the publisher app’s `Info.plist` we would include a new object in the bid request that provides the necessary information to create a signature. Object would only be present if both the SSP SDK version and the OS version (iOS 14.0+) support SKAdNetwork.

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>versions</code>
      </td>
      <td>
        Array of strings containing the supported skadnetwork versions. Always "2.0" or higher. Dependent on both the OS version and the SDK version.
      </td>
      <td>
        array of strings
      </td>
      <td>
        "versions": ["2.0", "2.1", "2.2", "3.0", "4.0"]
      </td>
    </tr>
    <tr>
      <td>
        <code>version</code>
      </td>
      <td>
        Version of skadnetwork supported. Always "2.0" or higher. Dependent on both the OS version and the SDK version. </br></br><strong>Note</strong>: With the release of SKAdNetwork 2.1, this field is deprecated in favor of the <code>BidRequest.imp.ext.skadn.versions</code> to support an array of version numbers.
      </td>
      <td>
        string
      </td>
      <td>
        "version": "2.0"
      </td>
    </tr>
    <tr>
      <td>
        <code>sourceapp</code>
      </td>
      <td>
        ID of publisher app in Apple’s App Store. Should match <code>app.bundle</code> in OpenRTB 2.x and <code>app.storeid</code> in AdCOM 1.x
      </td>
      <td>
        string
      </td>
      <td>
        "sourceapp": "880047117"
      </td>
    </tr>
    <tr>
      <td>
        <code>skadnetids</code>
      </td>
      <td>
        A subset of SKAdNetworkItem entries in the publisher app’s Info.plist, <strong>expressed as lowercase strings</strong>, that are relevant to the bid request. Recommended that this list not exceed 10. </br></br><strong>Note</strong>:<code>BidRequest.imp.ext.skadn.skadnetlist.addl</code> is the preferred method to express raw SKAdNetwork IDs.
      </td>
      <td>
        array of strings
      </td>
      <td>
        "skadnetids": ["cdkw7geqsh.skadnetwork", "qyjfv329m4.skadnetwork"]
      </td>
    </tr>
    <tr>
      <td>
        <code>skadnetlist</code>
      </td>
      <td>
        Object containing the IABTL list definition
      </td>
      <td>
        object
      </td>
      <td>
        "skadnetlist": {
          "max":306,
          "excl":[2,8,10,55]
        }
      </td>
    </tr>
    <tr>
      <td>
        <code>productpage</code>
      </td>
      <td>
        Custom Product Page support. See Apple's <a href="https://developer.apple.com/app-store/custom-product-pages/">Custom Product Page</a> doc for details.
      </td>
      <td>
        integer
      </td>
      <td class="text-monospace">
        "productpage": 1
      </td>
    </tr>
    <tr>
      <td>
        <code>skoverlay</code>
      </td>
      <td>
        <a href="https://developer.apple.com/documentation/storekit/skoverlay">SKOverlay</a> support. If set, SKOverlay is supported.
      </td>
      <td>
        integer
      </td>
      <td>
        "skoverlay": 1
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        Placeholder for exchange-specific extensions to OpenRTB.
      </td>
      <td>
        object
      </td>
      <td>
        "ext": {}
      </td>
    </tr>
  </tbody>
</table>



#### Object: `BidRequest.imp.ext.skadn.skadnetlist`

IABTL skadnetwork object list attributes.

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>max</code>
      </td>
      <td>
        IABTL list containing the max entry ID of SKAdNetwork ID.
        Format will be:
        "max entity ID" where 306 in the example on the right will be all SKAdNetwork IDs entry number 306 and below.
      </td>
      <td>
        integer
      </td>
      <td>
        "max":306
      </td>
    </tr>
    <tr>
      <td>
        <code>excl</code>
      </td>
      <td>
        Comma separated list of integer IABTL registration IDs to be excluded from IABTL shared list.
      </td>
      <td>
        array of integers
      </td>
      <td>
        "excl": [44,14,18]
      </td>
    </tr>
    <tr>
      <td>
        <code>addl</code>
      </td>
      <td>
        Comma separated list of string SKAdNetwork IDs, <strong>expressed as lowercase strings</strong>, not included in the IABTL shared list. The intention of addl is to be the permanent home for raw SKAdNetwork IDs, migrating away from <code>BidRequest.imp.ext.skadn.skadnetids</code>. Recommended that this list not exceed 10.
      </td>
      <td>
        array of strings
      </td>
      <td>
        "addl": ["cdkw7geqsh.skadnetwork", "qyjfv329m4.skadnetwork"]
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        Placeholder for exchange-specific extensions to OpenRTB.
      </td>
      <td>
        object
      </td>
      <td>
        "ext":{}
      </td>
    </tr>
  </tbody>
</table>


#### Example

Used for direct SSP to DSP connections where a DSP wants to only consume their own relevant SKAdNetwork IDs.

```
{
  "imp": [
    {
      "ext": {
        "skadn": {
          "versions": ["2.0", "2.1", "2.2", "3.0", "4.0"],
          "sourceapp": "880047117",
          "productpage": 1,
          "skadnetlist":{
              "max":306,
              "excl":[2,8,10,55],
              "addl": [
                "cdkw7geqsh.skadnetwork",
                "qyJfv329m4.skadnetwork"
              ]
          },
          "skoverlay": 1
        }
      }
    }
  ]
}
```

### Bid Response

If the bid request included the `BidRequest.imp.ext.skadn` object, then a DSP could choose to add the following object to their bid response. Please refer to Apple’s documentation for submitting the [correctly formatted values][4]. If the object is present in the response, then SSP would submit the click data and signature to [loadProduct()][7] for attribution.

**Note:** Due to breaking changes introduced by Apple in SKAdNetwork v2.2 to support [View Through Attribution and fidelity-type][14], several structural changes to the bid response were required to support multiple fidelity types.

#### Object: `BidResponse.seatbid.bid.ext.skadn`

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>version</code>
      </td>
      <td>
        Version of SKAdNetwork desired. Must be 2.0 or above.
      </td>
      <td>
        string
      </td>
      <td>
        "version": "4.0"
      </td>
    </tr>
    <tr>
      <td>
        <code>network</code>
      </td>
      <td>
        Ad network identifier used in signature. Should match one of the items in the skadnetids array in the request
      </td>
      <td>
        string
      </td>
      <td>
        "network": "cdkw7geqsh.skadnetwork"
      </td>
    </tr>
    <tr>
      <td>
        <code>sourceidentifier</code>
      </td>
      <td>
        A four-digit integer that ad networks define to represent the ad campaign. Used in SKAdNetwork 4.0+, replaces Campaign ID `campaign`. DSPs must generate signatures in 4.0+ using the Source Identifier. Please refer to the <a href="https://developer.apple.com/documentation/storekit/skadnetwork/skadnetwork_release_notes/skadnetwork_4_release_notes" target="_blank">SKAdNetwork 4 release notes</a> for more details.
      </td>
      <td>
        string
      </td>
      <td>
        "sourceidentifier": "4321"
      </td>
    </tr>
    <tr>
      <td>
        <code>campaign</code>
      </td>
      <td>
        Campaign ID compatible with Apple’s spec. As of 2.0, should be an integer between 1 and 100, expressed as a string. </br></br><strong>Note</strong>: Used in SKAdNetwork 3.0 and below. Replaced by Source Identifier <code>sourceidentifier</code> in 4.0 and above
      </td>
      <td>
        string
      </td>
      <td>
        "campaign": "45"
      </td>
    </tr>
    <tr>
      <td>
        <code>itunesitem</code>
      </td>
      <td>
        ID of advertiser’s app in Apple’s app store. Should match <code>BidResponse.seatbid.bid.bundle</code>
      </td>
      <td>
        string
      </td>
      <td>
        "itunesitem": "123456789"
      </td>
    </tr>
    <tr>
      <td>
        <code>productpageid</code>
      </td>
      <td>
        Custom Product Page ID (UUID)
      </td>
      <td>
        string
      </td>
      <td>
        "productpageid": "45812c9b-c296-43d3-c6a0-c5a02f74bf6e"
      </td>
    </tr>
    <tr>
      <td>
        <code>fidelities</code>
      </td>
      <td>
        Supports multiple fidelity types introduced in SKAdNetwork v2.2
      </td>
      <td>
        object array
      </td>
      <td>
        "fidelities": [
          {
             "fidelity": 0,
             "signature": "MEQCIEQlmZRNfYzK…",
             "nonce": "473b1a16…",
             "timestamp": "1594406341"
          }
        ]
      </td>
    </tr>
    <tr>
      <td>
        <code>nonce</code>
      </td>
      <td>
        An id unique to each ad response. Refer to Apple’s documentation for the <a href="https://developer.apple.com/documentation/storekit/skstoreproductparameteradnetworknonce">proper UUID format requirements</a>
        </br></br>
        <strong>Note</strong>: With the release of SKAdNetwork v2.2, this field is deprecated in favor of the <code>BidResponse.seatbid.bid.ext.skadn.fidelities.nonce</code> to support multiple fidelity-types.
      </td>
      <td>
        string
      </td>
      <td>
        "nonce": "473b1a16-b4ef-43ad-9591-fcf3aefa82a7"
      </td>
    </tr>
    <tr>
      <td>
        <code>sourceapp</code>
      </td>
      <td>
        ID of publisher’s app in Apple’s app store. Should match <code>BidRequest.imp.ext.skad.sourceapp</code>
      </td>
      <td>
        string
      </td>
      <td>
        "sourceapp": "880047117"
      </td>
    </tr>
    <tr>
      <td>
        <code>timestamp</code>
      </td>
      <td>
        Unix time in millis string used at the time of signature
        </br></br>
        <strong>Note</strong>: With the release of SKAdNetwork 2.2, this field is deprecated in favor of the <code>BidResponse.seatbid.bid.ext.skadn.fidelities.timestamp</code> to support multiple fidelity-types.
      </td>
      <td>
        string
      </td>
      <td>
        "timestamp": "1594406341232"
      </td>
    </tr>
    <tr>
      <td>
        <code>signature</code>
      </td>
      <td>
        SKAdNetwork signature as specified by Apple
        </br></br>
        <strong>Note</strong>: Apple requires that both the ad network nonce and ad network identifier be lowercase when signing for either fidelity type (impressions or clicks), as per SKAdNetwork specifications.
        </br></br>
        <strong>Note</strong>: With the release of SKAdNetwork 2.2, this field is deprecated in favor of the <code>BidResponse.seatbid.bid.ext.skadn.fidelities.signature</code> to support multiple fidelity-types.
      </td>
      <td>
        string
      </td>
      <td>
        "signature": "MEQCIEQlmZRNfYzK…"
      </td>
    </tr>
    <tr>
      <td>
        <code>skoverlay</code>
      </td>
      <td>
        <a href="https://developer.apple.com/documentation/storekit/skoverlay">SKOverlay</a> support.
      </td>
      <td>
        object
      </td>
      <td>
        "skoverlay": {"delay": 5, "endcarddelay": 0, "dismissible": 0, "pos": 1}
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        Placeholder for exchange-specific extensions to OpenRTB.
      </td>
      <td>
        object
      </td>
      <td>
        "ext": {}
      </td>
    </tr>
  </tbody>
</table>

#### Object: `BidResponse.seatbid.bid.ext.skadn.fidelities`

Fields that should have different values for the different fidelity types (e.g. `fidelity`, `nonce`, `signature`) are wrapped into an array of objects.

**Note:** Adding `timestamp` to this list allows bidders to parallelize the cryptography portions of creating their bid response when supporting multiple fidelities. The same timestamp can be used across fidelities if desired but this move provides bidders with greater implementation flexiblity.

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>fidelity</code>
      </td>
      <td>
        The fidelity-type of the attribution to track
      </td>
      <td>
        integer
      </td>
      <td>
        "fidelity": 0
      </td>
    </tr>
    <tr>
      <td>
        <code>nonce</code>
      </td>
      <td>
        An id unique to each ad response. Refer to Apple’s documentation for the <a href="https://developer.apple.com/documentation/storekit/skstoreproductparameteradnetworknonce">proper UUID format requirements</a>
      </td>
      <td>
        string
      </td>
      <td>
        "nonce": "473b1a16-b4ef-43ad-9591-fcf3aefa82a7"
      </td>
    </tr>
    <tr>
      <td>
        <code>timestamp</code>
      </td>
      <td>
        Unix time in millis string used at the time of signature
      </td>
      <td>
        string
      </td>
      <td>
        "timestamp": "1594406341"
      </td>
    </tr>
    <tr>
      <td>
        <code>signature</code>
      </td>
      <td>
        SKAdNetwork signature as specified by Apple
      </td>
      <td>
        string
      </td>
      <td>
        "signature": "MEQCIEQlmZRNfYzK…"
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        Placeholder for exchange-specific extensions to OpenRTB.
      </td>
      <td>
        object
      </td>
      <td>
        "ext": {}
      </td>
    </tr>
  </tbody>
</table>


**Note:** Apple also introduced `adtype`, `addescription`, and `adpurchasername` for fidelity-type 0 in v2.2. Until more clarity is provided by Apple about their use, these APIs have been intentionally omitted from the SKAdNetwork Extension.

#### Object: `BidResponse.seatbid.bid.ext.skadn.skoverlay`

Presents an [SKOverlay][17] StoreKit Ad during an ad experience using the `SKOverlay.AppConfiguration` Storekit API.

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>delay</code>
      </td>
      <td>
        Delay before presenting the SKOverlay in seconds. If set to 0, the overlay will be shown immediately. If this field is not set, the overlay will not be shown.
      </td>
      <td>
        integer
      </td>
      <td>
        "delay": 0
      </td>
    </tr>
    <tr>
      <td>
        <code>endcarddelay</code>
      </td>
      <td>
        Delay before presenting the SKOverlay on an endcard in seconds. If set to 0, the overlay will be shown immediately. If this field is not set, the overlay will not be shown.
      </td>
      <td>
        integer
      </td>
      <td>
        "endcarddelay": 0
      </td>
    </tr>
    <tr>
      <td>
        <code>dismissible</code>
      </td>
      <td>
        Whether the overlay can be dismissed by the user, where 0 = no, 1 = yes
      </td>
      <td>
        integer
      </td>
      <td>
        "dismissible": 0
      </td>
    </tr>
    <tr>
      <td>
        <code>pos</code>
      </td>
      <td>
        Position of the Overlay. 0 = bottom, 1 = bottomRaised
      </td>
      <td>
        integer
      </td>
      <td>
        "pos": 1
      </td>
    </tr>
    <tr>
      <td>
        <code>ext</code>
      </td>
      <td>
        Placeholder for exchange-specific extensions to OpenRTB.
      </td>
      <td>
        object
      </td>
      <td>
        "ext": {}
      </td>
    </tr>
  </tbody>
</table>

#### Example v4.0

```
{
  "seatbid": [
    {
      "bid": [
        {
          "ext": {
            "skadn": {
              "version": "4.0",
              "network": "cdkw7geqsh.skadnetwork",
              "sourceidentifier": "4321",
              "itunesitem": "123456789",
              "sourceapp": "880047117",
              "productpageid": "45812c9b-c296-43d3-c6a0-c5a02f74bf6e",
              "fidelities": [
                {
                  "fidelity": 0,
                  "signature": "MEQCIEQlmZRNfYzKBSE8QnhLTIHZZZWCFgZpRqRxHss65KoFAiAJgJKjdrWdkLUOCCjuEx2RmFS7daRzSVZRVZ8RyMyUXg==",
                  "nonce": "473b1a16-b4ef-43ad-9591-fcf3aefa82a7",
                  "timestamp": "1594406341"
                },
                {
                  "fidelity": 1,
                  "signature": "GRlMDktMmE5Zi00ZGMzLWE0ZDEtNTQ0YzQwMmU5MDk1IiwKICAgICAgICAgICAgICAgICAgInRpbWVzdGTk0NDA2MzQyIg==",
                  "nonce": "e650de09-2a9f-4dc3-a4d1-544c402e9095",
                  "timestamp": "1594406342"
                }
              ],
              "skoverlay":{
                "delay": 5,
                "endcarddelay": 0,
                "dismissible": 1,
                "pos": 1
              }
            }
          }
        }
      ]
    }
  ]
}
```

#### Example v2.2

```
{
  "seatbid": [
    {
      "bid": [
        {
          "ext": {
            "skadn": {
              "version": "2.2",
              "network": "cdkw7geqsh.skadnetwork",
              "campaign": "45",
              "itunesitem": "123456789",
              "sourceapp": "880047117",
              "productpageid": "45812c9b-c296-43d3-c6a0-c5a02f74bf6e",
              "fidelities": [
                {
                  "fidelity": 0,
                  "signature": "MEQCIEQlmZRNfYzKBSE8QnhLTIHZZZWCFgZpRqRxHss65KoFAiAJgJKjdrWdkLUOCCjuEx2RmFS7daRzSVZRVZ8RyMyUXg==",
                  "nonce": "473b1a16-b4ef-43ad-9591-fcf3aefa82a7",
                  "timestamp": "1594406341"
                },
                {
                  "fidelity": 1,
                  "signature": "GRlMDktMmE5Zi00ZGMzLWE0ZDEtNTQ0YzQwMmU5MDk1IiwKICAgICAgICAgICAgICAgICAgInRpbWVzdGTk0NDA2MzQyIg==",
                  "nonce": "e650de09-2a9f-4dc3-a4d1-544c402e9095",
                  "timestamp": "1594406342"
                }
              ]
            }
          }
        }
      ]
    }
  ]
}
```

#### Example v2.0

```
{
  "seatbid": [
    {
      "bid": [
        {
          "ext": {
            "skadn": {
              "version": "2.0",
              "network": "cdkw7geqsh.skadnetwork",
              "campaign": "45",
              "itunesitem": "123456789",
              "nonce": "473b1a16-b4ef-43ad-9591-fcf3aefa82a7",
              "sourceapp": "880047117",
              "timestamp": "1594406341232",
              "signature": "MEQCIEQlmZRNfYzKBSE8QnhLTIHZZZWCFgZpRqRxHss65KoFAiAJgJKjdrWdkLUOCCjuEx2RmFS7daRzSVZRVZ8RyMyUXg=="
            }
          }
        }
      ]
    }
  ]
}
```

### Loss Reason Code

Bid responses that contain invalid or malformed SKAdNetwork extensions may be rejected. This rejection can be communicated in loss notifications (lurl) using [Loss Reason Code][16] `214`.

<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Definition</strong></td>
  </tr>
  <tr>
    <td>214</td>
    <td>Creative Filtered - Invalid SKAdNetwork</td>
  </tr>
</table>

### SKAdNetwork Support Flow

![iOS 14 SKAdNetwork Flowchart][3]

_Flow diagram of SSP SDK’s SKAdNetwork support. Objects in blue have a change required to pre-iOS-14 ad flows._

1. SSP SDK retrieves the SKAdNetworkItems from the publisher app’s Info.plist
2. SDK makes ad request to ad server including SKAdNetworkItems
3. SSP determines from Info.plist which DSPs have SKAdNetwork capabilities. Bid request to eligible DSPs includes the imp.ext.skadn object, defined above
4. DSP responds, including `BidResponse.seatbid.bid.ext.skadn` if the campaign requires SKAdNetwork support
5. Ad response to SDK includes `skadn` object
6. If the impression is shown and the user clicks, SSP calls `loadProduct()` with the appropriate data, including the DSP-signed signature. If valid, Apple will consider the app for install attribution
7. Target app must register that user for SKAdNetwork attribution on app launch.
8. (Optional). Target app can choose to provide an additional 6 bits of conversion value information.
9. If SKAdNetwork determines that the DSP’s click led to the install, Apple will send a postback to the DSP’s registered endpoint with the ids of the source app, target app and campaign, and conversion value if provided by the target app.

## Device Extension

### Bid request

#### Object: `BidRequest.device.ext`

If the IDFA is not available, DSPs require an alternative, limited-scope identifier in order to provide basic frequency capping functionality to advertisers. The [IDFV][5] is the same for apps from the same vendor but different across vendors. Please refer to Apple's Guidelines for further information about when it can be accessed and used.

DSPs may also want to understand what is the status of a user on iOS 14+. The `atts` field will pass the AppTrackingTransparency Framework's [authorization status][6].

<table>
  <thead>
    <tr>
      <th>
        Attribute
      </th>
      <th>
        Description
      </th>
      <th>
        Type
      </th>
      <th>
        Example
      </th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        atts
      </td>
      <td>
        (iOS Only) An integer passed to represent the app's app tracking authorization status, where <br>
        0 = not determined <br>
        1 = restricted <br>
        2 = denied <br>
        3 = authorized
      </td>
      <td>
        integer
      </td>
      <td>
        "atts": 3
      </td>
    </tr>
    <tr>
      <td>
        ifv
      </td>
      <td>
        IDFV of the device in that publisher. Listed as ifv to match ifa field format.
      </td>
      <td>
        string
      </td>
      <td>
        "ifv": "336F2BC0-245B-4242-8029-83762AB47B15"
      </td>
    </tr>
  </tbody>
</table>

#### Example

```
{
  "device": {
    "ext": {
      "atts": 2,
      "ifv": "336F2BC0-245B-4242-8029-83762AB47B15"
    }
  }
}
```

### DNT, LMT and App Tracking Transparency Guidance

(Pending iOS 14 Golden Master) For iOS 14 and above, the 'DNT' and 'LMT' parameters will be informed by the 'ATTS' status, where
* "DNT" or "LMT" = 1 when "ATTS" = 0, 1, 2
* "LMT" or "DNT" = 0 when "ATTS" = 3


## IABTL managed SKAdnetwork ID list

The IABTL managed list of SKAdNetwork IDs is available at https://tools.iabtechlab.com/skadnetwork, and is intended to address the communication of large lists across complex programmatic supply chains where the use of `skadnetids` is not feasible.
* IABTL SKAdNetwork ID List is used to transmit a range of IDs supported by an IAB Tech Lab SKAdNetwork ID List up to the max ID. This list would serve a similar purpose that the TCF 2.0 [Global Vendor List][8] serves to identify common IDs in a compact range. Primary use case is "intermediary" SSP to SSP to DSP integrations where sending a subset of IDs is not feasible.


"IABTL SKAdNetwork ID List" is a common list of networks, DSPs, Advertisers and others who support Apple’s SKAdNetwork API. This list would serve a similar purpose that the TCF 2.0 [Global Vendor List][8] serves to identify common IDs in a compact range. It can be used in addition to the distributed lists supplied by SSPs and Networks or in place of those.

In the IABTL list model, the total list of SKAdNetworks on a device should be the union of the IABTL list and raw SKAdNetworks IDs supplied in the bid stream minus those in the exclude list


### IABTL SKAdNetwork ID Format

This list would use the same format as the [SKANetwork ID Lists for App Developers][9] with the possible addition of an "id" field for the JSON metadata that would autoincrement for each added SKAdNetwork ID. The details of how this list would be maintained (pull requests / submission, who would check and approve etc) are yet to be determined. We would like to first get feedback on the need for (and arguments against) such a centralized list.

#### Example


```
{
  "company_name": "IAB Tech Lab",
  "company_address": "116 East 27th Street, 7th Floor, New York, New York 10016",
  "company_domain": "iabtechlab.com",
  "skadnetwork_ids": [
    {
      "id": 1,
      "entity_name": "DSP1",
      "entity_domain": "DSP1.com",
      "skadnetwork_id": "4fzdc2evr5.skadnetwork",
      "creation_date": "2020-08-21T00:00:00Z"
    },
    {
      "id": 2,
      "entity_domain": "MMP1.com",
      "skadnetwork_id": "v72qych5uu.skadnetwork",
      "creation_date": "2020-08-25T00:00:00Z"
    }
  ]
}
```


## SKAdNetwork ID Lists for App Developers

SKAdNetwork ID Lists is a list of SKAdNetwork IDs published by a hosting company (e.g. SSP/SDK). App developers who work with the hosting company should use this file when generating a consolidated list of SKAdNetwork IDs to include in their app’s Info.plist file. For convenience, the SKAdNetwork IDs are provided in both XML and JSON formats. See each format for details and use cases.

<div style="background-color: #f3ddde; color: #a94443; border: 1px solid #a94443;">
<strong>Warning:</strong> SKAdNetwork IDs should be stored on the device (in info.plist) as lowercase even if received in upper case or mixed case characters. Failure to do so can result in postbacks to not occur, potentially causing a loss in revenue as advertiers shift spend away from inventory that does not result in any attribution.
</div>

### URL Path

Hosting companies should publish the SKAdNetwork ID files at the following locations:
* domain.com/skadnetworkids.xml
* domain.com/skadnetworkids.json

If the server response indicates an HTTP/HTTPS redirect (301, 302, 307 status codes), entities
should follow the redirect and consume the data as authoritative for the source of the redirect. Multiple redirects are valid.

### skadnetworkids.xml

The XML uses Apple’s Info.plist format. This provides developers with SKAdNetwork IDs in the exact SKAdNetworkItems array format for easy copy/paste into their Info.plist file.

#### File Format

All data in the file is serialized using XML (Extensible Markup Language)

#### Objects

Please refer to [Apple documentation][2] for details.

#### Example

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>SKAdNetworkItems</key>
    <array>
        <dict>
            <key>SKAdNetworkIdentifier</key>
            <string>4dzt52r2t5.skadnetwork</string>
        </dict>
        <dict>
            <key>SKAdNetworkIdentifier</key>
            <string>bvpn9ufa9b.skadnetwork</string>
        </dict>
    </array>
</dict>
</plist>
```

### skadnetworkids.json

The JSON provides developers with additional metadata about the SKAdNetwork IDs that they will add to the Info.plist SKAdNetworkItems array. Helpful data like the entity name will ensure app developers know who the owners of each SKAdNetwork ID.

#### File Format

All data in the file is serialized using JSON (JavaScript Object Notation)

#### Objects

##### Object: Parent top-level object

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>company_name</code>
      </td>
      <td>
        Name describing the hosting company
      </td>
      <td>
        string (recommended)
      </td>
      <td>
        SSP
      </td>
    </tr>
    <tr>
      <td>
        <code>company_address</code>
      </td>
      <td>
        The business address of the hosting company
      </td>
      <td>
        string
      </td>
      <td>
        SSP, 1234 Advertising Lane, San Francisco, CA 94121
      </td>
    </tr>
    <tr>
      <td>
        <code>company_domain</code>
      </td>
      <td>
        The business website of the hosting company
      </td>
      <td>
        string (required)
      </td>
      <td>
        company.com
      </td>
    </tr>
    <tr>
      <td>
        <code>skadnetwork_ids</code>
      </td>
      <td>
        List of SKAdNetwork IDs
      </td>
      <td>
        object array (required)
      </td>
      <td>
        [{ "entity_name": "DSP1", "entity_domain": "DSP1.com", "skadnetwork_id": "4fzdc2evr5.skadnetwork", "creation_date": "2020-08-21T00:00:00Z" }]
      </td>
    </tr>
  </tbody>
</table>

##### Object: skadnetwork_ids

<table>
  <thead>
    <tr>
      <td>
        <strong>Attribute</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
      <td>
        <strong>Type</strong>
      </td>
      <td>
        <strong>Example</strong>
      </td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>
        <code>entity_name</code>
      </td>
      <td>
        Name of entity with SKAdNetwork ID
      </td>
      <td>
        string (recommended)
      </td>
      <td>
        DSP Name
      </td>
    </tr>
    <tr>
      <td>
        <code>entity_domain</code>
      </td>
      <td>
        Domain of the entity with SKAdNetwork ID. For joint SKAdNetwork ID, entity owning Apple Developer account should be listed (this field may not be unique)
      </td>
      <td>
        string (recommended)
      </td>
      <td>
        DSP.com
      </td>
    </tr>
    <tr>
      <td>
        <code>skadnetwork_id</code>
      </td>
      <td>
        SKAdNetwork ad network ID (this field should be unique)
      </td>
      <td>
        string (required)
      </td>
      <td>
        4gwi8v3kwu.skadnetwork
      </td>
    </tr>
    <tr>
      <td>
        <code>creation_date</code>
      </td>
      <td>
        Date entity(s) added to JSON file
      </td>
      <td>
        String in ISO 8601 (YYYY-MM-DDThh:mm:ssZ)
      </td>
      <td>
        2020-08-21T00:00:00Z
      </td>
    </tr>
  </tbody>
</table>

#### Example

```
{
  "company_name": "SSP",
  "company_address": "SSP, address, country",
  "company_domain": "company.com",
  "skadnetwork_ids": [
    {
      "entity_name": "DSP1",
      "entity_domain": "DSP1.com",
      "skadnetwork_id": "4fzdc2evr5.skadnetwork",
      "creation_date": "2020-08-21T00:00:00Z"
    },
    {
      "entity_name": "MMP1",
      "entity_domain": "MMP1.com",
      "skadnetwork_id": "v72qych5uu.skadnetwork",
      "creation_date": "2020-08-25T00:00:00Z"
    }
  ]
}
```

### SDK Guidance

SDKs and/or app developers are encouraged to develop scripts to automate the process of retrieving the SKAdNetwork IDs from each SDK partner by parsing either the XML or JSON files to generate a complete list of IDs for their app’s Info.plist file. To aid in that process of ID retrieval from each SDK partner, include a file in your SDK named SKAdNetworks (with no extension), with each line in the file pointing to a publicly available SKAdNetworks.xml or SKAdNetworks.json.

#### Example

```
https://domain.com/skadnetworks.xml
https://domain.com/skadnetworks.json
```

## Changelog

* **[11/11/2023]**
    * Added support for SKOverlay in the Bid Request and Bid Response.
* **[11/16/2022]**
    * Updated for v4.0
    * Added `sourceidentifier` string to support SKAdNetwork 4.0 in the Bid Response.
    * Deprecated `campaign`, use `sourceidentifier` in 4.0
    * Added updated examples for 4.0 in Bid Request and Bid Response examples.
* **[10/19/2022]**
    * Added `productpage` for Bid Requests and `productpageid` for Bid Responses to support Apple's Custom Product Page
* **[03/01/2021]**
    * Updated for v2.2
    * Added `fidelities` object array to support multiple fidelity types in the Bid Response.
    * `signature`, `nonce` and `timestamp` moved to `fidelities` and deprecated root `skadn` versions of the values.
    * Added `fidelity` within `fidelities`
    * Removed Version Compatibility since IABTL approved of that proposal.
* **[02/01/2021]**
    * Updated for v2.1
    * Added `versions` for Bid Requests and deprecated `version`
    * Add guidance for lowercasing values based on Apple clarifications
* **[11/02/2020]**
    * Added `skadnetlist`
* **[09/16/2020]**
    * Original release for public comment



[1]: https://developer.apple.com/documentation/storekit/skadnetwork
[2]: https://developer.apple.com/documentation/storekit/skadnetwork/configuring_the_participating_apps
[3]: https://d2al1opqne3nsh.cloudfront.net/images/skadnetwork-flow@2x.png
[4]: https://developer.apple.com/documentation/storekit/skadnetwork/generating_the_signature_to_validate_an_installation
[5]: https://developer.apple.com/documentation/uikit/uidevice/1620059-identifierforvendor
[6]: https://developer.apple.com/documentation/apptrackingtransparency/attrackingmanager/authorizationstatus
[7]:  https://developer.apple.com/documentation/storekit/skstoreproductviewcontroller/1620632-loadproduct
[8]: https://github.com/InteractiveAdvertisingBureau/GDPR-Transparency-and-Consent-Framework/
[9]: #skadnetwork-id-lists-for-app-developers
[10]: #skadnetwork-extension
[11]: #device-extension
[12]: #skadnetwork-id-lists-for-app-developers
[13]: #proposals-for-large-skadnetwork-id-list-management
[14]: https://developer.apple.com/documentation/storekit/skadnetwork/generating_the_signature_to_validate_view-through_ads
[15]: #IABTL-managed-SKAdnetwork-ID-list
[16]: https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/OpenRTB%20v3.0%20FINAL.md#list--loss-reason-codes-
[17]: https://developer.apple.com/documentation/storekit/skoverlay
