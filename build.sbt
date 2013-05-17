name := "Lift 2.5 starter template"

version := "0.0.1"

organization := "net.liftweb"

scalaVersion := "2.10.0"

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
"staging" at "http://oss.sonatype.org/content/repositories/staging",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases"
                )

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  val liftVersion = "2.5-RC6"
  Seq(
    "net.liftweb"       %%  "lift-webkit"            % liftVersion        % "compile",
    "net.liftweb"       %%  "lift-mapper"            % liftVersion        % "compile",
    "net.liftmodules"   %%  "lift-jquery-module_2.5" % "2.3",
    "ch.qos.logback"    %   "logback-classic"        % "1.0.6",
    "com.h2database"    %   "h2"                     % "1.3.167"
  )
}

libraryDependencies ++= Seq(
  "org.eclipse.jetty" % "jetty-nosql"   % "9.0.2.v20130417" % "compile",
  "org.eclipse.jetty" % "jetty-webapp"  % "9.0.2.v20130417"  % "compile",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "compile" artifacts Artifact("javax.servlet", "jar", "jar")
)

watchSources <++= baseDirectory map { base =>
  val webappBase = base / "src" / "main" / "webapp"
  (webappBase ** "*").get
}

resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map
{ (managedBase, base) =>
  val webappBase = base / "src" / "main" / "webapp"
  for {
    (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase / "main" / "webapp")
  } yield {
    Sync.copy(from, to)
    to
  }
}