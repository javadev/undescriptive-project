# Dragons of Mugloar solution
Simple java library to solve Dragons of Mugloar game.

[![Maven Central](https://img.shields.io/maven-central/v/com.github.javadev/Dragons-of-Mugloar-solution.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.javadev%22%20AND%20a%3A%22Dragons-of-Mugloar-solution%22)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/javadev/undescriptive-project/blob/master/LICENSE)
[![Build Status](https://secure.travis-ci.org/javadev/undescriptive-project.svg)](https://travis-ci.org/javadev/undescriptive-project)
[![Coverage Status](https://coveralls.io/repos/javadev/undescriptive-project/badge.svg?branch=master)](https://coveralls.io/r/javadev/undescriptive-project)
[![codecov.io](http://codecov.io/github/javadev/undescriptive-project/coverage.svg?branch=master)](http://codecov.io/github/javadev/undescriptive-project?branch=master)
[![Circle CI](https://circleci.com/gh/javadev/undescriptive-project.svg?style=badge)](https://circleci.com/gh/javadev/undescriptive-project)
[![Build Status](https://drone.io/github.com/javadev/undescriptive-project/status.png)](https://drone.io/github.com/javadev/undescriptive-project/latest)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1ef9ebbd64c64745915d78621af8bb7a)](https://www.codacy.com/app/javadev75/undescriptive-project?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=javadev/undescriptive-project&amp;utm_campaign=Badge_Grade)
[![Dependency Status](https://www.versioneye.com/user/projects/578ef4b688bf880040a26eae/badge.svg?style=flat)](https://www.versioneye.com/user/projects/578ef4b688bf880040a26eae)

[![Join the chat at https://gitter.im/javadev/undescriptive-project](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/javadev/undescriptive-project?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

1. [Add new module to your project](#1-add-new-module-to-your-project)
2. [Create client](#2-create-client)
3. [Load a game](#3-load-a-game)
4. [Load weather information](#4-load-weather-information)
5. [Generate game solution](#5-generate-game-solution)
6. [Submit solution to a server](#6-submit-solution-to-a-server)
7. [Check results](#7-check-results)


###1. Add new module to your project
Include the following in your `pom.xml` for Maven:

```
<dependencies>
  <dependency>
    <groupId>com.github.javadev</groupId>
    <artifactId>Dragons-of-Mugloar-solution</artifactId>
    <version>1.1</version>
  </dependency>
  ...
</dependencies>
```

###2. Create client

```java
GameClient client = AsyncClient.createDefault();
```

###3. Load a game

```java
GameResponse game = client.getGame().get();
```

###4. Load weather information

```java
WeatherResponse weatherResponse = client.getWeather(game.getGameId()).get();
```

###5. Generate game solution

```java
SolutionRequest request = client.generateGameSolution(game.getGameResponseItem(), weatherResponse);
```

###6. Submit solution to a server

```java
SolutionResponse response = client.sendSolution(game.getGameId(), request).get();
```

###7. Check results

```java
if ("Victory".equals(response.getStatus())) {
    // We win a game
} else if ("SRO".equals(weatherResponse.getCode())) {
    // Storm weather
}
```
