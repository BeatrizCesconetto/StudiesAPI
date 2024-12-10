package br.com.cesconetto.service

import br.com.cesconetto.exceptions.ResourceNotFoundException
import br.com.cesconetto.data.vo.v1.PersonVO
import br.com.cesconetto.exceptions.RequiredObjectIsNullException
import br.com.cesconetto.mapper.DozerMapper
import br.com.cesconetto.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger
import br.com.cesconetto.model.Person
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.transaction.annotation.Transactional
import br.com.cesconetto.controller.PersonController as PersonController1


@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var assembler: PagedResourcesAssembler<PersonVO>



    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<PersonVO>> {

        logger.info("Finding all people!")
        val persons = repository.findAll(pageable)
        val vos = persons.map {p -> DozerMapper.parseObject(p,PersonVO::class.java)}
        vos.map { p -> p.add(linkTo(PersonController1::class.java).slash(p.key).withSelfRel()) }
        return assembler.toModel(vos)
    }

    fun findPersonByName(firstName: String, pageable: Pageable): PagedModel<EntityModel<PersonVO>> {

        logger.info("Finding all people!")

        val persons = repository.findPersonByName(firstName, pageable)
        val vos = persons.map {p -> DozerMapper.parseObject(p,PersonVO::class.java)}
        vos.map { p -> p.add(linkTo(PersonController1::class.java).slash(p.key).withSelfRel()) }
        return assembler.toModel(vos)
    }

    fun findById(id: Long): PersonVO {

        logger.info("Finding one Person with ID $id!")

        val person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }
        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController1::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()
        //chamada ao repositório e consequentemente ao banco
        logger.info("Creating one PersonVO with name ${person.firstName}!")
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController1::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun update(person: PersonVO?) : PersonVO {
        if (person == null) throw RequiredObjectIsNullException()
        logger.info("updating one PersonVO with id ${person.key}!")
        val entity = repository.findById(person.key)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        val personVO: PersonVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController1::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    @Transactional
    fun disabledPerson(id: Long): PersonVO {
        logger.info("Disabling one Person with ID $id!")
        repository.disabledPerson(id)
        val person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }

        val personVO: PersonVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController1::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Deleting one Person with ID $id!")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }

}