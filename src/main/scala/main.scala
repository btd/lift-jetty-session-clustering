package ingo

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.WebAppContext

import org.eclipse.jetty.nosql._
import org.eclipse.jetty.nosql.mongodb._
import org.eclipse.jetty.server.session._

trait Name {
  def name: String
}

object Main extends App with Name{
  override val name = "Instance 1"

  code.snippet.Account.name = name

  val tempDir = System.getProperty("jetty.tmpdir", System.getProperty("java.io.tmpdir"))
  val port = System.getProperty("jetty.port", "8090").toInt
  val runMode = System.getProperty("run.mode", "development")

  val webctx = new WebAppContext()

  webctx.setContextPath("/")
  if (runMode == "development") {
    webctx.setResourceBase("./target/scala-2.10/resource_managed/main/webapp")
    webctx.setExtractWAR(false)
  } else {
    val webappDirFromJar = webctx.getClass.getClassLoader.getResource("webapp").toExternalForm
    webctx.setWar(webappDirFromJar)
    webctx.setExtractWAR(true)
  }

  webctx.setCopyWebInf(false)
  webctx.setTempDirectory(new java.io.File(tempDir))

  val server = new Server(port)

  val idMgr = new MongoSessionIdManager(server)
  //idMgr.setWorkerName("fred")
  idMgr.setScavengePeriod(60)
  server.setSessionIdManager(idMgr)

  val mongoMgr = new MongoSessionManager()
  mongoMgr.setSessionIdManager(server.getSessionIdManager)

  webctx.setSessionHandler(new SessionHandler(mongoMgr))
  
  server.setHandler(webctx)
  server.start()
  server.join()
}

object Main1 extends App with Name{
  override val name = "Instance 2"

  code.snippet.Account.name = name

  val tempDir = System.getProperty("jetty.tmpdir", System.getProperty("java.io.tmpdir"))
  val port = System.getProperty("jetty.port", "8091").toInt
  val runMode = System.getProperty("run.mode", "development")

  val webctx = new WebAppContext()

  webctx.setContextPath("/")
  if (runMode == "development") {
    webctx.setResourceBase("./target/scala-2.10/resource_managed/main/webapp")
    webctx.setExtractWAR(false)
  } else {
    val webappDirFromJar = webctx.getClass.getClassLoader.getResource("webapp").toExternalForm
    webctx.setWar(webappDirFromJar)
    webctx.setExtractWAR(true)
  }

  webctx.setCopyWebInf(false)
  webctx.setTempDirectory(new java.io.File(tempDir))

  val server = new Server(port)

  val idMgr = new MongoSessionIdManager(server)
  //idMgr.setWorkerName("fred")
  idMgr.setScavengePeriod(60)
  server.setSessionIdManager(idMgr)

  val mongoMgr = new MongoSessionManager()
  mongoMgr.setSessionIdManager(server.getSessionIdManager)

  webctx.setSessionHandler(new SessionHandler(mongoMgr))
  
  server.setHandler(webctx)
  server.start()
  server.join()
}