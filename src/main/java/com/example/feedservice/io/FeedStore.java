package com.example.feedservice.io;

import java.util.List;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public interface FeedStore {
	
	/**
	 * Creates a new empty feed in the store
	 * @param name
	 * @return
	 */
	SyndFeed createFeed(String name);
	
	/**
	 * Creates a feed with the given name if doesn't already exist, or adds entries to feed if it does 
	 * @param name
	 * @param feed
	 */
	void saveFeed(String name, SyndFeed feed);

	/**
	 * Adds a list of feed entries to an existing feed
	 * @param feedName
	 * @param entries
	 */
	void saveEntries(String feedName, List<SyndEntry> entries);
	
	/**
	 * Retrieves a feed from the store, or null if the feed is not found
	 *
	 * The feed instance returned is independent of any instance in the store.
	 * @param name
	 * @return SyndFeed instance or null if feed cannot be found
	 */
	SyndFeed retrieveFeed(String name);
	
	/**
	 * Removes all feeds from the store
	 */
	void clear();
}