package database

import liquibase.{Contexts, Liquibase}
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.{ClassLoaderResourceAccessor, ResourceAccessor}
import org.slf4j.{Logger, LoggerFactory}
import settings.AppSettings

import java.sql.{Connection, DriverManager}
import scala.util.Try

object MigrationService {

  def changelogFile: String = "migration/changelog.xml"

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  private lazy val resourceAccessor: ResourceAccessor = new ClassLoaderResourceAccessor(this.getClass.getClassLoader)
  private def create(appSettings: AppSettings): Try[Connection] =
    Try(DriverManager.getConnection(uri(appSettings)))

  private def uri(appSettings: AppSettings): String =
    s"jdbc:postgresql://localhost/${appSettings.postgresql.databaseName}?user=${appSettings.postgresql.username}&password=${appSettings.postgresql.password}"

  def update(appSettings: AppSettings): Try[Unit] = {
    logger.info("Updating database...")
    for {
      connection <- create(appSettings)
      jdbcConnection = new JdbcConnection(connection)
      liquibase <- Try(new Liquibase(changelogFile, resourceAccessor, jdbcConnection))
      liquibaseContexts = new Contexts()
      _ <- Try(liquibase.update(liquibaseContexts))
    } yield {
      logger.info("Database updated")
    }
  }

}
