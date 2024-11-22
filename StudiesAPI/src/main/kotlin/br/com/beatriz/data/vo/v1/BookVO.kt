package br.com.beatriz.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.util.Date

@JsonPropertyOrder("id", "author", "launchDate", "price", "title")
data class BookVO (

    @Mapping("id")
    @field:JsonProperty("id")
    var key: Long = 0,
    var author: String = "",
    var title: String = "",
    var price: Double = 0.0,
    var launchDate: Date? = null
) : RepresentationModel<BookVO>()