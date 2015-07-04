package controllers

import java.sql.Timestamp
import java.time
import java.time.Instant
import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import com.sample.akka.RabbitMqListener
import play.api._
import play.api.db._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import com.sample._
import IdGen._

/**
 * Created by pragati on 12.04.15.
 */
object Application extends Controller with DBSession{

  val apps = List[BootStrapHook](new RabbitMqListener)

  def index = Action {
    Ok(views.html.index("Your next application is ready."))
  }

  def sample = Action {
    apps.foreach(hook => hook.postStart())
    Ok("Your next application is ready.")
  }

  def todo = Action {
    apps.foreach(hook => hook.preStop())
    Ok("Your next application is ready.")
  }

  def redirect = Action {
    Redirect(routes.Application.todo)
  }

  def square(i:Int) =  Action {
    Ok(" Square = " + i*i)
  }

  val personForm: Form[User] = Form {
    mapping(
      "name" -> text,
      "email" -> email,
      "age" -> number,
      "id" -> ignored(1l)
    )(User.apply)(User.unapply)
  }

  val personActionForm: Form[UserAction] = Form {
    mapping(
      "id" -> ignored(1),
      "user" -> longNumber,
      "message" -> text(minLength = 0, maxLength = 25)
    )(UserAction.apply)(UserAction.unapply)
  }

  def showUserManagment() = Action {
    Ok(views.html.user(personForm, personActionForm, UserDAO.getUserAccountMap))
  }

  def addUser() = Action { implicit request =>
    personForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.user(formWithErrors, personActionForm, List[UserAccountMapUser]()))
      },
      userData => {
        /* binding success, you get the actual value. */
        val newUser = User(userData.name, userData.email, userData.age, random.getAndIncrement)
        UserDAO.addUser(newUser)
        Redirect(routes.Application.showUserManagment())
      }
    )
  }

  def addUserAccount() = Action { implicit request =>
    personForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.user(formWithErrors, personActionForm, List[UserAccountMapUser]()))
      },
      userData => {
        /* binding success, you get the actual value. */
        val newUser = User(userData.name, userData.email, userData.age, random.getAndIncrement)
        UserDAO.addUser(newUser)
        Redirect(routes.Application.showUserManagment())
      }
    )
  }

  def addUserMessage() = Action { implicit request =>
    personActionForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.user(personForm, personActionForm, List[UserAccountMapUser]()))
      },
      userData => {
        /* binding success, you get the actual value. */
        val newUserAction = UserAction(userData.id, userData.userId, userData.message)
        UserDAO.addUserAction(newUserAction)
        Redirect(routes.Application.showUserManagment())
      }
    )
  }

  def addUserDetail() = Action { implicit request =>
    personActionForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.user(personForm, personActionForm, List[UserAccountMapUser]()))
      },
      userData => {
        /* binding success, you get the actual value. */
        val newUserAction = UserAction(userData.id, userData.userId, userData.message)
        UserDAO.addUserAction(newUserAction)
        Redirect(routes.Application.showUserManagment())
      }
    )
  }

}
