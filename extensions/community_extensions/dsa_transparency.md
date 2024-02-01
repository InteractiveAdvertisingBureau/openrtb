<h1>DSA Transparency</h1>
This specification is stewarded by IAB Tech Lab's <a href="https://iabtechlab.com/working-groups/global-privacy-working-group/">Global Privacy Working Group</a>. IAB Tech Lab and <a href="https://iabeurope.eu">IAB Europe</a> will continue partnership to steward this solution. IAB Tech Lab will maintain the technical specifications. IAB Europe will provide policy guidance, such as implementation guidelines.

<h2>Introduction</h2>
<p>The Digital Services Act (DSA) was adopted in October 2022, and the date of applicability for Platform companies is 16 February 2024.&nbsp; Along with the Digital Markets Act (DMA), the DSA is intended to improve the confidence of both private consumers and business users of Online Platforms in the products and services they access via those platforms, as well as the advertising they are exposed to on them, and to ensure a level playing field between platforms.&nbsp; The DSA lays down transparency obligations in relation to advertising; these obligations apply to Online Platforms, &ldquo;Very Large Online Platforms&rdquo; (VLOPs), and &ldquo;Very Large Online Search Engines&rdquo; (VLOSEs) as defined by the Digital Services Act.&nbsp;&nbsp;</p>
<p>Article 26 of the DSA requires Online Platforms to ensure that users have real-time access to certain elements of information about any ad shown to them on an Online Platform:</p>
<ul>
<li>That the ad is indeed an ad;</li>
<li>The identity of the advertiser;</li>
<li>The identity of the party that financed the ad, if it is different from the advertiser;</li>
<li>Information about the &ldquo;main parameters&rdquo; used to select the ad presented to the end-user;</li>
<li>Where applicable, information about any means users may have at their disposal to change those main parameters.&nbsp;&nbsp;</li>
</ul>
<p>Although the legal obligation to provide the user-facing information disclosures applies to Online Platforms, it is clear that in many advertising scenarios, those platforms will need to rely on third-party vendors for the data that will be required to populate the disclosures.&nbsp; To ensure that the third parties are equipped to provide this support, this technical specification standardizes the collection, compilation and transport of the data, leaving Online Platforms free to decide how they wish to make the user-facing disclosures, including if they want to delegate the disclosures to another party.&nbsp;&nbsp;</p>
<p>This technical specification provides data formats and transport for the advertising industry to implement relevant DSA transparency information. This solution should be applicable across most relevant use cases including; programmatic and non-programmatic media buys, channels including desktop web, mobile (web/app), video, CTV.</p>
<h2>Object Specification for OpenRTB 2.X</h2>
<h3>Bid Request</h3>
<h4>Object: Regs</h4>
<div>
<table>
<tbody>
<tr>
<td><strong>Attribute</strong></td>
<td><strong>Type</strong></td>
<td><strong>Description</strong></td>
</tr>
<tr>
<td><code>ext.dsa</code></td>
<td>object&nbsp;</td>
<td>Extension for DSA transparency information</td>
</tr>
</tbody>
</table>
</div>
<h4>Object: DSA</h4>
<div>
<table>
<tbody>
<tr>
<td><strong>Attribute</strong></td>
<td><strong>Type</strong></td>
<td><strong>Description</strong></td>
</tr>
<tr>
<td><code>dsarequired</code></td>
<td>integer</td>
<td>Flag to indicate if DSA information should be made available. This will signal if the bid request belongs to an Online Platform/VLOP, such that a buyer should respond with DSA Transparency information based on the pubrender value.
    <p><code>0</code> = Not required</p>
    <p><code>1</code> = Supported, bid responses with or without DSA object will be accepted</p>
    <p><code>2</code> = Required, bid responses without DSA object will not be accepted</p>
    <p><code>3</code> = Required, bid responses without DSA object will not be accepted, Publisher is an Online Platform</p></td>
