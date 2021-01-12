<h1>Cross-platform campaign identification : Trust.id</H1>

By using <i>Trust.id</i>, <strong>Advertisers and Publishers will soon get more transparency and tracking for their programmatic buying and selling</strong>.

Several advertising associations, representing all stakeholders involved in the buying process, including <strong>Edipub</strong>, stand together to make sure to find consensual solutions, to meet compliance and to define best practices, in responses to transparency requirements.
<strong>Trust.Id: A worldwide approach for a shared need</strong>.

<h3>Problem statement</h3>
What is wrong, what transparency is lacking, why can’t publishers or advertisers track campaigns effectively now?

<h2>Definition</h2>
<strong>Trust.id</strong> is a key enabling identification and tracking throughout a campaign lifecycle. The goal is to keep following a campaign, provide more transparency, and bring the links in the programmatic chain closer during the reporting operations.
<br><br>
The trading desk is used to working with many DSPs. This unique and dedicated <strong>Trust.id</strong> field helps to efficiently track the campaign, regardless of the DSPs and SSPs used.

<h2>Who is involved?</h2>

- Advertisers
- Trading desks (independent and agencies / disclosed and undisclosed)
- DSP
- SSP
- Publishers (direct or through saleshouses)

<h2>The genuine specification</h2>

```
Trust.id = Advertiser ID + Advertiser brand + Advertiser product code + Customer Product Estimate + Ext (optional) 
```

See below the description of each piece from the genuine specification :

- <strong>Advertiser ID</strong> : It will be the [GLN](https://en.wikipedia.org/wiki/Global_Location_Number) (global location number) which is an existing international ID used to identify a company (advertiser).
The registry of GLNs is managed by [GEPIR](https://gepir.gs1.org/) (Global Electronic Party Information Registry) which allows identification of a company thanks to its GLN.<br><br>Example of request :<br>![Search](https://github.com/InteractiveAdvertisingBureau/openrtb/new/master/extensions/community_extensions/assets/trustid_gln_search.png)<br><br>Result :<br>![Result](https://github.com/InteractiveAdvertisingBureau/openrtb/new/master/extensions/community_extensions/assets/trustid_gln_result.png)

- <strong>Advertiser brand, Advertiser product code and Customer Product Estimate</strong> are defined at the advertiser's discretion.

<h3>Example with a media brief from Nestlé</h3>

The trading desk Publicis has a campaign with Nestlé for the Olympics Games in 2020 :
- <strong>Advertiser ID</strong> = 0050000000951
- <strong>Advertiser Brand</strong> = Nestlé <br><strong>Note</strong> : We do not use the adomain field in OpenRTB because a lot of different adomains are created and linked to the same brand. Allowing the buyer to put the advertiser brand from the media brief will help to give more transparency about the campaign.
- <strong>Advertiser Product</strong> = OG2020
- <strong>Customer Product Estimate</strong> = Publicis

At the end, the Trust.Id is <strong>0050000000951+Nestlé+OG2020+Publicis</strong>

<h2>How Trust.id works</h2>


1. The advertiser sends a media brief with its own information (brand, product, targeting etc...) to the trading desk. <strong>This brief must contain enough data to generate Trust.id</strong>. 

2. With the advertiser’s brief, the trading desk sets and gives the input to the campaign within the DSPs. During the setup, <strong>the trading desk ensures to set correctly Trust.id</strong>.

3. DSPs deliver the campaign.

4. <strong>Trust.id</strong> is carried within the Bid response. Campaign is tracked by SSPs and the publishers, thanks to Trust.id <strong>received in the bid response</strong>.

5. During and/or at the end of the campaign display, **all the intermediaries in the programmatic chain** can compare the data with Trust.id.<br>

Below is the brief media from an advertiser to a trading desk :

<table>
<tr>
<th>Trust ID</th>
<th>Creative ID</th>
</tr>
<tr>
<td>3015619200106+Booking+MTEL+CPExxx</td>
<td>Creative Id 1</td>
</tr>
<tr>
<td>3015619200106+Booking+MTEL+CPExxx</td>
<td>Creative Id 2</td>
</tr>
<tr>
<td>3015619200106+Booking+MTEL+CPExxx</td>
<td>Creative Id 3</td>
</tr>
</table>

The trading desk puts the unique Trust.id (<strong>3015619200106+Booking+MTEL+CPExxx</strong>) within the DSP to identify and track the campaign with the 3 creatives (<strong>Creative Id 1, Creative Id 2, Creative Id 3</strong>).

<h2>Trust.id support flow</h2>


Following the OpenRTB protocol, <code>BidResponse.seatbid.bid.cid</code> was first evaluated as a potential way to communicate the trust.id in the bid response. However, this one is used in many ways by the DSPs:

- DSP generates automatically <code>cid</code> for each new campaign (a technical ID, vs trust.Id who is a business ID).

- DSP sends other data via this field (ex: buyer info)

- DSP does not use this field because it does not want to share sensitive data.

- DSP does not use/support this field at all.

As we can see above, <code>cid</code> is used for many different reasons or is not used at all. **That’s why we need to create a new field in order to track** <strong>Trust.id</strong>. This new field will help to get an official process and will avoid custom and costly developments on the SSP and DSP.

<h2>Request change</h2>

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
<td><code>trustid</code></td>
<td>Unique key enabling to identify and track a campaign during its lifecycle regardless of the SSP and DSP used. <br><br><strong>Note</strong> : <code>trustid</code> contains the AdvertiserID (Global location number) + Advertiser brand + Advertiser product code + Customer Product Estimate + Ext (optional)</td>
<td>string</td>
<td>"trustid": "3015619200106+Booking+MTEL+CPExxx"</td>
</tr>
</table>



<H3>Example request</H3>

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
                        "trustid": "3015619200106+Booking+MTEL+CPExxx"
                    }
                ]
            }
        ]
    }
}
```

