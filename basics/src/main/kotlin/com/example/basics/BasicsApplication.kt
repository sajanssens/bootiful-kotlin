package com.example.basics

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

// First, run create_database.sql on mysql
fun main(args: Array<String>) {
    runApplication<BasicsApplication>(*args)
}

@SpringBootApplication
class BasicsApplication {
    @Bean
    fun runner(cs: CustomerService) = ApplicationListener<ApplicationReadyEvent> {
        cs.all().forEach { println(it) }
    }
}

interface CustomerService {
    fun all(): Collection<Customer>
}

object CustomerTable : Table("Customer") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
}

@Service
@Transactional
@Profile("exposed")
class ExposedCustomerService : CustomerService {
    override fun all() =
        CustomerTable.selectAll().map { Customer(it[CustomerTable.id], it[CustomerTable.name]) }
}

@Service
@Profile("jdbc")
class JdbcCustomerService(val jdbcTemplate: JdbcTemplate) : CustomerService {
    // In verbose Java, it would be like this:
    // override fun all(): Collection<Customer> {
    //     val list = this.jdbcTemplate.query(
    //         "select * from CUSTOMERS",
    //         RowMapper<Customer>({ rs, i ->
    //             Customer(rs.getInt("id"), rs.getString("name"))
    //         })
    //     )
    //     return list
    // }

    // In concise kotlin:
    override fun all() =
        this.jdbcTemplate.query("SELECT * FROM CUSTOMER") { rs, _ ->
            Customer(rs.getInt("id"), rs.getString("name"))
        }
}

data class Customer(val id: Int, val name: String)



