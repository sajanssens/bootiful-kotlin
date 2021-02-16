package com.example.dsls

import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

fun main(args: Array<String>) {
    runApplication<DslsApplication>(*args)
}

@SpringBootApplication
class DslsApplication {
    @Bean
    fun httpEndpoints(cr: CustomerRepository) =
        // Allow to create easily a WebFlux.fn RouterFunction with a Coroutines router Kotlin DSL.
        coRouter {
            // http://localhost:8080/customers/1
            GET("/customers/{id}") {
                val customer: Customer = cr
                    .findById(Integer.parseInt(it.pathVariable("id"))) // returns a Mono<Customer>
                    .awaitFirst() // TODO show: await!
                ServerResponse.ok().bodyValueAndAwait(customer)
            }

            // TODO LAB: create an endpoint to get all customers
            /*
            // Solution
            // http://localhost:8080/customers
            GET("/customers") {
                val fluxAll = cr.findAll() // returns a Flux<Customer>
                ServerResponse.ok().bodyAndAwait(fluxAll.asFlow())
            }*/

            // TODO LAB: create an endpoint to delete a customer
            /*
            // Solution
            // http://localhost:8080/customers/1
            DELETE("/customers/{id}") {
                val id = Integer.parseInt(it.pathVariable("id"))
                cr.findById(id).map { cr.delete(it) }
                ServerResponse.noContent().buildAndAwait()
            }*/
        }
}

data class Customer(@Id val id: Int, val name: String)

interface CustomerRepository : ReactiveCrudRepository<Customer, Int>


