package com.example.feedservice.io;

import java.util.ArrayList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import com.example.feedservice.io.MemoryFeedStore;

public class MemoryFeedStoreTest {

    @Test
    public void givenFeedNotExists_whenSaveFeedEntries_thenSuccess() {
        MemoryFeedStore store = new MemoryFeedStore();
        
        String feedName = "feed";
        
        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("test feed");
        store.saveFeed(feedName, feed);
        
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("entry 1");
        entries.add(entry);
        
        store.saveEntries(feedName, entries);
        
        SyndFeed retrievedFeed = store.retrieveFeed(feedName);
        
        assertEquals(retrievedFeed.getEntries().size(), 1);
    }
    
    @Test
    public void givenStoreNotEmpty_whenEmptyCalled_thenStoreIsEmptied() {
        MemoryFeedStore store = new MemoryFeedStore();
        
        String feedName = "feed";
        
        SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("test feed");
        store.saveFeed(feedName, feed);
    	
        store.clear();
        
        assertNull(store.retrieveFeed(feedName));
    }
}