package controllers

import com.sample.BootStrapHook
import com.sample.akka.RabbitMqListener
import play.api.{Application, GlobalSettings}

/**
 * Created by pragati on 13.06.15.
 */
object Bootstrapper extends GlobalSettings{

    val apps = List[BootStrapHook](new RabbitMqListener)

    override def onStart(app:Application) = {
        apps.foreach(hook => hook.postStart())
    }

    override def onStop(app:Application):Unit = {
      apps.foreach(hook => hook.preStop())
    }

}
