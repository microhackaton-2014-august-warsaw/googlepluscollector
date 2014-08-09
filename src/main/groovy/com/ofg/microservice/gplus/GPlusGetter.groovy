package com.ofg.microservice.gplus

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.ofg.microservice.model.SimpleActivity
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
    Collection<SimpleActivity> getActivities(String gPlusLogin) {
        hasText(gPlusLogin)
        def people = google.plusOperations().getPerson(gPlusLogin)

        def listOfActivities = new ArrayList()

        if (!people != null) {
            def id = people.id
            def activities = google.plusOperations().getActivities(id)
            for (Activity activity : activities.items) {
                SimpleActivity simpleActivity = new SimpleActivity()
                simpleActivity.id = activity.actor.id
                simpleActivity.content = activity.content

                listOfActivities.add(simpleActivity);
            }
        }
        return Collections.emptyList();
    }
}
