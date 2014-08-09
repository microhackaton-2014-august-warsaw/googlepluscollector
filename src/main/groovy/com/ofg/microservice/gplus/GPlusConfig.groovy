package com.ofg.microservice.gplus

import groovy.transform.TypeChecked
import net.sf.ehcache.config.CacheConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.google.connect.GoogleConnectionFactory

@Configuration
@TypeChecked
class GPlusConfig {

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new GoogleConnectionFactory("73915658310-35gtaevji4f4812rq6l8fht5j3qqmqis.apps.googleusercontent.com",
                "v4l0veV8ev-lpRhSH3r4GPkO"));
        return registry;
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
