var Fork = function() {
    this.state = 0; // 0 means the fork is free
    return this;
}

Fork.prototype.acquire = function(ind, cb) {
    // Use Binary Exponential Backoff algorithm for retrying
    const checkState = (waitTime) => {
        setTimeout(() => {
            if (this.state !== 0) {
                // If fork is taken, double the wait time and retry
                checkState(waitTime * 2);
            } else {
                // Acquire the fork
                this.state = ind + 1;
                if (cb) cb();
            }
        }, waitTime);
    };

    checkState(1); // Start with a 1ms delay
}

Fork.prototype.release = function() {
    this.state = 0; // Set fork as free
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length; // First fork
    this.f2 = (id + 1) % forks.length; // Second fork
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    const { forks, f1, f2, id } = this;

    const eatCycle = (i) => {
        if (i < count) {
            const delay = Math.floor(Math.random() * 100) + 500;

            // Try to acquire the first fork
            forks[f1].acquire(id, () => {
                console.log(`Philosopher ${id} acquired fork ${f1}`);
                forks[f2].acquire(id, () => {
                    console.log(`Philosopher ${id} acquired fork ${f2}`);
                    console.log(`Philosopher ${id} is eating`);

                    // Simulate eating
                    setTimeout(() => {
                        // Release both forks after eating
                        forks[f1].release();
                        forks[f2].release();
                        console.log(`Philosopher ${id} released forks ${f1} and ${f2}`);
                        console.log(`Philosopher ${id} is thinking`);

                        // Repeat the cycle
                        eatCycle(i + 1);
                    }, delay);
                });
            });
        } else {
            console.log(`Philosopher ${id} has finished.`);
        }
    };

    eatCycle(0); // Start the eating cycle
}

// Main simulation
const N = 3; // Number of philosophers
const forks = [];
const philosophers = [];

for (let i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (let i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

for (let i = 0; i < N; i++) {
    philosophers[i].startNaive(10);
}

