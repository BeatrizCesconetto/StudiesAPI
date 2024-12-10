package br.com.cesconetto.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import br.com.cesconetto.model.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

@Repository
//Long porque o id(chave primária) é Long
interface PersonRepository : JpaRepository <Person, Long?> {

    @Modifying
    @Transactional
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    fun disabledPerson(@Param("id") id: Long?)

    //se tivesse procurando por and seria %and%
    //Ai ele retornaria, por exemplo, ⇒ Leandro, Fernanda, Alessandra
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT ('%', :firstName, '%'))")
    fun findPersonByName(@Param("firstName") firstName: String, pageable: Pageable): Page<Person>
}