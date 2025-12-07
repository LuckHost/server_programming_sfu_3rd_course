package edu.sfu

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["edu.sfu"])
@EntityScan(basePackages = ["edu.sfu.entity"])
@EnableJpaRepositories(basePackages = ["edu.sfu.repository"])
open class LibraryApplication

fun main(args: Array<String>) {
    SpringApplication.run(LibraryApplication::class.java, *args)
}