</tr>
<tr>
<td><code>pubrender</code></td>
<td>integer</td>
<td>Flag to indicate if the publisher will render the DSA Transparency info. This will signal if the publisher is able to and intends to render an icon or other appropriate user-facing symbol and display the DSA transparency info to the end user.
    <p><code>0</code> = Publisher can't render</p>
    <p><code>1</code> = Publisher could render depending on adrender</p>
    <p><code>2</code> = Publisher will render</p></td>
</tr>
<tr>
<td><code>datatopub</code></td>
<td>integer</td>
<td>Independent of pubrender, the publisher may need the transparency data for audit purposes.
    <p><code>0</code> = do not send transparency data</p>
    <p><code>1</code> = optional to send transparency data</p>
    <p><code>2</code> = send transparency data</p></td>
</tr>
<tr>
<td><code>transparency</code></td>
<td>array of object</td>
<td>Array of objects of the entities that applied user parameters and the parameters they applied.</td>
</tr>
</tbody>
</table>
</div>
<h4>Object: Transparency&nbsp;</h4>
<div>
<table>
<tbody>
<tr>
<td><strong>Attribute</strong></td>
<td><strong>Type</strong></td>
<td><strong>Description</strong></td>
</tr>
<tr>
<td><code>domain</code></td>
<td>string&nbsp;</td>
<td>Domain of the entity that applied user parameters&nbsp;</td>
</tr>
<tr>
<td><code>dsaparams</code></td>
<td>array of integer</td>
<td>Array for platform or sell-side use of any user parameters (using <a href="#user_parameters" target="_blank" rel="noopener">the list provided by DSA Transparency Taskforce</a>). <em>Note; See definition and list of possible </em><a href="#user_parameters" target="_blank" rel="noopener"><em>user parameters as listed here</em></a><em>, applied consistently in both bid request and/or bid response.</em></td>
</tr>
</tbody>
</table>
</div>
<h4>Sample OpenRTB 2.6 Bid Request with DSA transparency:</h4>

```
{
    "id": "80ce30c53c16e6ede735f123ef6e32361bfc7b22",
    "at": 1,
    "cur": [
        "USD"
    ],
    "regs": {
        "ext": {
            "dsa": {
                "dsarequired": 3, 
                "pubrender": 0,
                "datatopub": 2,
                "transparency": [{
                    "domain": "platform1domain.com",
                    "dsaparams": [1]},
                    {"domain": "SSP2domain.com",
                    "dsaparams": [1,2]
                    }]
            }
        }
    },
    "imp": [{
        "id": "1"
    }],
    "site": {
        "id": "102855"
    },
    "user": {
        "id": "55816b39711f9b5acf3b90e313ed29e51665623f"
    }
}

```

<h3>Bid Response</h3>
<h4>Object: Bid</h4>
<div>
<table>
<tbody>
<tr>
<td><strong>Attribute</strong></td>
<td><strong>Type</strong></td>
<td><strong>Description</strong></td>
</tr>
<tr>
<td><code>ext.dsa</code></td>
<td>object&nbsp;</td>
<td>DSA Ad Transparency information</td>
</tr>
</tbody>
</table>
</div>
<h4>Object: DSA</h4>
<div>
<table>
<tbody>
<tr>
<td><strong>Attribute</strong></td>
<td><strong>Type</strong></td>
<td><strong>Description</strong></td>
</tr>
<tr>
<td><code>behalf</code></td>
<td>string</td>
<td>Advertiser Transparency: Free UNICODE text string with a name of whose behalf the ad is displayed. Maximum 100 characters.</td>
</tr>
<tr>
<td><code>paid</code></td>
<td>string</td>
<td>Advertiser Transparency: Free UNICODE text string of who paid for the ad. Must always be included even if it's the same as what is listed in the behalf attribute. Maximum 100 characters</td>
</tr>
<tr>
<td><code>transparency</code></td>
<td>array of object</td>
<td>Array of objects of the entities that applied user parameters and the parameters they applied.</td>
</tr>
<tr>
<td><code>adrender</code></td>
<td>integer</td>
<td>Flag to indicate that buyer/advertiser will render their own DSA transparency information inside the creative.
    <p><code>0</code> = buyer/advertiser will not render</p>
    <p><code>1</code> = buyer/advertiser will render</p></td>
