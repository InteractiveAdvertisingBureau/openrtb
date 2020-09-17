# SKAdNetwork

Sponsors: MoPub, Fyber

## Overview

The IAB Tech Lab has introduced technical specifications aimed at adapting Apple’s [SKAdNetwork][1], a method for validating advertiser app installations, for programmatic ad buying. The OpenRTB object extensions, APIs and file formats described in this document together enable the advertising ecosystem to communicate and manage the information needed to use the SKAdNetwork capabilities in iOS 14 and above.

The following are the updates provided in this document
1. A [SKAdNetwork extension][10] to support programmatic buying
   * Bid Request extension (`BidRequest.imp.ext.skadn`)
   * Bid Response extension (`BidResponse.seatbid.bid.ext.skadn`)
2. A [Device extension][11] (`BidRequest.device.ext`) to support IDFV and authorization status
3. Guidance for app developers to help [manage their Info.plists][12] and work with various SDKs.
4. A [request for feedback][13] on what we are considering for the future - more efficient options to communicate large lists (over 10) of SKAdNetwork IDs.
   * Separate SSP managed SKAdNetwork ID lists + APIs for mapping, and standardized hash to be passed on the bid request or
   * Tech Lab managed "common list" for SKAdNetwork IDs with universal assigned range ID values per SKAdNetwork to be passed on the bid request

## SKAdNetwork Extension

### Participant responsibilities

The responsibilities of each participant when using the SKAdNetwork specifications are as follows.

#### SSP/SDK responsibilities are to:

1. Provide publishers with access to their buying entities SKAdNetwork IDs through a publicly hosted lists on their own business domain
2. Support OpenRTB extension objects: `BidRequest.imp.ext.skadn` & `BidResponse.imp.ext.skadn`
3. Provide signed ads to the source app by calling `loadProduct()` with the appropriate data returned on the bid response

#### DSP/intermediary/buying entities responsibilities are to:

1. Provide SKAdNetwork IDs to each supply partner
2. Support OpenRTB extension objects: `BidRequest.imp.ext.skadn` & `BidResponse.imp.ext.skadn`
3. Return all necessary signed parameters to SSP/SDK to facilitate ad signatures and receive install validation postbacks at endpoint established during SKAdNetwork registration with Apple

#### Publishers/source app’s responsibilities are to:

1. Add the ad network’s ID to its Info.plist
2. Update Info.plist with new entries added to the SSP/SDK publicly hosted lists when publishing new app versions to the App Store

### Regulatory Guidance

OpenRTB implementations will need to ensure compliance on every transaction with all applicable regional legislation.

### Bid request

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
        <code>version</code>
      </td>
      <td>
        Version of skadnetwork supported. Always "2.0" or higher. Dependent on both the OS version and the SDK version.
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
        A subset of SKAdNetworkItem entries in the publisher app’s Info.plist that are relevant to the DSP. Recommended that this list not exceed 10.
      </td>
      <td>
        array
      </td>
      <td>
        "skadnetids": ["cDkw7geQsH.skadnetwork", "qyJfv329m4.skadnetwork"]
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

#### Example

Used for direct SSP to DSP connections where a DSP wants to only consume their own relevant SKAdNetwork IDs.

```
{
  "imp": [
    {
      "ext": {
        "skadn": {
          "version": "2.0",
          "sourceapp": "880047117",
          "skadnetids": [
            "cDkw7geQsH.skadnetwork",
            "qyJfv329m4.skadnetwork"
          ]
        }
      }
    }
  ]
}
```

### Bid response

If the bid request included the `BidRequest.imp.ext.skadn` object, then a DSP could choose to add the following object to their bid response. Please refer to Apple’s documentation for submitting the [correctly formatted values][4]. If the object is present in the response, then SSP would submit the click data and signature to [loadProduct()][7] for attribution.

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
        "version": "2.0"
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
        "network": "cDkw7geQsH.skadnetwork"
      </td>
    </tr>
    <tr>
      <td>
        <code>campaign</code>
      </td>
      <td>
        Campaign ID compatible with Apple’s spec. As of 2.0, should be an integer between 1 and 100, expressed as a string
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

