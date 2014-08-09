package com.ofg.microservice.gplus

import com.ofg.microservice.model.SimpleActivity
import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.social.google.api.Google
import org.springframework.social.google.api.plus.Person
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
        def people = getPerson(gPlusLogin)

        def listOfActivities = []

        if (people != null) {
            def id = people.id
            def activities = google.plusOperations().getActivities(id)

            for (activity in activities.items) {
                def simpleActivity = new SimpleActivity()
                simpleActivity.id = activity.actor.id
                simpleActivity.content = activity.content

                listOfActivities.add(simpleActivity)
            }
        }

        return Collections.emptyList()
    }

    private Person getPerson(String gPlusLogin) {
        def person = google.plusOperations().getPerson(gPlusLogin)

        if(person == null) {
            person = searchForPerson(gPlusLogin, person)
        }

        person
    }

    private Person searchForPerson(String gPlusLogin, Person person) {
        def peoplePage = google.plusOperations().searchPeople(gPlusLogin, null)

        if (peoplePage != null && peoplePage.items != null && peoplePage.items.size() > 0) {
            person = peoplePage.items.get(0)
        }

        person
    }
}
