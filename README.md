# Twitter Feed

## Requirements

* Java 1.8 JDK

## Build

The project includes the Maven Wrapper JAR, to build:

`./mvnw clean compile`

## Test

`./mvnw clean test`

## Package

This project packages into a single JAR file:

`./mvnw clean package`

## Example Demo

`java -jar target/twitter-feed.jar ./src/test/resources/users_1.txt ./src/test/resources/tweets_1.txt`

`java -jar target/twitter-feed.jar ./src/test/resources/users_2.txt ./src/test/resources/tweets_2.txt`

`java -jar target/twitter-feed.jar ./src/test/resources/users_3.txt ./src/test/resources/tweets_3.txt`

## Assumptions

1.) `users.txt`

If the same user if defined more than once, the last entry will replace the earlier entry.
For example:

Ward follows Alan

Ward follows Martin

This will result in `Ward` following `Martin` and **not** `Alan`.

2.) `users.txt`

The separating string is as follows: ` follows `

Example of an acceptable entry:

`Ward follows Martin, Alan`

Example of an unacceptable entry:

`Ward follow Martin, Alan`

3.) `tweets.txt`

If the message of the tweet is longer than 140 characters, an exception will be thrown
and the program will stop executing.

4.) `tweets.txt`

the tweeter and the tweet message must be split by 2 characters:

`> `

A `>` and a ` ` must be present.
