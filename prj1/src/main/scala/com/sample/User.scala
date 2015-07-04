package com.sample

import java.sql.Timestamp
import java.util.Calendar

import scala.slick.lifted.Tag
import scala.slick.driver.H2Driver.simple._

/**
 * Created by pragati on 27.03.15.
 */

  object UserStatus extends Enumeration {
    type UserStatus = Value
    val UNKNOWN, ACCEPTED, ACTIVE, NOT_ACTIVE, DELETED = Value

    implicit val enumMapper = MappedColumnType.base[UserStatus, String](
      { c => c.toString },
      { s => UserStatus.withName(s)}
    )
  }

  import UserStatus._
    case class User(val name: String,val email: String,val age: Int, val id: Long){
    }

    class Users(tag : Tag) extends Table[User](tag,"USERS"){
      def name = column[String]("USER_NAME")
      def email = column[String]("USER_EMAIL")
      def age = column[Int]("USER_AGE")
      def id = column[Long]("USER_ID", O.PrimaryKey)
      def * = (name, email, age, id)<>(User.tupled, User.unapply _)
    }

    case class UserAccount(val id: Option[Int], val last_time: Timestamp, val status: UserStatus, val userId:Long){
    }/**/

    class UserAccounts(tag : Tag) extends Table[UserAccount](tag,"USER_ACCOUNT"){
      def accId = column[Int]("USER_ACCOUNT_ID", O.PrimaryKey, O.AutoInc)
      def time = column[Timestamp]("USER_LAST_ACCESS")
      def status = column[UserStatus]("USER_STATUS")
      def userId = column[Long]("USER_ID")
      def * = (accId.?, time, status, userId)<>(UserAccount.tupled, UserAccount.unapply _)
/*      def * = (accId.?, time, status, userId) <>
        ({ t: Tuple4[Int, Timestamp, UserStatus, Long] => UserAccount(Some(t._1), t._2, t._3, t._4)},
        { (u: UserAccount) => Some((u.last_time, u.status, u.userId))})*/
      def user = foreignKey("USER_FK", column[Long]("USER_ID"), TableQuery[Users])(_.id)
    }

    case class UserAccountMapUser(val user: User, val userAccount: UserAccount, val userActions:List[UserAction]){

    }

    case class UserAction(val id: Int, val userId:Long, val message: String){

    }

    class UserActions(tag : Tag) extends Table[UserAction](tag,"USER_ACTION"){
      def actionId = column[Int]("USER_ACTION_ID", O.PrimaryKey, O.AutoInc)
      def userId = column[Long]("USER_ID")
      def message = column[String]("ACTION_MSG")
      def * = (actionId, userId, message)<>(UserAction.tupled, UserAction.unapply _)
      def User = foreignKey("USER_FK",userId, TableQuery[Users])(_.id)
    }


