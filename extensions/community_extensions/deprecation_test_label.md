### Cookie Deprecation: Pass Chrome-facilitated cookie deprecation test label

**Sponsor**: Epsilon

**Background**:  
Google recently published an update regarding the UK CMA-aligned Chrome cookie deprecation testing labels announced in May. Chrome intends to label 9.5% percent of Chrome Stable browsers globally. A browser instance, if labeled, is either Mode A (8.5%, opt-in testing beginning Q4 2023) or Mode B (1%, forced cookie deprecation beginning Q1 2024). Two means are offered to access the label. One can read a request header after setting a special partitioned (CHIPS) cookie or client-side via a new `Navigator` script API promise. 

**Proposal**:  
Entities are encouraged to access the label and to share the value unmodified with partners via the following `Device` extension.

### Object: `Device.ext` <a name="object"></a>
 <table>
  <tr>
    <td><strong>Attribute&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></td>
    <td><strong>Description</strong></td>
  </tr>
  <tr>
    <td><code>cdep</code></td>
    <td>string</td>
    <td>the label as received from Chrome or an upstream partner</td>
  </tr>
</table>

<br/><br/>  
  
**Notes**  
Privacy Sandbox enrollment is _not_ required to access the labels.  

If you have opinions or feedback about the test group labeling plans or deprecation in general, the Chrome team encourages interested parties to weigh in on the Privacy Sandbox Developer Support “chrome-testing” [issues](https://github.com/GoogleChromeLabs/privacy-sandbox-dev-support/labels/chrome-testing).


<br/>

**References**  
Cookie Deprecation Labeling Explainer (Google, GitHub, 25 Oct 2023)  
[https://github.com/privacysandbox/tpcd-labeling/blob/main/cookie_deprecation_labeling_explainer.md](https://github.com/privacysandbox/tpcd-labeling/blob/main/cookie_deprecation_labeling_explainer.md)

Intent to Experiment: Cookie Deprecation Labeling (Google, blink-dev, 25 Oct 2023)  
[https://groups.google.com/a/chromium.org/g/blink-dev/c/3escBQGtIpM/m/ntcytva5BgAJ](https://groups.google.com/a/chromium.org/g/blink-dev/c/3escBQGtIpM/m/ntcytva5BgAJ)

Cookie Deprecation Labeling (Chromes Feature Card, 25 Oct 2023)  
[https://chromestatus.com/feature/5189079788683264](https://chromestatus.com/feature/5189079788683264)

Chrome-facilitated testing (Chrome Developer Relations)   
[https://developer.chrome.com/docs/privacy-sandbox/chrome-testing/](https://developer.chrome.com/docs/privacy-sandbox/chrome-testing/)

Privacy Sandbox Developer Support “chrome-testing” issues, Github  
[https://github.com/GoogleChromeLabs/privacy-sandbox-dev-support/labels/chrome-testing](https://github.com/GoogleChromeLabs/privacy-sandbox-dev-support/labels/chrome-testing)

UK Competition and Markets Authority (CMA) Test Guidelines  
[https://www.gov.uk/cma-cases/investigation-into-googles-privacy-sandbox-browser-changes#industry-testing](https://www.gov.uk/cma-cases/investigation-into-googles-privacy-sandbox-browser-changes#industry-testing)

PSA: Third-party cookie deprecation for 1% of Chrome Stable starting Q1 2024 (Google, blink-dev)
[https://groups.google.com/a/chromium.org/g/blink-dev/c/GJl_aMO6Qt4/m/gFEMFo8FAgAJ](https://groups.google.com/a/chromium.org/g/blink-dev/c/GJl_aMO6Qt4/m/gFEMFo8FAgAJ)  
  
Intent to Prototype: Third-Party Cookie Phaseout (Google, blink-dev)  
[https://groups.google.com/a/chromium.org/g/blink-dev/c/8mlWTOcEzcA/m/NZJSW0weAQAJ](https://groups.google.com/a/chromium.org/g/blink-dev/c/8mlWTOcEzcA/m/NZJSW0weAQAJ)
 
Deprecate Third-Party Cookies (Chrome feature card)  
[https://chromestatus.com/feature/5133113939722240](https://chromestatus.com/feature/5133113939722240)

Privacy Sandbox enrollment and attestations model, GitHub  
[https://github.com/privacysandbox/attestation/blob/main/README.md](https://github.com/privacysandbox/attestation/blob/main/README.md)
