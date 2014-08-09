package com.ofg.microservice.gplus


public interface GPlusCollector {
    void collectAndPassToAnalyzers(String twitterLogin, Long pairId)
}