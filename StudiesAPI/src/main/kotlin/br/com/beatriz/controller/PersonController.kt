package br.com.beatriz.controller

import br.com.beatriz.converters.NumberConverter
import br.com.beatriz.exceptions.UnsupportedMathOperationException
import br.com.beatriz.math.SimpleMath
import br.com.beatriz.model.Person
import br.com.beatriz.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/person")
class PersonController {val counter: AtomicLong = AtomicLong()

    @Autowired
    private lateinit var service: PersonService //lateinit precisa ser var
    //var service: PersonService = PersonService() não precisa fazer assim, é melhor usar o @Autowired

    @RequestMapping( method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun findAll(): List <Person> {
        return service.findAll()
    }

    @RequestMapping( value = ["/{id}"], method = [RequestMethod.GET],
        produces = [MediaType.APPLICATION_JSON_VALUE]  )
    fun finById(@PathVariable(value = "id") id: Long): Person {
       return service.findById(id)
    }


    @RequestMapping( method = [RequestMethod.POST],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun create(@RequestBody person: Person): Person {
        return service.create(person)
    }

    @RequestMapping( method = [RequestMethod.PUT],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun update(@RequestBody person: Person): Person {
        return service.update(person)
    }

    @RequestMapping( value = ["/{id}"], method = [RequestMethod.DELETE],
        produces = [MediaType.APPLICATION_JSON_VALUE]  )
    fun delete(@PathVariable(value = "id") id: Long) {
       service.delete(id)
    }


}