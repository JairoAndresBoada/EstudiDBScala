package controllers

import play.api.data.Form
import play.api.data.Forms._
import sun.awt.geom.Crossings.NonZero

object ParqueaderoForm{

 case class ParqueaderoData (tipo_vehiculo: String, placa: String , cilindraje: String)

  val formularioParqueadero = Form(
    mapping(
      "tipo_vehiculo" -> nonEmptyText,
      "placa" -> nonEmptyText,
      "cilindraje" -> nonEmptyText
    )(ParqueaderoData.apply)(ParqueaderoData.unapply)
  )
}