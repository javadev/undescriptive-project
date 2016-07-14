Dragons of Mugloar API
======================

Create a program which:
1.Fetches new game from game API
2.Optionally fetches weather from "third party" weather API
3.Solves the game by selectively distributing 20 points on a dragon stats
4.Sends the response to solve API
5.Logs down results somehow
6.Repeat from step 1 specified amount of times

Task is considered acceptable when the application is reliably able to achieve battle success ratio above 50% (higher the better). Once you are done, upload your accomplishment to version control, however keep the name of your project undescriptive to avoid someone stealing it. 

Start battle
GET http://www.dragonsofmugloar.com/api/game

Will return a game in JSON format
```javascript
{
    "gameId":483159,
    "knight": {
        "name": "Sir. Russell Jones of Alberta",
        "attack": 2,
        "armor": 7,
        "agility": 3,
        "endurance": 8
    }
}
```

Solve battle
PUT http://www.dragonsofmugloar.com/api/game/{gameid}/solution

PUT a message with an optional "Dragon" in the object to the specified URL. You must spread 20 points between all four stats.
```javascript
{
    "dragon": {
        "scaleThickness": 10,
        "clawSharpness": 5,
        "wingStrength": 4,
        "fireBreath": 1
    }
}
```

You will receive a message stating whether battle was successful or not, and if not then why not.
