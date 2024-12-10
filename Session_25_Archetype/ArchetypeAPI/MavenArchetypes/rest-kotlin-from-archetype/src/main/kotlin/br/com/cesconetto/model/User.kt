package br.com.cesconetto.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "user_name", unique = true)
    var userName: String? = null

    @Column(name = "full_name")
    var fullName: String? = null

    @Column(name = "password")
    private var password: String? = null

    @Column(name = "account_non_expired")
    var accountNonExpired: Boolean? = null

    @Column(name = "account_non_locked")
    var account_non_locked: Boolean? = null

    @Column(name = "credentials_non_expired")
    var credentials_non_expired: Boolean? = null

    @Column(name = "enabled")
    var enabled: Boolean? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_permission",
        joinColumns = [JoinColumn(name = "id_user")],
        inverseJoinColumns = [JoinColumn(name = "id_permission")] )
    var permissions: List<Permission>? = null

    val roles: List<String?>
        get() {
            val roles: MutableList<String?> = ArrayList()
            for (permission in permissions!!) {
                roles.add(permission.description)
            }
            return  roles
        }

    override fun getAuthorities(): Collection <GrantedAuthority> {
        return permissions!!
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun getUsername(): String {
        return userName!!
    }

    override fun isAccountNonExpired(): Boolean {
        return accountNonExpired!!
    }

    override fun isAccountNonLocked(): Boolean {
        return account_non_locked!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credentials_non_expired!!
    }

    override fun isEnabled(): Boolean {
        return enabled!!
    }

}