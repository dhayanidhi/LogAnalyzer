package com.sample

import com.mongodb.client.model.IndexOptions
import com.mongodb.{Mongo, DBCollection, MongoClient}
import com.mongodb.client.MongoCollection
import org.bson.Document

/**
 * Created by pragati on 16.05.15.
 */
object MongoDBConn {

  val NAME_UNIQUE = "{ \"name\": 1 }, { unique: true }";

  val collection:MongoCollection[Document] = presetIndex(collectionObj)

  private def presetIndex(f:()=>MongoCollection[Document]):MongoCollection[Document] = {
    val coll = f()
    val indexOptions = new IndexOptions().unique(true)
    coll.createIndex(Document.parse(NAME_UNIQUE), indexOptions)
    return coll
  }

  private def collectionObj() = new MongoClient("localhost", 32770).getDatabase("test").getCollection("user");
}
