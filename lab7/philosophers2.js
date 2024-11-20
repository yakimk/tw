// Widelec
var Fork = function() {
    this.state = 0; // 0 - wolny, 1 - zajęty
    return this;
};

Fork.prototype.acquire = function(cb, delay = 1) {
    const tryAcquire = () => {
        if (this.state === 0) {
            this.state = 1;
            cb(); // Udało się zdobyć widelec
        } else {
            setTimeout(() => tryAcquire(), delay); // Spróbuj ponownie z opóźnieniem
            delay *= 2; // BEB - podwajanie opóźnienia
        }
    };
    tryAcquire();
};

Fork.prototype.release = function() {
    this.state = 0; // Zwolnienie widelca
};

// Filozof
var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id + 1) % forks.length;
    return this;
};

// Naiwne podejście
Philosopher.prototype.startNaive = function(count) {
    const forks = this.forks;
    const f1 = this.f1;
    const f2 = this.f2;
    const id = this.id;

    const eatCycle = (n) => {
        if (n <= 0) return;

        forks[f1].acquire(() => {
            forks[f2].acquire(() => {
                console.log(`Philosopher ${id} is eating.`);
                setTimeout(() => {
                    forks[f2].release();
                    forks[f1].release();
                    console.log(`Philosopher ${id} is thinking.`);
                    eatCycle(n - 1);
                }, Math.random() * 100); // Jedzenie trwa losowy czas
            });
        });
    };

    eatCycle(count);
};

// Asymetryczne podejście
Philosopher.prototype.startAsym = function(count) {
    const forks = this.forks;
    const f1 = this.f1;
    const f2 = this.f2;
    const id = this.id;
    const firstFork = id % 2 === 0 ? f2 : f1;
    const secondFork = id % 2 === 0 ? f1 : f2;

    const eatCycle = (n) => {
        if (n <= 0) return;

        forks[firstFork].acquire(() => {
            forks[secondFork].acquire(() => {
                console.log(`Philosopher ${id} is eating.`);
                setTimeout(() => {
                    forks[secondFork].release();
                    forks[firstFork].release();
                    console.log(`Philosopher ${id} is thinking.`);
                    eatCycle(n - 1);
                }, Math.random() * 100); // Jedzenie trwa losowy czas
            });
        });
    };

    eatCycle(count);
};

// Lokaj
var Conductor = function(maxActive) {
    this.state = maxActive; // Maksymalna liczba aktywnych filozofów
    return this;
};

Conductor.prototype.acquire = function(cb, delay = 1) {
    const tryAcquire = () => {
        if (this.state > 0) {
            this.state--;
            cb(); // Udało się zdobyć zgodę lokaja
        } else {
            setTimeout(() => tryAcquire(), delay); // Spróbuj ponownie z opóźnieniem
            delay *= 2; // BEB - podwajanie opóźnienia
        }
    };
    tryAcquire();
};

Conductor.prototype.release = function() {
    this.state++;
};

Philosopher.prototype.startConductor = function(count, conductor) {
    const forks = this.forks;
    const f1 = this.f1;
    const f2 = this.f2;
    const id = this.id;

    const eatCycle = (n) => {
        if (n <= 0) return;

        conductor.acquire(() => {
            forks[f1].acquire(() => {
                forks[f2].acquire(() => {
                    console.log(`Philosopher ${id} is eating.`);
                    setTimeout(() => {
                        forks[f2].release();
                        forks[f1].release();
                        conductor.release();
                        console.log(`Philosopher ${id} is thinking.`);
                        eatCycle(n - 1);
                    }, Math.random() * 100); // Jedzenie trwa losowy czas
                });
            });
        });
    };

    eatCycle(count);
};

// Eksperyment
var N = 5; // Liczba filozofów
var forks = [];
var philosophers = [];
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

var conductor = new Conductor(N - 1); // Maksymalnie N-1 aktywnych filozofów

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

// Uruchamianie eksperymentów
console.log("Starting naive approach...");
for (var i = 0; i < N; i++) {
    philosophers[i].startNaive(10); // Naiwne podejście (prawdopodobnie deadlock)
}

setTimeout(() => {
    console.log("\nStarting asymmetric approach...");
    for (var i = 0; i < N; i++) {
        philosophers[i].startAsym(10); // Asymetryczne podejście
    }
}, 10000);

setTimeout(() => {
    console.log("\nStarting conductor approach...");
    for (var i = 0; i < N; i++) {
        philosophers[i].startConductor(10, conductor); // Rozwiązanie z lokajem
    }
}, 20000);

