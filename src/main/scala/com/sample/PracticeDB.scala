
import java.util.concurrent.atomic.{AtomicLong, AtomicInteger}

import com.sample._

/**
 * Created by pragati on 04.04.15.
 */
object PracticeDB extends App{

  var id:AtomicLong = new AtomicLong()

  def initiate = {UserDAO.initiateDDL()}

  def clear = {UserDAO.dropDDL()}

  def user(name:String,email:String="notavailable", age:Int):Unit={
    UserDAO.addUser(User(name,email,age,id.get()))
  }

  def getEmailByName(name:String) = UserDAO.getUserByName(name).head.email

  def updateAgeByName(name:String, age:Int):Unit = {
    UserDAO.updateUser(User(name,"",age,-1))
  }

  def removeUser(name:String):Unit={
    UserDAO.removeUser(User(name, "", -1, -1))
  }
}
