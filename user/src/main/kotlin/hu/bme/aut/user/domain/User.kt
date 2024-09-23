package hu.bme.aut.user.domain

import hu.bme.aut.userapi.dto.UserDto
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
    constructor() : this(0, "", "", "", false, Role.USER)

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

    companion object {
        fun fromDto(userDto: UserDto): User {
            return User(userDto.id, userDto.username, userDto.email, "", userDto.enabled)
        }

        fun toDto(user: User): UserDto {
            return UserDto(user.id, user.username, user.email, "", user.enabled)
        }
    }
}