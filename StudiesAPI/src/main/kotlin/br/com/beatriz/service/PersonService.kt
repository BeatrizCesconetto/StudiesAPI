package br.com.beatriz.service

import br.com.beatriz.exceptions.ResourceNotFoundException
import br.com.beatriz.data.vo.v1.PersonVO
import br.com.beatriz.exceptions.RequiredObjectIsNullException
import br.com.beatriz.data.vo.v2.PersonVO as PersonVOV2
import br.com.beatriz.mapper.DozerMapper
import br.com.beatriz.mapper.custom.PersonMapper
import br.com.beatriz.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger
import br.com.beatriz.model.Person
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import br.com.beatriz.controller.PersonController as PersonController1


@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List <PersonVO> {
        logger.info("Finding all people!")
        val persons = repository.findAll()

        val vos = DozerMapper.parseListObjects(persons, PersonVO::class.java)
        for (person in vos) {
            val withSelfRel = linkTo(PersonController1::class.java).slash(person.key).withSelfRel()
            person.add(withSelfRel)
        }

        return vos
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
    fun createV2(person: PersonVOV2?): PersonVOV2 {
        if (person == null) throw RequiredObjectIsNullException()
        //chamada ao repositório e consequentemente ao banco
        logger.info("Creating one PersonVO with name ${person.firstName}!")
        val entity: Person = mapper.mapVOToEntity(person)
        val personVO: br.com.beatriz.data.vo.v2.PersonVO = mapper.mapEntityToVO(repository.save(entity))
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

    fun delete(id: Long) {
        logger.info("Deleting one Person with ID $id!")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }

}