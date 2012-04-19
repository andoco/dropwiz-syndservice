package com.example.feedservice.resources;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.example.feedservice.io.FeedStore;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.yammer.metrics.annotation.Timed;

@Path("/feed")
@Produces(MediaType.APPLICATION_XML)
public class FeedResource {

    private final String defaultFeedType;
    private final FeedStore store;

    public FeedResource(String defaultFeedType, FeedStore store) {
        this.defaultFeedType = defaultFeedType;
        this.store = store;
    }
    
    @POST
    @Timed
    public void postFeed(
    		@FormParam("name") String name, 
    		@FormParam("title") String title, 
    		@FormParam("description") String description,
    		@FormParam("link") String link) {
    	
    	SyndFeed feed = new SyndFeedImpl();
    	
    	feed.setTitle(title);
    	feed.setDescription(description);
    	feed.setLink(link);
    	
    	this.store.saveFeed(name, feed);    	
    }
    
    @Path("/{name}")
    @POST
    @Timed
    public void postEntry(
    		@PathParam("name") String feedName,
    		@FormParam("title") String title, 
    		@FormParam("description") String description,
    		@FormParam("link") String link) {
    	
    	SyndEntry entry = new SyndEntryImpl();
    	entry.setTitle(title);
    	
		SyndContent descriptionContent = new SyndContentImpl();
		descriptionContent.setType("text/plain");
		descriptionContent.setValue(description);
		entry.setDescription(descriptionContent);
		
		entry.setLink(link);

		List<SyndEntry> entries = Arrays.asList(entry);
    	
    	this.store.saveEntries(feedName, entries);
    }

    @Path("/{name}")
    @GET
    @Timed
    public SyndFeed getFeed(
    	@PathParam("name") String name,
    	@QueryParam("type") Optional<String> feedType) {
    	
    	if (Strings.isNullOrEmpty(name) || name.trim().isEmpty())
    		throw new IllegalArgumentException("Feed name is required");
        	
    	SyndFeed feed = this.store.retrieveFeed(name);
    	feed.setFeedType(feedType.or(this.defaultFeedType));
    	
    	return feed;		
    }
}