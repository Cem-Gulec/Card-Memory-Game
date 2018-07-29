var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var playernum = 1;
var sayac=0;

server.listen(3000,function(){
	console.log('Server is running..');
});

io.on('connection',function(socket){
	console.log('player ' +playernum+ ' connected');
	playernum++;
	socket.emit('socketID', { id: socket.id });

	socket.broadcast.emit('newPlayer', { id: socket.id});

    socket.on('cevirEvent', function(cevir){
    	      cevir.id = socket.id;
    	      socket.broadcast.emit("cevirEventhappen", cevir);
    	})

	socket.on('scoreEvent', function(score){
	      score.id = socket.id;
	      socket.broadcast.emit("scoreEventhappen", score);
	})

	socket.on('hataEvent', function(hata){
	    hata.id = socket.id;
        socket.broadcast.emit("hataEventhappen", hata);
	})

	socket.on('join', function(userNickname){
	    socket.broadcast.emit('userjoinedthechat', userNickname+ " has joined");
	})

	/*socket.on('cardevent', function(onplanID,context){

	    let bilgi = {"onplanID": onplanID, "context":context  }
        socket.broadcast.emit("cardeventhappen", bilgi);

	});*/

	socket.on('disconnect',function(){
		playernum--;
		console.log('player ' +playernum+ ' disconnected');
		socket.broadcast.emit('playerDisconnected', {id :socket.id});

	});

});

