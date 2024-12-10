package br.com.beatriz.integrationtests.vo.wrappers

import br.com.beatriz.integrationtests.vo.BookVO
import com.fasterxml.jackson.annotation.JsonProperty

class BookEmbeddedVO {

    @JsonProperty("bookVOList")
    var books: List <BookVO>? = null
}