package controllers

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by pragati on 13.05.15.
 */
trait UserJson {

  object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(new Timestamp(format.parse(str).getTime))
    }
    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  object emailValidator extends Reads[String] {
    def reads(json: JsValue) = {
      val str = json.as[String]
      if(str contains "@") JsSuccess(str) else JsError("email is not valid")
    }
  }

  implicit val userR = (
    (__ \ 'name).read[String] and
      (__ \ 'age).read[Long] and
      (__ \'email).read(emailValidator)
    ) tupled

  implicit val messageR = (
    (__ \ 'user).read[String] and
      (__ \ 'message).read[String] and
      (__ \ 'date).format(timestampFormat)
    ) tupled
}
