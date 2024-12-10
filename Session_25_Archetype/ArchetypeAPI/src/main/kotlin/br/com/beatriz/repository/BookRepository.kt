package br.com.beatriz.repository

import br.com.beatriz.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import br.com.beatriz.model.Person

@Repository
//Long porque o id(chave primária) é Long
interface BookRepository : JpaRepository <Book, Long?>