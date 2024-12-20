package br.com.cesconetto.unittests.mockito.services

import br.com.cesconetto.exceptions.RequiredObjectIsNullException
import br.com.cesconetto.repository.BookRepository
import br.com.cesconetto.service.BookService
import br.com.cesconetto.unittests.mocks.MockBook
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
class BookServiceTest {

   private lateinit var inputObject: MockBook

   @InjectMocks
   private lateinit var service: BookService

   @Mock
   private lateinit var repository: BookRepository

    @BeforeEach
    fun setUpMock() {
        inputObject = MockBook()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findById() {
        val book = inputObject.mockEntity(1)
        book.id = 1
        `when`(repository.findById(1)).thenReturn(Optional.of(book))

        val result = service.findById(1)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        println(result.links)
        assertTrue(result.links.toString().contains("/api/book/v1/1"))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)
    }

    @Test
    fun createWithNullBook() {
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
        assertTrue(result.links.toString().contains("/api/book/v1/1"))
        assertEquals("Some Title1", result.title)
        assertEquals("Some Author1", result.author)
        assertEquals(25.0, result.price)

        @Test
        fun updateWithNullBook() {
            val exception: Exception = assertThrows(
                RequiredObjectIsNullException::class.java
            ) { service.update(1, null) }

            val expectedMessage = "It is not allow to persist a null object!"
            val actualMessage = exception.message
            assertTrue(actualMessage!!.contains(expectedMessage))
        }

        @Test
        fun update() {
            val entity = inputObject.mockEntity(1)

            val persisted = entity.copy()
            persisted.id = 1

            `when`(repository.save(entity)).thenReturn(persisted)
            `when`(repository.findById(1)).thenReturn(Optional.of(entity))

            val vo = inputObject.mockVO(1)
            val result = service.update(1, vo)

            assertNotNull(result)
            assertNotNull(result.key)
            assertNotNull(result.links)
            println(result.links)
            assertTrue(result.links.toString().contains("/api/book/v1/1"))
            assertEquals("Some Title1", result.title)
            assertEquals("Some Author1", result.author)
            assertEquals(25.0, result.price)
        }

        @Test
        fun delete() {
            val entity = inputObject.mockEntity(1)
            `when`(repository.findById(1)).thenReturn(Optional.of(entity))
            service.delete(1)
        }


    }
}
