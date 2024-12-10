package br.com.cesconetto.integrationtests.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.wait.strategy.Wait

@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
open class AbstractIntegrationTest {

    @Configuration
    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(applicationContext: ConfigurableApplicationContext) {

            mysql.start()

            val environment = applicationContext.environment
            val testcontainers = MapPropertySource(
                "testcontainers", createConnectionConfiguration()
            )

            environment.propertySources.addFirst(testcontainers)
        }

        companion object {

            private val mysql: MySQLContainer<*> = MySQLContainer("mysql:8.0")
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("password")
                .waitingFor(Wait.forListeningPort())

            private fun createConnectionConfiguration(): MutableMap<String, Any> {
                return mutableMapOf(
                    "spring.datasource.url" to mysql.jdbcUrl,
                    "spring.datasource.username" to mysql.username,
                    "spring.datasource.password" to mysql.password,
                )
            }
        }
    }
}