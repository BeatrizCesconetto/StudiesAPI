package br.com.cesconetto.repository

import br.com.cesconetto.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import br.com.cesconetto.model.Person
import br.com.cesconetto.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
//Long porque o id(chave primária) é Long
interface UserRepository : JpaRepository <User?, Long?> {

    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    fun findByUsername(@Param("userName") userName: String?) : User?
}
