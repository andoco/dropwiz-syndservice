package com.example.feedservice.resources;

import java.util.List;

import com.example.feedservice.io.FeedStore;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

public class FeedHelper {
	public void createFeedInStore(FeedStore store, String feedName, String feedTitle) {
    	SyndFeed feed = new SyndFeedImpl();    	
    	feed.setTitle(feedTitle);
    	feed.setDescription("description of " + feedName);
    	feed.setLink("http://example.com/" + feedName);
    	
    	store.saveFeed(feedName, feed);
	}
	
	public void saveTestEntriesInFeed(FeedStore store, String feedName) {
		SyndFeed feed = store.retrieveFeed(feedName);
		
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("test entry 1");
        entry.setLink("http://example.com/" + feedName + "/1");
        
        List<SyndEntry> entries = feed.getEntries();
        entries.add(entry);
        
    	store.saveFeed(feedName, feed);
	}
}
