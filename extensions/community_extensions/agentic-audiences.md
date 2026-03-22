# Agentic Audiences in OpenRTB

**Sponsors**: LiveRamp, IAB Tech Lab

## Version History

| Date | Version | Comments |
| :-- | :-- | :-- |
| March 2026 | 1.0 | Initial community extension for OpenRTB 2.x |

## Overview

Agentic Audiences (formerly the User Context Protocol/UCP) is an open standard that defines how intelligent agents in advertising exchange signals—identity, contextual, and reinforcement information—that represent a consumer's real-time intent and response to advertising. Thanks to a [donation from LiveRamp](https://liveramp.com/blog/accelerating-ai-with-standards-governance/), Agentic Audiences has been added to IAB Tech Lab's open-source agentic initiative and is maintained in the [IABTechLab/agentic-audiences](https://github.com/IABTechLab/agentic-audiences) repository.

Rather than exchanging raw data points or text descriptions, Agentic Audiences leverages **embeddings**—compact, learned vector representations that efficiently encode complex signals in a privacy-preserving, interoperable format. This approach enables the sub-100ms response times required for real-time bidding.

This extension defines how Agentic Audiences embeddings are conveyed in OpenRTB bid requests via `BidRequest.user.data`, using the existing Data and Segment object structure. Each data provider (e.g., identity provider, data platform) contributes its own Data object, with each Segment representing a single embedding entry.

## Goal

The goal of this community extension is to define how Agentic Audiences embeddings are conveyed in OpenRTB bid requests, enabling SSPs, identity providers, and data platforms to pass learned vector representations to DSPs and buyers for use in real-time bidding decisions. Implementations such as the [Prebid.js Agentic Audience Adapter](https://github.com/prebid/Prebid.js/pull/14626) read agentic audience data from browser storage and inject it into `user.data` as segment extensions for downstream bidders.

## Specification

### Object: `BidRequest.user.data`

Per the OpenRTB 2.x API, the Data and Segment objects allow additional data about the user to be specified. For Agentic Audiences, each configured provider gets its own Data object. The `name` field identifies the provider (e.g., `live_ramp`, `optable`) and the `segment` array contains that provider's embedding entries.

### Object: Agentic Audiences Segment (extends `Segment`)

For Agentic Audiences Data objects, each element in the `segment` array is an embedding entry with the following structure:

<table>
  <thead>
    <tr>
      <td><strong>Attribute</strong></td>
      <td><strong>Type</strong></td>
      <td><strong>Description</strong></td>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>ver</code></td>
      <td>string</td>
      <td>Specification version (e.g., "1.0").</td>
    </tr>
    <tr>
      <td><code>vector</code></td>
      <td>number array</td>
      <td>Vector embedding (float array). Typically 256–1024 dimensions.</td>
    </tr>
    <tr>
      <td><code>model</code></td>
      <td>string</td>
      <td>Model identifier that produced the embedding (e.g., "sbert-mini-ctx-001", "optable-embed-v1").</td>
    </tr>
    <tr>
      <td><code>dimension</code></td>
      <td>number</td>
      <td>Vector dimension (length of the vector array).</td>
    </tr>
    <tr>
      <td><code>type</code></td>
      <td>number array</td>
      <td>Embedding type(s). Values: 1 = identity, 2 = contextual, 3 = reinforcement. An entry may encode multiple types (e.g., [1, 2] for identity and contextual).</td>
    </tr>
  </tbody>
</table>

### List: Embedding Types

| Value | Description |
| :---- | :---- |
| 1 | Identity – Who the user is (hashed identifiers, segments, behavioral history) |
| 2 | Contextual – What the user is doing right now (page content, time of day, device, engagement patterns) |
| 3 | Reinforcement – How the user responds to advertising (impressions, clicks, conversions, engagement metrics) |

## Example Bid Request

### Single provider (LiveRamp only)

```json
{
  "id": "req-12345",
  "imp": [{ "id": "1", "banner": { "w": 300, "h": 250 } }],
  "user": {
    "data": [
      {
        "name": "live_ramp",
        "segment": [
          {
            "ver": "1.0",
            "vector": [0.1, -0.2, 0.3],
            "model": "sbert-mini-ctx-001",
            "dimension": 3,
            "type": [1, 2]
          }
        ]
      }
    ]
  }
}
```

### Multiple providers (LiveRamp and Optable)

When multiple providers supply agentic audience data, each gets its own Data object:

```json
{
  "user": {
    "data": [
      {
        "name": "live_ramp",
        "segment": [
          {
            "ver": "1.0",
            "vector": [0.1, -0.2, 0.3],
            "model": "sbert-mini-ctx-001",
            "dimension": 3,
            "type": [1]
          }
        ]
      },
      {
        "name": "optable",
        "segment": [
          {
            "ver": "1.0",
            "vector": [0.5, 0.6, -0.1],
            "model": "optable-embed-v1",
            "dimension": 3,
            "type": [2]
          }
        ]
      }
    ]
  }
}
```

*Note: Vector arrays in examples are truncated for illustration; actual vectors are typically 256–1024 dimensions.*

## Client-Side Storage Format

Implementations that read agentic audience data from browser storage (localStorage or cookie) should use the following format:

**Encoding:** Data stored in cookie or localStorage **must be base64-encoded**. The decoded JSON (unencoded data) is what gets injected into the bid request and sent over the wire to bidders—not the base64 string.

**Format:** The decoded JSON must contain an `entries` array. Each entry must include: `ver`, `vector`, `model`, `dimension`, and `type` as defined above.

**Default storage keys** (for reference implementations):

| Provider | Default storage key |
| :------- | :------------------ |
| LiveRamp | `_lr_agentic_audience_` |
| Optable | `_optable_agentic_audience_` |

## Implementation Notes

- **Provider identification:** The Data object `name` should use snake_case for the provider (e.g., `liveRamp` → `live_ramp`).
- **Vector dimensions:** Embedding vectors are typically 256–1024 dimensions. Similarity computations (e.g., cosine similarity) are meaningful only within compatible vector spaces (same model/provider).
- **Privacy:** Embeddings encode semantic meaning without exposing raw user data. Implementers must ensure appropriate consent and data handling policies are followed.
- **Performance:** Compact vector representation supports sub-100ms inference, critical for real-time bidding.
- **Related standards:** This extension aligns with the [Agentic Audiences](https://github.com/IABTechLab/agentic-audiences) specification and complements the [Agentic Real Time Framework (ARTF)](https://iabtechlab.com/agentic-rtb-framework-specification-v1-0/).

## References

- [Agentic Audiences | IAB Tech Lab](https://iabtechlab.com/standards/agentic-audiences/)
- [Agentic Audiences GitHub Repository](https://github.com/IABTechLab/agentic-audiences)
- [Prebid.js Agentic Audience Adapter (PR #14626)](https://github.com/prebid/Prebid.js/pull/14626)
- [Agentic Real Time Framework Specification v1.0](https://iabtechlab.com/agentic-rtb-framework-specification-v1-0/)
