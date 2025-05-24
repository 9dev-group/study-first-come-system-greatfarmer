package kr.co.coupon.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "coupon_issue",
    uniqueConstraints = [UniqueConstraint(name = "uq_user_coupon", columnNames = ["user_id", "coupon_code"])]
)
class CouponIssueEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_issue_no")
    val couponIssueNo: Long = 0,

    @Column(name = "user_id", nullable = false, length = 100)
    val userId: String,

    @Column(name = "issued_at", nullable = false)
    val issuedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "coupon_code", length = 50)
    val couponCode: String? = null,

    @Column(name = "status", length = 20)
    val status: String = "SUCCESS"
)