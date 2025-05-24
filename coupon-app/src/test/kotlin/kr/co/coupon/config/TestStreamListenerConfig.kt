package kr.co.coupon.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Consumer

@Configuration
class TestStreamListenerConfig {
    @Bean
    fun testQueue(): BlockingQueue<String> = LinkedBlockingQueue()

    @Bean
    fun coupon(testQueue: BlockingQueue<String>): Consumer<String> =
        Consumer { message -> testQueue.add(message) }
}