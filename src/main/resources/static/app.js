var uid = randString(32);
var gid = null;
var gamestate = null;
var player = "zielony";
var socket;
var winner = 0;
var ended = 0;

var canvas;
var ctx;
var currentPlayer;
var arr = [
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0]];
var debug = 0;
var startedGame = 0;

var uri = "ws://localhost/ttt-websocket";

function connect(reconnected ) {
    var toEncode = "JOIN:" + gid + ":" + uid + ":" + playerNum();
    var msg = encode(toEncode);

    if (socket === undefined) {
        var sck = new WebSocket(uri);
        sck.binaryType = "arraybuffer";
        sck.onmessage = handleMessage;
        socket = sck;

        sck.onopen = function (frame) {
            console.log('Connected WS: ' + frame);
            sck.send(msg);
            if(reconnected) socket.send(encode("STATE:"+gid));
        };
    } else {
        socket.send(msg);
    }
    saveState();
}

function reconnectWS() {
    if(socket === undefined) {
        connect(1);
    }
}

function handleMessage(event) {
    if (event.data instanceof ArrayBuffer) {
        var decodedMsg = decode(event.data);
        if (decodedMsg === "STARTED") {
            winner = 0;
            ended = 0;
            startedGame = 1;
            gamestate = {started: true, disconnect: false, ended: false, draw: false}
        } else if (decodedMsg.startsWith("WINNER")) {
            winner = parseInt(decodedMsg[7]);
            ended = 1;
            gamestate = {started: true, disconnect: false, ended: true, draw: false}
        } else if (decodedMsg.startsWith("DRAW")) {
            ended = 1;
            gamestate = {started: true, disconnect: false, ended: true, draw: true}
        } else if (decodedMsg.startsWith("DISCONNECT")) {
            // socket.close();
            // socket = undefined;
            ended = 1;
            gamestate = {started: true, disconnect: true, ended: true, draw: false}
        } else {
            for (var i = 5; i >= 0; i--) {
                for (var j = 0; j < 7; j++) {
                    arr[5 - i][j] = parseInt(decodedMsg[i * 7 + j]);
                }
            }
            currentPlayer = parseInt(decodedMsg[42]);
            redrawBoard();
        }
        saveState();
        gameStatus();
    }
}

//
// function intToArrayBuffer(player, arr) {
//     var array = new Array(7 * 6 + 1);
//     for (var i = 0; i < 7; i++) {
//         for (var j = 0; j < 6; j++) {
//             array[i * 6 + j] = arr[i][j]
//         }
//     }
//     array.push(player);
//     return Uint8Array(array);
// }
//
// function arrayBufferToGameMatrix(buffArray) {
//     var array = [];
//     for (var i = 0; i < 7; i++) {
//         array.push(new Array());
//         for (var j = 0; j < 6; j++) {
//             array[i].push()
//         }
//     }
// }

function disconnect() {
    if (socket !== undefined) {
        socket.send(encode("DISCONNECT:" + gid + ":" + uid));
    }

    $('#disconnectModal').modal('hide');

    $("#menu").show();
    $("#tictactoe").hide();

    cleanGamestate();

    // if (gid != null) {
    //     $.ajax({
    //         url: "/ttt/game",
    //         type: "patch",
    //         data: {
    //             id: gid,
    //             player: uid,
    //             disconnect: true
    //         }
    //     }).done(refresh);
    // }


}

function rematch() {
    if (gamestate.winner || gamestate.draw) {
        $.ajax({
            url: "/ttt/game",
            type: "patch",
            data: {
                id: gid,
                player: uid,
                rematch: true
            }
        });
    }
}

function sendMove(col) {
    console.log("send move");
    var msg = "MOVE:" + playerNum() + ":" + col;
    if (isMyMove()) {
        socket.send(encode(msg));
    }
}

function refresh() {
    $.getJSON("/ttt/game/", function (data) {
        $("#games").empty();

        if (data.length > 0) {
            for (var game in data) {
                game = data[game];
                $("#games").append("<tr><td><button id=\"" + game.id + "\" class=\"btn btn-success btn-sm\">Dołącz</button>&nbsp;" + game.name + "</td></tr>");
            }
        }
        else {
            $("#games").append("<tr><td>Brak gier. Spróbuj stworzyć własną!</td></tr>");
        }
    });
}

function playerNum() {
    return player === "zielony" ? 1 : 2;
}

