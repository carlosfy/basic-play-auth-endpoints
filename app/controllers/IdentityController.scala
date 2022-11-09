package controllers

import database.PostgreSQLService
import play.api.mvc._

import scala.concurrent.ExecutionContext
import models.UserInfoWithCredentials
import scala.concurrent.Future
import models.Credentials

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

  def signup(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => //Should be Action[UserInfoWithCredentials]
    val jsonBody = request.body.asJson

    val username: Option[String] = jsonBody.map(json => (json \ "username").as[String])
    val password: Option[String] = jsonBody.map(json => (json \ "password").as[String])
    val gender: Option[String] = jsonBody.map(json => (json \ "gender").as[String])
    val age: Option[Int] = jsonBody.map(json => (json \ "age").as[Int])

    if (username.isEmpty || password.isEmpty)
      Future(BadRequest(views.html.index(s"Empty username or password")))
    else{
      postgreSQLService
      .addUser(UserInfoWithCredentials(username.get, password.get, gender, age))
      .map(_ match {
        case Right(_) => Ok(views.html.index(s"User $username has been registered")) // Maybe generate a token here
        case Left(e)  => BadRequest(views.html.index(s"User $username already exists. ErrorMsg: ${e.getMessage()}"))
      })

    }
    
    }

  def login(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => //Should be Action[UserInfoWithCredentials]
    val jsonBody = request.body.asJson

    val username: Option[String] = jsonBody.map(json => (json \ "username").as[String])
    val password: Option[String] = jsonBody.map(json => (json \ "password").as[String])

    if (username.isEmpty || password.isEmpty)
      Future(BadRequest(views.html.index(s"Empty username or password")))
    else{
      postgreSQLService
      .getPasswordHash(Credentials(username.get, password.get))
      .map(_ match {
        case Some(storedHash) if(password.get == storedHash) => 
          val token = models.Session.createToken(username.get)
          Ok(token) // return json: {"token": token}
        case _  => BadRequest(views.html.index(s"User or password incorrect"))
      })

    }
    
    }

  def userInfo(): Action[AnyContent] = ???
}
