package com.ofg.microservice.gplus

import com.googlecode.googleplus.model.activity.Activity
import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

import static org.springframework.util.StringUtils.hasText

@TypeChecked
@Component
@PackageScope class GPlusGetter {
    private int numberOfActivities

    //@Autowired
    GPlusGetter(/*Twitter twitter, @Value('${numberOfActivities:100}') Integer numberOfActivities*/) {
        //notNull(twitter)
        //this.twitter = twitter
        //this.numberOfActivities = numberOfActivities
    }

    @Cacheable("activities")
    Collection<Activity> getTweets(String gPlusLogin) {
        hasText(gPlusLogin)
        //return twitter.timelineOperations().getUserTimeline(gPlusLogin, numberOfActivities).findAll{!it.isRetweet()}

        return Collections.emptyList();
    }
}
