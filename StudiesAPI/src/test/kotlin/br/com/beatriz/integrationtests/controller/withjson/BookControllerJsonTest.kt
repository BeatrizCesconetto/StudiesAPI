package br.com.beatriz.integrationtests.controller.withjson

import br.com.beatriz.Startup
import br.com.beatriz.integrationtests.TestConfigs
import br.com.beatriz.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.beatriz.integrationtests.vo.AccountCredentialsVO
import br.com.beatriz.integrationtests.vo.BookVO
import br.com.beatriz.integrationtests.vo.TokenVO
import br.com.beatriz.integrationtests.vo.wrappers.WrapperBookVO
import br.com.beatriz.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [Startup::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class BookControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var book: BookVO
    @BeforeAll
    fun setupTest() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        book = BookVO()
    }

    @Test
    @Order(1)
    fun authorization() {
        val user = AccountCredentialsVO(
            username = "leandro",
            password = "admin123"
        )
       val token = given()
            .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
            .`when`()
            .post()
                .then()
                    .statusCode(200)
                        .extract()
                    .body()
                        .`as`(TokenVO::class.java)
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
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        mockBook()
        val content: String = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(book)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        book = objectMapper.readValue(content, BookVO::class.java)

        Assertions.assertNotNull(book.key)
        Assertions.assertNotNull(book.title)
        Assertions.assertNotNull(book.author)
        Assertions.assertNotNull(book.price)
        assertTrue(book.key > 0)
        Assertions.assertEquals("Docker Deep Dive", book.title)
        Assertions.assertEquals("Nigel Poulton", book.author)
        Assertions.assertEquals(55.99, book.price)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {
        book.title = "Docker Deep Dive - Updated"

        val content: String = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", book.key)
            .body(book)
            .`when`()
            .put("/{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val bookUpdated: BookVO = objectMapper.readValue(content, BookVO::class.java)

        Assertions.assertNotNull(bookUpdated.key)
        Assertions.assertNotNull(bookUpdated.title)
        Assertions.assertNotNull(bookUpdated.author)
        Assertions.assertNotNull(bookUpdated.price)
        assertEquals(bookUpdated.key, book.key)
        Assertions.assertEquals("Docker Deep Dive - Updated", bookUpdated.title)
        Assertions.assertEquals("Nigel Poulton", bookUpdated.author)
        Assertions.assertEquals(55.99, bookUpdated.price)
    }

    @Test
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val content: String = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", book.key)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val foundBook: BookVO = objectMapper.readValue(content, BookVO::class.java)
        Assertions.assertNotNull(foundBook.key)
        Assertions.assertNotNull(foundBook.title)
        Assertions.assertNotNull(foundBook.author)
        Assertions.assertNotNull(foundBook.price)
        assertEquals(foundBook.key, book.key)
        Assertions.assertEquals("Docker Deep Dive - Updated", foundBook.title)
        Assertions.assertEquals("Nigel Poulton", foundBook.author)
        Assertions.assertEquals(55.99, foundBook.price)
    }

    @Test
    @Order(5)
    fun testDeleteBook() {
          given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", book.key)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(6)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val strContent = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(strContent, WrapperBookVO::class.java)
        val content = wrapper.embedded!!.books

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
        book.price = 55.99
        book.launchDate = Date()
    }

}