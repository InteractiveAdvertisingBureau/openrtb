name := "ads-cert-example"

mainClass := Some("Main")

version := "0.1.0"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.59",
  "org.bouncycastle" % "bcpkix-jdk15on" % "1.59"
)

licenses := Seq("Apache License 2.0" → url("https://opensource.org/licenses/Apache-2.0"))