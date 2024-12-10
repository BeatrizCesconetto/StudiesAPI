package br.com.cesconetto.repository

import br.com.cesconetto.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import br.com.cesconetto.model.Person

@Repository
//Long porque o id(chave primária) é Long
interface BookRepository : JpaRepository <Book, Long?>