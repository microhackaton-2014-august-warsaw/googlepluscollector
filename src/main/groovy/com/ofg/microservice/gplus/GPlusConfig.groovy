package com.ofg.microservice.gplus

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.SecurityUtils
import groovy.transform.TypeChecked
import net.sf.ehcache.config.CacheConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.social.google.api.Google
import org.springframework.social.google.api.impl.GoogleTemplate

@Configuration
@TypeChecked
class GPlusConfig {

    private static final String SERVICE_ACCOUNT_EMAIL = "149133260843-tle9n3ag03r0bh2l7skd2atq123aprg3@developer.gserviceaccount.com"

    private static final String GOOGLE_PLUS_ME = "https://www.googleapis.com/auth/plus.me"

    private final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
    private final JsonFactory JSON_FACTORY = new JacksonFactory()

    @Bean
    public Google google() {
        String accessToken = generateAccessToken()

        return new GoogleTemplate(accessToken)
    }

    private String generateAccessToken() {

        def privateKeyFromKeyStore = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
                getClass().getResourceAsStream("/key.p12"),
                "notasecret",
                "privatekey",
                "notasecret")

        GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountScopes(Collections.singleton(GOOGLE_PLUS_ME))
                .setServiceAccountPrivateKey(privateKeyFromKeyStore)
                .build()

        credential.refreshToken()
        def accessToken = credential.getAccessToken()

        accessToken
    }

    @Bean
    CacheManager cacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration()
        cacheConfiguration.setName("activities")
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU")
        cacheConfiguration.setMaxEntriesLocalHeap(1000)
        cacheConfiguration.setTimeToLiveSeconds(60 * 10)
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration()
        config.addCache(cacheConfiguration)
        return new EhCacheCacheManager( net.sf.ehcache.CacheManager.newInstance(config) )
    }
}
