package com.knoldus.neo4jServices.factories

import org.neo4j.driver.v1._
import com.typesafe.config.ConfigFactory

trait DatabaseAccess {

  val config = ConfigFactory.load("application.conf")
  val neo4jUrl = config.getString("neo4j.url")
  val userName = config.getString("neo4j.userName")

  val userPassword = config.getString("neo4j.userPassword")

  case class User(name: String, email: String, age: Int, city: String)

  def insertRecord(user: User): Int = {
    val driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(userName, userPassword))
    val session = driver.session
    val script = s"CREATE (user:Users {name:'${ user.name }',email:'${ user.email }',age:${
      user
        .age
    },city:'${ user.city }'})"
    val result: StatementResult = session.run(script)
    session.close()
    driver.close()
    result.consume().counters().nodesCreated()
  }

  def retrieveRecord(email: String): Option[User] = {
    val driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(userName, userPassword))
    val session = driver.session
    val script = s"MATCH (a:Users) WHERE a.email = '$email' RETURN a.name AS name, a.email AS " +
                 s"email, a.age AS age, a.city AS city"
    val result = session.run(script)
    val record_data: Option[User] = if (result.hasNext()) {
      val record = result.next()
      val results: User = new User(record.get("name").asString(),
        record.get("email").asString(),
        record.get("age").asInt(),
        record.get("city").asString())
      Some(results)
    } else {
      None
    }
    session.close()
    driver.close()
    record_data
  }

  def updateRecord(email: String, newName: String): Boolean = {
    val driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(userName, userPassword))
    val session = driver.session
    val script =
      s"MATCH (user:Users) where user.email ='$email' SET user.name = '$newName' RETURN user.name" +
      s" AS name, user.email AS email," +
      s" user.age AS age, user.city AS city"
    val result = session.run(script)
    session.close()
    driver.close()
    result.consume().counters().containsUpdates()
  }

  def deleteRecord(email: String): Int = {
    val driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(userName, userPassword))
    val session = driver.session
    val script = s"MATCH (user:Users) where user.email ='$email' Delete user"
    val result = session.run(script)
    session.close()
    driver.close()
    result.consume().counters().nodesDeleted()
  }

  def createNodesWithRelation(user_name: String, userList: List[String], relation: String) = {
    val driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(userName, userPassword))
    val session = driver.session
    val nameOfFriends = "\"" + userList.mkString("\", \"") + "\""
    val script = s"MATCH (user:Users {name: '$user_name'}) FOREACH (name in [$nameOfFriends] | " +
                 s"CREATE (user)-[:$relation]->(:Users {name:name}))"
    val result = session.run(script)
    session.close()
    driver.close()
    result.consume().counters().relationshipsCreated()
  }
}

object DatabaseAccess extends DatabaseAccess