</tr>
</tbody>
</table>
</div>
<h4>Object: Transparency</h4>
<div>
<table>
<tbody>
<tr>
<td><strong>Attribute</strong></td>
<td><strong>Type</strong></td>
<td><strong>Description</strong></td>
</tr>
<tr>
<td><code>domain</code></td>
<td>string</td>
<td>Domain of the entity that applied user parameters&nbsp;</td>
</tr>
<tr>
<td><code>dsaparams</code></td>
<td>array of integer</td>
<td>Array of buy-side applied user parameter targeting (using <a href="#user_parameters" target="_blank" rel="noopener">the list provided by DSA Transparency Taskforce</a>). Include support for multiple vendors who may add their own user-targeting parameters.</td>
</tr>
</tbody>
</table>
</div>
<h4>Sample&nbsp; OpenRTB 2.6 Bid Response with DSA transparency:</h4>

```
{
    "id": "1234567890",
    "bidid": "abc1123",
    "seatbid": [{
   	 "seat": "512",
   	 "bid": [{
   		 "id": "1",
   		 "nurl": "http://adserver.com/winnotice?impid=102",
   		 "iurl": "http://adserver.com/pathtosampleimage",
   		 "adomain": [
   			 "advertiserdomain.com"
   		 ],
   		 "ext": {
   			 "dsa": {
   				 "behalf": "Advertiser",
   				 "paid": "Advertiser",
   				 "transparency": [{
   					 "domain": “dsp1domain.com”,
   					 "dsaparams": [1,2]
   				 }],
   				 "adrender": 1
   			 }
   		 }
   	 }]
    }]
}
```

<h3><a id="user_parameters">List of User Parameters</a></h3>
<p>The definitions below are not meant to be consumer-facing messages.</p>
<div>
<table>
<tbody>
<tr>
<td>User Parameter ID</td>
<td><strong>User Parameter</strong></td>
<td><strong>Definition</strong></td>
<td><strong>TCF Purpose IDs</strong></td>
</tr>
<tr>
<td>1</td>
<td>Profiling</td>
<td>Information about the user, collected and used across contexts, that is about the user's activity, interests, demographic information, or other characteristics.</td>
<td>TCF Purposes 4</td>
</tr>
<tr>
<td>2</td>
<td>Basic advertising</td>
<td>Use of real-time information about the context in which the ad will be shown, to show the ad, including information about the content and the device, such as: device type and capabilities, user agent, URL, IP address, non-precise geolocation data. Additionally, use of basic cross-context information not based on user behavior or user characteristics, for uses such as frequency capping, sequencing, brand safety, anti-fraud.</td>
<td>TCF Purpose 2</td>
</tr>
<tr>
<td>3</td>
<td>Precise geolocation</td>
<td>The precise real-time geolocation of the user, i.e. GPS coordinates within 500 meter radius precision.</td>
<td>TCF Special Feature 1</td>
</tr>
</tbody>
</table>
</div>
<h2>URL-based support</h2>
<p>Some ad systems may depend on URLs to communicate information. The following parameters and macros can be used to carry relevant DSA information in these cases.</p>
<div>
<table>
<tbody>
<tr>
<td><strong>URL Parameter</strong></td>
<td><strong>Corresponding Macro</strong></td>
<td><strong>Representation in URL</strong></td>
</tr>
<tr>
<td><code>dsarequired</code></td>
<td><code>DSAREQUIRED</code></td>
<td><code>&amp;dsarequired=${DSAREQUIRED}</code></td>
</tr>
<tr>
<td><code>dsabehalf</code></td>
<td><code>DSABEHALF</code></td>
<td><code>&amp;dsabehalf=${DSABEHALF}</code></td>
</tr>
<tr>
<td><code>dsapaid</code></td>
<td><code>DSAPAID</code></td>
<td><code>&amp;dsapaid=${DSAPAID}</code></td>
</tr>
<tr>
<td><code>dsaparams</code></td>
<td><code>DSAPARAMS</code></td>
<td><code>&amp;dsaparams=${DSAPARAMS}</code></td>
</tr>
<tr>
<td><code>dsatransparency</code></td>
<td><code>DSATRANSPARENCY</code></td>
<td><code>&amp;dsatransparency=${DSATRANSPARENCY}</code></td>
</tr>
<tr>
<td><code>dsapubrender</code></td>
<td><code>DSAPUBRENDER</code></td>
<td><code>&amp;dsapubrender=${DSAPUBRENDER}</code></td>
</tr>
<tr>
<td><code>dsadatatopubs</code></td>
<td><code>DSADATATOPUBS</code></td>
<td><code>&amp;dsadatatopubs=${DSADATATOPUBS}</code></td>
</tr>
</tbody>
</table>
</div>

