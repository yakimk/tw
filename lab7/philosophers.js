const fs = require('fs');
const logFile = 'philosophers_logs.txt';
// fs.writeFileSync(logFile, '');

var advanced_logging = false;
var logging = false;
var timeWaitingBias = 50;
var timeWaitingMul = 100;

function log(forks) {
    if (advanced_logging) console.log(forks);
}

function log1(msg){
	if (logging){ console.log(msg)}
}


var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(ind, cb) { 
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
    this.f2 = (id + 1) % forks.length;
    this.waitingTime = 0;
    this.tableWaitingTime = 0;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    function iter(i) { 
        if (i < count) { 
            let delay = Math.floor((Math.random() * timeWaitingMul) + timeWaitingBias);
            setTimeout(() => {
                forks[f1].acquire(id, () => {
                    forks[f2].acquire(id, () => {
                        log(forks);
                        log1(`Philosopher ${id} is eating`);
                        setTimeout(() => {
                            forks[f1].release();
                            forks[f2].release();
                            log(forks);
                            log1(`Philosopher ${id} is thinking`);
                            iter(i + 1);
                        }, delay);
                    });
                });
            }, delay);
        } else {
            log1(`Finish ${id}.`);
        }
    }

    iter(0);
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        id = this.id;
    const firstFork = id % 2 === 0 ? this.f2 : this.f1;
    const secondFork = id % 2 === 0 ? this.f1 : this.f2;
    var self = this;

    function iter(i) { 
        if (i < count) { 
            let startWait = Date.now();
            let delay = Math.floor((Math.random() * timeWaitingMul) + timeWaitingBias);
            setTimeout(() => {
                forks[firstFork].acquire(id, () => {
                    forks[secondFork].acquire(id, () => {
                        let endWait = Date.now();
                        self.waitingTime += (endWait - startWait);
                        log(forks);
                        log1(`Philosopher ${id} is eating`);
                        setTimeout(() => {
                            forks[firstFork].release();
                            forks[secondFork].release();
                            log(forks);
                            log1(`Philosopher ${id} is thinking`);
                            iter(i + 1);
                        }, delay);
                    });
                });
            }, delay);
        } else {
            log1(`Finish ${id}. Total fork waiting time: ${self.waitingTime}ms`);
            fs.appendFileSync(
                logFile,
                `Philosopher ${id} (Asymmetric). Total fork waiting time: ${self.waitingTime}ms\n`
            );
        }
    }

    iter(0);
}

Philosopher.prototype.startConductor = function(count, conductor) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;
    var self = this;

    function iter(i) { 
        if (i < count) { 
            let startTableWait = Date.now();
            let delay = Math.floor((Math.random() * timeWaitingMul) + timeWaitingBias);
            setTimeout(() => {
                conductor.acquire(() => {
                    let endTableWait = Date.now();
                    self.tableWaitingTime += (endTableWait - startTableWait);
                    let startForkWait = Date.now();
                    forks[f1].acquire(id, () => {
                        forks[f2].acquire(id, () => {
                            let endForkWait = Date.now();
                            self.waitingTime += (endForkWait - startForkWait);
                            log(forks);
                            log1(`Philosopher ${id} is eating`);
                            setTimeout(() => {
                                forks[f1].release();
                                forks[f2].release();
                                conductor.leaveTable();
                                log(forks);
                                log1(`Philosopher ${id} is thinking`);
                                iter(i + 1);
                            }, delay);
                        });
                    });
                });
            }, delay);
        } else {
            log1(`Finish ${id}. Total table waiting time: ${self.tableWaitingTime}ms. Total fork waiting time: ${self.waitingTime}ms`);
            fs.appendFileSync(
                logFile,
                `Philosopher ${id} (Conductor). Total table waiting time: ${self.tableWaitingTime}ms. Total fork waiting time: ${self.waitingTime}ms\n`
            );
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

function runTest(method, iterations, N) {
    let forks = [];
    for (var i = 0; i < N; i++) forks.push(new Fork());
    let philosophers = [];
    for (var i = 0; i < N; i++) philosophers.push(new Philosopher(i, forks));

    console.log(`Running ${method} method with ${iterations} iterations and ${N} philosophers`);

    switch (method) {
        case "asym":
            for (var i = 0; i < N; i++) philosophers[i].startAsym(iterations);
            break;
        case "conduct":
            const conductor = new Conductor(N);
            for (var i = 0; i < N; i++) philosophers[i].startConductor(iterations, conductor);
            break;
        default:
            console.log(`Unknown method: ${method}`);
            break;
    }
}
var N = 5;
var iters = 10;
var forks = [];
var method = 'asym';
var philosophers = [];
var conductor = new Conductor(N);

if (process.argv.length >= 3) {
    method = process.argv[2];
    if (process.argv.length >= 4) {
        N = parseInt(process.argv[3], 10);
    }
    if (process.argv.length >= 5) {
        iters = parseInt(process.argv[4], 10);
    }

}

for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}


runTest(method, iters, N)
// switch (method) {
// 	case "naive":
// 		for (var i = 0; i < N; i++) {
// 		    philosophers[i].startNaive(iters);
// 		}
// 		break;
//
// 	case "asym":
// 		for (var i = 0; i < N; i++) {
// 		    philosophers[i].startAsym(iters);
// 		}
// 		break;
//
// 	case "conduct":
// 		for (var i = 0; i < N; i++) {
// 		    philosophers[i].startConductor(iters, conductor);
// 		}
// 		break;
// }
