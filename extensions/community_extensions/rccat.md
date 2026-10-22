### Restricted-category eligibility (rccat)

#### Issue: [#240](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/240)

Sponsors: IAB Australia. Seller and buyer co-sponsors welcome.

Status: draft for comment.

A seller-certified, impression-level statement of whether a restricted advertising category is eligible on content and time grounds, and which legal or policy bases the seller applied. First use case: Australian wagering advertising under Part 7C of the [Interactive Gambling Act 2001](https://www.legislation.gov.au/C2026A00072/asmade/text), from 1 January 2027.

`rccat` carries no user IDs, `user.id`, `eids`, IFAs, age, login state, opt-out status or register status, and no per-person eligibility signal. Whether a viewer passed the seller's user checks (account, age, opt-out) is implied by the wagering deal. The deal terms or the Deals API record the legal bases and policy reference for those checks.

Request Changes

| Imp Extension Object | Type | Description |
| --- | --- | --- |
| `imp.ext.rccat.elig` | object array, required | One entry per restricted category the seller evaluated for this impression. No entry for a category means the seller hasn't attested eligibility for it; treat as not eligible |
| `elig.cattax` | integer, required | Taxonomy for `cat`, from AdCOM List: Category Taxonomies. `8` = Ad Product Taxonomy 2.0 |
| `elig.cat` | string array, required | Most specific advertised-product category IDs evaluated. An attestation covers the stated ID and its children, never its parents or siblings |
| `elig.eligible` | integer, required | `1` = eligible on content and time grounds. `0` = blocked on content or time grounds that are the same for every viewer, such as a live sport window in the viewer's legal local time. Never used for user state |
| `elig.deal_only` | integer, default `1` | `1` = eligible only through the deal IDs in `imp.pmp.deals`; with no deal, treat as not eligible. `0` = eligible in any auction on this impression, including the open exchange. Use `0` only where the regime has no user-level rules, because user checks travel with the deal. Australian wagering is always `1` |
| `elig.basis` | string array | Content and time bases evaluated, as `jurisdiction-regime-provision` codes. See "Basis codes" |
| `elig.policy` | string | Seller control-set or policy-version reference. Never identifies a user |
| `elig.checked_at` | string, date-time | UTC time the seller made this impression's decision |

Use limits

Receivers may use `rccat` and deal presence only for the bid decision and compliance audit logs. They must not store either against user or device identifiers; use either for targeting, segmentation, frequency capping or measurement; infer restricted-user status from the presence or absence of a wagering deal or an attestation; or retain either beyond its purpose and the applicable retention period.

How receivers read `rccat`

Read the rows in order and stop at the first that applies. "Category" means the restricted category of the creative being considered.

| What the receiver sees | Outcome for that category |
| --- | --- |
| The category, or its parent, is in `bcat` | Not eligible. `bcat` always wins |
| No `rccat` entry covers the category | Not eligible. The seller hasn't attested |
| Entry with `eligible: 0` | Not eligible on content or time grounds. This is the same for every viewer and says nothing about the user |
| Entry with `eligible: 1` and `deal_only: 1` (or absent), and a deal for this category in `imp.pmp.deals` | Eligible, through that deal only |
| Entry with `eligible: 1` and `deal_only: 1` (or absent), and no such deal | Not eligible. Don't infer why: the deal may be missing for commercial reasons or because user checks failed, and the use limits forbid treating it as either |
| Entry with `eligible: 1` and `deal_only: 0` | Eligible in any auction on content and time grounds. Valid only for regimes without user-level rules |

An entry covers the stated category ID and its children, never its parents or siblings. If a creative is declared with a parent category, such as Gambling `1361`, and the entry covers a child, such as Sports Betting `1366`, treat the creative as not covered unless the deal terms say otherwise.

Basis codes

Codes take the form `jurisdiction-regime-provision`: an ISO 3166-1 alpha-2 country code in lower case, a short regime name, then the provision, all lower case and hyphen-separated. Sub-national regimes add the subdivision after the country, for example `ca-on-...`. New codes are added to this table by pull request.

| Code | Regime and provision | Meaning |
| --- | --- | --- |
| `au-iga-62r` | Interactive Gambling Act 2001 (Cth), s 62R | Live sport, 5:00 am to 8:30 pm: no wagering ads on the service from 15 minutes before the scheduled start to 5 minutes after the end, breaks included |
| `au-iga-62ra` | Interactive Gambling Act 2001 (Cth), s 62RA | Live sport, 8:30 pm to 5:00 am: no wagering ads from immediately before the start to immediately after the end, breaks excepted |
| `au-iga-62ua` | Interactive Gambling Act 2001 (Cth), s 62UA | BVOD and SVOD: no wagering ads in programs wholly or principally directed towards children, 5:00 am to 8:30 pm |
| `au-iga-62ub` | Interactive Gambling Act 2001 (Cth), s 62UB | BVOD and SVOD: no wagering ads in G, C or P programs, 6:00 to 8:30 am and 4:00 to 7:00 pm |

User-level provisions, such as `au-iga-62p` and `au-iga-62q`, belong in the deal terms, not in `basis`.

Records and retention

Sellers keep a record of each attestation (`cat`, `eligible`, `deal_only`, `basis`, `policy`, `checked_at`, deal ID and impression ID) for the period their regime requires. For Australian wagering this is 12 months (s 62ZD(1)(b)). Receivers keep `rccat` values and deal presence only in compliance audit logs, not joined to user or device identifiers, and for no longer than their legal or contractual audit period.

JSON Schema: [assets/rccat.schema.json](assets/rccat.schema.json)

Example Request: eligible wagering impression

```
{
  "imp": [{
    "id": "1",
    "pmp": {
      "private_auction": 1,
      "deals": [{ "id": "wagering-eligible-123" }]
    },
    "ext": {
      "rccat": {
        "elig": [{
          "cattax": 8,
          "cat": ["1366"],
          "eligible": 1,
          "deal_only": 1,
          "basis": ["au-iga-62r", "au-iga-62ra"],
          "policy": "seller-wagering-policy-2027-01",
          "checked_at": "2027-03-14T05:00:00Z"
        }]
      }
    }
  }]
}
```

Example Request: open-exchange impression during a live sport window

```
{
  "imp": [{
    "id": "1",
    "ext": {
      "rccat": {
        "elig": [{
          "cattax": 8,
          "cat": ["1366"],
          "eligible": 0,
          "basis": ["au-iga-62r"]
        }]
      }
    }
  }],
  "cattax": 8,
  "bcat": ["1361", "1366"]
}
```
