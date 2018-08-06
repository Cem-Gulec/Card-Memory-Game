var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var playernum = 0;
var sayac=0;
var arr = [];

function fillArray() {
	arr = [];
	while(arr.length < 16){
	var randomnumber = Math.floor(Math.random()*16) + 1;
	if(arr.indexOf(randomnumber) > -1) continue;
	arr[arr.length] = randomnumber;
}
}

server.listen(3000,function(){
	console.log('Server is running..');
	fillArray();
});

io.on('connection',function(socket){
	playernum++;
	console.log('player ' +playernum+ ' connected');
  	socket.emit('socketID', { id: socket.id , array: arr });


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



	socket.on('disconnect',function(){
		playernum--;
		console.log('player ' +playernum+ ' disconnected');
		socket.broadcast.emit('playerDisconnected', {id :socket.id});
		if(playernum==0){
 		fillArray();
 		}
	});

});

