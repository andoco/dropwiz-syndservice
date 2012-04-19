package com.example.feedservice.resources;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.example.feedservice.io.FeedStore;
import com.example.feedservice.io.MemoryFeedStore;
import com.google.common.base.Optional;
import com.sun.syndication.feed.synd.SyndFeed;

public class FeedResourceTest {

	private FeedStore store;
	private FeedHelper helper;
	
	@BeforeTest
	public void before() {
		store = new MemoryFeedStore();
		helper = new FeedHelper();
	}
	
	@DataProvider(name = "feedTypes")
	public Object[][] createFeedTypes() {
		return new Object[][] {
	        { "rss_0.9" },
	        { "rss_0.92" },
	        { "rss_0.93" },
	        { "rss_0.94" },
            { "rss_1.0" },
            { "rss_2.0" },
            { "atom_0.3" },
            { "atom_1.0" }
        };
	}
	
	@Test
	public void givenFeedNotExists_whenFeedPosted_thenFeedIscreated() {
    	String defaultFeedType = "atom_1.0";
        FeedResource resource = new FeedResource(defaultFeedType, store);
        
        String feedName = "test-feed";
        String feedTitle = "A test feed";
        String feedDescription = "Testing a feed description";
        String feedLink = "http://example.com/test-feed";
        
        resource.postFeed(feedName, feedTitle, feedDescription, feedLink);
                
        SyndFeed feed = store.retrieveFeed(feedName);
        
        assertNotNull(feed);
	}
	
	@Test
	public void givenFeedExists_whenEntryPosted_thenEntryAppendedToFeed() {
    	String defaultFeedType = "atom_1.0";
        FeedResource resource = new FeedResource(defaultFeedType, store);

        String feedName = "test-feed";
        String feedTitle = "A test feed";
        String feedDescription = "Testing a feed description";
        String feedLink = "http://example.com/test-feed";
        
        resource.postFeed(feedName, feedTitle, feedDescription, feedLink);

        String entryTitle = "test entry";
        String entryDescription = "Description of test entry";
        String entryLink = "http://example.com/test-feed/testentry";
        
        resource.postEntry(feedName, entryTitle, entryDescription, entryLink);
        
        SyndFeed feed = this.store.retrieveFeed(feedName);
        
        assertEquals(feed.getEntries().size(), 1);
	}

    @Test(dataProvider="feedTypes")
    public void givenFeedExists_whenFeedWithTypeRequested_thenFeedOfTypeIsReturned(String feedType) {    
    	String defaultFeedType = "atom_1.0";
        FeedResource resource = new FeedResource(defaultFeedType, store);
        
        String feedNameParam = "testfeed";
		this.helper.createFeedInStore(store, feedNameParam, "Test feed");
		this.helper.saveTestEntriesInFeed(store, feedNameParam);
        Optional<String> feedTypeParam = Optional.of(feedType);
        
        SyndFeed feed = resource.getFeed(
        	feedNameParam, 
        	feedTypeParam);
        
        assertEquals(feed.getFeedType(), feedTypeParam.get());        
    }

	@Test
	public void givenFeedExists_whenFeedWithNoTypeRequested_thenFeedOfDefaultTypeIsReturned() {
    	String defaultFeedType = "atom_1.0";
        FeedResource resource = new FeedResource(defaultFeedType, store);
        
        String feedNameParam = "testfeed";
		this.helper.createFeedInStore(store, feedNameParam, "Test feed");
		this.helper.saveTestEntriesInFeed(store, feedNameParam);
        Optional<String> feedTypeParam = Optional.absent();
        
        SyndFeed feed = resource.getFeed(
        	feedNameParam, 
        	feedTypeParam);
      
        assertEquals(feed.getFeedType(), defaultFeedType);
	}
	
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void givenFeedNotExists_whenFeedRequested_thenException() {
    	String defaultFeedType = "atom_1.0";
        FeedResource resource = new FeedResource(defaultFeedType, store);
        
        String feedNameParam = null;
        Optional<String> feedTypeParam = Optional.absent();
        
        resource.getFeed(
        	feedNameParam, 
        	feedTypeParam);
	}		
}