package kr.co.coupon.repository

import kr.co.coupon.domain.CouponIssueEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CouponIssueRepository: JpaRepository<CouponIssueEntity, Long> {
}