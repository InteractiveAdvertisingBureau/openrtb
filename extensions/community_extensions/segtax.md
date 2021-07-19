# Segment Taxonomies

Sponsors: CafeMedia, Magnite (formerly Rubicon Project), PubMatic

## Overview

The goal of this change is to support publisher, proprietary, and standardized segments, with a simple structure that avoids significant repetitive text. This necessitates a means to communicate the applicable taxonomy which, in turn, necessitates a process for maintenance of the set of taxonomies.

## Request change

This extension provides a means to qualify the Segment IDs in Data objects, specifying the taxonomy for those IDs. For backward compatibility, segment IDs should (still) be considered fully proprietary when no `segtax` is provided.

### Specification <a name="object"></a>

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
        The ID for a taxonomy that is registered centrally, in a list maintained below.
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

#### Example

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
          { "id": "1001" },
          { "id": "1002" }
        ]
      }
    }
  }
}
```

### Taxonomies <a name="enum"></a>

#### Process

The aim of this process is to provide flexibility and quick turnaround on approvals without red tape (i.e., no dependency on IAB Tech Lab schedules or public comment periods):

* This document is the official definition of the enumeration for vendor specific taxonomies.
* The values for vendor specific taxonomies should be 500+ - as defined in [this AdCOM list](https://github.com/InteractiveAdvertisingBureau/AdCOM/blob/master/AdCOM%20v1.0%20FINAL.md#list_categorytaxonomies) and should not already exist in the table below.
* Pull requests (PRs) can be submitted by anyone on an ongoing basis.
* The approvers are amit@iabtechlab.com and/or ben@iabtechlab.com.
* The PR submitter must notify the approvers of the PR submission.
* The approvers will review for ID conflicts, and otherwise will approve the PR on the spot (i.e., no need for a formal Working Group review).

#### Enumeration of Vendor Specific Taxonomies

<table>
  <thead>
    <tr>
      <td>
        <strong>Value</strong>
      </td>
      <td>
        <strong>Description</strong>
      </td>
    </tr>
<tr>
      <td>
        <strong>501</strong>
      </td>
      <td>
        <strong>Iris video taxonomy. More info - <a href="https://blog.iris.tv/video-programming-playbook-metadata-taxonomy">https://blog.iris.tv/video-programming-playbook-metadata-taxonomy</a> </strong>
      </td>
    </tr>
  </thead>
  <tbody>
<!--
    <tr>
      <td>
        Value
      </td>
      <td>
        Description
      </td>
    </tr>
-->
  </tbody>
</table>
