import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

//// Producent i konsument  z wybranym pośrednikiem
//
//// N - liczba posredników
//
//[PRODUCER:: p: porcja;
//*[true -> produkuj(p);
//[(i:0..N-1) POSREDNIK(i)?JESZCZE() -> POSREDNIK(i)!p]
//]
//||POSREDNIK(i:0..N-1):: p: porcja;
//*[true -> PRODUCER!JESZCZE() ;
//[PRODUCER?p -> CONSUMER!p]
//]
//||CONSUMER:: p: porcja;
//*[(i:0..N-1) POSREDNIK(i)?p -> konsumuj(p)]

private const val N = 5

fun main(): Unit = runBlocking {
    val intermediaryChannels = List(N) { Channel<Int>() }

    launch(CoroutineName("producer")) {
        while (true) {
            val p = (5..10).random()
            delay((100 * p).toLong())
            println("Produced: $p")

            select {
                intermediaryChannels.forEachIndexed { index, channel ->
                    channel.onSend(p) {
                        println("Producer sent $p to intermediary-$index")
                    }
                }
            }
        }
    }

    val consumerChannel = Channel<Int>()
    repeat(N) { index ->
        launch(CoroutineName("intermediary-$index")) {
            for (p in intermediaryChannels[index]) {
                println("Intermediary $index received: $p")
                consumerChannel.send(p)
                println("Intermediary $index forwarded: $p")
            }
        }
    }
    launch(CoroutineName("consumer")) {
        consumer(consumerChannel)
    }


}

private suspend fun consumer( consumerChannel : Channel<Int>) {
    for (p in consumerChannel) {
        println("consuming value: $p")
        val waitTime = (1000..3000).random().toLong()
        delay(waitTime)
    }
}