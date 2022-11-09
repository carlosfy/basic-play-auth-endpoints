package database

import doobie._
import doobie.implicits._
import models.UserInfo

object PostgreSQLQueries {

  def selectWelcomeMessage(): ConnectionIO[Option[String]] =
    sql"SELECT message FROM welcome_message".query[String].to[List].map(_.headOption)

  def findId(username: String): ConnectionIO[Option[Int]] = 
    sql"""
    |SELECT id 
    |FROM users 
    |WHERE username = $username
    """".stripMargin.query[Int].option

  
  def addUserUnsafe(user: String, passwordHash: Int, gender: Option[String], age: Option[Int]): Update0 = 
    sql"""
    |INSERT INTO users
    |VALUES ($user, $passwordHash, $gender, $age)
    """".stripMargin.update

  def getPassWordHash(username: String): ConnectionIO[Option[Int]] = 
    sql"""
    |SELECT password_hash 
    |FROM users 
    |WHERE username = $username
    """".stripMargin.query[Int].option

  def getUser(username: String): ConnectionIO[Option[UserInfo]] = 
    sql"""
    |SELECT username, gender, age
    |FROM users 
    |WHERE username = $username
    """".stripMargin
    .query[(String, Option[String], Option[Int])]
    .option
    .map(_.map(tri => UserInfo(tri._1, tri._2, tri._3)))

  def getAllUsers(): ConnectionIO[List[UserInfo]] = 
    sql"""
    |SELECT username
    |FROM users
    """".stripMargin
    .query[(String, Option[String], Option[Int])]
    .to[List]
    .map(_.map(tri => UserInfo(tri._1, tri._2, tri._3)))

}
