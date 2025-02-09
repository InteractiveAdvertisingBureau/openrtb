# Alternate BCAT Taxonomy Blocks
Sponsor: Cafemedia, Mediavine

## Background

The popularity of [IAB Segment Taxonomy 1](https://iabtechlab.com/standards/content-taxonomy/) for banned advertiser categories seems difficult to transition off of, even though it is deprecated and its use is recommended against by the IAB. Ads receive only the legacy taxonomy category and publishers must express their blocks in it or risk completely ineffective blocks. 

The legacy categories for example, do not have a tobacco advertiser category, a payday loans category, an online gambling category, nor a pharmaceutical drugs category. Some conventions have emerged, such as banning cigars meaning banning all tobacco, but this is not widely publicized. A publisher wishing to ban payday loans may need to ban all banks from advertising on their site. 

The taxonomy migration problem has already been solved in publisher content categorization, with publishers or exchanges able to specify content categories on multiple simultaneous taxonomies. Blocks need the same flexibility to enable a transition. The bcat taxonomy field, `cattax`, while well intentioned, is ineffective because there is no current path to its use. 

Entities managing ads for multiple domains may have a large variety of advertiser category tolerences across those domains. For example, a magazine company may wish to block car ads on its car magazine website and alcohol ads on its children's magazine. Frequently, SSP backends accept BCATs per account, and allow request based overrides. Other SSP backends accept BCATs per account, and allow request based appends. It would also be quite useful to be able to communicate if blocked categories should be appended to or replace an existing list. 

## Request Changes

<table>
  <tr>
    <th>Site Object</th>
    <th>Type</th>
    <th>Example values</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>site.ext.bcat.segtax</td>
    <td>integer</td>
    <td>6</td>
    <td>A taxonomy enumerated in the [segtax extension](https://raw.githubusercontent.com/InteractiveAdvertisingBureau/openrtb/blob/master/extensions/community_extensions/segtax.md)</td>
  </tr>
  <tr> 
    <td>site.ext.bcat.ltype</td>
    <td>integer</td>
    <td>1</td>
    <td>1 represents replace, 2 represents append</td>
  </tr> 
  <tr>
    <td>site.ext.bcat.segment.id</td> 
    <td>string</td>
    <td>"324"</td>
    <td>Identifies a segment in the segment taxonomy</td>
  </tr>
</table>

The following example illustrates the usage of the new field in `BidRequest.site`, though the equivalent applies to other appearances of the same structure, e.g., in `BidRequest.app`. Below, one blocks advertising in both the IAB Product Taxonomy and the Content Taxonomy 3. 

```
{ ...,
"site": { "ext": { "bcat": [
                         { "segtax": 3, "ltype": 1, "segment": [ { "id": "1001" }, { "id": "1002" } ]},
                         { "segtax": 7, "ltype": 1, "segment": [ { "id": "1003" }, { "id": "1004" } ]}
                         ],
...,             }
         }
}
 ```

## How To Use This Extension

### If the advertiser category is known within any `site.ext.bcat.segtax`
Ignore `bcat` and `cattax` and prefer these taxonomies when responding to bid requests. If ltype is 2, append the list in the bid request to any stored list associated with that requestor. 

### If the advertiser category is unknown in all `site.ext.bcat.segtax`
Fallback to top level `bcat` and `cattax`
