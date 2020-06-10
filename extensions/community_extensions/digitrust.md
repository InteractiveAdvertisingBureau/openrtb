### DigiTrust

Issue: [#33](https://github.com/InteractiveAdvertisingBureau/openrtb/issues/33)

Sponsor: Rubicon Project

The goal is to support [Digitru.st](http://www.digitru.st/) as a third party identity provider to the ecosystem.

Request Changes

<table>
 <tr>
  <th>User Object</th>
  <th>Type</th>
  <th>Example values</th>
  <th>Description</th>
 </tr>
 <tr>
  <td>user.ext.digitrust</td>
  <td>Object</td>
  <td>{ }</td>
  <td>Container object for DigiTrust ID and related attributes.</td>
 </tr>
 <tr>
  <td>user.ext.digitrust.id</td>
  <td>String</td>
  <td>T4MNpfKqn9D9sMuPuH/e3c+14heCiUWXjbg+vN5OqYlYFmh3V8GAtQhQRYx2vuUJ8wJ7nEtFgkmX0jAKW+0i3dvm4Ak/GraYN92IsGHofjubb5zxShu8kQgUQzGn8pSZoNnNOt6OfXy5MiJ5izmM/wSeoA3hwbSaiLaNwbksYcNGrbbxQtDD+ni6hyRsS2vdrqKLE4gGob4MqcZBQQ95YhGr0Ix22EJs2CgD9KnDJ+O9X53d36vi2qbEtFi9+w7tG+LwTfLqTqq6XK30UAc9HxtuWBM1HQFdP9JvDShRo5V0yFZ0PsLgay3puVIHzAaQhj6gBkjsjRzG10CW8DnrHA==</td>
  <td>Encrypted user ID as provided by DigiTrust.</td>
 </tr>
 <tr>
  <td>user.ext.digitrust.keyv</td>
  <td>Integer</td>
  <td>2</td>
  <td>Key version responsible for encryption of the ID.</td>
 </tr>
</table>

Example Request

```
{
 "user":{
  "ext":{
   "digitrust": {
     "id":"F6vrzeiV625KD2WaTcGs68ajfRYokPFm6jNUSsawIKAdo/K8vOCPQ24l7hvEatiOBnsBOABTCjmcMe7C2PkzWgZ4zQeZ6qA5ZfmOVfRbdWjxSO2T+wAuMrLkczqHeXCSLBlR4iyz5ho1o44IPwXyxuYI7iXehBk7F/4QJvnSmK1pCyhp+7UszarryqwGVAjIwwekBxmYm/sU8OZeiUOe8iLa/ggQwf0S1Gl5WpFB7IecyrxGF5fu1jRsbmiF5viYTDUBAiq9Q5TlnA2uawkaNYJGmFT83GEIbYjt27ZiEXMru1NzzaaQBdZA1u97LPDQekj/bvCARwQxbrr1P/uJbA==",
     "keyv": 1
    }
  }
 }
}
```
