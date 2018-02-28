package controllers

import javax.inject.{Inject, Singleton}

import scala.collection.mutable._
import play.api.mvc._
import controllers.ConsultasController
import controllers.ParqueaderoForm.ParqueaderoData
import models.Parqueadero
import play.api.data._

@Singleton
class ParqueaderoController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc){

  val listaParqueos: ArrayBuffer[Parqueadero] = new ArrayBuffer[Parqueadero]()
  val ruta = routes.ParqueaderoController.parquear()
  val c: ConsultasController = new ConsultasController(cc)


  def listaParqueados = Action{ implicit requerimiento : MessagesRequest[AnyContent] =>

    if(listaParqueos.length > 0){
      listaParqueos.remove(0, listaParqueos.length)
    }

    val resultado = c.executeQuerySelect("Select * From Vehiculo")

    while (resultado.next()) {
      val placa = resultado.getString("placa")
      val vehiculo_tipo = resultado.getString("vehiculo_tipo")
      val cilindraje = resultado.getString("cilindraje")

      def tipoVehi = vehiculo_tipo match{
        case "1" => "Moto"
        case "2" => "Carro"
        case _ => "es un unicornio"
      }

      def cilindr = cilindraje match{
        case null => "0"
        case notnull => cilindraje
        case _ => "es un unicornio"
      }

      var p: Parqueadero = new Parqueadero(tipoVehi, placa, cilindraje)
      listaParqueos += p
    }

    Ok(views.html.Parqueadero(ruta,ParqueaderoForm.formularioParqueadero,listaParqueos))
  }

  def parquear = Action{ implicit requerimiento : MessagesRequest[AnyContent] =>

    val error = { formularioErrores: Form[ParqueaderoData] =>
      BadRequest(views.html.Parqueadero(ruta, ParqueaderoForm.formularioParqueadero, listaParqueos))
    }

    val success = { datos: ParqueaderoData =>
      val park = Parqueadero(tipo_vehiculo = datos.tipo_vehiculo, placa = datos.placa, cilindraje = datos.cilindraje)
      listaParqueos.append(park)
      var tipo = if (datos.tipo_vehiculo == "1") "Moto" else "Carro"
      c.executeQueryInsert("Insert Into vehiculo Values ('"+datos.tipo_vehiculo+"','"+datos.placa+"','"+datos.cilindraje+"')")
      Redirect(routes.ParqueaderoController.listaParqueados()).flashing("info"->"Haz agregado un Vehiculo a la lista...")
    }

    val formValidationResult = ParqueaderoForm.formularioParqueadero.bindFromRequest()
    formValidationResult.fold(error, success)

  }

}