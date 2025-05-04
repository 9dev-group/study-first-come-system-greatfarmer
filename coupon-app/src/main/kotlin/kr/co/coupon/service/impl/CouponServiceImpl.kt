package kr.co.coupon.service.impl

import kr.co.coupon.service.CouponService
import kr.co.coupon.util.Log
import org.springframework.stereotype.Service

@Service
class CouponServiceImpl(

): CouponService, Log {

    override fun getCouponCount(): Int {
        return 10
    }
}