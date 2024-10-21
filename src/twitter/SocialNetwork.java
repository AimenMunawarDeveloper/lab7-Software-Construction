/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

import java.util.*;
/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
	public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
	    Map<String, Set<String>> followsGraph = new HashMap<>();
	    Pattern pat = Pattern.compile("@(\\w+)"); 
	    for (int i = 0; i < tweets.size(); i++) { 
	        String author = tweets.get(i).getAuthor().toLowerCase();
	        String text = tweets.get(i).getText(); 
	        Matcher matcher = pat.matcher(text); /*matching tweet text with pattern to find mentioned user*/
	        while (matcher.find()) {
	        	String username = matcher.group(0).substring(1).toLowerCase(); /*getting mentioned user name without @*/
		        if (!username.equals(author)) {
	                followsGraph.putIfAbsent(author, new HashSet<>()); /*adding author in the graph if not already present*/
	                followsGraph.get(author).add(username);  /*getting author from graph and adding set of user's that where mentioned by the author*/
	            }/*as in this graph our key is user and value is followers of that user*/
	        }
	    }
	    return followsGraph;     
	}
    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
	public static List<String> influencers(Map<String, Set<String>> followsGraph) {
	    Map<String, Integer> followerCountMap = new HashMap<>();
	    List<String> users = new ArrayList<>(followsGraph.keySet()); /*user list from follower graph*/

	    for (int i = 0; i < users.size(); i++) {
	        String user = users.get(i); 
	        Set<String> followedUsers = followsGraph.get(user); // finding set of users that this user follow
	        List<String> followedList = new ArrayList<>(followedUsers); 
	        
	        for (int j = 0; j < followedList.size(); j++) {
	            String followedUser = followedList.get(j);
	            /*iterating over each followed user and increasing their follower count and if they are not yet in the count map we initialize them with 1*/
	            if (followerCountMap.containsKey(followedUser)) {
	                followerCountMap.put(followedUser, followerCountMap.get(followedUser) + 1);
	            } else {
	                followerCountMap.put(followedUser, 1);
	            }
	        }
	    }
	    List<String> sortedInfluencers = new ArrayList<>(followerCountMap.keySet());
	    sortedInfluencers.sort((userA, userB) -> followerCountMap.get(userB) - followerCountMap.get(userA));
	    
	    return sortedInfluencers;}



}
