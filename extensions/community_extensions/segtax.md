# Segment Taxonomies

Sponsors: CafeMedia, Magnite (formerly Rubicon Project), PubMatic, Audigent

## Overview

The goal of this change is to support publisher, proprietary, and standardized segments, with a simple structure that avoids significant repetitive text. This necessitates a means to communicate the applicable taxonomy which, in turn, necessitates a process for maintenance of the set of taxonomies.

## Request change

This extension provides a means to qualify the Segment IDs in Data objects, specifying the taxonomy for those IDs. For backward compatibility, segment IDs should (still) be considered fully proprietary when no `segtax` is provided.

### Specification <a name="object"></a>

#### Object: `Data.ext`https://docs.prebid.org/features/firstPartyData.html

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
* The values for vendor specific taxonomies should be 500+
* Pull requests (PRs) can be submitted by anyone on an ongoing basis.
* The approvers are jill@iabtechlab.com and/or ben@iabtechlab.com and some other leaders on the Tech Lab Working Group if needed.
* The PR submitter must notify the approvers of the PR submission.
* The approvers will review for ID conflicts, and otherwise will approve the PR on the spot (i.e., no need for a formal Working Group review).

#### Enumeration of Taxonomies

##### Standard Taxonomies 
Source : AdCOM [https://github.com/InteractiveAdvertisingBureau/AdCOM/blob/master/AdCOM%20v1.0%20FINAL.md#list_categorytaxonomies](https://github.com/InteractiveAdvertisingBureau/AdCOM/blob/master/AdCOM%20v1.0%20FINAL.md#list_categorytaxonomies).


<table>
  <tr>
    <td><strong>Value</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td>1</td>
    <td>IAB Tech Lab Content Category Taxonomy 1.0. - Deprecated, and recommend NOT be used since it does not have SCD flags. </td>
  </tr>
  <tr>
    <td>2</td>
    <td>IAB Tech Lab Content Category Taxonomy 2.0:  Deprecated, and recommend NOT be used since it does not have SCD flags.</td>
  </tr>
  <tr>
    <td>3</td>
    <td> <a href="https://iabtechlab.com/wp-content/uploads/2020/10/IABTL-Ad-Product-Taxonomy-1.0-Final.xlsx">IAB Tech Lab Ad Product Taxonomy 1.0.</A> </td>
  </tr>
  <tr>
    <td>4</td>
    <td><a href="https://iabtechlab.com/standards/audience-taxonomy/">IAB Tech Lab Audience Taxonomy 1.1</a></td>
  </tr>
  <tr>
    <td>5</td>
    <td><a href="https://iabtechlab.com/standards/content-taxonomy/">IAB Tech Lab Content Taxonomy 2.1</a></td>
  </tr>
    <tr>
    <td>6</td>
    <td><a href="https://iabtechlab.com/standards/content-taxonomy/">IAB Tech Lab Content Taxonomy 2.2</a></td>
  </tr>
 <tr>
    <td>7</td>
    <td><a href="https://iabtechlab.com/standards/content-taxonomy/">IAB Tech Lab Content Taxonomy 3.0</a></td>
  </tr>

  <tr>
    <td>500+</td>
    <td>Vendor-specific codes.</td>
  </tr>
</table>


##### Approved Vendor specific Taxonomies 
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
  </thead>
  <tbody>
    <tr>
      <td>501</td>
      <td>
        IRIS.TV-enabled Video context taxonomy - More info - <a href="support.iris.tv">https://support.iris.tv</a>
      </td>
    </tr>
    <tr>
      <td>502</td>
      <td>JW Player video content taxonomy</td>
    </tr>
    <tr>
      <td>503</td>
      <td>Akamai Data Activation Platform (DAP) - Buyer Defined Audiences (BDA), Scrambled</td>
    </tr>
    <tr>
      <td>504</td>
      <td>Akamai Data Activation Platform (DAP) - Buyer Defined Audiences (BDA), Encrypted TRUSTX Spectrum Custom Audiences</td>
    </tr>
    <tr>
      <td>505</td>
      <td>Akamai Data Activation Platform (DAP) - Custom Audiences, Reserved 1</td>
    </tr>
    <tr>
      <td>506</td>
      <td>Akamai Data Activation Platform (DAP) - Custom Audiences, Reserved 2</td>
    </tr>
    <!-- 1plusX -->
    <tr>
      <td>525</td>
      <td>1plusX - Taxonomies</td>
    </tr>
    <tr>
      <td>526</td>
      <td>1plusX - Custom Audience Taxonomy</td>
    </tr>
    <tr>
      <td>527</td>
      <td>1plusX - Custom Topics Taxonomy</td>
    </tr>
    <tr>
      <td>528 - 540</td>
      <td>1plusX - Custom Taxonomies Reserved range</td>
    </tr>
    <!--  -->
    <tr>
      <td>543</td>
      <td>CafeMedia Custom Audience Taxonomy</td>
    </tr>
    <tr>
      <td>544</td>
      <td>CafeMedia Custom Content Taxonomy</td>
    </tr>
    <tr>
      <td>600</td>
      <td>Chromium Topics API taxonomy</td>
    </tr>
    <tr>
      <td>800</td>
      <td>NumberEight Audience Taxonomy</td>
    </tr>
    <tr>
      <td>5000</td>
      <td>Optable Data Collaboration Platform - Public Audiences</td>
    </tr>
    <tr>
      <td>5001</td>
      <td>Optable Data Collaboration Platform - Private Member Defined Audiences</td>
    </tr>
    <tr>
      <td>5002</td>
      <td>Optable Data Collaboration Platform - Test Audiences</td>
    </tr>
    <tr>
      <td>103000</td>
      <td>
        Audigent Warner Music Group Artists Taxonomy 1.0
      </td>
    </tr>
    <tr>
      <td>103001</td>
      <td>
        Audigent Bands in Town Artists Taxonomy 1.0
      </td>
    </tr>
    <tr>
      <td>103002</td>
      <td>
        Audigent Fandom Interests & Audiences Taxonomy 1.0
      </td>
    </tr>
    <tr>
      <td>103003</td>
      <td>
        Audigent Big Machine Label Group Artists Taxonomy 1.0
      </td>
    </tr>
    <tr>
      <td>103004</td>
      <td>
        Audigent Music Festival Partner Taxonomy 1.0
      </td>
    </tr>
    <tr>
      <td>103005</td>
      <td>
        Audigent Fashion & Apparel Taxonomy 1.0
      </td>
    </tr>
    <tr>
      <td>103006-103008</td>
      <td>
        Audigent Private Audience Taxonomies
      </td>
    </tr>
    <tr>
      <td>103009-103011</td>
      <td>
        Audigent Private Contextual Taxonomies
      </td>
    </tr>
    <tr>
      <td>103012-103014</td>
      </td>
      <td>
        Audigent Campaign Taxonomies
      </td>
    </tr>
  </tbody>
</table>
