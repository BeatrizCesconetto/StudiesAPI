package br.com.beatriz.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import br.com.beatriz.model.Person
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
//Long porque o id(chave primária) é Long
interface PersonRepository : JpaRepository <Person, Long?> {

    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    fun disabledPerson(@Param("id") id: Long?)
}