package models

final case class UserInfo(username: String, gender: Option[String], age: Option[Int])
final case class UserInfoWithCredentials(username: String, password: String, gender: Option[String], age: Option[Int])
