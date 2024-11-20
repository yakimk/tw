function printAsync(s, cb) {
   var delay = Math.floor((Math.random()*100)+500);
   setTimeout(function() {
       console.log(s);
       if (cb) cb();
   }, delay);
}

function task1(cb) {
    printAsync("1", function() {
        task2(cb);
    });
}

function task2(cb) {
    printAsync("2", function() {
        task3(cb);
    });
}

function task3(cb) {
    printAsync("3", cb);
}

// wywolanie sekwencji zadan


/* 
** Zadanie:
** Napisz funkcje loop(n), ktora powoduje wykonanie powyzszej 
** sekwencji zadan n razy. Czyli: 1 2 3 1 2 3 1 2 3 ... done
** 
*/

function loop(n) {

	function innerLoop (i) {
		if (i < n){
			task1(function () {
				innerLoop(i + 1)
			});
		}else {
			console.log("Done");
		}
	}

	innerLoop(0);
}

loop(4);
