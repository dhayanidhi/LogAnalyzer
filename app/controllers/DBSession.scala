package controllers

import play.api.db.slick.DB
import play.api.Play._


/**
 * Created by pragati on 29.04.15.
 */

trait DBSession {

  lazy implicit val session = DB.createSession()
}
