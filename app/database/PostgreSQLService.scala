package database

import cats.effect.IO
import doobie._
import doobie.implicits._

import scala.concurrent.{ExecutionContext, Future}
import cats.effect.unsafe.implicits.global
import settings.AppSettings

import cats.MonadError
import models.UserInfoWithCredentials
import org.slf4j.{Logger, LoggerFactory}

class PostgreSQLService(
  implicit ec: ExecutionContext,
  appSettings: AppSettings) {

  val cio: MonadError[ConnectionIO, Throwable] = implicitly
  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    s"jdbc:postgresql:${appSettings.postgresql.databaseName}",
    appSettings.postgresql.username,
    appSettings.postgresql.password,
  )

  def getWelcomeMessage(): Future[Option[String]] =
    PostgreSQLQueries.selectWelcomeMessage().transact(xa).unsafeToFuture()

  def addUser(credentials: UserInfoWithCredentials): Future[Either[Exception, Unit]] = 
    (for {
      userMaybe <- PostgreSQLQueries.findId(credentials.username) // Some(id) if user already exist
      _         <- cio fromEither userMaybe 
        .map(_ => new Exception(s"User ${credentials.username} already exists")) 
        .toLeft(()) 
      _         <- PostgreSQLQueries.addUserUnsafe(
        credentials.username, 
        (credentials.password + appSettings.secrets.salt).hashCode(),
        credentials.gender, 
        credentials.age
        ).run

    } yield Right(())).transact(xa).unsafeToFuture()

  def addUserSilent(credentials: UserInfoWithCredentials): Future[Unit] = 
    PostgreSQLQueries.addUserUnsafe(
        credentials.username, 
        (credentials.password + appSettings.secrets.salt).hashCode(),
        credentials.gender, 
        credentials.age
        ).run
        .map(_ => ()) 
        .transact(xa).unsafeToFuture()


}
