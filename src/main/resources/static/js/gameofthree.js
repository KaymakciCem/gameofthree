let stompClient = null;
let game = null;

/**
 * Sends a message to the server using the STOMP client.
 * @param {Object} message - The message to be sent. Must contain at least a "type" field.
 */
const sendMessage = (message) => {
    console.log(message);
    stompClient.send(`/app/${message.type}`, {}, JSON.stringify(message));
}

/**
 * An object containing functions to handle each type of message received from the server.
 */
const messagesTypes = {
    "game.join": (message) => {
        updateGame(message);
    },
    "game.move": (message) => {
        updateGame(message);
    }
}

/**
 * Handles a message received from the server.
 * @param {Object} message - The message received.
 */
const handleMessage = (message) => {
    console.log("handle message executed cem");

    if (messagesTypes[message.type])
        messagesTypes[message.type](message);
}

/**
 * Converts a message received from the server into a game object.
 * @param {Object} message - The message received.
 * @returns {Object} The game object.
 */
const messageToGame = (message) => {
    return {
        gameStatus: message.gameStatus,
        gameId: message.gameId,
        playerId: message.playerId,
        outputNumber: message.outputNumber.value,
        addedNumber: message.addedNumber,
        winner: message.winner
    }
}

/**
 * Starts the process of joining a game. Asks the player to enter their name and sends a message to the server requesting to join the game.
 */
const joinGame = (gameId) => {
    sendMessage({
        type: "game.join",
        gameId: gameId,
        playerName: "cem",
        playMode: "AUTO"
    });
}

/**
 * Connects the STOMP client to the server and subscribes to the "/topic/game.state" topic.
 */
const connect = () => {
    console.log("connect executed cem");

    const socket = new SockJS('/game-of-three-ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/game.state', function (message) {
            handleMessage(JSON.parse(message.body));
        });
    });
}

const updateGame = (message) => {
    game = messageToGame(message);

    var gameBoardDiv = document.getElementById("gameBoard");

    var gameRound = document.createElement('div');
    gameRound.innerHTML = 'GAME ROUND!';

    var currentPlayer = document.createElement('div');
    currentPlayer.innerHTML = 'current player: ' + game.playerId;

    var resultingNumber = document.createElement('div');
    resultingNumber.innerHTML = 'output number: ' + game.outputNumber;

    var addedNumber = document.createElement('div');
    addedNumber.innerHTML = 'added number: ' + game.addedNumber;

    var winner = document.createElement('div');
    winner.innerHTML = 'winner: ' + game.winner;

    gameBoardDiv.appendChild(gameRound);
    gameBoardDiv.appendChild(currentPlayer);
    gameBoardDiv.appendChild(resultingNumber);
    gameBoardDiv.appendChild(addedNumber);
    gameBoardDiv.appendChild(winner);
}

window.onload = function () {
    connect();
}
