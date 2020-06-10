### URLs for Brand Safety

Issue: [#39](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/39)

Sponsor: Flipboard

The goal is to support brand safety in feed type environments where ads may appear between links (or previews) of various other pieces of content.

Request Changes

<table>
 <tr>
  <th>Content Object</th>
  <th>Type</th>
  <th>Example values</th>
  <th>Description</th>
 </tr>
 <tr>
  <td>content.ext.urls</td>
  <td>string array</td>
  <td>{ }</td>
  <td>An array of URLs of content that may be adjacent to the ad placement, for buy-side contextualization or review.</td>
 </tr>
</table>

Example Request

```
{
 "content":{
  "ext":{
   "urls": [ "https://www.puba.com/content1.html", "https://www.pubb.com/content2", "https://www.pubc.com/content3/abc" ]
  }
 }
}
```
