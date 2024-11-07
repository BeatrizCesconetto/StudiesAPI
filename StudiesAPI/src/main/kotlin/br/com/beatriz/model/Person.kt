package br.com.beatriz.model

data class Person (
     //Não pode usar val em data class
    var id: Long = 0,
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var gender: String = ""
)