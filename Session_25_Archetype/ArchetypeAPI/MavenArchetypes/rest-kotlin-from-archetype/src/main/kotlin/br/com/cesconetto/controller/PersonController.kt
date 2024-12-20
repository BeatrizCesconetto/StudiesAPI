package br.com.cesconetto.controller

import br.com.cesconetto.data.vo.v1.PersonVO
import br.com.cesconetto.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import br.com.cesconetto.util.MediaType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam


@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")
class PersonController {

    @Autowired
    private lateinit var service: PersonService //lateinit precisa ser var
    //var service: PersonService = PersonService() não precisa fazer assim, é melhor usar o @Autowired

    @GetMapping( produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    @Operation(summary = "Finds all people", description = "Finds all people",
        tags = ["People"],
        responses = [
            ApiResponse(description = "Success", responseCode = "200", content = [
                Content(array = ArraySchema(schema = Schema(implementation = PersonVO::class)))
            ]),
            ApiResponse( description = "No content", responseCode = "204", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Bad request", responseCode = "400", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Not found", responseCode = "404", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )
    fun findAll(@RequestParam(value = "page", defaultValue = "0")page: Int,
                @RequestParam(value = "limit", defaultValue = "12")limit: Int,
                @RequestParam(value = "direction", defaultValue = "asc")direction: String
    ): ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        val sortDirection: Sort.Direction = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page,limit, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findAll(pageable))
    }


    @GetMapping(value = ["/findPersonByName/{firstName}"], produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    @Operation(summary = "Finds people by name", description = "Finds all people",
        tags = ["People"],
        responses = [
            ApiResponse(description = "Success", responseCode = "200", content = [
                Content(array = ArraySchema(schema = Schema(implementation = PersonVO::class)))
            ]),
            ApiResponse( description = "No content", responseCode = "204", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Bad request", responseCode = "400", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Not found", responseCode = "404", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )
    fun findPersonByName(@PathVariable(value = "firstName") firstName: String,
                @RequestParam(value = "page", defaultValue = "0")page: Int,
                @RequestParam(value = "limit", defaultValue = "12")limit: Int,
                @RequestParam(value = "direction", defaultValue = "asc")direction: String
    ): ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        val sortDirection: Sort.Direction = if ("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page,limit, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findPersonByName(firstName,pageable))
    }

    @CrossOrigin(origins = ["http://localhost:8080"])
    @GetMapping( *["/{id}"], produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    @Operation(summary = "Finds a Person ", description = "Finds a Person",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
            ]),

            ApiResponse( description = "No content", responseCode = "204", content = [
                    Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Bad request", responseCode = "400", content = [
                    Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                    Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Not found", responseCode = "404", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )

    fun finById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.findById(id)
    }

    @CrossOrigin(origins = ["http://localhost:8080", "https://beatriz.com.br"])
    @PostMapping(consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    @Operation(summary = "Adds a Person ", description = "Adds a Person",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
                ]),

            ApiResponse( description = "Bad request", responseCode = "400", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )
    fun create(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PutMapping(consumes = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    @Operation(summary = "Updates a person's information ", description = "Updates a person's information ",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
                ]),

            ApiResponse( description = "No content", responseCode = "204", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Bad request", responseCode = "400", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Not found", responseCode = "404", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )
    fun update(@RequestBody PersonVO: PersonVO): PersonVO {
        return service.update(PersonVO)
    }

    @PatchMapping( *["/{id}"], produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML] )
    @Operation(summary = "Disable a Person ", description = "Finds a Person",
        tags = ["People"],
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
                ]),

            ApiResponse( description = "No content", responseCode = "204", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Bad request", responseCode = "400", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Not found", responseCode = "404", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )

    fun disabledPersonById(@PathVariable(value = "id") id: Long): PersonVO {
        return service.disabledPerson(id)
    }

    @DeleteMapping( value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML]  )
    @Operation(summary = "Deletes a Person ", description = "Deletes a Person",
        tags = ["People"],
        responses = [
            ApiResponse( description = "No content", responseCode = "204", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Bad request", responseCode = "400", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Unauthorized", responseCode = "401", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Not found", responseCode = "404", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
            ApiResponse( description = "Internal Error", responseCode = "500", content = [
                Content(schema = Schema(implementation = Unit::class))
            ]),
        ]
    )
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }


}