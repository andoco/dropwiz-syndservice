package com.example.feedservice.health;

import java.util.ArrayList;
import com.yammer.metrics.core.HealthCheck;

public class FeedTypeHealthCheck extends HealthCheck {
    private final String feedType;

    public FeedTypeHealthCheck(String feedType) {
        super("feedType");
        this.feedType = feedType;
    }

    @Override
    protected Result check() throws Exception {
		ArrayList<String> options = new ArrayList<String>();
		options.add("atom_1.0");
		options.add("rss_1.0");
		options.add("rss_2.0");

		if (!options.contains(feedType))
	    	return Result.unhealthy("feedType is not recognised");
	    	    
        return Result.healthy();
    }
}