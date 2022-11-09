package settings

import play.api.Configuration

trait Settings {

  def configuration: Configuration

  object postgresql {

    val databaseName = configuration.get[String]("app.postgresql.database-name")
    val username     = configuration.get[String]("app.postgresql.username")
    val password     = configuration.get[String]("app.postgresql.password")

  }

}

class AppSettings(val configuration: Configuration) extends Settings
