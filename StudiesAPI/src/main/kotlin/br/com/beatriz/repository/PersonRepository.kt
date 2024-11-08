package br.com.beatriz.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import br.com.beatriz.model.Person

@Repository
//Long porque o id(chave primária) é Long
interface PersonRepository : JpaRepository <Person, Long?>