function create() {
    var name = $("#gamename").val() || undefined;

    $('#createGameModal').modal('hide');

    $.post({
        url: "/ttt/game",
        data: {
            // player: uid,
            player: "zielony",
            name: name
        }
    }).done(function (data) {
        console.log("Created Game");

        player = "zielony";

        $("#menu").hide();
        $("#tictactoe").show();

        gid = data.id;
        connect();
        initBoard();
        updateGamestate(data);

    });
}

function join(id) {

    $.ajax({
        url: "/ttt/game",
        type: "patch",
        data: {
            player: "zolty",
            id: id
        }
    }).done(function (data) {
        if (!data) {
            // alert("Game is no longer available.", refresh);
            refresh();
            return;
        }

        console.log("Dolaczono do gry");

        player = "zolty";

        $("#menu").hide();
        $("#tictactoe").show();

        gid = data.id;
        connect();
        initBoard();
        updateGamestate(data);
    });
}

function updateGamestate(data) {
    gamestate = data;

    $("#rematch").prop("disabled", !canRematch());

    gameStatus();
    saveState();
    drawBoard();
}

function canRematch() {
    return !gamestate.disconnect && (gamestate.winner || gamestate.draw)
}

function cleanGamestate() {
    gamestate = null;

    for (var x = 0; x < 3; x++) {
        for (var y = 0; y < 3; y++) {
            $("#".concat(x).concat(y)).text("");
        }
    }
}

function drawBoard() {
    redrawBoard();
}

//todo
function gameStatus() {
    var status = "";
    var status2 = "";

    // status
    if (!gamestate.started) {
        status = "Oczekiwanie na drugiego gracza...";
    }
    else if (gamestate.disconnect) {
        status = "Przeciwnik opuścił gre !"
    }
    else {
        var player2 = (playerNum() === 1) ? "zolty" : "zielony";
        status = "Gracz '" + player2 + "' dołączył! Twój kolor: '" + player + "'."
    }

    // status2

    if (gamestate.started && !gamestate.ended && !gamestate.draw) {
        if (playerNum() === currentPlayer) {
            status2 = "Twoj ruch";
        } else {
            status2 = "Ruch przeciwnika"
        }
    }
    else if (gamestate.ended && !gamestate.disconnect) {
        if (winner === playerNum()) {
            status2 = "Wygrales !";
        }
        else {
            status2 = "Przegrales!";
        }
    }
    else if (gamestate.draw) {
        status2 = "Remis!"
    }
    else {
        status2 = "";
    }

    $("#status").text(status);
    $("#status2").text(status2);
}

function isMyMove() {
    return playerNum() === currentPlayer;
}

function randString(length) {
    var text = "";
    var alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < length; i++) {
        text += alphanum.charAt(Math.floor(Math.random() * alphanum.length));
    }

    return text;
}

var unloadCalled = false;

function doUnload() {
    if (!unloadCalled) {
        unloadCalled = true;
        disconnect();
    }
};

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $("#tictactoe").hide();

    $("#refresh").click(function () {
        refresh();
    });
    $("#create").click(function () {
        create();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#rematch").click(function () {
        rematch();
    });

    $("#games").on("click", "button", function () {
        join($(this).attr('id'));
    });


    refresh();
});


var inited = 0;

function initBoard() {
    startedGame = 0;
    currentPlayer = 1;


    canvas = document.getElementById('connectFour');
    ctx = canvas.getContext('2d');
    ctx.strokeStyle = '#000000';
    ctx.beginPath();
    ctx.moveTo(174, 67);
    ctx.lineTo(177, 455);
    ctx.stroke();
    ctx.closePath();

// fill with gray color
    ctx.fillStyle = "gray";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    if (inited !== 1) {
        canvas.addEventListener('mousedown', function (e) {
            drawOnBox(canvas, e);
            inited = 1;
        });
    }


    for (var i = 0; i < 7; i++) {
        for (var j = 0; j < 6; j++) {

            ctx.strokeStyle = '#000000';
            ctx.lineWidth = 3;
            ctx.beginPath();
            ctx.arc(i * 100 + 50, j * 100 + 50, 37, 0, 2 * Math.PI);
            ctx.stroke();

            ctx.beginPath();
            ctx.fillStyle = '#FFFFFF';
            ctx.arc(i * 100 + 50, j * 100 + 50, 35, 0, 2 * Math.PI);
            ctx.fill();
        }
    }

    arr = [
        [0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0]];
}

