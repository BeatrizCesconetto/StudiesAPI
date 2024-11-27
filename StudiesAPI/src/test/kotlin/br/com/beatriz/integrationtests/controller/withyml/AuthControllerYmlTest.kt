package br.com.beatriz.integrationtests.controller.withyml

import br.com.beatriz.Startup
import br.com.beatriz.integrationtests.TestConfigs
import br.com.beatriz.integrationtests.controller.withyml.mapper.YMLMapper
import br.com.beatriz.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.beatriz.integrationtests.vo.AccountCredentialsVO
import br.com.beatriz.integrationtests.vo.TokenVO
import io.restassured.RestAssured.given
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.ContentType
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [Startup::class])
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class AuthControllerYmlTest : AbstractIntegrationTest() {

    private lateinit var objectMapper: YMLMapper
    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTest() {
        tokenVO = TokenVO()
        objectMapper = YMLMapper()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "leandro",
            password = "admin123"
        )

        tokenVO = given()
            .config(RestAssuredConfig
                .config()
                .encoderConfig(
                    EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                ))
            .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(user,objectMapper)
            .`when`()
                .post()
                    .then()
                        .statusCode(200)
                        .extract()
                    .body()
                        .`as`(TokenVO::class.java, objectMapper)

        Assertions.assertNotNull(tokenVO.accessToken)
        Assertions.assertNotNull(tokenVO.refreshToken)
    }

    @Test
    @Order(1)
    fun testRefreshToken() {
        tokenVO = given()
            .config(RestAssuredConfig
                .config()
                .encoderConfig(
                    EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                ))
            .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("username", tokenVO.username)
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer ${tokenVO.refreshToken}")
            .`when`()
                .put("{username}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .`as`(TokenVO::class.java, objectMapper)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }
}