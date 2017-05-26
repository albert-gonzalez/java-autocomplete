name := "java-autocomplete"
organization := "com.agm"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += "org.elasticsearch.client" % "rest" % "5.4.0"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
libraryDependencies ++= Seq(
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.2.5.Final" // replace by your jpa implementation
)
libraryDependencies += evolutions