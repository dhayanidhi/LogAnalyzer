package com.sample

import com.mongodb.{BasicDBList, BasicDBObject}
import com.mongodb.client.{FindIterable, MongoCollection}
import org.bson.types.BasicBSONList
import org.bson.{Document, BSON}

/**
 * Created by pragati on 16.05.15.
 */
object UserNoSqlDAO {

  val FIND_BY_NAME = "{ \"name\": %s }";

  val collection = MongoDBConn.collection

  def insertUser(user:String):Unit =
  collection.insertOne(Document.parse(user).append("msgs",new BasicDBList()))

  def getAllUser:FindIterable[Document] = collection.find()

  def updateUserByMsg(user:String, msg:String):Unit = {
    collection.findOneAndUpdate(new BasicDBObject("name",user),
      new BasicDBObject("$push", new BasicDBObject("msgs", Document.parse(msg))))
  }

  def deleteUser(user:String):Unit = collection.deleteOne(new BasicDBObject("name", user))


}

