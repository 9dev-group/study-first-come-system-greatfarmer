package kr.co.coupon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FirstComeFirstServedApplication

fun main(args: Array<String>) {
	runApplication<FirstComeFirstServedApplication>(*args)
}