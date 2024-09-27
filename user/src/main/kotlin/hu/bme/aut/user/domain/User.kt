package hu.bme.aut.user.domain

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("username")),
        UniqueConstraint(columnNames = arrayOf("email"))]
)
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "username", nullable = false)
    private var username: String,

    @Column(name = "first_name", nullable = false)
    open var firstName: String,

    @Column(name = "last_name", nullable = false)
    open var lastName: String,

    @Column(name = "email", nullable = false)
    private var email: String,

    @Column(name = "password", nullable = false)
    private var password: String,

    @Column(name = "enabled", nullable = false)
    open var enabled: Boolean = false,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    open var role: Role = Role.USER,

    ) : UserDetails {

    constructor() : this(
        0,
        "",
        "",
        "",
        "",
        "",
        false,
        Role.USER
    )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    open fun getEmail(): String {
        return email
    }

    open fun setPassword(password: String) {
        this.password = password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}