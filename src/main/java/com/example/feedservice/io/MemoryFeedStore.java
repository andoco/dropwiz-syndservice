package com.example.feedservice.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.yammer.dropwizard.logging.Log;

public class MemoryFeedStore implements FeedStore {
	private static final Log LOG = Log.forClass(MemoryFeedStore.class);
    private final Map<String, SyndFeed> feeds = new HashMap<String, SyndFeed>();
    
    @Override
    public SyndFeed createFeed(String name) {
    	SyndFeed feed = new SyndFeedImpl();
    	feed.setTitle(name);
    	this.feeds.put(name, feed);
    	
    	SyndFeed feedCopy = null;
    	
		try {
			feedCopy = (SyndFeed)feed.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return feedCopy;
    }
    
	@Override
	public void saveFeed(String name, SyndFeed feed) {
		SyndFeed existingFeed = this.feeds.get(name);
		
		if (existingFeed == null) {
			// if new feed we can simply save a clone of the supplied feed
			try {
				SyndFeed feedCopy = (SyndFeed)feed.clone();
				this.feeds.put(name, feedCopy);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		} else {
			// add clones of entries to existing feed instance
			
			for (int i=0; i < feed.getEntries().size(); i++) {
				SyndEntry entry = (SyndEntry)feed.getEntries().get(i);
				
				try {
					SyndEntry entryCopy = (SyndEntry)entry.clone();
					existingFeed.getEntries().add(entryCopy);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveEntries(String feedName, List<SyndEntry> entries) {
		LOG.debug("Saving {} entries to the feed {}", entries.size(), feedName);
		
		SyndFeed feed = this.feeds.get(feedName);
		
		for (SyndEntry entry : entries) {
			try {
				// clone the entry
				SyndEntry entryCopy = (SyndEntry)entry.clone();
				
				// add to feed entries list
				List<SyndEntry> currentEntries = (List<SyndEntry>)feed.getEntries();
				currentEntries.add(entryCopy);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}		
	}

	@Override
	public SyndFeed retrieveFeed(String name) {
		SyndFeed feed = null;
		
		try {
			feed = (SyndFeed)this.feeds.get(name);
			if (feed != null)
				feed = (SyndFeed)feed.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return feed;
	}
	
	@Override
	public void clear() {
		this.feeds.clear();
	}
}