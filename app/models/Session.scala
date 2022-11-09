package models

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID

final case class Session(username: String, expiration: LocalDateTime, token: String)

object Session {

    private val sessions = scala.collection.mutable.Map.empty[String, Session]

    def getSession(token: String): Option[Session] = 
        sessions.get(token)

    def createToken(username: String): String = {
        val token = s"$username.${UUID.randomUUID().toString}"
        sessions += (token  -> Session(username, LocalDateTime.now(ZoneOffset.UTC).plusHours(1), token ))

        token 
    }

}
