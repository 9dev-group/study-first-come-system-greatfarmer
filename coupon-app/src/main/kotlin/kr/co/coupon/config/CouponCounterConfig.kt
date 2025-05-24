package kr.co.coupon.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.atomic.AtomicInteger

@Configuration
class CouponCounterConfig {
    @Bean
    fun couponCounter(): AtomicInteger = AtomicInteger(0)
}