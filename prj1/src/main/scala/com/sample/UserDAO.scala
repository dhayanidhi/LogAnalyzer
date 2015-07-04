package com.sample

import java.sql.Timestamp
import java.time.Instant

import scala.slick.jdbc.{StaticQuery => Q}
import scala.slick.driver.H2Driver.simple._

/**
 * Created by pragati on 27.03.15.
 */
object UserDAO{
  //val url = "jdbc:h2:mem:hello;INIT=runscript from '/home/pragati/Work/SbtPractice/prj1/src/main/resources/sql/Sample1.sql';DB_CLOSE_DELAY=-1"
  //val url = "jdbc:h2:mem:hello;DB_CLOSE_DELAY=-1"
  //val db = Database.forURL(url, driver = "org.h2.Driver")

  trait accessor[R, T <: Table[R]] {
    var tableQ:TableQuery[T]
  }

  implicit object UserAccessor extends accessor[User, Users] {
    override var tableQ = TableQuery[Users]
  }

  implicit object UserActionAccessor extends accessor[UserAction, UserActions] {
    override var tableQ = TableQuery[UserActions]
  }

  implicit object UserAccountAccessor extends accessor[UserAccount, UserAccounts] {
    override var tableQ = TableQuery[UserAccounts]
  }

  def initiateDDL(implicit session:Session):Unit={
      Q.updateNA("CREATE TABLE USERS " +
        "(USER_NAME VARCHAR(255)," +
        "USER_EMAIL VARCHAR(50)," +
        "USER_AGE INT," +
        "USER_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY);").execute
  }

  def dropDDL()(implicit session:Session):Unit={
      Q.updateNA("DROP TABLE IF EXISTS USERS").execute
  }

  def addUser(user: User)(implicit accessor: accessor[User, Users], session: Session): Unit ={
    session.withTransaction{
      accessor.tableQ += user
      addUserAccount(UserAccount(None,Timestamp.from(Instant.now()), UserStatus.ACCEPTED, user.id))
    }
}

  def getUserByName(name:String)(implicit accessor: accessor[User, Users], session: Session):List[User]={
      accessor.tableQ.filter(_.name===name).list
  }

  def getAllUser(implicit accessor: accessor[User, Users], session: Session):List[User]={
    accessor.tableQ.list
  }

  def updateUser(user: User)(implicit accessor: accessor[User, Users], session: Session):Unit={
      val res = accessor.tableQ.filter(_.name===user.name).map(_.age)
      res.update(user.age)
  }

  def removeUser(user: User)(implicit accessor: accessor[User, Users], session: Session):Unit={
    //db.withSession(implicit session => accessor.query.filter(_.name===user.name).delete)
    accessor.tableQ.filter(_.name===user.name).delete
  }

  def addUserAccount(userAccount: UserAccount)(implicit accessor: accessor[UserAccount, UserAccounts], session: Session): Unit ={
    //accessor.tableQ += userAccount //TO-DO DEBUG TO SEE HOW IT WORKS
    accessor.tableQ.insert(userAccount) //this insert ignores all autincrement column
/*    println(userAccount.id match {
        case None => "nothing"
        case _ => userAccount.id.get
      }
    )*/
  }

  def getUserAccountMap(implicit accessor1: accessor[User, Users],
                        accessor2: accessor[UserAccount, UserAccounts], session: Session):List[UserAccountMapUser]={
    accessor1.tableQ.join(accessor2.tableQ).on(_.id === _.userId).
      list.map{case(a,b) => UserAccountMapUser(a,b,getUserAction(a))}
  }

  def getUserAction(user:User)(implicit accessor: accessor[UserAction, UserActions], session: Session):List[UserAction]={
    accessor.tableQ.filter(_.userId === user.id).list
  }

  def addUserAction(userAction: UserAction)(implicit accessor: accessor[UserAction, UserActions], session: Session):Unit={
    accessor.tableQ.insert(userAction)
  }
}
