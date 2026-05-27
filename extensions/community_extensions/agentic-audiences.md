# Agentic Audiences in OpenRTB

**Sponsors**: LiveRamp, Raptive

## Overview

Agentic Audiences (formerly the User Context Protocol/UCP) is an open standard that defines how intelligent agents in advertising exchange signals—identity, contextual, and reinforcement information—that represent a consumer's real-time intent and response to advertising. Agentic Audiences has been added to IAB Tech Lab's open-source agentic initiative and is maintained in the [IABTechLab/agentic-audiences](https://github.com/IABTechLab/agentic-audiences) repository.

Rather than exchanging raw data points or text descriptions, Agentic Audiences leverages **embeddings**—compact, learned vector representations that efficiently encode complex signals in a privacy-preserving, interoperable format. This enables the sub-100ms response times required for real-time bidding.

## Extension Scope

Embeddings are conveyed in `BidRequest.user.data` using the existing `Data` and `Segment` objects. Each data provider contributes one `Data` object whose `segment` array contains one or more embedding entries. Each segment is a standard OpenRTB Segment with `id` and `name` for identification. The **`aa`** envelope object at `Segment.ext.aa` carries the embedding and metadata that downstream systems need to interpret, filter, and use the vector—for example, for similarity matching or model-specific processing.

## Specification

### Object: `BidRequest.user.data`

Per the OpenRTB 2.x API, the `Data` object array in `user.data` allows additional data about the user to be specified. For Agentic Audiences, each provider contributes one `Data` object with:

- **name**: Provider identifier (e.g., `liveramp`). Used to identify the source of the embedding data.
- **segment**: Array of Agentic Audiences segment entries (see below).

### Object: `Data.segment` (Agentic Audiences Segment Extension)

When conveying Agentic Audiences embeddings, each element in the `segment` array is a standard OpenRTB Segment object. The segment uses `id` and `name` for identification, with Agentic Audiences–specific attributes under `ext.aa`:

| Attribute | Type | Description |
| :-- | :-- | :-- |
| `id` | string | ID of the data segment (standard Segment field). |
| `name` | string | Descriptive name for the segment (standard Segment field). |
| `ext` | object | Extension object; Agentic Audiences fields are grouped in `ext.aa` (see below). |

### Object: `Segment.ext` (Agentic Audiences placement)

The standard `Segment.ext` object may contain keys from multiple extensions. For Agentic Audiences, all fields defined in this specification are nested under the **`aa`** property so they do not collide with other `ext` extensions.

| Attribute | Type | Description |
| :-- | :-- | :-- |
| `aa` | object | Agentic Audiences envelope containing the embedding and metadata (see `Segment.ext.aa` below). |

### Object: `Segment.ext.aa` (Agentic Audiences envelope)

The `aa` object carries the embedding vector and metadata so that buyers can interpret the segment (e.g., by model or signal type), validate compatibility, and use the vector for scoring or similarity operations.

| Attribute | Type | Description |
| :-- | :-- | :-- |
| `ver` | string | Specification version for embedding schema compatibility (e.g., "1.0.0"). |
| `vector` | string | Base64-encoded embedding. The binary payload is Float32 values packed as [IEEE 754](https://standards.ieee.org/standard/754_2019.html) binary32 in **little-endian** byte order (4 bytes per value), concatenated, then standard Base64 (RFC 4648). |
| `dimension` | integer | Number of Float32 values in the embedding. Must equal `(Base64-decoded byte length) / 4`. The decoded byte length must be divisible by 4. This field allows a DSP (or other consumer) to perform **rapid validation**—for example, by comparing `dimension` to an expected length for a given `model` before or without fully decoding the vector. |
| `model` | string | Model identifier that produced the embedding (e.g., "sbert-mini-ctx-001"). |
| `type` | integer array | Embedding type(s): 1 = identity, 2 = contextual, 3 = reinforcement. An entry may encode multiple signal types. |

#### Vector encoding

**Encode:** write each float as 4-byte little-endian Float32, concatenate, Base64-encode.

**Decode:** Base64-decode to raw bytes; reject if `byteLength % 4 !== 0` or if `byteLength / 4` does not equal `dimension`. Read each 4-byte little-endian Float32 to obtain the float array.

Example (JavaScript) — encode:

```javascript
const floats = [
  1.2345678, -2.5, 0.0, 3.1415927, 12345.678,
  -0.00012345, 42.42, -999.999, 0.000001, 987654.25
];

function floats32ToBase64(arr) {
  const buffer = new ArrayBuffer(arr.length * 4);
  const view = new DataView(buffer);
  arr.forEach((x, i) => view.setFloat32(i * 4, x, true)); // little-endian
  const bytes = new Uint8Array(buffer);
  let binary = "";
  for (const b of bytes) binary += String.fromCharCode(b);
  return btoa(binary);
}

/* floats32ToBase64(floats) →
   UQaePwAAIMAAAAAA2w9JQLbmQEZbcgG5FK4pQvD/ecS9N4Y1ZCBxSQ== */
```

Example (JavaScript) — decode:

```javascript
function base64ToFloats32(b64) {
  const binary = atob(b64);
  const bytes = new Uint8Array(binary.length);
  for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i);
  if (bytes.byteLength % 4 !== 0) {
    throw new Error("Invalid vector payload: byte length must be a multiple of 4");
  }
  const view = new DataView(bytes.buffer, bytes.byteOffset, bytes.byteLength);
  const out = [];
  for (let i = 0; i < bytes.byteLength; i += 4) {
    out.push(view.getFloat32(i, true)); // little-endian
  }
  return out;
}

/* base64ToFloats32("UQaePwAAIMAAAAAA2w9JQLbmQEZbcgG5FK4pQvD/ecS9N4Y1ZCBxSQ==")
   → [
     1.2345677614212036, -2.5, 0, 3.1415927410125732, 12345.677734375,
     -0.0001234499941347167, 42.41999816894531, -999.9990234375, 9.999999974752427e-7, 987654.25
   ] */
```

### List: Embedding Type Values

| Value | Description |
| :-- | :-- |
| 1 | Identity – Who the user is (hashed identifiers, segments, behavioral history) |
| 2 | Contextual – What the user is doing right now (page content, time of day, device, engagement) |
| 3 | Reinforcement – How the user responds to advertising (impressions, clicks, conversions, engagement) |

## Example Bid Request

### Single provider

```json
{
  "user": {
    "data": [
      {
        "name": "data-provider",
        "segment": [
          {
            "id": "some-id",
            "name": "descriptive-name",
            "ext": {
              "aa": {
                "ver": "1.0.0",
                "vector": "UQaePwAAIMAAAAAA2w9JQLbmQEZbcgG5FK4pQvD/ecS9N4Y1ZCBxSQ==",
                "dimension": 10,
                "model": "sbert-mini-ctx-001",
                "type": [1, 2]
              }
            }
          }
        ]
      }
    ]
  }
}
```

### Multiple providers

```json
{
  "user": {
    "data": [
      {
        "name": "provider-a",
        "segment": [
          {
            "id": "seg-1",
            "name": "identity-contextual",
            "ext": {
              "aa": {
                "ver": "1.0.0",
                "vector": "UQaePwAAIMAAAAAA2w9JQLbmQEZbcgG5FK4pQvD/ecS9N4Y1ZCBxSQ==",
                "dimension": 10,
                "model": "sbert-mini-ctx-001",
                "type": [1, 2]
              }
            }
          }
        ]
      },
      {
        "name": "provider-b",
        "segment": [
          {
            "id": "seg-2",
            "name": "contextual",
            "ext": {
              "aa": {
                "ver": "1.0.0",
                "vector": "AAAAP5qZGT/NzMy9",
                "dimension": 3,
                "model": "contextual-model-v1",
                "type": [2]
              }
            }
          }
        ]
      }
    ]
  }
}
```

*Note: Real embeddings are typically 256–1024 Float32 values; the longer `vector` example encodes 10 floats. `dimension` must match the number of Float32 values encoded in `vector` (i.e. decoded byte length ÷ 4).*

## Implementation Notes

- **Vector length**: Embeddings typically contain 256–1024 Float32 values. Implementers should agree on expected length and vector-space alignment when interoperating across producers and consumers. Consumers should validate that `dimension` equals `(Base64-decoded byte length) / 4` and reject invalid payloads.
- **Privacy**: Embeddings encode semantic meaning without exposing raw user data. Implementers must ensure appropriate consent and data handling policies are followed.
- **Model interoperability**: The `model` field enables downstream systems to select compatible embeddings. Similarity computations are meaningful only within the same model/vector space.

## References

- [Agentic Audiences | IAB Tech Lab](https://iabtechlab.com/standards/agentic-audiences/)
- [Agentic Audiences GitHub Repository](https://github.com/IABTechLab/agentic-audiences)
