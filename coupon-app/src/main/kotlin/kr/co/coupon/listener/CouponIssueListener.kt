package kr.co.coupon.listener

import kr.co.coupon.domain.CouponIssueEntity
import kr.co.coupon.repository.CouponIssueRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

@Component
class CouponIssueListener(
    private val couponCounter: AtomicInteger,
    private val couponIssueRepository: CouponIssueRepository
) {
    @Bean
    fun coupon(): Consumer<String> = Consumer { message ->
        val current = couponCounter.incrementAndGet()
        if (current <= 100) {
            println("쿠폰 발급 성공! 선착순 번호: $current, 메시지: $message")
            // 실제 쿠폰 발급 로직 (DB 저장 등)
            couponIssueRepository.save(
                CouponIssueEntity(
                    userId = message,
                    couponCode = generateCouponCodeWithUUID(12)
                )
            )
        } else {
            println("쿠폰 발급 마감! 메시지: $message")
            // 실패 처리 (예: 대기열 등록, 실패 응답 등)
        }
    }

    fun generateCouponCodeWithUUID(length: Int = 10): String {
        // UUID를 제거하고, 대문자+숫자만 남기고, 원하는 길이만큼 자름
        val uuid = UUID.randomUUID().toString().replace("-", "").uppercase()
        val chars = uuid.filter { it.isLetterOrDigit() }
        return chars.take(length)
    }
}