package br.com.beatriz.controller

import br.com.beatriz.data.vo.v1.PersonVO
import br.com.beatriz.data.vo.v2.PersonVO as PersonVOV2
import br.com.beatriz.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import br.com.beatriz.util.MediaType
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
@RequestMapping("/api/person/v1")
class PersonController {

    @Autowired
    private lateinit var service: PersonService //lateinit precisa ser var
    //var service: PersonService = PersonService() não precisa fazer assim, é melhor usar o @Autowired

    @GetMapping( produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    fun findAll(): List <PersonVO> {
        return service.findAll()
    }

    @GetMapping( *["/{id}"],
            produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]  )
    fun finById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }


    @PostMapping(consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    fun create(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PostMapping(value = ["/v2"], consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    fun createV2(@RequestBody person: PersonVOV2): PersonVOV2 {
        return service.createV2(person)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    fun update(@RequestBody PersonVO: PersonVO): PersonVO {
        return service.update(PersonVO)
    }

    @DeleteMapping( value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]  )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }


}