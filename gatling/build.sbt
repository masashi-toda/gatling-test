enablePlugins(GatlingPlugin)

name := "learning-orientdb-gatling"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.gatling"            % "gatling-core"              % "2.1.7" % "compile",
  "io.gatling"            % "gatling-test-framework"    % "2.1.7" % "test",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7" % "test"
)
