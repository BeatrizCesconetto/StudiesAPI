package br.com.beatriz.config

import br.com.beatriz.serialization.converter.YamlJackson2HttpMessageConverter
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig  : WebMvcConfigurer{
    private val MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml")

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(YamlJackson2HttpMessageConverter())
    }
    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
       /* configurer.favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(true)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            Para fica com xml no postman -> http://localhost:8080/api/person/v1?mediaType=xml*/

        //para usar o header  no postman
        configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("x-yml", MEDIA_TYPE_APPLICATION_YML)
    }
}