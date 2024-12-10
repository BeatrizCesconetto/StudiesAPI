package br.com.cesconetto.integrationtests.vo.wrappers

import br.com.cesconetto.integrationtests.vo.BookVO
import com.fasterxml.jackson.annotation.JsonProperty

class BookEmbeddedVO {

    @JsonProperty("bookVOList")
    var books: List <BookVO>? = null
}