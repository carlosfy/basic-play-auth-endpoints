package settings

import play.api.Configuration

trait Settings {

  def configuration: Configuration

  object postgresql {

    val databaseName = configuration.get[String]("app.postgresql.database-name")
    val username     = configuration.get[String]("app.postgresql.username")
    val password     = configuration.get[String]("app.postgresql.password")

  }

  object secrets {
    val salt = configuration.get[String]("app.secrets.salt")
  }

}

class AppSettings(val configuration: Configuration) extends Settings
