<H1>Trust.id</H1>

Sponsors : Smart, Xandr

<h2>Overview</h2>

The goal is to support the [Trust.id](https://docs.google.com/document/d/1kI-wI3Y2U4ITefV0Nno_Mxu_9kfbeUjyTa2ZIlirOcY/edit?usp=sharing) because the trading desk is used to working with many DSPs. This unique and dedicated field <code>BidResponse.seatbid.bid.ext.trustid</code> helps to track efficiently the campaign, regardless of the DSP and SSP used.


<h2>Responsibilities</h2>

1. The advertiser sends a media brief with its own information (brand, product, targeting etc...) to the trading desk. <strong>This brief must contain enough data to generate</strong> <code>trustid</code>. The actual structure of the Trust.id remains the advertiser’s choice, but will impact the granularity on which they can report later on. Only the first part of <code>trustid</code>, the advertiser unique identifier, currently using the advertiser GLN is mandatory.

2. With the advertiser’s brief, the trading desk sets and gives the input to the campaign within the DSPs. During the setup, <strong>the trading desk ensures to set correctly</strong> <code>trustid</code>.

3. DSPs deliver the campaign.

4. <code>trustid</code> is carried within the Bid response. Campaign is tracked by SSPs and the publishers, thanks to <code>trustid</code> <strong>received in the bid response</strong>.

5. During and/or at the end of the campaign display, **all the intermediaries in the programmatic chain** can compare the data with <code>trustid</code>.

<h2>Trust.id support flow</h2>


Following the OpenRTB protocol, <code>BidResponse.seatbid.bid.cid</code> was first evaluated as a potential way to communicate the trust.id in the bid response. However, this one is used in many ways by the DSPs:

- DSP generates automatically <code>cid</code> for each new campaign (a technical ID, vs trust.Id who is a business ID).

- DSP sends other data via this field (ex: buyer info)

- DSP does not use this field because it does not want to share sensitive data.

- DSP does not use/support this field at all.

As we can see above, <code>cid</code> is used for many different reasons or is not used at all. **That’s why we need to create a new field in order to track** <code>trustid</code>. This new field will help to get an official process and will enable to avoid custom and costly developments on SSP and DSP. 

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
<td>"trustid": "3015619200106+Booking+MTEL+CPE123"</td>
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
