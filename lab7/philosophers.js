var logging = true;

function log(forks) {
	if (logging) console.log(forks);
}

var Fork = function() {
    this.state = 0;
    return this;
}

function printForkState(forks) {
	console.log(forks);
}

Fork.prototype.acquire = function(ind, cb) { 
    // zaimplementuj funkcje acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwsza proba podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy proba jest nieudana, zwieksza czas oczekiwania dwukrotnie
    //    i ponawia probe itd.
    const checkState = (waitTime) => {
        setTimeout(() => {
            if (this.state !== 0) {
                checkState(waitTime * 2);
            } else {
                this.state = ind + 1;
                if (cb) cb();
            }
        }, waitTime);
    };

    checkState(1);
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    
    // zaimplementuj rozwiazanie naiwne
    // kazdy filozof powinien 'count' razy wykonywac cykl
    // podnoszenia widelcow -- jedzenia -- zwalniania widelcow

	function iter(i) { 
		if ( i < count) { 
			delay = Math.floor((Math.random()*100)+500);
			setTimeout ( () => {
			delay = Math.floor((Math.random()*100)+500);
			forks[f1].acquire(id, () => {
				log(forks);;
				forks[f2].acquire(id, () => {
					log(forks);
					console.log(`Philosopher ${id} is eating`);

					setTimeout ( () => {
						forks[f1].release();
						forks[f2].release();
						log(forks);
						console.log(`Philosopher ${id} is thinking`);
						iter (i + 1);
					}, delay);
					});
			});
			}, delay);
			
		}else {
			console.log("Finish " + id);
		}
	}
	iter(0);
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        id = this.id;
	f1 = this.f1
	f2 = this.f2
	    const firstFork = id % 2 === 0 ? f2 : f1;
	    const secondFork = id % 2 === 0 ? f1 : f2;
	console.log(id + " " + f1 + " "+  f2)
    
    // zaimplementuj rozwiazanie asymetryczne
    // kazdy filozof powinien 'count' razy wykonywac cykl
    // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
	function iter(i) { 
		if ( i < count) { 
			delay = Math.floor((Math.random()*100)+500);
			setTimeout ( () => {
			delay = Math.floor((Math.random()*100)+500);
			forks[firstFork].acquire(id, () => {
				log(forks);
				forks[secondFork].acquire(id, () => {
					log(forks);
					console.log(`Philosopher ${id} is eating`);

					setTimeout ( () => {
						forks[firstFork].release();
						forks[secondFork].release();
						log(forks);
						console.log(`Philosopher ${id} is thinking`);
						iter (i + 1);
					}, delay);
					});
			});
			}, delay)
		}else {
			console.log("Finish " + id);
		}
	}

	iter(0);
}


var Conductor = function(n) { 
	this.n = n;
	this.current = 0;
}

Conductor.prototype.acquire = function(cb) {
    const checkState = (waitTime) => {
        setTimeout(() => {
            if (this.n == this.current + 1) {
                checkState(waitTime * 2);
            } else {
                this.current++;
                if (cb) cb();
            }
        }, waitTime);
    };

    checkState(1);
}

Conductor.prototype.leaveTable = function() {
	this.current--;
}

Philosopher.prototype.startConductor = function(count, conductor) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    
    // zaimplementuj rozwiazanie z kelnerem
    // kazdy filozof powinien 'count' razy wykonywac cykl
    // podnoszenia widelcow -- jedzenia -- zwalniania widelcow


	function iter(i) { 
		if ( i < count) { 

			delay = Math.floor((Math.random()*100)+500);
			setTimeout ( () => {
			conductor.acquire( () => {
			delay = Math.floor((Math.random()*100)+500);
			forks[f1].acquire(id, () => {
				log(forks);;
				forks[f2].acquire(id, () => {
					log(forks);
					console.log(`Philosopher ${id} is eating`);

					setTimeout ( () => {
						forks[f1].release();
						forks[f2].release();
								conductor.leaveTable();
						log(forks);
						console.log(`Philosopher ${id} is thinking`);
						iter (i + 1);
					}, delay);
					});
			});
				});
			}, delay);
			
		}else {
			console.log("Finish " + id);
		}
	}
	iter(0);
}


var N = 5;
var forks = [];
var philosophers = []
var conductor = new Conductor(N);
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

for (var i = 0; i < N; i++) {
    philosophers[i].startConductor(10, conductor);
}