#### Example

```
{
  "seatbid": [
    {
      "bid": [
        {
          "ext": {
            "skadn": {
              "version": "2.0",
              "network": "cDkw7geQsH.skadnetwork",
              "campaign": "45",
              "itunesitem": "123456789",
              "nonce": "473b1a16-b4ef-43ad-9591-fcf3aefa82a7",
              "sourceapp": "880047117",
              "timestamp": "1594406341",
              "signature": "MEQCIEQlmZRNfYzKBSE8QnhLTIHZZZWCFgZpRqRxHss65KoFAiAJgJKjdrWdkLUOCCjuEx2RmFS7daRzSVZRVZ8RyMyUXg=="
            }
          }
        }
      ]
    }
  ]
}
```

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
      <td class="text-monospace">
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
      <td class="text-monospace">
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

## SKAdNetwork ID Lists for App Developers

SKAdNetwork ID Lists is a list of SKAdNetwork IDs published by a hosting company (e.g. SSP/SDK). App developers who work with the hosting company should use this file when generating a consolidated list of SKAdNetwork IDs to include in their app’s Info.plist file. For convenience, the SKAdNetwork IDs are provided in both XML and JSON formats. See each format for details and use cases.

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
    <key>SKAdNetworkItems</key>
    <array>
        <dict>
            <key>SKAdNetworkIdentifier</key>
            <string>4DZT52R2T5.skadnetwork</string>
        </dict>
        <dict>
            <key>SKAdNetworkIdentifier</key>
            <string>bvpn9ufa9b.skadnetwork</string>
        </dict>
    </array>
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
        [{ "entity_name": "DSP1", "entity_domain": "DSP1.com", "skadnetwork_id": "4FZDC2EVR5.skadnetwork", "creation_date": "2020-08-21T00:00:00Z" }]
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
        4GWI8V3KWU.skadnetwork
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
      "skadnetwork_id": "4FZDC2EVR5.skadnetwork",
      "creation_date": "2020-08-21T00:00:00Z"
    },
    {
      "entity_name": "MMP1",
      "entity_domain": "MMP1.com",
      "skadnetwork_id": "V72QYCH5UU.skadnetwork",
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

## Proposals for Large SKAdNetwork ID List Management

Note: The below sections are still in development. The IAB Tech Lab is looking for additional comments on these proposals.

These proposals attempt to tackle how to transmit large lists of SKAdNetwork IDs across more complex programmatic supply chains where the use of `skadnetids` is not feasible.
* [SKAdNetwork Hash List Proposal][14]: Used to transmit a hash ID that can be tied back to a full list in a hash table via `skadnhash` and `hashdomain`. Primary use case is “intermediary” SSP to SSP to DSP integrations where sending a subset of IDs is not feasible.
* [IABTL SKAdNetwork ID List Proposal][15]: Used to transmit a range of IDs supported by an IAB Tech Lab SKAdNetwork ID List via `skadnrng`. This list would serve a similar purpose that the TCF 2.0 [Global Vendor List][8] serves to identify common IDs in a compact range. Primary use case is “intermediary” SSP to SSP to DSP integrations where sending a subset of IDs is not feasible.

## SKAdNetwork Hash List Proposal

Used to transmit a hash ID that can be tied back to a full list in a hash table. Primary use case is “intermediary” SSP to SSP to DSP integrations where sending a subset of IDs is not feasible.

SSP/SDK retrieves a list of all SKAdNetwork IDs and generates a standardized hash of the values. These hashes would be identical across SSP/SDK networks. A standardized list can easily be generated from the client device, simplifying transmission for prebid or other use cases.

### Participant responsibilities

The responsibilities of each participant when using the SKAdNetwork specifications are as follows (in addition to the responsibilities at the beginning of this document).

#### SSP/SDK responsibilities are to:

4. (Optional) Assist intermediary buyers with accessing the full list SKAdNetwork IDs on the bid request, provided in a compact hash format `skadnhash` and a `hashdomain` where the full list of SKAdNetwork IDs for that list can be retrieved
5. Provide an API (separate from the bid request) where buyers can access the mapping of SKAdNetwork IDs to `skadnhash`

#### DSP/intermediary/buying entities responsibilities are to:

4. (Optional) Pull in SKADNetwork hash tables from each SSP/SDK API separately, if ingesting `skadnhash` and `hashdomain` values

### Generating the SKAdNetwork standard hash

1. Pull all SKAdNetwork IDs from Info.plist
2. String to lower (assumes Apple SKAdNetwork IDs are case insensitive, checking with Apple to confirm)
3. Sort Ascending
4. Dedupe
5. Concatenate comma-separated (no whitespace)
6. Use SHA256 function to hash IDs

#### Example

Grab list from Info.plist:
```
2U9PT9HC89.skadnetwork
4FZDC2EVR5.skadnetwork
7UG5ZH24HU.skadnetwork
mlmmfzh3r3.skadnetwork
T38B2KH725.skadnetwork
W9Q455WK68.skadnetwork
YCLNXRL5PM.skadnetwork
```

Process list:
```
2U9PT9HC89.skadnetwork,4FZDC2EVR5.skadnetwork,7UG5ZH24HU.skadnetwork,mlmmfzh3r3.skadnetwork,T38B2KH725.skadnetwork,W9Q455WK68.skadnetwork,YCLNXRL5PM.skadnetwork
```

Output using SHA256:
```
93f901d8b1cc722e48d6bbe46f2e4ce38fb851f857131fa429eaed2489453b52
```

Looking up the above hash in the lookup table (provided for static download/API from SSP) will yield the list:
```
2U9PT9HC89.skadnetwork,4FZDC2EVR5.skadnetwork,7UG5ZH24HU.skadnetwork,mlmmfzh3r3.skadnetwork,T38B2KH725.skadnetwork,W9Q455WK68.skadnetwork,YCLNXRL5PM.skadnetwork
```

### Retrieving the SKAdNetwork Hash List

SSP/SDKs make their own separate list of hashes for each app version available to DSPs through two methods:

#### Method 1

List of hashes may be retrieved from API request calls from the SSP/SDK. API has the option for buying entities to only query data relevant to them. SSP/SDK to make API documentation available for their own platform.

API Endpoint:
domain.com/skadnetwork?hash=HASHID

File Format: JSON
Recommended Data Format:

```
{"hash": "key1", "skadnetwork_ids":["id1", "id2"]}
```

#### Method 2

List of hashes may be pulled from a static file hosted at the SSP/SDKs designated location. SSPs/SDKs should use a standard file format for listing the hashes in the static file, as described below

File Format: Text File
Data Format:

```
{"hash": "key1", "skadnetwork_ids":["id1", "id2"]}
{"hash": "key2", "skadnetwork_ids":["id1", "id3"]}
```

#### Best Practices

It is recommended that the list of hashes are updated with new app versions data at least once per hour. It is recommended that buying entities refresh their data at minimum every 12 hours and at maximum every 1 hour.

### Bid request

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
        <code>skadnhash</code>
      </td>
      <td>
        A hash of the full list of SKAdNetworkItem entries. Hash table will be provided outside the bidstream for DSPs to consume. See <a href="#generating-the-skadnetwork-standard-hash">SKAdNetwork IDs Hash</a> for more details.
      </td>
      <td>
        string
      </td>
      <td class="text-monospace">
        "93f901d8b1cc722e48d6bbe46f2e4ce38fb851f857131fa429eaed2489453b52"
      </td>
    </tr>
    <tr>
      <td>
        <code>hashdomain</code>
      </td>
      <td>
        The domain of the entity where the hash list can be retrieved. Should be sent whenever <code>skadnhash</code> is present in the bid request, so DSPs can identify where to look up the corresponding hash list.
      </td>
      <td>
        string
      </td>
      <td class="text-monospace">
        "example.com"
      </td>
    </tr>
  </tbody>
</table>

#### Example for `skadnhash`

Used for intermediary SSP to SSP/DSP to DSP connections where a full list SKAdNetwork IDs is required. Provided in a compact hash format. See <a href="#generating-the-skadnetwork-standard-hash">SKAdNetwork IDs Hash</a> for more details.

```
{
  "imp": [
    {
      "ext": {
        "skadn": {
          "version": "2.0",
          "sourceapp": "880047117",
          "skadnhash": "93f901d8b1cc722e48d6bbe46f2e4ce38fb851f857131fa429eaed2489453b52",
          "hashdomain": "example.com"
        }
      }
    }
  ]
}
```

## IABTL SKAdNetwork ID List Proposal

“IABTL SKAdNetwork ID List” is a common list of networks, DSPs, Advertisers and others who support Apple’s SKAdNetwork API. This list would serve a similar purpose that the TCF 2.0 [Global Vendor List][8] serves to identify common IDs in a compact range. It can be used in addition to the distributed lists supplied by SSPs and Networks or in place of those.

### Participant responsibilities

The responsibilities of each participant when using the SKAdNetwork specifications are as follows (in addition to the responsibilities at the beginning of this document).

#### SSP/SDK responsibilities are to:

4. (Optional) Assist intermediary buyers with accessing the range of IABTL supported SKAdNetwork IDs on the bid request, provided in a compact range format `skadnrng` where the full list of support SKAdNetwork IDs for that list can be retrieved

#### DSP/intermediary/buying entities responsibilities are to:

4. (Optional) Pull in the latest IABTL SKADNetwork List if ingesting the `skadnrng` value to understand which values in the range are useful for the DSP

### IABTL SKAdNetwork ID Format

This list would use the same format as the [SKANetwork ID Lists for App Developers][9] with the possible addition of an "id" field for the JSON metadata that would autoincrement for each added SKAdNetwork ID. The details of how this list would be maintained (pull requests / submission, who would check and approve etc) are yet to be determined. We would like to first get feedback on the need for (and arguments against) such a centralized list.

#### Example

```
{
  "company_name": "SSP",
  "company_address": "SSP, address, country",
  "company_domain": "company.com",
  "skadnetwork_ids": [
    {
      "id": 1,
      "entity_name": "DSP1",
      "entity_domain": "DSP1.com",
      "skadnetwork_id": "4FZDC2EVR5.skadnetwork",
      "creation_date": "2020-08-21T00:00:00Z"
    },
    {
      "id": 2,
      "entity_domain": "MMP1.com",
      "skadnetwork_id": "V72QYCH5UU.skadnetwork",
      "creation_date": "2020-08-25T00:00:00Z"
    }
  ]
}
```

### Generating the SKAdNetwork range

The SDK would be responsible for pulling the SKAdNetwork IDs listed on each app’s info.plist IDs and mapping them back to their corresponding "id" value from the IABTL List in order to generate the range.

### Bid request

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
        <code>skadnrng</code>
      </td>
      <td>
        A range of the full list of SKAdNetworkItem entries as defined by an IABTL SKAdNetwork ID List.
      </td>
      <td>
        string
      </td>
      <td class="text-monospace">
        Placeholder
      </td>
    </tr>
  </tbody>
</table>

#### Example

Used for intermediary SSP to SSP/DSP to DSP connections where support for the IAB Tech Lab’s SKAdNetwork ID List is required. Provided in a compact range format.

```
{
  "imp": [
    {
      "ext": {
        "skadn": {
          "version": "2.0",
          "sourceapp": "880047117",
          "skadnrng": "Placeholder for structure"
        }
      }
    }
  ]
}
```

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
[14]: #skadnetwork-hash-list-proposal
[15]: #iabtl-skadnetwork-id-list-proposal
