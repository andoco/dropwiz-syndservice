package com.example.feedservice;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class FeedConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String defaultFeedType;

    public String getDefaultFeedType() {
        return defaultFeedType;
    }
    
    @NotEmpty
    @JsonProperty
    private String supportedFeedTypes;

    public String getSupportedFeedTypes() {
        return supportedFeedTypes;
    }    
}