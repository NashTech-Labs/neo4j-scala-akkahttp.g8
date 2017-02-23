

package com.knoldus.neo4jServices

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.knoldus.neo4jServices.routes.Neo4jService
import org.neo4j.driver.v1.{AuthTokens, GraphDatabase}
import org.scalatest.{BeforeAndAfterAll, Matchers, Sequential, WordSpec}


class Neo4jServicesSpec
  extends WordSpec with Matchers with ScalatestRouteTest with Neo4jService with BeforeAndAfterAll {
  Sequential

  override def beforeAll {
    println("Neo4j Akka http test cases ready for executing")
  }

  override def afterAll() {
    val driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(userName, userPassword))
    val session = driver.session
    val script = "MATCH (n:Users)-[r:friend]->() DELETE r"
    val scriptUser = "MATCH (n:Users) DELETE n"
    session.run(script)
    session.run(scriptUser)
    session.close()
    driver.close()
    println("###### delete nodes")
  }

  "The service" should {

    "be able to insert data in the neo4j" in {

      val json_data =
        """{
          |"name":"Anurag",
          |"email":"anurag.rbl.06@gmail.com",
          |"age":25,
          |"city":"Jaipur"
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      neo4jRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }

    "be able to insert data_0 in the neo4j" in {

      val json_data =
        """{
          |"name":"Manish",
          |"email":"manish@gmail.com",
          |"age":26,
          |"city":"Jaipur"
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      neo4jRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }

    }

    "be able to insert data_1 in the neo4j" in {
      val json_data =
        """{
          |"name":"Shivansh",
          |"email":"shivansh@gmail.com",
          |"age":22,
          |"city":"Lucknow"
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      neo4jRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }

    "be able to insert data_2 in the neo4j" in {
      val json_data =
        """{
          |"name":"Gaurav",
          |"email":"gaurav@gmail.com",
          |"age":23,
          |"city":"Kanpur"
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      neo4jRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }

    }

    "be able to insert data_3 in the neo4j" in {
      val json_data =
        """{
          |"name":"Sandy",
          |"email":"phalodi@gmail.com",
          |"age":25,
          |"city":"Jaipur"
          |}""".stripMargin
      Post("/insert", HttpEntity(`application/json`, json_data)) ~>
      neo4jRoutes ~>
      check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }

    "to be able to retrieve data via cypher" in {
      Get("/get/email/anurag.rbl.06@gmail.com") ~> neo4jRoutes ~> check {
        responseAs[String].contains("Anurag") shouldEqual true
      }
    }

    "be able to update data via KV operation" in {
      Get(s"/update/name/Akash/email/manish@gmail.com") ~> neo4jRoutes ~> check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }

    "be able to create relation with data" in {
      Get(s"/createrelation/name/Anurag/relation/friend/user_list/Shivansh:Gaurav:Sandy") ~>
      neo4jRoutes ~> check {
        responseAs[String].contains("Data is successfully persisted") shouldEqual true
      }
    }

    "be able to delete data via email" in {
      Get(s"/delete/email/manish@gmail.com") ~> neo4jRoutes ~> check {
        responseAs[String].contains("Data is successfully deleted") shouldEqual true
      }
    }
  }
}
