package com.ben.twitter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TwitterFeed {

    public static void main(String[] args) {

        // args [0] - path to user.txt
        // args [1] - path to tweet.txt

        if (args.length != 2) {
            throw new RuntimeException("Expected 2 arguments to be provided");
        }

        // get users and list the accounts they follow from file
        Map<String, List<String>> users;
        try {
            users = getUsersFromFile(args[0]);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read users file: " + e.getMessage());
        }

        // get tweets from file
        List<String> tweets;
        try {
            tweets = getTweetsFromFile(args[1]);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read tweets file: " + e.getMessage());
        }

        // get list of tweets for each user
        Map<String, List<String>> tweetsForEachUser = getTweetsForUser(users, tweets);

        // get ordered list of people
        List<String> orderedNames = getOrderedNames(users);

        // print out tweets for each user
        printOutput(tweetsForEachUser, orderedNames);
    }

    private static void printOutput(Map<String, List<String>> tweetsForEachUser, List<String> orderedNames) {

        for (String orderedName : orderedNames) {
            System.out.println(orderedName);

            if (!tweetsForEachUser.containsKey(orderedName)) continue;

            for (String s : tweetsForEachUser.get(orderedName)) {
                System.out.println(s);
            }
        }
    }

    private static Map<String, List<String>> getTweetsForUser(Map<String, List<String>> users, List<String> tweets) {

        Map<String, List<String>> userTweetsMap = new HashMap<>();

        for (String tweet : tweets) {

            String[] split = tweet.split("> ");
            String name = split[0];
            String tweetMessage = "@" + name + ": " + split[1];

            // list of tweets for each user
            addToUsersTimeLine(userTweetsMap, name, tweetMessage);

            // add tweets to follows
            updateFollowersTimeLine(users, userTweetsMap, name, tweetMessage);
        }

        return userTweetsMap;
    }

    private static void updateFollowersTimeLine(Map<String, List<String>> users,
                                                Map<String, List<String>> userTweetsMap,
                                                String name,

                                                String tweetMessage) {

        for (Map.Entry<String, List<String>> entry : users.entrySet()) {
            String userName = entry.getKey();
            List<String> userFollowingList = entry.getValue();

            if (userFollowingList.contains(name)) {
                addToUsersTimeLine(userTweetsMap, userName, tweetMessage);
            }
        }
    }

    private static void addToUsersTimeLine(Map<String, List<String>> userTweetsMap, String name, String tweetMessage) {

        if (!userTweetsMap.containsKey(name)) {
            addNew(userTweetsMap, name, tweetMessage);
        } else {
            updateExisting(userTweetsMap, name, tweetMessage);
        }
    }

    private static void addNew(Map<String, List<String>> userTweetsMap, String name, String tweet) {

        ArrayList<String> tweets = new ArrayList<>();
        tweets.add(tweet);
        userTweetsMap.put(name, tweets);
    }

    private static void updateExisting(Map<String, List<String>> userTweetsMap, String name, String tweet) {

        List<String> tweets = userTweetsMap.get(name);
        tweets.add(tweet);

        userTweetsMap.put(name, tweets);
    }

    private static List<String> getOrderedNames(Map<String, List<String>> users) {

        List<String> names = new ArrayList<>(users.keySet());
        names.sort(String::compareTo);
        return names;
    }

    private static List<String> getTweetsFromFile(String path) throws IOException {

        Path p = Paths.get(path);
        return Files.readAllLines(p);
    }

    private static Map<String, List<String>> getUsersFromFile(String path) throws IOException {

        Map<String, List<String>> userMap = new HashMap<>();

        Path p = Paths.get(path);
        List<String> lines = Files.readAllLines(p);

        for (String line : lines) {
            String[] split = line.split(" follows ");

            // name at split[0]
            // list of people they follow at split [1]

            String[] following = split[1].split(", ");

            addUsersWhoDoNotFollow(userMap, following);

            userMap.put(split[0], Arrays.asList(following));
        }

        return userMap;
    }

    private static void addUsersWhoDoNotFollow(Map<String, List<String>> userMap, String[] following) {

        for (String f : following) {
            if (!userMap.containsKey(f)) {
                userMap.put(f, new ArrayList<>());
            }
        }
    }
}
