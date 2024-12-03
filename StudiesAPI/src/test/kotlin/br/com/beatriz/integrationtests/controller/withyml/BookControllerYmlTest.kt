package br.com.beatriz.integrationtests.controller.withyml

import br.com.beatriz.Startup
import org.junit.jupiter.api.TestInstance.Lifecycle
import br.com.beatriz.integrationtests.TestConfigs
import br.com.beatriz.integrationtests.controller.withyml.mapper.YMLMapper
import br.com.beatriz.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.beatriz.integrationtests.vo.AccountCredentialsVO
import br.com.beatriz.integrationtests.vo.BookVO
import br.com.beatriz.integrationtests.vo.TokenVO
import br.com.beatriz.integrationtests.vo.wrappers.WrapperBookVO
import com.fasterxml.jackson.core.JsonProcessingException
import io.restassured.http.ContentType
import com.fasterxml.jackson.databind.JsonMappingException
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [Startup::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class BookControllerYamlTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: YMLMapper
    private lateinit var book: BookVO

    @BeforeAll
    fun setup() {
        objectMapper = YMLMapper()
        book = BookVO()
    }

    @Test
    @Order(0)
    fun authorization() {
        val user = AccountCredentialsVO()
        user.username = "leandro"
        user.password = "admin123"
        val token = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)
            .accessToken
        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/book/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(1)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        mockBook()
        book = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(book, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)
        assertNotNull(book.key)
        assertNotNull(book.title)
        assertNotNull(book.author)
        assertNotNull(book.price)
        assertTrue(book.key > 0)
        assertEquals("Docker Deep Dive", book.title)
        assertEquals("Nigel Poulton", book.author)
        assertEquals(55.99, book.price)
    }

    @Test
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {

        book.title = "Docker Deep Dive - Updated"

        val bookUpdated: BookVO = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("id", book.key)
            .body(book, objectMapper)
            .`when`()
            .put("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)

        assertNotNull(bookUpdated.key)
        assertNotNull(bookUpdated.title)
        assertNotNull(bookUpdated.author)
        assertNotNull(bookUpdated.price)
        assertEquals(bookUpdated.key, book.key)
        assertEquals("Docker Deep Dive - Updated", bookUpdated.title)
        assertEquals("Nigel Poulton", bookUpdated.author)
        assertEquals(55.99, bookUpdated.price)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val foundBook = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("key", book.key)
            .`when`()
            .get("{key}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(BookVO::class.java, objectMapper)

        assertNotNull(foundBook.key)
        assertNotNull(foundBook.title)
        assertNotNull(foundBook.author)
        assertNotNull(foundBook.price)
        assertEquals(foundBook.key, book.key)
        assertEquals("Docker Deep Dive - Updated", foundBook.title)
        assertEquals("Nigel Poulton", foundBook.author)
        assertEquals(55.99, foundBook.price)
    }

    @Test
    @Order(4)
    fun testDelete() {
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("key", book.key)
            .`when`()
            .delete("{key}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(5)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val wrapper = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(WrapperBookVO::class.java, objectMapper)


        val content = wrapper.embedded?.books

        val foundBookOne = content?.get(0)

        assertNotNull(foundBookOne!!.key)
        assertNotNull(foundBookOne.title)
        assertNotNull(foundBookOne.author)
        assertNotNull(foundBookOne.price)
        assertTrue(foundBookOne.key > 0)
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.title)
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.author)
        assertEquals(54.00, foundBookOne.price)


        val foundBookFive: BookVO? = content?.get(4)

        Assertions.assertNotNull(foundBookFive!!.key)
        Assertions.assertNotNull(foundBookFive.title)
        Assertions.assertNotNull(foundBookFive.author)
        Assertions.assertNotNull(foundBookFive.price)
        assertTrue(foundBookFive.key > 0)
        Assertions.assertEquals("Domain Driven Design", foundBookFive.title)
        Assertions.assertEquals("Eric Evans", foundBookFive.author)
        Assertions.assertEquals(92.0, foundBookFive.price)
    }

    private fun mockBook() {
        book.title = "Docker Deep Dive"
        book.author = "Nigel Poulton"
        book.price = (java.lang.Double.valueOf(55.99))
        book.launchDate = Date()
    }
}