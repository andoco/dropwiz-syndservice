package com.example.feedservice;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;

import com.example.feedservice.resources.FeedResource;
import com.example.feedservice.health.FeedTypeHealthCheck;
import com.example.feedservice.io.MemoryFeedStore;

public class FeedService extends Service<FeedConfiguration> {
    public static void main(String[] args) throws Exception {
        new FeedService().run(args);
    }

    //private FeedService() {
    public FeedService() {
        super("feed");
    }

    @Override
    protected void initialize(FeedConfiguration configuration,
                              Environment environment) {

        final String defaultFeedType = configuration.getDefaultFeedType();
        
        environment.addResource(new FeedResource(defaultFeedType, new MemoryFeedStore()));
        environment.addHealthCheck(new FeedTypeHealthCheck(defaultFeedType));
    }
}