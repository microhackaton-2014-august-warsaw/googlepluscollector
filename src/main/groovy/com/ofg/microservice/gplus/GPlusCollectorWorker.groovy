package com.ofg.microservice.gplus

import com.googlecode.googleplus.model.activity.Activity
import com.ofg.infrastructure.discovery.ServiceResolver
import com.ofg.infrastructure.web.filter.correlationid.CorrelationIdHolder
import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@TypeChecked
@Component
@PackageScope class GPlusCollectorWorker implements GPlusCollector  {

    public static final MediaType TWITTER_PLACES_ANALYZER_MEDIA_TYPE = new MediaType('application/json')

    private GPlusGetter gPlusGetter
    private RestTemplate restTemplate = new RestTemplate()
    private ServiceResolver serviceResolver

    @Autowired
    GPlusCollectorWorker(GPlusGetter gPlusGetter, ServiceResolver serviceResolver) {
        this.gPlusGetter = gPlusGetter
        this.serviceResolver = serviceResolver
    }

    void collectAndPassToAnalyzers(String twitterLogin, Long pairId) {
        Collection<Activity> activites = gPlusGetter.getTweets(twitterLogin)
        String analyzerUrl = serviceResolver.getUrl('analyzer').get()
        restTemplate.put("$analyzerUrl/api/{pairId}", createEntity(activites), pairId)
    }

    private HttpEntity<Object> createEntity(Object object) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(TWITTER_PLACES_ANALYZER_MEDIA_TYPE)
        headers.set(CorrelationIdHolder.CORRELATION_ID_HEADER, CorrelationIdHolder.get())
        return new HttpEntity<Object>(object, headers);
    }
}