import _root_.controllers.AssetsComponents
import com.softwaremill.macwire.wire
import play.api.ApplicationLoader.Context
import play.api._
import play.api.i18n._
import play.api.routing.Router
import router.Routes


class MainApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = new MainApplicationComponents(context).application
}

class MainApplicationComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with MainModule
  with AssetsComponents
  with I18nComponents
  with play.filters.HttpFiltersComponents {

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val router: Router = {
    val prefix: String = "/"
    wire[Routes]
  }
}