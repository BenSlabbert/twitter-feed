package com.ben.twitter;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.*;

public class TwitterFeedTest {

    private TwitterFeed twitterFeed;

    @Before
    public void setUp() {
        twitterFeed = new TwitterFeed();
    }

    @Test
    public void testOutput_1() {
        TwitterFeed.main(new String[]{"src/test/resources/users_1.txt", "src/test/resources/tweets_1.txt"});
    }

    @Test
    public void testOutput_2() {
        TwitterFeed.main(new String[]{"src/test/resources/users_2.txt", "src/test/resources/tweets_2.txt"});
    }

    @Test
    public void testOutput_3() {
        TwitterFeed.main(new String[]{"src/test/resources/users_3.txt", "src/test/resources/tweets_3.txt"});
    }

    @Test(expected = RuntimeException.class)
    public void testOutput_4() {
        TwitterFeed.main(new String[]{""});
    }

    @Test(expected = RuntimeException.class)
    public void testOutput_5() {
        TwitterFeed.main(new String[]{"src/test/resources/users_3.txt", ""});
    }

    @Test(expected = RuntimeException.class)
    public void testOutput_6() {
        TwitterFeed.main(new String[]{"", "src/test/resources/tweets_3.txt"});
    }

    @Test
    public void getUsersFromFileTest_1() {

        String path = "src/test/resources/users_1.txt";
        Map<String, List<String>> users = ReflectionTestUtils.invokeMethod(twitterFeed, "getUsersFromFile", path);

        assertNotNull(users);

        assertEquals(3, users.size());

        // validate allan
        assertTrue(users.containsKey("Alan"));
        List<String> alan = users.get("Alan");
        assertEquals(1, alan.size());
        assertEquals("Martin", alan.get(0));

        // validate ward
        assertTrue(users.containsKey("Ward"));
        List<String> ward = users.get("Ward");
        assertEquals(2, ward.size());
        assertEquals("Martin", ward.get(0));
        assertEquals("Alan", ward.get(1));
    }

    @Test
    public void getUsersFromFileTest_2() {

        String path = "src/test/resources/users_2.txt";
        Map<String, List<String>> users = ReflectionTestUtils.invokeMethod(twitterFeed, "getUsersFromFile", path);

        assertNotNull(users);

        assertEquals(3, users.size());

        // validate ward
        assertTrue(users.containsKey("Ward"));
        List<String> ward = users.get("Ward");
        assertEquals(1, ward.size());
        assertEquals("Martin", ward.get(0));
    }

    @Test
    public void getTweetsFromFileTest_1() {

        String path = "src/test/resources/tweets_1.txt";
        List<String> tweets = ReflectionTestUtils.invokeMethod(twitterFeed, "getTweetsFromFile", path);

        assertNotNull(tweets);

        assertEquals(3, tweets.size());

        assertEquals("Alan> If you have a procedure with 10 parameters, you probably missed some.", tweets.get(0));
        assertEquals("Ward> There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors.", tweets.get(1));
        assertEquals("Alan> Random numbers should not be generated with a method chosen at random.", tweets.get(2));
    }

    @Test
    public void getOrderedNamesTest_1() {

        Map<String, List<String>> users = new HashMap<>();
        users.put("aaaa", Collections.EMPTY_LIST);
        users.put("bbbb", Collections.EMPTY_LIST);

        List<String> orderedNames = ReflectionTestUtils.invokeMethod(twitterFeed, "getOrderedNames", users);

        assertNotNull(orderedNames);

        assertEquals(2, orderedNames.size());
        assertEquals("aaaa", orderedNames.get(0));
        assertEquals("bbbb", orderedNames.get(1));
    }

    @Test
    public void getTweetsForUserTest_1() {

        Map<String, List<String>> users = new HashMap<>();

        ArrayList<String> follows = new ArrayList<>();
        follows.add("Ward");
        users.put("Alan", follows);

        follows = new ArrayList<>();
        users.put("Ward", follows);

        List<String> tweets = new ArrayList<>();
        tweets.add("Ward> tweet 1");
        tweets.add("Ward> tweet 2");

        Map<String, List<String>> userTweetsMap = ReflectionTestUtils.invokeMethod(twitterFeed, "getTweetsForUser", users, tweets);

        assertNotNull(userTweetsMap);
        assertEquals(2, userTweetsMap.size());

        assertEquals("@Ward: tweet 1", userTweetsMap.get("Ward").get(0));
        assertEquals("@Ward: tweet 2", userTweetsMap.get("Ward").get(1));

        assertEquals("@Ward: tweet 1", userTweetsMap.get("Alan").get(0));
        assertEquals("@Ward: tweet 2", userTweetsMap.get("Alan").get(1));
    }

    @Test
    public void getTweetsForUserTest_2() {

        Map<String, List<String>> users = new HashMap<>();

        ArrayList<String> follows = new ArrayList<>();
        follows.add("Ward");
        users.put("Alan", follows);

        follows = new ArrayList<>();
        users.put("Ward", follows);

        List<String> tweets = new ArrayList<>();
        tweets.add("Alan> tweet 1");
        tweets.add("Alan> tweet 2");

        Map<String, List<String>> userTweetsMap = ReflectionTestUtils.invokeMethod(twitterFeed, "getTweetsForUser", users, tweets);

        assertNotNull(userTweetsMap);
        assertEquals(1, userTweetsMap.size());

        assertEquals("@Alan: tweet 1", userTweetsMap.get("Alan").get(0));
        assertEquals("@Alan: tweet 2", userTweetsMap.get("Alan").get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTweetsForUserTest_3_tweetTooLong() {

        Map<String, List<String>> users = new HashMap<>();

        ArrayList<String> follows = new ArrayList<>();
        follows.add("Ward");
        users.put("Alan", follows);

        // create too long tweet
        char[] chars = new char[141];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = 'a';
        }

        String tooLong = new String(chars);

        List<String> tweets = new ArrayList<>();
        tweets.add("Alan> " + tooLong);

        ReflectionTestUtils.invokeMethod(twitterFeed, "getTweetsForUser", users, tweets);
    }
}
