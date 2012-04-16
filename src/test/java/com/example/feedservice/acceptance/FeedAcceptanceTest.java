package com.example.feedservice.acceptance;

import static org.testng.Assert.assertEquals;

import javax.ws.rs.core.MediaType;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.example.feedservice.io.FeedStore;
import com.example.feedservice.io.MemoryFeedStore;
import com.example.feedservice.resources.FeedHelper;
import com.example.feedservice.resources.FeedResource;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.yammer.dropwizard.testing.ResourceTest;

public class FeedAcceptanceTest extends ResourceTest {
	
	private FeedHelper helper;
	private FeedStore store;
		
	@BeforeTest
	public void before() throws Exception {
		this.helper = new FeedHelper();
		this.store = new MemoryFeedStore();
		this.setUpJersey();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		// clear store before each test method to ensure clean state
		this.store.clear();
	}
	
	@AfterTest
	public void after() throws Exception {
		this.tearDownJersey();
	}

	@Override
	protected void setUpResources() throws Exception {
		addResource(new FeedResource("rss_1.0", store));
	}

	@Test
	public void givenFeedNotExists_whenFeedPosted_thenFeedCreated() {
		String feedName = "testfeed";
		Form f = new Form();
		f.add("name", feedName);
		f.add("title", "title of test feed");
		f.add("description", "description of test feed");
		
		ClientResponse resp = client().resource("/feed")
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.post(ClientResponse.class, f);
		
		assertEquals(resp.getStatus(), 204, "Feed creation response status incorrect");
		
		SyndFeed feed = store.retrieveFeed(feedName);
		assertEquals(feed.getTitle(), f.getFirst("title"));
		assertEquals(feed.getDescription(), f.getFirst("description"));
	}
	
	@Test
	public void givenFeedExists_whenEntryPostedToFeed_thenEntryAddedToFeed() {
		String feedName = "testfeed";
		this.helper.createFeedInStore(store, feedName, "Test feed");
		
		int countBefore = store.retrieveFeed(feedName).getEntries().size();
		
		Form f = new Form();
		f.add("title", "title of test entry");
		f.add("description", "description of test feed");

		ClientResponse resp = client().resource("/feed/" + feedName)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.post(ClientResponse.class, f);
		
		assertEquals(resp.getStatus(), 204, "Entry creation response status incorrect");
		
		SyndFeed feed = store.retrieveFeed(feedName);
		int countAfter = feed.getEntries().size();
		assertEquals(countAfter, countBefore + 1, "Incorrect number of entries in feed");
		
		SyndEntry entry = (SyndEntry)feed.getEntries().get(countAfter - 1);
		assertEquals(entry.getTitle(), f.getFirst("title"));
		assertEquals(entry.getDescription().getValue(), f.getFirst("description"));
	}
	
	@Test
	public void givenFeedExists_whenGetFeed_thenFeedReturned() {
		String feedName = "testfeed";
		this.helper.createFeedInStore(store, feedName, "Test feed");
		this.helper.saveTestEntriesInFeed(store, feedName);
		
		ClientResponse resp = client().resource("/feed/" + feedName)
				.accept(MediaType.APPLICATION_XML_TYPE)
				.get(ClientResponse.class);
		
		assertEquals(resp.getStatus(), 200, "Feed response status incorrect");
	}
}
