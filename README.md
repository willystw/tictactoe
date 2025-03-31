## Overview 
This project is a Tic-Tac-Toe game implemented in Java using Spring Boot. It provides a RESTful API for players to create games, make moves, and track match history. The application supports real-time gameplay and maintains player statistics.

## Features
- Real-time multiplayer gameplay.
- RESTful API for creating and managing games. 
- Player statistics tracking (wins, losses). 
- Support for customizable board sizes. 
- Surrender functionality during an ongoing game.

## Installation
### Prerequisites
- Java 21 or higher
- Gradle 8.13

### Steps
1. Clone the repository: `git clone https://github.com/willystw/tictactoe.git`
2. Navigate to the project directory: `cd tictactoe`
3. Install dependencies: `./gradlew build`
4. Start the application: `./gradlew bootRun`

## Usage
### Creating a user
Send a POST request to create a new user:
```
curl --request POST \
  --url http://localhost:8080/api/v1/players/create \
  --header 'content-type: application/json' \
  --data '{
  "username": "player"
}'
```

### Creating a game
Send a POST request to create a new game:
```
curl --request POST \
  --url http://localhost:8080/api/v1/games/create \
  --header 'content-type: application/json' \
  --header 'x-user-id: 1' \
  --data '{
  "game_size": "3"
}'
```

### Joining a game
Send a POST request to join a game:
```
curl --request POST \
  --url http://localhost:8080/api/v1/games/join \
  --header 'content-type: application/json' \
  --header 'x-user-id: 2' \
  --data '{
  "game_id" : 1
}' 
```

### Making a Move
Send a POST request to make a move: 
``` 
curl --request POST \
  --url http://localhost:8080/api/v1/boards/move/1 \
  --header 'content-type: application/json' \
  --header 'x-user-id: 1' \
  --data '{
  "row": "0",
  "col": "3",
  "symbol": "O"
}'
```