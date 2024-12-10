package br.com.beatriz.integrationtests.controller.withyml.mapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.databind.type.*
import io.restassured.mapper.ObjectMapper
import io.restassured.mapper.ObjectMapperDeserializationContext
import io.restassured.mapper.ObjectMapperSerializationContext
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper as JacksonObjectMapper

class YMLMapper : ObjectMapper {

    private val objectMapper: JacksonObjectMapper = JacksonObjectMapper(YAMLFactory())
    private val typeFactory: TypeFactory = TypeFactory.defaultInstance()

    init {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    override fun deserialize(context: ObjectMapperDeserializationContext): Any? {
        try {
            val dataToSerialize = context.dataToDeserialize.asString()
            val type = context.type as Class<*>
            return objectMapper.readValue(dataToSerialize, typeFactory.constructType(type))
        } catch (e: JsonMappingException) {
            e.printStackTrace()
        }catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return null
    }

    override fun serialize(context: ObjectMapperSerializationContext): Any? {
        try {
            return objectMapper.writeValueAsString(context.objectToSerialize)
        }catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return null
    }
}