import controllers.{IdentityController, UserManagementController}
import database.{MigrationService, PostgreSQLService}
import play.api.Configuration
import play.api.mvc.ControllerComponents
import settings.AppSettings

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

trait MainModule {

  import com.softwaremill.macwire._

  val executor                      = Executors.newFixedThreadPool(4)
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(executor)

  def controllerComponents: ControllerComponents
  def configuration: Configuration

  implicit lazy val appSettings: AppSettings = wire[AppSettings]

  lazy val postgreSQLService: PostgreSQLService       = wire[PostgreSQLService]
  lazy val identityController: IdentityController     = wire[IdentityController]
  lazy val managementController: UserManagementController = wire[UserManagementController]

  MigrationService.update(appSettings)

}
