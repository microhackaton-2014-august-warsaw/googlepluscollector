googlepluscollector
=================

Microservice collecting google plus activities

INPUT
-----------------

Hit it at (GET): /activities/{gPlusLogin}/{pairId}

gPlusLogin -> Google Plus ID for example: https://plus.google.com/102358108554182716756 --> 102358108554182716756

OUTPUT
-----------------
HTTP 200
Empty body

And it will hit analyzers at /{pairId} with activities taken from Google Plus

```
    [
        {
            "placeName":null,
            "kind":null,
            "updated":null,
            "provider":null,
            "title":null,
            "url":null,
            "object":{
                "resharers":null,
                "attachments":null,
                "originalContent":null,
                "plusoners":null,
                "actor":null,
                "content":null,
                "url":null,
                "replies":null,
                "id":null,
                "objectType":null
            },
            "placeId":null,
            "actor":null,
            "id":null,
            "access":null,
            "verb":null,
            "geocode":null,
            "radius":null,
            "address":null,
            "crosspostSource":null,
            "placeholder":false,
            "annotation":null,
            "published":null
        }
    ]
```

## Build status
[![Build Status](https://travis-ci.org/microhackaton/googlepluscollector.svg?branch=master)](https://travis-ci.org/microhackaton/googlepluscollector)