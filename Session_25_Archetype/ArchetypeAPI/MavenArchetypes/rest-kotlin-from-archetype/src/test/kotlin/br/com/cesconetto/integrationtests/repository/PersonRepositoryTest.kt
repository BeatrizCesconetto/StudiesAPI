package br.com.cesconetto.integrationtests.repository

import br.com.cesconetto.Startup
import br.com.cesconetto.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.cesconetto.model.Person
import br.com.cesconetto.repository.PersonRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Startup::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class PersonRepositoryTest : AbstractIntegrationTest(){

    @Autowired
    private lateinit var repository: PersonRepository

    private lateinit var person: Person

    @BeforeAll
    fun setup() {
        person = Person()
    }

    @Test
    @Order(0)
    fun testFindByName() {
        val pageable: Pageable = PageRequest.of(0,12, Sort.by(Sort.Direction.ASC, "firstName"))

        person = repository.findPersonByName("ayr", pageable).content[0]

        assertNotNull(person)
        assertNotNull(person.id)
        assertNotNull(person.firstName)
        assertNotNull(person.lastName)
        assertNotNull(person.address)
        assertNotNull(person.gender)
        assertEquals("Ayrton", person.firstName)
        assertEquals("Senna", person.lastName)
        assertEquals("São Paulo", person.address)
        assertEquals("Male", person.gender)
        assertEquals(true, person.enabled)
    }

    @Test
    @Order(1)
    fun testDisabledPerson() {
        repository.disabledPerson(person.id)
        person = repository.findById(person.id).get()

        assertNotNull(person)
        assertNotNull(person.id)
        assertNotNull(person.firstName)
        assertNotNull(person.lastName)
        assertNotNull(person.address)
        assertNotNull(person.gender)
        assertEquals("Ayrton", person.firstName)
        assertEquals("Senna", person.lastName)
        assertEquals("São Paulo", person.address)
        assertEquals("Male", person.gender)
        assertEquals(false, person.enabled)
    }
}