package it.giovi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoAuthenticationJwTbaseApplication

fun main(args: Array<String>) {
	runApplication<DemoAuthenticationJwTbaseApplication>(*args)
}
