package kr.co.coupon.service.impl

import kr.co.coupon.service.CouponService
import kr.co.coupon.util.Log
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class CouponServiceImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val streamBridge: StreamBridge
): CouponService, Log {

    companion object {
        const val FIRST_COME_COUPON_LIMIT = 100
        const val FIRST_COME_COUPON_KEY = "first-come-coupon:count"
        const val FIRST_COME_COUPON_LOCK_KEY = "first-come-coupon:lock"
        private val FIRST_COME_COUPON_LOCK_TIME_OUT = Duration.ofMillis(100) // 1ms 이하로 설정 불가

        const val FIRST_COME_COUPON_BINDER = "coupon-out-0"
    }

    override fun getCouponCount(): Int {
        return getCouponCountFromRedis()
    }

    override fun issueCoupon(): Int {
        issueCouponFromRedis()
        return getCouponCountFromRedis()
    }

    override fun issueCouponWithLock(): Int {
        val lockId = UUID.randomUUID().toString()
        val lockAcquired = tryLock(FIRST_COME_COUPON_LOCK_KEY, lockId, FIRST_COME_COUPON_LOCK_TIME_OUT)

        if (!lockAcquired) {
            throw IllegalStateException("다른 사용자가 쿠폰 발급 중입니다.")
        }

        try {
            issueCouponFromRedis()
        } finally {
            unlock(FIRST_COME_COUPON_LOCK_KEY, lockId)
        }

        return getCouponCountFromRedis()
    }

    private fun tryLock(key: String, value: String, timeout: Duration): Boolean {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout) ?: false
    }

    private fun unlock(key: String, value: String) {
        val luaScript = """
            if redis.call("get", KEYS[1]) == ARGV[1] then
                return redis.call("del", KEYS[1])
            else
                return 0
            end
        """.trimIndent()

        val redisScript = DefaultRedisScript<Long>().apply {
            setScriptText(luaScript)
            resultType = Long::class.java
        }

        redisTemplate.execute(
            redisScript,
            listOf(key),
            value
        )
    }

    private fun issueCouponFromRedis() {
        val isNew = redisTemplate.opsForValue().setIfAbsent(FIRST_COME_COUPON_KEY, "1")
        if (isNew == false) {
            val currentCount = getCouponCountFromRedis()
            if (currentCount < FIRST_COME_COUPON_LIMIT) {
                redisTemplate.opsForValue().increment(FIRST_COME_COUPON_KEY)
            } else {
                throw IllegalStateException("쿠폰이 모두 소진되었습니다.")
            }
        }
    }

    private fun getCouponCountFromRedis() : Int {
        return redisTemplate.opsForValue().get(FIRST_COME_COUPON_KEY)?.toInt() ?: 0
    }

    override fun issueCouponWithKafka(): String {
        val result = streamBridge.send(FIRST_COME_COUPON_BINDER, UUID.randomUUID())
        return if (result) "success" else "fail"
    }
}