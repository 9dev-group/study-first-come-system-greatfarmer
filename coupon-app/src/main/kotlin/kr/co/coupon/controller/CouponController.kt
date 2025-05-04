package kr.co.coupon.controller

import kr.co.coupon.service.CouponService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/coupon")
class CouponController(
    val couponService: CouponService
) {
    @PostMapping("/issue")
    fun issueCoupon() =
        ResponseEntity.ok(couponService.issueCoupon())

    @PostMapping("/issue/lock")
    fun issueCouponWithLock() =
        ResponseEntity.ok(couponService.issueCouponWithLock())

    @GetMapping("/count")
    fun getCouponCount() =
        ResponseEntity.ok(couponService.getCouponCount())

}