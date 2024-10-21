/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;


public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    /*guessFollowsGraph*/
    @Test
    /*Verifies that an empty input returns an empty graph.*/
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    @Test
    /*Ensures that when there are no mentions, the graph remains empty.*/
    public void testGuessFollowsGraphNoMentions() {
    	List<Tweet> tweets = Arrays.asList(
    			new Tweet(1,"hafsa","aimen how ar you",Instant.now()),
    			new Tweet(2,"javeria","good morning",Instant.now())
    	);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    @Test
    /*Checks if a user who mentions another user is following the mentioned user..*/
    public void testGuessFollowsGraphSingleTweetWithOneMention() {
    	List<Tweet> tweets = Arrays.asList(
    			new Tweet(1,"hafsa","@aimen how ar you",Instant.now())
    	);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("hafsa follow aimen", followsGraph.containsKey("hafsa"));
        assertTrue("hafsa follow aimen", followsGraph.get("hafsa").contains("aimen"));
    }
    @Test
    /*Ensures that a user who mentions multiple users is following all the mentioned users.*/
    public void testGuessFollowsGraphSingleTweetWithMultipleMentions() {
    	List<Tweet> tweets = Arrays.asList(
    			new Tweet(1,"hafsa","@aimen,@javeria how ar you",Instant.now())
    	);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("hafsa follow aimen", followsGraph.containsKey("hafsa"));
        assertTrue("hafsa follow aimen", followsGraph.get("hafsa").contains("aimen"));
        assertTrue("hafsa follow javeria", followsGraph.get("hafsa").contains("javeria"));
    }
    @Test
    /*Verifies that multiple tweets by the same user mentioning different users are captured.*/
    public void testGuessFollowsGraphMultipleTweetsBySameAuthor() {
    	List<Tweet> tweets = Arrays.asList(
    			new Tweet(1,"hafsa","@aimen how ar you",Instant.now()),
    			new Tweet(2,"hafsa","@ayesha how are you?",Instant.now())
    	);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("hafsa follow aimen", followsGraph.containsKey("hafsa"));
        assertTrue("hafsa follow aimen", followsGraph.get("hafsa").contains("aimen"));
        assertTrue("hafsa follow ayesha", followsGraph.get("hafsa").contains("ayesha"));
    }
    @Test
    /*Verifies that the graph includes multiple authors and the correct relationships*/
    public void testGuessFollowsGraphMultipleTweetsByMultipleAuthors() {
    	List<Tweet> tweets = Arrays.asList(
    			new Tweet(1,"hafsa","@aimen how ar you",Instant.now()),
    			new Tweet(2,"aimen","@ayesha how are you?",Instant.now()),
    			new Tweet(3,"maheen","@ayesha how are you?",Instant.now()),
    			new Tweet(4,"hadia","@hafsa how are you?",Instant.now())
    	);
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("hafsa follow aimen", followsGraph.containsKey("hafsa"));
        assertTrue("hafsa follow aimen", followsGraph.get("hafsa").contains("aimen"));
        
        assertTrue("aimen follow ayesha", followsGraph.containsKey("aimen"));
        assertTrue("aimen follow ayesha", followsGraph.get("aimen").contains("ayesha"));
        
        assertTrue("maheen follow ayesha", followsGraph.containsKey("maheen"));
        assertTrue("maheen follow ayesha", followsGraph.get("maheen").contains("ayesha"));
        
        assertTrue("hadia follow hafsa", followsGraph.containsKey("hadia"));
        assertTrue("hadia follow hafsa", followsGraph.get("hadia").contains("hafsa"));
    }
   /*Influencer method*/
    @Test
    public void testInfluencersEmptyGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("Expected empty list", influencers.isEmpty());
    }
    @Test
    /*when there's only one user in the followsGraph with no followers, the influencers function returns an empty list.*/
    public void testInfluencersSingleUserWithNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("maheen", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("Expected empty list", influencers.isEmpty());
    }
    @Test
    /*when there's a single influencer in the followsGraph, the influencers function correctly identifies and returns that influencer as the only user in the list.*/
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("hafsa", new HashSet<>(Arrays.asList("ayesha")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("one influencer in output", 1, influencers.size());
        assertEquals("ayesha", influencers.get(0));
    }
    @Test
    /*the influencers function can correctly identify and return the top influencers when there are multiple users in the followsGraph with varying numbers of followers.*/
    public void testInfluencersWithMultipleInfluencersWithDifferentNumberOfFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("hafsa", new HashSet<>(Arrays.asList("ayesha", "hania")));
        followsGraph.put("ayesha", new HashSet<>(Arrays.asList("hania")));

        List<String> topInfluencers = SocialNetwork.influencers(followsGraph);
        assertEquals("two influencers", 2, topInfluencers.size());
        assertEquals("hania", topInfluencers.get(0));
        assertEquals("ayesha", topInfluencers.get(1));
    }
    @Test
    /*the influencers function correctly handles cases where multiple users have an equal number of followers and returns them in any order.*/
    public void testInfluencersWithMultipleInfluencersWithEqualNumberOfFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("maya", new HashSet<>(Arrays.asList("hania")));
        followsGraph.put("ali", new HashSet<>(Arrays.asList("hania")));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals("Expected one influencer", 1, influencers.size());
        assertEquals("hania", influencers.get(0));
    }
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */
}
