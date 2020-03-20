package com.github.lant.letsmath

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.thymeleaf.Thymeleaf
import io.ktor.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import io.ktor.http.content.*
import com.fasterxml.jackson.databind.*
import com.github.lant.letsmath.com.github.lant.letsmath.OperationsController
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.request.receiveParameters

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(Locations) {
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(DataConversion)

    val operationsController = OperationsController()

    val numOperations = 10

    routing {
        get("/") {
            val operations = operationsController.getOperations(numOperations)
            call.respond(ThymeleafContent("index", mapOf("operations" to operations)))
        }

        post("/validate") {
          val params = call.receiveParameters()

           val results = (1..numOperations+1).map {
                operationsController.evaluate(
                    params["operation_${it}"]!!,
                    params["response_${it}"]!!.toInt(),
                    params["solution_${it}"]!!.toInt())
            }
            val score = results.count { it.valid }
            val total = results.size

          call.respond(ThymeleafContent("result", mapOf(
              "results" to results,
              "score" to score,
              "total" to total)))
        }

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
            files("js")
        }
    }
}
