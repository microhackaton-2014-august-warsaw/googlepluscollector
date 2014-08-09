package com.ofg.microservice.gplus

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.SecurityUtils
import groovy.transform.TypeChecked
import net.sf.ehcache.config.CacheConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.google.api.Google
import org.springframework.social.google.api.impl.GoogleTemplate
import org.springframework.social.google.connect.GoogleConnectionFactory

@Configuration
@TypeChecked
class GPlusConfig {

    private static final String SERVICE_ACCOUNT_EMAIL = "149133260843-tle9n3ag03r0bh2l7skd2atq123aprg3@developer.gserviceaccount.com"

    @Bean
    public Google google() {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        def privateKeyFromKeyStore = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), getClass().getResourceAsStream("/key.p12"), "notasecret", "privatekey", "notasecret");

        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountScopes(Collections.singleton("https://www.googleapis.com/auth/plus.me"))
                .setServiceAccountPrivateKey(privateKeyFromKeyStore)
                .build();

        credential.refreshToken();
        def accessToken = credential.getAccessToken();

        return new GoogleTemplate(accessToken)
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
