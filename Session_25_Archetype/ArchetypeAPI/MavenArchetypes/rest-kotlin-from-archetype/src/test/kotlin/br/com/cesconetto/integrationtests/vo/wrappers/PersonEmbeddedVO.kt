package br.com.cesconetto.integrationtests.vo.wrappers

import br.com.cesconetto.integrationtests.vo.PersonVO
import com.fasterxml.jackson.annotation.JsonProperty

class PersonEmbeddedVO {

    @JsonProperty("personVOList")
    var persons: List <PersonVO>? = null
}