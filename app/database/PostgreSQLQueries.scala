package database

import doobie._
import doobie.implicits._

object PostgreSQLQueries {

  def selectWelcomeMessage(): ConnectionIO[Option[String]] =
    sql"SELECT message FROM welcome_message".query[String].to[List].map(_.headOption)

}
