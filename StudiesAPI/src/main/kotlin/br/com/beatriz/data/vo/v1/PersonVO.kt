package br.com.beatriz.data.vo.v1

import br.com.beatriz.data.vo.v2.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import jakarta.xml.bind.annotation.XmlRootElement
import org.springframework.hateoas.RepresentationModel
import java.util.*

@XmlRootElement
@JsonPropertyOrder("id", "firstName", "lastName", "address", "gender")
data class PersonVO (

    @Mapping("id")
    @field:JsonProperty("id")
    var key: Long = 0,
    //@field:JsonProperty("first_name") Para mudar o nome que aparece
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = "",
    var birthDay: Date? = null
): RepresentationModel<PersonVO>()