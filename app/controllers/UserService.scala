package controllers

import java.util.function.Consumer

import com.sample._
import org.bson.Document
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._


/**
 * Created by pragati on 13.05.15.
 */
object UserService extends Controller with UserJson{

  def createUser = Action { implicit request =>
    request.body.asJson.map{json=>
      json.validate(userR).map{ res => UserNoSqlDAO.insertUser(json.toString()); Ok("print some done")}
        .recoverTotal(err => BadRequest("Detected error:"+ JsError.toFlatJson(err)))}.get
  }

  def getAllUser = Action {
    val s = StringBuilder.newBuilder
    //userColl.find().forEach(((t:DBObject) => s.append(t.toString)).asInstanceOf[Consumer[DBObject]])
    UserNoSqlDAO.getAllUser.forEach(new Consumer[Document] {
      override def accept(t: Document): Unit = s.append(t.toJson).append("\n")
    })
    Ok(s.toString())
  }

  def getUser(i:String) = Action {
    Ok(views.html.index("Your next application is ready."))
  }

  def createMsg(i:String) = Action {implicit request =>
    request.body.asJson.map(json=>
      json.validate(messageR).map{ res => UserNoSqlDAO.updateUserByMsg(i, json.toString()); Ok("insert done")}
        .recoverTotal(err => BadRequest("Detected error:"+ JsError.toFlatJson(err)))).get
  }

  def deleteUser(i:String) = Action {
      try UserNoSqlDAO.deleteUser(i) catch {
        case _: Throwable => InternalServerError("Failed to delete user " + i)
      }
    Ok("its removed")
  }

  //      json.validate(userR).map{ res => if (insert(json)) Ok("print some done") else InternalServerError("failed to persist")}

}
