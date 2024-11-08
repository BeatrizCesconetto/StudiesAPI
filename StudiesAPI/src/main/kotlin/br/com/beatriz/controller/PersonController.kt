package br.com.beatriz.controller

import br.com.beatriz.converters.NumberConverter
import br.com.beatriz.exceptions.UnsupportedMathOperationException
import br.com.beatriz.math.SimpleMath
import br.com.beatriz.model.Person
import br.com.beatriz.repository.PersonRepository
import br.com.beatriz.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/person")
class PersonController {

    @Autowired
    private lateinit var service: PersonService //lateinit precisa ser var
    //var service: PersonService = PersonService() não precisa fazer assim, é melhor usar o @Autowired

    @GetMapping( produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun findAll(): List <Person> {
        return service.findAll()
    }

    @GetMapping( value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]  )
    fun finById(@PathVariable(value = "id") id: Long): Person {
       return service.findById(id)
    }


    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun create(@RequestBody person: Person): Person {
        return service.create(person)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun update(@RequestBody person: Person): Person {
        return service.update(person)
    }

    @DeleteMapping( value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]  )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*> {
       service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }


}