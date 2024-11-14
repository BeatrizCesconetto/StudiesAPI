package br.com.beatriz.service

import br.com.beatriz.controller.BookController
import br.com.beatriz.data.vo.v1.BookVO
import br.com.beatriz.exceptions.ResourceNotFoundException
import br.com.beatriz.exceptions.RequiredObjectIsNullException
import br.com.beatriz.mapper.DozerMapper
import br.com.beatriz.model.Book
import br.com.beatriz.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo


@Service
class BookService {

    @Autowired
    private lateinit var repository: BookRepository

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(): List <BookVO> {
        logger.info("Finding all books!")
        val books = repository.findAll()

        val vos = DozerMapper.parseListObjects(books, BookVO::class.java)
        for (book in vos) {
            val withSelfRel = linkTo(BookController::class.java).slash(book.key).withSelfRel()
            book.add(withSelfRel)
        }

        return vos
    }

    fun findById(id: Long): BookVO {
        logger.info("Finding one Book with ID $id!")
        val book = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }

        val bookVO: BookVO = DozerMapper.parseObject(book, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun create(book: BookVO?): BookVO {
        if (book == null) throw RequiredObjectIsNullException()
        //chamada ao repositório e consequentemente ao banco
        logger.info("Creating one BookVO with title ${book.title}!")
        val entity: Book = DozerMapper.parseObject(book, Book::class.java)
        val bookVO: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }
    fun update(book: BookVO?) : BookVO {
        if (book == null) throw RequiredObjectIsNullException()
        logger.info("updating one BookVO with id ${book.key}!")
        val entity = repository.findById(book.key)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }

        entity.author = book.author
        entity.title = book.title
        entity.price = book.price
        entity.launchDate = book.launchDate

        val bookVO: BookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }

    fun delete(id: Long) {
        logger.info("Deleting one Book with ID $id!")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }

}