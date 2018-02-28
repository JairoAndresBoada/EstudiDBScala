
package controllers


import java.sql.ResultSet
import javax.inject._

import play.api.mvc._
import play.api.mvc.Action

import scala.reflect.macros.whitebox
@Singleton
class ConsultasController @Inject()(cc: ControllerComponents) extends AbstractController(cc){

  import java.sql.DriverManager
  import java.sql.Connection

  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://localhost:3306/parqueadero"
  val username = "root"
  val password = "root"

  //Class.forName(driver)

  var driverLoaded = false

  def exe = Action{
    Ok(views.html.index("Your new application is ready."))
  }


  def loadDriver()  {
    try{
      Class.forName("com.mysql.jdbc.Driver")
      driverLoaded = true
    }catch{
      case e: Exception  => {
        println("ERROR: Driver not available: " + e.getMessage)
        throw e
      }
    }
  }

  def executeQuerySelect(query: String): ResultSet = {
    loadDriver()
    val connection: Connection = DriverManager.getConnection(url, username, password)
    try {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(query)

      resultSet

    } catch {
      case e => e.printStackTrace
      null
    }

  }


  def executeQueryInsert(query: String): Boolean = {
    loadDriver()
    val connection: Connection = DriverManager.getConnection(url, username, password)
    try {
      val statement = connection.createStatement()
      val resultSet = statement.execute(query)

      resultSet

    } catch {
      case e => e.printStackTrace
        false
    }

  }
}
