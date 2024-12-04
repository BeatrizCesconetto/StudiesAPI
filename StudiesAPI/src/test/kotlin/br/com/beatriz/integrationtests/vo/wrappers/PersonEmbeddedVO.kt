package br.com.beatriz.integrationtests.vo.wrappers

import br.com.beatriz.integrationtests.vo.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty

class PersonEmbeddedVO {

    @JsonProperty("personVOList")
    var persons: List <PersonVO>? = null
}