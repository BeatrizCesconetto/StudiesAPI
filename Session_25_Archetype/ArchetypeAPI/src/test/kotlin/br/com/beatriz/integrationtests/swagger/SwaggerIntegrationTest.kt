package br.com.beatriz.integrationtests.swagger

import br.com.beatriz.Startup
import br.com.beatriz.integrationtests.TestConfigs
import br.com.beatriz.integrationtests.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [Startup::class])
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
class SwaggerIntegrationTest : AbstractIntegrationTest() {

	@Test
	fun shouldDisplaySwaggerUiPage() {
		val content = given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
			.`when`()
			.get()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()
		assertTrue(content.contains("Swagger UI"))
	}
}