function redrawBoard() {
    for (var i = 0; i < 6; i++) {
        for (var j = 0; j < 7; j++) {
            ctx.strokeStyle = '#000000';
            ctx.lineWidth = 3;
            ctx.beginPath();
            ctx.arc(i * 100 + 50, j * 100 + 50, 37, 0, 2 * Math.PI);
            ctx.stroke();

            ctx.beginPath();
            ctx.fillStyle = '#FFFFFF';
            ctx.arc(i * 100 + 50, j * 100 + 50, 35, 0, 2 * Math.PI);
            ctx.fill();
        }
    }
    for (var i = 0; i < 6; i++) {
        for (var j = 0; j < 7; j++) {
            if (arr[i][j] !== 0) {
                ctx.beginPath();
                if (arr[i][j] === 2) {
                    ctx.fillStyle = '#FFFF00';
                } else {
                    ctx.fillStyle = '#00FF00';
                }
                ctx.arc(j * 100 + 50, i * 100 + 50, 32, 0, 2 * Math.PI);
                ctx.fill();
            }
        }
    }
}

// function fillXYReversed(x, y, player) {
//     var canvas = document.getElementById('connectFour');
//     var ctx = canvas.getContext('2d');
//     ctx.beginPath();
//
//     if (player === 1) {
//         ctx.fillStyle = '#FFFF00';
//     } else {
//         ctx.fillStyle = '#00FF00';
//     }
//     ctx.arc(x * 100 + 50, y * 100 + 50, 32, 0, 2 * Math.PI);
//     ctx.fill();
//     if (debug) console.log(x + "," + y + " -> " + player);
// }
//
// function fillColRow(x, y, player) {
//     // fillXYReversed(x - 1, 6 - y, player)
// }

function drawOnBox(canvas, event) {
    if (!startedGame || ended) {
        return;
    }
    var rect = canvas.getBoundingClientRect();
    var x = event.clientX - rect.left;
    var col = Math.floor(x / 100);

    sendMove(col);
}

var enc = new TextEncoder();
var dec = new TextDecoder();

function encode(str) {
    return enc.encode(str);
}

function decode(str) {
    return dec.decode(str);
}

// function intToArrayBuffer(player, arr) {
//     var array = new Array();
//     for (var i = 0; i < arr.length; i++) {
//         for (var j = 0; j < arr[i].length; j++) {
//             array.push(arr[i][j]);
//         }
//     }
//     array.push(player);
//     return new Uint8Array(array);
// }
//
// function makeMoveOnColumn(col) {
//     for (var i = 0; i < 6; i++) {
//         if (arr[i][col] == 0) {
//             arr[i][col] = player;
//         }
//     }
// }
//
// function arrayBufferToGameMatrix(buffArray) {
//     var array = [];
//     for (var i = 0; i < 6; i++) {
//         array.push([]);
//         for (var j = 0; j < 7; j++) {
//             array[i].push(buffArray[i * 7 + j])
//         }
//     }
//     // var player = buffArray[7 * 6];
//     return [buffArray[6 * 7], array];
// }


var storage = window.sessionStorage;

var localStorageStateKey = 'state';

function saveState() {
    storage.setItem(localStorageStateKey,
        JSON.stringify({
            uid: uid,
            gid: gid,
            gamestate: gamestate,
            player: player,
            startedGame: startedGame,
            currentPlayer: currentPlayer
        })
    );
}

function updateStateFromLocalStorage() {
    var json = JSON.parse(storage.getItem(localStorageStateKey));
    uid = json.uid;
    gid = json.gid;
    gamestate = json.gamestate;
    player = json.player;
    startedGame = json.startedGame;
    currentPlayer = json.currentPlayer;
    if(gid !== null){
        $("#menu").hide();
        $("#tictactoe").show();
        reconnectWS();
        initBoard();
        redrawBoard();
        gameStatus();
    }

}

function checkIfReloaded() {
    if (storage.getItem(localStorageStateKey) !== null) {
        updateStateFromLocalStorage()
    }
}
window.onload = checkIfReloaded;
// var stompClient = null;
// var uid = randString(32);
// var gid = null;
// var gamestate = null;
// var player = "zielony";
// var socket;
// var winner = 0;
// var ended = 0;
//
// var canvas;
// var ctx;
// var currentPlayer;
// var arr = [
//     [0, 0, 0, 0, 0, 0, 0],
//     [0, 0, 0, 0, 0, 0, 0],
//     [0, 0, 0, 0, 0, 0, 0],
//     [0, 0, 0, 0, 0, 0, 0],
//     [0, 0, 0, 0, 0, 0, 0],
//     [0, 0, 0, 0, 0, 0, 0]];
// var debug = 0;
// var startedGame = 0;

