package br.com.beatriz.service

import br.com.beatriz.controller.BookController
import br.com.beatriz.data.vo.v1.BookVO
import br.com.beatriz.exceptions.ResourceNotFoundException
import br.com.beatriz.exceptions.RequiredObjectIsNullException
import br.com.beatriz.mapper.DozerMapper
import br.com.beatriz.model.Book
import br.com.beatriz.repository.BookRepository
import br.com.beatriz.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException


@Service
class UserService(@field:Autowired var repository: UserRepository) : UserDetailsService {

//    @Autowired
//    private lateinit var repository: BookRepository

    private val logger = Logger.getLogger(UserService::class.java.name)

    override fun loadUserByUsername(username: String?): UserDetails {
        logger.info("Finding one User by Username $username!")
        val user = repository.findByUsername(username)
        return user ?: throw UsernameNotFoundException("Username $username not found!")
    }

}