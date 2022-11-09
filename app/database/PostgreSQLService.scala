package database

import cats.effect.IO
import doobie._
import doobie.implicits._

import scala.concurrent.{ExecutionContext, Future}
import cats.effect.unsafe.implicits.global
import settings.AppSettings

class PostgreSQLService(
  implicit ec: ExecutionContext,
  appSettings: AppSettings) {

  private val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    s"jdbc:postgresql:${appSettings.postgresql.databaseName}",
    appSettings.postgresql.username,
    appSettings.postgresql.password,
  )

  def getWelcomeMessage(): Future[Option[String]] =
    PostgreSQLQueries.selectWelcomeMessage().transact(xa).unsafeToFuture()

}
