package hu.bme.aut.authhelper

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.security.SecureRandom
import java.util.*
import java.util.function.Function

@Component
class JWTUtil {


    @Value("\${movesong.jwtSecret}")
    private lateinit var secretKey: String

    @Value("\${movesong.jwtExpiration}")
    private lateinit var expiration: String

    private val key: Key by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    }

    fun extractUsername(token: String?): String {
        return extractClaim(token) { obj: Claims -> obj.subject }
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(userDetails: UserDetails?): String {
        return generateToken(HashMap(), userDetails)
    }

    private fun generateToken(
        extraClaims: Map<String?, Any?>?,
        userDetails: UserDetails?
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails?.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * expiration.toLong() * 24))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateNumericToken(): String {
        return SecureRandom().nextInt(100000, 999999).toString()
    }

    fun isTokenValid(token: String?): Boolean {
        return !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String?): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
}