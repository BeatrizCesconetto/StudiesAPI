package br.com.beatriz.service

import br.com.beatriz.exceptions.ResourceNotFoundException
import br.com.beatriz.data.vo.v1.PersonVO
import br.com.beatriz.mapper.DozerMapper
import br.com.beatriz.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger
import br.com.beatriz.model.Person

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List <PersonVO> {
        logger.info("Finding all people!")
        val persons = repository.findAll()

        return DozerMapper.parseListObjects(persons, PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one Person!")

        var person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID") }

        return DozerMapper.parseObject(person, PersonVO::class.java)
    }

    fun create(person: PersonVO): PersonVO {
        //chamada ao repositório e consequentemente ao banco
        logger.info("Creating one PersonVO with name ${person.firstName}!")
        var entity: Person = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun update(person: PersonVO) : PersonVO {
        logger.info("updating one PersonVO with id ${person.id}!")
        val entity = repository.findById(person.id)
          .orElseThrow { ResourceNotFoundException("No records found for this id") }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long) {
        logger.info("Deleting one Person with ID $id!")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }

}
