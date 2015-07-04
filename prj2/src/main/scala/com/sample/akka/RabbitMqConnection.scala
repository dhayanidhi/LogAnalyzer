package com.sample.akka

import com.rabbitmq.client.{Channel, ConnectionFactory, Connection}


/**
 * Created by pragati on 13.06.15.
 */
object RabbitMqConnection {

  val RABBIT_HOST = "127.0.0.1" //ConfigFactory.load().getString("rabbitmq.host");
  val RABBIT_EXCHANGE = "serverlog" //ConfigFactory.load().getString("rabbitmq.exchange");
  val RABBIT_QUEUE = "queue"

  private implicit val connection:Connection = createConnection;

  def getChannel:Channel = connection.createChannel()

  def createConnection : Connection = {
    val factory = new ConnectionFactory();
    factory.setHost(RABBIT_HOST);
    return factory.newConnection();
  }
}
