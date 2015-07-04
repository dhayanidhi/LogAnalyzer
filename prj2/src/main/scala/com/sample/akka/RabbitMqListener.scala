package com.sample.akka

import akka.actor.Actor
import com.rabbitmq.client.{AMQP, Envelope, DefaultConsumer, Channel}
import com.sample.BootStrapHook

/**
 * Created by pragati on 13.06.15.
 */
class RabbitMqListener extends BootStrapHook{

  private var channel:Channel = null;

  import RabbitMqConnection.{RABBIT_QUEUE => queueName, RABBIT_EXCHANGE => exchange}

  def postStart(): Unit ={
    if(channel == null) {
      channel = RabbitMqConnection.getChannel
      channel.queueDeclare(queueName,true,false, false,null)
      channel.queueBind(queueName,exchange, "")
      new CustomConsumer(channel, new AkkaSender, queueName).config
    }
    print(" post start triggered")
  }

  def preStop(): Unit ={
    channel match {
      case null => {}
      case _ => {channel.abort()}
    }
    print(" pre stop triggered")
  }

  //Receive rabbitmq msg
  class CustomConsumer(channel:Channel, actor : AkkaSender, queueName: String) extends DefaultConsumer(channel) {

    def config = {channel.basicConsume(queueName, this)}

    override def handleDelivery(consumerTag:String, envelope:Envelope, properties:AMQP.BasicProperties, body : Array[Byte]) {
        actor.receiveMsg(body)
    }
  }

  //Send akka msg to akka system
  class AkkaSender /*extends Actor*/ {

    def receiveMsg(msg : Array[Byte]): Unit ={
      print("Got something with size " + new String(msg))
    }

/*    def receive = {
      case _ => print("do nothing")
    }*/

  }

}
