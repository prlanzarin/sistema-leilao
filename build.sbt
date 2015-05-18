import com.github.retronym.SbtOneJar._

lazy val root = (project in file(".")).
  settings(
    name := "leilao",
    version := "1.0",
    scalaVersion := "2.9.2"
  )

oneJarSettings

// or if using sbt version < 0.13
// seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)
