import sbt._
import sbt.Keys._
import play._


object MyBuild extends Build {

  lazy val root = project.in(file(".")).
    settings(
      name := "sbtPractice"
      ,
      libraryDependencies ++= List(
        "com.typesafe.play" %% "play" % "2.3.8" withSources,
        "com.typesafe.play" %% "play-json" % "2.3.8" withSources,
        "com.typesafe.play" %% "play-slick" % "0.8.1" withSources
        //"org.mongodb" % "casbah-core_2.10" % "2.8.1"
         )
    )
    .dependsOn(prj1,prj2)
    .enablePlugins(PlayScala)

  lazy val prj1 = Project(id="prj1", base=file("prj1"),
    settings = Defaults.coreDefaultSettings ++ Seq(
    libraryDependencies ++= List(
      "com.typesafe.slick" %% "slick" % "2.1.0",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.h2database" % "h2" % "1.3.170",
      "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0" withSources)
  ))
  lazy val prj2 = Project(id="prj2", base=file("prj2"),
    settings = Defaults.coreDefaultSettings ++ Seq(
      libraryDependencies ++= List(
        "org.mongodb" % "mongodb-driver" % "3.0.1" withSources,
        "com.rabbitmq" % "amqp-client" % "3.5.3" withSources,
        "com.typesafe.akka" %% "akka-actor" % "2.3.4" withSources,
        "com.typesafe.akka" %% "akka-slf4j" % "2.3.4" withSources)
    ))

}