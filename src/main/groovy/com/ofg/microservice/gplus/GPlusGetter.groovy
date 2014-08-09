package com.ofg.microservice.gplus

import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.social.twitter.api.Tweet
import org.springframework.social.twitter.api.Twitter
import org.springframework.stereotype.Component

import static org.springframework.util.Assert.notNull
import static org.springframework.util.StringUtils.hasText

@TypeChecked
@Component
@PackageScope class GPlusGetter {
    private Twitter twitter
    private int numberOfActivities

    @Autowired
    GPlusGetter(Twitter twitter, @Value('${numberOfActivities:100}') Integer numberOfActivities) {
        notNull(twitter)
        this.twitter = twitter
        this.numberOfActivities = numberOfActivities
    }

    @Cacheable("activities")
    Collection<Tweet> getTweets(String gPlusLogin) {
        hasText(gPlusLogin)
        return twitter.timelineOperations().getUserTimeline(gPlusLogin, numberOfActivities).findAll{!it.isRetweet()}
    }
}
