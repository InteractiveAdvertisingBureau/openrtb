# Segment Taxonomies

Sponsors: CafeMedia, Magnite (formerly Rubicon Project), PubMatic

## Overview

The goal of this change is to support publisher, proprietary, and standardized segments, with a simple structure that avoids significant repetitive text.

## Request change

This extension provides a means to qualify the Segment IDs in Data objects, specifying the taxonomy for those IDs. For backward compatibility, segment IDs should (still) be considered fully proprietary when no `segtax` is provided.

### Specification

#### Object: `Data.ext`

Per the OpenRTB 2.x API, the Data and Segment objects together allow additional data about the related object (e.g., User, Content) to be specified. The Data object can appear in several places in an OpenRTB 2.x request, and the same extension is supported in all cases.

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
        <code>segtax</code>
      </td>
      <td>
        The ID for a taxonomy that is registered centrally, in a list maintained by the IAB.
      </td>
      <td>
        integer
      </td>
      <td>
        <code>3</code>
      </td>
    </tr>
  </tbody>
</table>

### Example

The following example illustrates the usage of the new field in `BidRequest.user.data`, though the equivalent applies to other appearances of the same structure, e.g., in `BidRequest.app.content.data` and `BidRequest.site.content.data`.

```
{
  ...,
  "user": {
    "data": [
      {
        "name": "a-data-provider.com",
        "ext": {
          "segtax": 3
        },
        "segment": [
          { "id": 1001 }, 
          { "id": 1002 }
        ]
      }
    }
  }
}
```
