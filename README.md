# Twitter Feed

## Build

The project includes the Maven Wrapper JAR, to build:

`./mvnw clean compile`

## Test

`./mvnw clean test`

## Package

This project packages into a single JAR file:

`./mvnw clean package`

## Testing

`./mvnw clean test`

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

the tweeter and the tweet message must be split by 2 charaters:

`> `

A `>` and a ` ` must be present.
