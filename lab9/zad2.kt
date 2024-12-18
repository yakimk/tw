import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

//// Producent i konsumer  oraz procesy przetwarzające
//
//// N - liczba przetwarzaczy;
//
//[PRODUCER:: p: porcja;
//*[true -> produkuj(p); PRZETWARZACZ(0)!p]
//||PRZETWARZACZ(i:O..N-l):: p: porcja;
//*[true -> [i = 0 -> PRODUCER?p
//[]i <> 0 -> PRZETWARZACZ(i-l)?p];
//[i = N-l -> CONSUMER!p
//[]i <> N-l -> PRZETWARZACZ(i+l)!p]
//]
//|| CONSUMER:: p: porcja;
//*[PRZETWARZACZ(N-l)?p -> konsumuj(p)]
//]

private const val N = 4

fun main(): Unit = runBlocking {

    val intermediaryChannels = List(N + 1) { Channel<Int>() }

    launch(CoroutineName("producer")) {
        var p = 0
        while (true) {
            delay((100 * p).toLong())
            println("Produced: $p")
            intermediaryChannels[0].send(p)
            p++
        }
    }

    repeat(N) { index ->
        launch(CoroutineName("intermediary-$index")) {
            for (p in intermediaryChannels[index]) {
                println("Intermediary $index received: $p")
                delay((100 * p).toLong())
                intermediaryChannels[index + 1].send(p)
                println("Intermediary $index forwarded: $p")
            }
        }
    }
    launch(CoroutineName("consumer")) {
        consumer(intermediaryChannels[N])
    }


}

private suspend fun consumer( consumerChannel : Channel<Int>) {
    for (p in consumerChannel) {
        println("consuming value: $p")
        val waitTime = (1000..3000).random().toLong()
        delay(waitTime)
    }
}