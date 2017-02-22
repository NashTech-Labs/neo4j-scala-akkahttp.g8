package com.knoldus.neo4jServices

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.knoldus.neo4jServices.routes.Neo4jService


class StartNeo4jServer(implicit val system: ActorSystem,
                       implicit val materializer: ActorMaterializer) extends Neo4jService {
  def startServer(address: String, port: Int) = {
    Http().bindAndHandle(neo4jRoutes, address, port)
  }
}

object StartApplication extends App {
  StartApp
}

object StartApp {
  implicit val system: ActorSystem = ActorSystem("Neo4j-Akka-Service")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val server = new StartNeo4jServer()
  val config = server.config
  val serverUrl = config.getString("http.interface")
  val port = config.getInt("http.port")
  server.startServer(serverUrl, port)
}