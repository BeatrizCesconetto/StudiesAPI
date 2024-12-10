package br.com.beatriz.unittests.mockito.services

import br.com.beatriz.exceptions.RequiredObjectIsNullException
import br.com.beatriz.repository.PersonRepository
import br.com.beatriz.service.PersonService
import br.com.beatriz.unittests.mocks.MockPerson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@ExtendWith(MockitoExtension::class)
class PersonServiceTest {

   private lateinit var inputObject: MockPerson

   @InjectMocks
   private lateinit var service: PersonService

   @Mock
   private lateinit var repository: PersonRepository

    @BeforeEach
    fun setUpMock() {
        inputObject = MockPerson()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findById() {
        val person = inputObject.mockEntity(1)
        person.id = 1
        `when`(repository.findById(1)).thenReturn(Optional.of(person))

        val result = service.findById(1)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        println(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun createWithNullPerson() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) {service.create(null) }

        val expectedMessage = "It is not allow to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }


    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)
        val result = service.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        println(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun updateWithNullPerson() {
        val exception: Exception = assertThrows(
            RequiredObjectIsNullException::class.java
        ) {service.update(null) }

        val expectedMessage = "It is not allow to persist a null object!"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun update() {val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1

        `when`(repository.save(entity)).thenReturn(persisted)
        `when`(repository.findById(1)).thenReturn(Optional.of(entity))

        val vo = inputObject.mockVO(1)
        val result = service.update(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        println(result.links)
        assertTrue(result.links.toString().contains("/api/person/v1/1"))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun delete() {
        val entity = inputObject.mockEntity(1)
        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
        service.delete(1)
    }



}