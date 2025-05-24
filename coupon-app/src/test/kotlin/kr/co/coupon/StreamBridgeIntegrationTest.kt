import kr.co.coupon.FirstComeFirstServedApplication
import kr.co.coupon.config.TestStreamListenerConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.function.StreamBridge
import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit

@SpringBootTest(classes=[FirstComeFirstServedApplication::class, TestStreamListenerConfig::class])
class StreamBridgeIntegrationTest(
    @Autowired private val streamBridge: StreamBridge,
    @Autowired private val testQueue: BlockingQueue<String>
) {

    @Test
    @Timeout(10)
    fun `StreamBridge로 메시지를 보내면 Consumer에서 받을 수 있다`() {
        // given
        val message = "hello-cloud-stream"

        // when
        val sendResult = streamBridge.send("coupon-out-0", message)
        assertThat(sendResult).isTrue()

        // then
        val received = testQueue.poll(5, TimeUnit.SECONDS)
        assertThat(received).isEqualTo(message)
    }
}