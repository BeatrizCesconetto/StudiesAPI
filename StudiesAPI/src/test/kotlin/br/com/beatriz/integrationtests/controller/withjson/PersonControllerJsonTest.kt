package br.com.beatriz.integrationtests.controller.withjson

import br.com.beatriz.Startup
import br.com.beatriz.integrationtests.TestConfigs
import br.com.beatriz.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.beatriz.integrationtests.vo.AccountCredentialsVO
import br.com.beatriz.integrationtests.vo.PersonVO
import br.com.beatriz.integrationtests.vo.TokenVO
import br.com.beatriz.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.databind.DeserializationFeature
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [Startup::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class PersonControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO
    @BeforeAll
    fun setupTest() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
    }

    @Test
    @Order(0)
    fun testLogin() {
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
               .setBasePath("/api/person/v1")
           .setPort(TestConfigs.SERVER_PORT)
               .addFilter(RequestLoggingFilter(LogDetail.ALL))
               .addFilter(ResponseLoggingFilter(LogDetail.ALL))
           .build()
    }

    @Test
    @Order(1)
    fun testCreatePerson() {
        mockPerson()

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

        val item = objectMapper.readValue(content, PersonVO::class.java)

        assertNotNull(item.id)
        assertTrue(item.id > 0)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Richard", item.firstName)
        assertEquals("Stallman", item.lastName)
        assertEquals("New York CIty, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(true, item.enabled)
    }
    @Test
    @Order(2)
    fun testUpdatePerson() {
        // Create the person first to ensure it exists
        mockPerson()

        val createdContent = given()
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

        val createdPerson = objectMapper.readValue(createdContent, PersonVO::class.java)
        assertNotNull(createdPerson.id)
        person.id = createdPerson.id

        // Update the person's last name
        person.lastName = "Matthew Stallman"

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)

        assertNotNull(item.id)
        assertEquals(person.id, item.id )
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Richard", item.firstName)
        assertEquals("Matthew Stallman", item.lastName)
        assertEquals("New York CIty, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(true, item.enabled)
    }
    @Test
    @Order(3)
    fun disabledPersonById() {
        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertEquals(person.id, item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Richard", item.firstName)
        assertEquals("Matthew Stallman", item.lastName)
        assertEquals("New York CIty, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)
    }


    @Test
    @Order(4)
    fun testFindById() {
          val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertEquals(person.id, item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Richard", item.firstName)
        assertEquals("Matthew Stallman", item.lastName)
        assertEquals("New York CIty, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)
    }

    @Test
    @Order(5)
    fun testDeletePerson() {
          given()
            .spec(specification)
            .pathParam("id", person.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(6)
    fun testFindAll() {
        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .queryParams("page", 3,
                        "limit", 12,
                        "direction", "desc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.persons

        val item1 = people?.get(0)

        assertNotNull(item1!!.id)
        assertNotNull(item1.firstName)
        assertNotNull(item1.lastName)
        assertNotNull(item1.address)
        assertNotNull(item1.gender)
        assertEquals("Wallace", item1.firstName)
        assertEquals("Penhall", item1.lastName)
        assertEquals("8024 Starling Point", item1.address)
        assertEquals("Male", item1.gender)
        assertEquals(false, item1.enabled)

        val item2 = people[5]

        assertNotNull(item2.id)
        assertNotNull(item2.firstName)
        assertNotNull(item2.lastName)
        assertNotNull(item2.address)
        assertNotNull(item2.gender)
        assertEquals("Vincent", item2.firstName)
        assertEquals("Fisk", item2.lastName)
        assertEquals("7086 Summit Circle", item2.address)
        assertEquals("Male", item2.gender)
        assertEquals(true, item2.enabled)
    }

    @Test
    @Order(7)
    fun testFindByName() {
        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("firstName", "ayr")
            .queryParams("page", 0,
                "limit", 12,
                "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.persons

        val item1 = people?.get(0)

        assertNotNull(item1!!.id)
        assertNotNull(item1.firstName)
        assertNotNull(item1.lastName)
        assertNotNull(item1.address)
        assertNotNull(item1.gender)
        assertEquals("Ayrton", item1.firstName)
        assertEquals("Senna", item1.lastName)
        assertEquals("São Paulo", item1.address)
        assertEquals("Male", item1.gender)
        assertEquals(false, item1.enabled)
    }

    @Test
    @Order(8)
    fun testFindAllWithoutToken() {

        val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
        given()
            .spec(specificationWithoutToken)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .`when`()
            .get()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()
    }

    @Test
    @Order(9)
    fun testHATEOS() {
        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .queryParams(
                "page", 1,
                "limit", 5,
                "direction", "asc"
            )
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val jsonNode = objectMapper.readTree(content)

        val links = jsonNode["_links"]
        assertNotNull(links)
        assertTrue(links.has("first"))
        assertTrue(links.has("prev"))
        assertTrue(links.has("self"))
        assertTrue(links.has("next"))
        assertTrue(links.has("last"))

        val embedded = jsonNode["_embedded"]
        assertNotNull(embedded)
        assertTrue(embedded.has("personVOList"))

        val personVOList = embedded["personVOList"]
        assertTrue(personVOList.isArray)
        assertTrue(personVOList.size() > 0)

        for (person in personVOList) {
            assertNotNull(person["id"])
            assertNotNull(person["firstName"])
            assertNotNull(person["lastName"])
            assertNotNull(person["address"])
            assertNotNull(person["gender"])
            assertNotNull(person["_links"])
            assertTrue(person["_links"].has("self"))
        }

        val page = jsonNode["page"]
        assertNotNull(page)
        assertTrue(page["size"].isInt)
        assertTrue(page["totalElements"].isInt)
        assertTrue(page["totalPages"].isInt)
        assertTrue(page["number"].isInt)
    }


    private fun mockPerson() {
        person.firstName = "Richard"
        person.lastName = "Stallman"
        person.address = "New York CIty, New York, US"
        person.gender = "Male"
        person.enabled = true
    }

}