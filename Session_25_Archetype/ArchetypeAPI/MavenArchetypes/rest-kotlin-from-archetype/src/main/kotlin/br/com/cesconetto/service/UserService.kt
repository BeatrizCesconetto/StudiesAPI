package br.com.cesconetto.service

import br.com.cesconetto.controller.BookController
import br.com.cesconetto.data.vo.v1.BookVO
import br.com.cesconetto.exceptions.ResourceNotFoundException
import br.com.cesconetto.exceptions.RequiredObjectIsNullException
import br.com.cesconetto.mapper.DozerMapper
import br.com.cesconetto.model.Book
import br.com.cesconetto.repository.BookRepository
import br.com.cesconetto.repository.UserRepository
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