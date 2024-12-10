package br.com.cesconetto.data.vo.v1

import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class AccountCredentialsVO(

    var username: String? = null,
    var password: String? = null
)