package br.com.cesconetto.mapper.custom

import br.com.cesconetto.data.vo.v1.PersonVO
import br.com.cesconetto.model.Person
import org.springframework.stereotype.Service
import java.util.Date

@Service
class PersonMapper {

    fun mapEntityToVO(person: Person): PersonVO {

        val vo = PersonVO()

        vo.key = person.id
        vo.address = person.address
        vo.birthDay = Date()
        vo.firstName = person.firstName
        vo.lastName = person.lastName
        vo.gender = person.gender

        return vo

    }

    fun mapVOToEntity(person: PersonVO): Person {

        val entity = Person()

        entity.id = person.key
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        return entity
    }
}