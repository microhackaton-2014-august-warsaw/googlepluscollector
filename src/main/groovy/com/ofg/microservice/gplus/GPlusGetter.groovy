package com.ofg.microservice.gplus

import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.social.google.api.Google
import org.springframework.social.google.api.plus.Activity
import org.springframework.stereotype.Component

import static org.springframework.util.StringUtils.hasText

@TypeChecked
@Component
@PackageScope class GPlusGetter {

    @Autowired
    Google google;

    @Cacheable("activities")
    Collection<Activity> getActivities(String gPlusLogin) {
        hasText(gPlusLogin)
        def peoples = google.plusOperations().searchPeople(gPlusLogin, null)

        if (!peoples.items.isEmpty()) {
            def id = peoples.items.get(0).id
            def activities = google.plusOperations().getActivities(id)
            return activities.items;
        }
        return Collections.emptyList();
    }
}
