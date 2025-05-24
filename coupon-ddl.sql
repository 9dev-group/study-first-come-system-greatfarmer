CREATE TABLE coupon_issue (
    coupon_issue_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,         -- 쿠폰을 받은 사용자 ID
    issued_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 발급 시각
    coupon_code VARCHAR(50),               -- (옵션) 쿠폰 코드
    status VARCHAR(20) DEFAULT 'SUCCESS',  -- (옵션) 발급 상태 (SUCCESS, FAIL 등)
    UNIQUE KEY uq_user_coupon (user_id, coupon_code)      -- (옵션) 사용자별 중복 방지
);