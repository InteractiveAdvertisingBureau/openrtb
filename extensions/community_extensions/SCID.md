<h1>Cross-platform campaign identification : SCID (Shared Campaign Identifier)</H1>

Sponsors: Smart Adserver, Xandr

<h2>Version History</h2>

| Date | Version | Comments |
| :-- | :-- | :-- |
| Sept 2021 | 1.0 | Added use of alternatives to GLN as Advertiser ID  |
| June 2021 | 1.0 | Trust.id name changes to SCID (Shared Campaign Identifier)  |


<h2>Overview</h2>

By using <i>SCID</i>, <strong>Advertisers and Publishers will get more transparency and tracking for their programmatic buying and selling</strong>.

Several advertising associations, representing all stakeholders involved in the buying process, including <strong>Edipub</strong>, stand together to make sure to find consensual solutions, to meet compliance and to define best practices, in responses to transparency requirements.
<strong>SCID: A worldwide approach for a shared need</strong>.

<h3>Problem statement</h3>
Lack of transparency in campaigns,where publishers or advertisers cannot track campaigns effectively.

<h2>Definition</h2>
<strong>SCID</strong> is a key enabling identification and tracking throughout a campaign lifecycle. The goal is to keep following a campaign, provide more transparency, and bring the links in the programmatic chain closer during the reporting operations.
<br><br>
The trading desk is used to working with many DSPs. This unique and dedicated <strong>SCID</strong> field helps to efficiently track the campaign, regardless of the DSPs and SSPs used.

<h2>Who is involved?</h2>

- Advertisers
- Trading desks (independent and agencies / disclosed and undisclosed)
- DSP
- SSP
- Publishers (direct or through saleshouses)

<h2>The genuine specification</h2>

```
SCID = Advertiser ID + Advertiser brand + Advertiser product code + Customer Product Estimate + Ext (optional) 
```

See below the description of each piece from the genuine specification :

- <strong>Advertiser ID</strong> : It contains the acronym or name of the registry and the registration number which is an existing international ID used to identify a company (advertiser). For example, GLN-123 or SIRET-456. 
<br>As an identifier, an advertiser can have a [GLN](https://en.wikipedia.org/wiki/Global_Location_Number) and/or a [DUNS](https://en.wikipedia.org/wiki/Data_Universal_Numbering_System) and/or a [SIRET](https://en.wikipedia.org/wiki/SIRET_code)/[SIREN](https://en.wikipedia.org/wiki/SIREN_code) or can use any other registry as long as this registration number is reputed unique.
<br><br>Below is an example of a request using GLN. The registry of GLNs is managed by [GEPIR](https://gepir.gs1.org/) (Global Electronic Party Information Registry) which allows identification of a company thanks to its GLN :<br>![Search](https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/extensions/community_extensions/assets/trustid_gln_search.png)<br><br>Result :<br>![Result](https://github.com/InteractiveAdvertisingBureau/openrtb/blob/master/extensions/community_extensions/assets/trustid_gln_result.png)

- <strong>Advertiser brand, Advertiser product code and Customer Product Estimate</strong> are defined at the advertiser's discretion.

<h3>Example with a media brief from Nestlé</h3>

The trading desk Publicis has a campaign with Nestlé for the Olympics Games in 2020 :
- <strong>Advertiser ID</strong> = GLN-0050000000951
- <strong>Advertiser Brand</strong> = Nestlé <br><strong>Note</strong> : We do not use the adomain field in OpenRTB because a lot of different adomains are created and linked to the same brand. Allowing the buyer to put the advertiser brand from the media brief will help to give more transparency about the campaign.
- <strong>Advertiser Product</strong> = OG2020
- <strong>Customer Product Estimate</strong> = Publicis

At the end, the SCID is <strong>GLN-0050000000951+Nestlé+OG2020+Publicis</strong>

<h2>How SCID works</h2>


1. The advertiser sends a media brief with its own information (brand, product, targeting etc...) to the trading desk. <strong>This brief must contain enough data to generate SCID</strong>. 

2. With the advertiser’s brief, the trading desk sets and gives the input to the campaign within the DSPs. During the setup, <strong>the trading desk ensures to set correctly SCID</strong>.

3. DSPs deliver the campaign.

4. <strong>SCID</strong> is carried within the Bid response. Campaign is tracked by SSPs and the publishers, thanks to SCID <strong>received in the bid response</strong>.

5. During and/or at the end of the campaign display, **all the intermediaries in the programmatic chain** can compare the data with SCID.<br>

Below is the brief media from an advertiser to a trading desk :

<table>
<tr>
<th>SCID</th>
<th>Creative ID</th>
</tr>
<tr>
<td>GLN-3015619200106+Booking+MTEL+CPExxx</td>
<td>Creative Id 1</td>
</tr>
<tr>
<td>GLN-3015619200106+Booking+MTEL+CPExxx</td>
<td>Creative Id 2</td>
</tr>
<tr>
<td>GLN-3015619200106+Booking+MTEL+CPExxx</td>
<td>Creative Id 3</td>
</tr>
</table>

The trading desk puts the unique SCID (<strong>GLN-3015619200106+Booking+MTEL+CPExxx</strong>) within the DSP to identify and track the campaign with the 3 creatives (<strong>Creative Id 1, Creative Id 2, Creative Id 3</strong>).

<h2>SCID support flow</h2>


Following the OpenRTB protocol, <code>BidResponse.seatbid.bid.cid</code> was first evaluated as a potential way to communicate the SCID in the bid response. However, this one is used in many ways by the DSPs:

- DSP generates automatically <code>cid</code> for each new campaign (a technical ID vs <strong>SCID</strong> who is a business ID).

- DSP sends other data via this field (ex: buyer info)

- DSP does not use this field because it does not want to share sensitive data.

- DSP does not use/support this field at all.

As we can see above, <code>cid</code> is used for many different reasons or is not used at all. **That’s why we need to create a new field in order to track** <strong>SCID</strong>. This new field will help to get an official process and will avoid custom and costly developments on the SSP and DSP.

<h2>Extension details</h2>

<H3>Bid response : </H3>

#### Object: `BidResponse.seatbid.bid.ext`

<table>
<tr>
<th>Attribute</th>
<th>Description</th>
<th>Type</th>
<th>Example</th>
</tr>
<tr>
<td><code>SCID</code></td>
<td>Unique key enabling to identify and track a campaign during its lifecycle regardless of the SSP and DSP used. <br><br><strong>Note</strong> : <code>scid</code> contains the AdvertiserID (Global location number) + Advertiser brand + Advertiser product code + Customer Product Estimate + Ext (optional)</td>
<td>string</td>
<td>"scid": "GLN-3015619200106+Booking+MTEL+CPExxx"</td>
</tr>
</table>



<H3>Example bid response</H3>

```javascript
{
    "id": "1234567890",
    "bidid": "abc1123",
    "cur": "USD",
    "seatbid": [{
            "seat": "512",
            "bid": [{
                    "id": "1",
                    "impid": "102",
                    "price": 9.43,
                    "nurl": "http://adserver.com/winnotice?impid=102",
                    "iurl": "http://adserver.com/pathtosampleimage",
                    "adomain": ["advertiserdomain.com"],
                    "cid": "campaign111",
                    "crid": "creative112",
                    "ext": {
                        "scid": "GLN-3015619200106+Booking+MTEL+CPExxx"
                    }
                ]
            }
        ]
    }
}
```
