package br.com.cesconetto.integrationtests.controller.withxml

import br.com.cesconetto.Startup
import br.com.cesconetto.integrationtests.TestConfigs
import br.com.cesconetto.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.cesconetto.integrationtests.vo.AccountCredentialsVO
import br.com.cesconetto.integrationtests.vo.TokenVO
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [Startup::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class AuthControllerXmlTest : AbstractIntegrationTest() {

    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTest() {
        tokenVO = TokenVO()
    }
    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "leandro",
            password = "admin123"
        )

        tokenVO = RestAssured.given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)

        Assertions.assertNotNull(tokenVO.accessToken)
        Assertions.assertNotNull(tokenVO.refreshToken)
    }

    @Test
    @Order(1)
    fun testRefreshToken() {
        tokenVO = given()
            .basePath("/auth/refresh")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .pathParam("username", tokenVO.username)
            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer ${tokenVO.refreshToken}")
            .`when`()
            .put("{username}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}