package controllers

import database.PostgreSQLService
import play.api.mvc._

import scala.concurrent.ExecutionContext

class IdentityController(
  val controllerComponents: ControllerComponents,
  val postgreSQLService: PostgreSQLService,
)(implicit ec: ExecutionContext)
    extends BaseController {

  def index() = Action.async {
    postgreSQLService
      .getWelcomeMessage()
      .map {
        case Some(value) => Ok(views.html.index(value))
        case None        => Ok(views.html.index("Can't fetch data from the database"))
      }
  }

  def signup(): Action[AnyContent] = ???

  def login(): Action[AnyContent] = ???

  def userInfo(): Action[AnyContent] = ???
}
