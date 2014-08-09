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
            "id":null,
            "content":null
        }
    ]
```

## Build status
[![Build Status](https://travis-ci.org/microhackaton/googlepluscollector.svg?branch=master)](https://travis-ci.org/microhackaton/googlepluscollector)