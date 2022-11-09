package controllers

import play.api.mvc._

import database.PostgreSQLService
import scala.concurrent.ExecutionContext

class UserManagementController(val controllerComponents: ControllerComponents,
  val postgreSQLService: PostgreSQLService)(implicit ec: ExecutionContext) extends BaseController {

  def users: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => 
    postgreSQLService
    .getAllUsers()
    .map(listOfUsers => Ok(listOfUsers.toString())) // This should be a json: {"users": [...]}
    }
}
