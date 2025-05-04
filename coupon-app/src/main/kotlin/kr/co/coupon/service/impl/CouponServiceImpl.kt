package kr.co.coupon.service.impl

import kr.co.coupon.service.CouponService
import kr.co.coupon.util.Log
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class CouponServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>
): CouponService, Log {

    companion object {
        const val FIRST_COME_COUPON_KEY = "first-come-coupon"
    }

    override fun getCouponCount(): Int {
        return getCouponCountFromRedis()
    }

    override fun issueCoupon(): Int {
        if (redisTemplate.opsForValue().get(FIRST_COME_COUPON_KEY) == null) {
            redisTemplate.opsForValue().set(FIRST_COME_COUPON_KEY, "1")
        } else {
            if (getCouponCountFromRedis() < 100) {
                redisTemplate.opsForValue().increment(FIRST_COME_COUPON_KEY)
            } else {
                throw IllegalStateException("쿠폰이 모두 소진되었습니다.")
            }
        }

        return getCouponCountFromRedis()
    }

    private fun getCouponCountFromRedis() : Int {
        return redisTemplate.opsForValue().get(FIRST_COME_COUPON_KEY)?.toInt() ?: 0
    }
}