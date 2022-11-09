package controllers

import play.api.mvc._

class UserManagementController(val controllerComponents: ControllerComponents) extends BaseController {

  def users: Action[AnyContent] = ???
}
