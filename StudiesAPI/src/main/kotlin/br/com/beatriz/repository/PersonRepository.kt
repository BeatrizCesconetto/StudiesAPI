package br.com.beatriz.repository

import br.com.beatriz.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
//Long porque o id(chave primária) é Long
interface PersonRepository : JpaRepository <Person, Long?>