import br.com.beatriz.Startup
import br.com.beatriz.integrationtests.TestConfigs
import br.com.beatriz.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.beatriz.integrationtests.vo.AccountCredentialsVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.boot.test.context.SpringBootTest
import br.com.beatriz.integrationtests.vo.PersonVO
import br.com.beatriz.integrationtests.vo.TokenVO
import org.junit.jupiter.api.TestInstance.Lifecycle
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,  classes = [Startup::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class PersonControllerCorsWithJson : AbstractIntegrationTest() {

	private lateinit var specification: RequestSpecification


	@Test
	@Order(0)
	fun authorization() {
		val user = AccountCredentialsVO(
			username = "leandro",
			password = "admin123"
		)
		token = given()
				.basePath("/auth/signin")// Define o caminho base da requisição
				.port(TestConfigs.SERVER_PORT)// Define a porta do servidor
					.contentType(TestConfigs.CONTENT_TYPE_JSON)// Define o tipo de conteúdo como JSON
					.body(user)// Define o corpo da requisição com o objeto 'user'
				.`when`()
					.post()// Envia uma requisição POST
						.then()
					.statusCode(200)// Verifica se o status da resposta é 200 (OK)
						.extract()
						.body()// Extrai o corpo da resposta
					.`as`(TokenVO::class.java)// Converte o corpo da resposta para uma instância de 'TokenVO'
						.accessToken!! // Obtém o valor do campo 'accessToken' da instância de 'TokenVO'
	}

	@Test
	@Order(1)
	fun testCreate() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_BEATRIZ
			)
			.addHeader(
				TestConfigs.HEADER_PARAM_AUTHORIZATION,
				"Bearer $token"
			)
			.setBasePath("/api/person/v1")// Define o caminho base da requisição
			.setPort(TestConfigs.SERVER_PORT)// Define a porta do servidor
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = given()
			.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(person)
				.`when`()
			.post()
			.then()
				.statusCode(200)
			.extract()
			.body()
				.asString()

		val createdPerson = objectMapper.readValue(
			content, PersonVO::class.java
		)
		person = createdPerson

		assertNotNull(createdPerson.id)
		assertNotNull(createdPerson.firstName)
		assertNotNull(createdPerson.lastName)
		assertNotNull(createdPerson.address)
		assertNotNull(createdPerson.gender)

		assertTrue(createdPerson.id > 0)

		assertEquals("Nelson", createdPerson.firstName)
		assertEquals("Piquet", createdPerson.lastName)
		assertEquals("Brasilia", createdPerson.address)
		assertEquals("Male", createdPerson.gender)
	}

	private fun mockPerson() {
		person.firstName = "Nelson"
		person.lastName = "Piquet"
		person.address = "Brasilia"
		person.gender = "Male"
	}
	companion object {
		private lateinit var objectMapper: ObjectMapper
		private lateinit var person: PersonVO
		private lateinit var token: String

		@JvmStatic
		@BeforeAll
		fun setUpTests() {
			objectMapper = ObjectMapper()
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			person = PersonVO()
			token = ""
		}
	}

	@Test
	@Order(2)
	fun testCreateWithWrongOrigin() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_CESCONETTO
			)
			.addHeader(
				TestConfigs.HEADER_PARAM_AUTHORIZATION,
				"Bearer $token"
			)
			.setBasePath("/api/person/v1")// Define o caminho base da requisição
			.setPort(TestConfigs.SERVER_PORT)// Define a porta do servidor
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = given()
			.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(person)
				.`when`()
			.post()
			.then()
				.statusCode(403)
			.extract()
			.body()
				.asString()

		assertEquals("Invalid CORS request", content)
	}

	@Test
	@Order(3)
	fun testFindById() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_LOCALHOST
			)
			.addHeader(
				TestConfigs.HEADER_PARAM_AUTHORIZATION,
				"Bearer $token"
			)
			.setBasePath("/api/person/v1")// Define o caminho base da requisição
			.setPort(TestConfigs.SERVER_PORT)// Define a porta do servidor
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", person.id)
			.`when`()["{id}"]
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		val createdPerson = objectMapper.readValue(
			content, PersonVO::class.java
		)

		assertNotNull(createdPerson.id)
		assertNotNull(createdPerson.firstName)
		assertNotNull(createdPerson.lastName)
		assertNotNull(createdPerson.address)
		assertNotNull(createdPerson.gender)

		assertTrue(createdPerson.id > 0)

		assertEquals("Nelson", createdPerson.firstName)
		assertEquals("Piquet", createdPerson.lastName)
		assertEquals("Brasilia", createdPerson.address)
		assertEquals("Male", createdPerson.gender)
	}
	@Test
	@Order(4)
	fun testFindByIdWithWrongOrigin() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_CESCONETTO
			)
			.addHeader(
				TestConfigs.HEADER_PARAM_AUTHORIZATION,
				"Bearer $token"
			)
			.setBasePath("/api/person/v1")// Define o caminho base da requisição
			.setPort(TestConfigs.SERVER_PORT)// Define a porta do servidor
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", person.id)
			.`when`()["{id}"]
			.then()
			.statusCode(403)
			.extract()
			.body()
			.asString()

		assertEquals("Invalid CORS request", content)
	}

}

