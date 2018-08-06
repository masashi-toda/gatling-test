package learning.orientdb.gatling

import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

import scala.language.postfixOps

class BasicSimulation extends Simulation {

  val maxUsers       = 2000
  val warmUpDuration = 50 seconds
  val keepDuration   = 300 seconds

  val httpProtocol = http
    .baseURL(s"http://localhost:8080")
    .header("Keep-Alive", "150")
    .disableAutoReferer
    .disableCaching
    .maxConnectionsPerHost(2000)
    .shareConnections

  val counter = new AtomicInteger()
  val clientIdRouter = Iterator.continually(1 to 100).flatten

  val feeder = Iterator.continually(
    (clientIdRouter.next(), counter.incrementAndGet()) match {
      case (i, cnt) =>
        val cid = s"tgr-$i"
        val iUid = s"idfa-uid-$cnt"
        val cUid = s"cookie-uid-$cnt"
        Map(
          "url" -> s"/function/local-idmapping/tigerselect/$cUid/$cid"
          //"url" -> s"/function/local-idmapping/tigerupsert/$iUid/$cUid/$cid"
        )
    }
  )

  val scn = scenario("simulation")
    .feed(feeder)
    .exec(
      http("search api")
        .post("${url}")
        .check(status.is(200))
    )

  setUp(
    scn.inject(
      rampUsersPerSec(1) to (maxUsers) during (warmUpDuration)
      //constantUsersPerSec(maxUsers) during (keepDuration)
    )
  ).protocols(httpProtocol)
}
