package com.ofg.microservice.gplus

import com.ofg.infrastructure.discovery.ServiceResolver
import com.ofg.infrastructure.web.filter.correlationid.CorrelationIdHolder
import groovy.transform.PackageScope
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.social.twitter.api.Tweet
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@TypeChecked
@Component
@PackageScope class GPlusCollectorWorker implements GPlusCollector  {

    public static final String TWITTER_PLACES_ANALYZER_CONTENT_TYPE_HEADER = 'vnd.com.ofg.gplus-places-analyzer.v1+json'
    public static final MediaType TWITTER_PLACES_ANALYZER_MEDIA_TYPE = new MediaType('application', TWITTER_PLACES_ANALYZER_CONTENT_TYPE_HEADER)

    private GPlusGetter tweetsGetter
    private RestTemplate restTemplate = new RestTemplate()
    private ServiceResolver serviceResolver

    @Autowired
    GPlusCollectorWorker(GPlusGetter tweetsGetter, ServiceResolver serviceResolver) {
        this.tweetsGetter = tweetsGetter
        this.serviceResolver = serviceResolver
    }

    void collectAndPassToAnalyzers(String twitterLogin, Long pairId) {
        Collection<Tweet> tweets = tweetsGetter.getTweets(twitterLogin)
        String analyzerUrl = serviceResolver.getUrl('analyzer').get()
        restTemplate.put("$analyzerUrl/api/{pairId}", createEntity(tweets), pairId)
    }

    private HttpEntity<Object> createEntity(Object object) {
        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(TWITTER_PLACES_ANALYZER_MEDIA_TYPE)
        headers.set(CorrelationIdHolder.CORRELATION_ID_HEADER, CorrelationIdHolder.get())
        return new HttpEntity<Object>(object, headers);
    }
}