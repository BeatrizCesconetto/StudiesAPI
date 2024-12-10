package br.com.cesconetto.model

import jakarta.persistence.*
import java.util.Date


@Entity
@Table(name = "books")
data class Book (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, length = 180)
    var author: String = "",

    @Column(name = "launch_Date")
    var launchDate: Date? = null,

    @Column(nullable = false, length = 100)
    var price: Double = 0.0,

    @Column(nullable = false, length = 250)
    var title: String = "",

)