<div>
<table>
<tbody>
<tr>
<td><strong>Macro</strong></td>
<td><strong>Possible Values</strong></td>
<td><strong>Definition</strong></td>
</tr>
<tr>
<td><code>${DSAREQUIRED}</code></td>
<td><p>0 = Not required 
    <p>1 = Supported, bid responses with or without DSA object will be accepted</p> 
    <p>2 = Required, bid responses without DSA object will not be accepted</p> 
    <p>3 = Required, bid responses without DSA object will not be accepted, Publisher is an Online Platform</p></td>
<td>Indicates if DSA information should be made available.&nbsp;</td>
</tr>
<tr>
<td><code>${DSABEHALF}</code></td>
<td>Free UNICODE text string with a name of whose behalf the ad is displayed. 
<p>Example:&nbsp;Advertiser</td>
<td>Populated from the DSP bid response.</td>
</tr>
<tr>
<td><code>${DSAPAID}</code></td>
<td>Free UNICODE text string of who paid for the ad
<p>Example:&nbsp;Advertiser</p></td>
<td>Populated from the DSP bid response.</td>
</tr>
<tr>
<td><code>${DSAPARAMS}</code></td>
<td>Any combination of the integer values representing the user parameters. When multiple values are included, they should be separated by an underscore &ldquo;_&rdquo;.&nbsp;
<p>Example:&nbsp;1_2_3</td>
<td>Populated based on the combination of information from the bid request and bid response user parameters.&nbsp;</td>
</tr>
<tr>
<td><code>${DSATRANSPARENCY}</code></td>
<td>Composed of the two items from the transparency object; the domain string and the params array. These two items are separated by a tilde “~”. Values in the params array are separated by an underscore “_”. Multiple transparency objects are separated by two tildes “~~”.
<p>Example:&nbsp;&dsatransparency=platform1domain.com~1~~SSP2domain.com~1_2</td>
<td>Populated based on the transparency object from the bid request and the bid response. &nbsp;</td>
</tr>
<tr>
<td><code>${DSAPUBRENDER}</code></td>
<td><p>0 = Publisher can’t render</p>
    <p>1 = Publisher could render depending on adrender</p>
    <p>2 = Publisher will render</p></td>
<td>Signals if the publisher is able to and intends to render an icon or other appropriate user-facing symbol and display the DSA transparency info to the end user. </td>
</tr>
<tr>
<td><code>${DSADATATOPUBS}</code></td>
<td><p>0 = do not send transparency data </p>
    <p>1 = optional to send transparency data</p>
    <p>2 = send transparency data</p></td>
<td>Independent of pubrender, the publisher may need the transparency data for audit purposes. </td>
</tr>
</tbody>
</table>
</div>

Changelog
<table>
<tbody>
<tr>
<td><strong>Date</strong></td>
<td><strong>Comments</strong></td>
</tr>
<tr>
<td>January 26, 2024</td>
<td>Updates to attributes to avoid reserved words:
    <li>Update of the <code>required</code> attribute name to <code>dsarequired</code></li>
    <li>Update of the <code>params</code> attribute name to <code>dsaparams</code></li>
</td>
</tr>
<tr>
<td>January 16, 2024</td>
<td>Original publish</td>
</tr>
</tbody>
</table>

