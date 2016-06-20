package com.mattjtodd.hashsalt

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import scala.concurrent.duration._

class HashSimulation extends Simulation {


  val httpConf = http
    .baseURL("http://localhost:8080")
    .header("Content-Type", "application/json")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")


  val scn =
    scenario("Scenario Name")
      .exec(http("Encode")
        .post("/hash")
        .body(StringBody("""{"characters":"madvillan", "iterations":"1", "saltBytes":"32", "algorithm":"PBKDF2WithHmacSHA512", "keyLength":"256"}""")))


  setUp(scn
    .inject(
      constantUsersPerSec(400) during(40 seconds) randomized,
      nothingFor(30 seconds),
      constantUsersPerSec(1500) during(120 seconds) randomized
    )
    .protocols(httpConf))
}
