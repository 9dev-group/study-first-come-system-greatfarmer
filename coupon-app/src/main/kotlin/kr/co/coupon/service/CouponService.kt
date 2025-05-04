package kr.co.coupon.service

interface CouponService {
    fun getCouponCount(): Int
    fun issueCoupon(): Int
}