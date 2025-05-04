package kr.co.coupon

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FirstComeFirstServedApplicationTests {
	@Autowired
	private lateinit var mockMvc: MockMvc

	companion object {
		const val ISSUE_COUPON_WITHOUT_LOCK_ENDPOINT = "/v1/coupon/issue"
		const val ISSUE_COUPON_WITH_LOCK_ENDPOINT =  "/v1/coupon/issue/lock"

		const val IS_TIME_DELAY = true
		const val DELAY_TIME = 3L // ms
	}

	@Test
	fun `동시 쿠폰 발급 테스트`() {
		val numberOfUsers = 200
		val latch = CountDownLatch(numberOfUsers)
		val executor = Executors.newFixedThreadPool(4) // 병렬 요청 수

		repeat(numberOfUsers) {
			executor.submit {
				try {
					if (IS_TIME_DELAY) {
						Thread.sleep(DELAY_TIME)
					}

					mockMvc.perform(
						MockMvcRequestBuilders.post(ISSUE_COUPON_WITH_LOCK_ENDPOINT)
							.contentType(MediaType.APPLICATION_JSON)
					).andExpect(MockMvcResultMatchers.status().isOk)
				} catch (e: Exception) {
					println("발급순서: ${latch.count} / 요청 중 예외 발생: ${e.message}")
				} finally {
					latch.countDown()
				}
			}
		}

		latch.await() // 모든 요청이 끝날 때까지 대기
	}
}
