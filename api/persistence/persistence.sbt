lazy val persistence = ZetaBuild.defaultProject(project).settings(
  name := "persistence",
  version := "0.1",

  libraryDependencies ++= Seq(
    // json parser
    "com.typesafe.play" %% "play-json" % "2.5.7",
    // sillhouette
    "com.mohiva" %% "play-silhouette" % ZetaBuild.silhouetteVersion,
    "com.mohiva" %% "play-silhouette-persistence" % ZetaBuild.silhouetteVersion,
    // mongoDB accessor
    "org.reactivemongo" %% "reactivemongo" % "0.12.3",
    "org.reactivemongo" %% "reactivemongo-play-json" % "0.12.3",
    // akka actor system
    "com.typesafe.akka" %% "akka-actor" %  "2.4.18"
  )
).dependsOn(
  ZetaBuild.common
).enablePlugins(
  JavaAppPackaging,
  DockerSpotifyClientPlugin
)
