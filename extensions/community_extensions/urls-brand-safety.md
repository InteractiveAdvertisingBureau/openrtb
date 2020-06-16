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
  <td>content.ext.feedurls</td>
  <td>string array</td>
  <td>{ }</td>
  <td>An array of URLs of content that are _directly_ adjacent to the ad placement, for buy-side contextualization or review. For example, in a typical vertical feed, there would be 2 URLs provided as adjacent to the ad (above & below). In a 9-tile example, there would be 8 URLs provided. </br> 
   Note : the URL to the main page (in which the feed and the ad appear) in the case of web content should be provided in the content.url field.</td>
 </tr>
</table>

Example Request

```
{
 "content":{
  "ext":{
   "feedurls": [ "https://www.puba.com/content1.html", "https://www.pubb.com/content2", "https://www.pubc.com/content3/abc" ]
  }
 }
}
```
