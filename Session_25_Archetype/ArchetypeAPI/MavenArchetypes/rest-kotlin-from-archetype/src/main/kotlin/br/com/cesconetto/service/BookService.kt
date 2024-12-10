package br.com.cesconetto.service

import br.com.cesconetto.controller.BookController
import br.com.cesconetto.controller.PersonController
import br.com.cesconetto.data.vo.v1.BookVO
import br.com.cesconetto.data.vo.v1.PersonVO
import br.com.cesconetto.exceptions.ResourceNotFoundException
import br.com.cesconetto.exceptions.RequiredObjectIsNullException
import br.com.cesconetto.mapper.DozerMapper
import br.com.cesconetto.model.Book
import br.com.cesconetto.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.PagedModel
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.stereotype.Service
import java.util.logging.Logger
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo


@Service
class BookService {

    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var assembler: PagedResourcesAssembler<BookVO>

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<BookVO>> {

        logger.info("Finding all books!")
        val books = repository.findAll(pageable)
        val vos = books.map {b -> DozerMapper.parseObject(b, BookVO::class.java)}
        vos.map { b -> b.add(linkTo(PersonController::class.java).slash(b.key).withSelfRel()) }
        return assembler.toModel(vos)
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
    fun update(id: Long, book: BookVO?) : BookVO {
        if (book == null) throw RequiredObjectIsNullException()
        logger.info("updating one BookVO with id $id!")
        val entity = repository.findById(id)
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