package br.com.beatriz.controller

import br.com.beatriz.data.vo.v1.PersonVO
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/person")
class PersonController {

    @Autowired
    private lateinit var service: PersonService //lateinit precisa ser var
    //var service: PersonService = PersonService() não precisa fazer assim, é melhor usar o @Autowired

    @GetMapping( produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun findAll(): List <PersonVO> {
        return service.findAll()
    }

    @GetMapping( value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]  )
    fun finById(@PathVariable(value = "id") id: Long): PersonVO {
       return service.findById(id)
    }


    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun create(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE] )
    fun update(@RequestBody PersonVO: PersonVO): PersonVO {
        return service.update(PersonVO)
    }

    @DeleteMapping( value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]  )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*> {
       service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }


}