lazy val scala212 = "2.12.16"
lazy val scala213 = "2.13.8"
lazy val scala3 = "3.1.3"
lazy val supportedScalaVersions = List(
  scala3,
  scala213,
  scala212
)

// Dependency versions
lazy val catsVersion = "2.8.0"
lazy val catsEffectVersion = "3.3.13"
lazy val munitVersion = "0.7.29"
lazy val munitEffectVersion = "1.0.7"
lazy val collectioncompatVersion = "0.2.27"

lazy val catsCore = "org.typelevel" %% "cats-core" % catsVersion
lazy val catsKernel = "org.typelevel" %% "cats-kernel" % catsVersion
lazy val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectVersion
lazy val munit = "org.scalameta" %% "munit" % munitVersion
lazy val munitEffects =
  "org.typelevel" %% "munit-cats-effect-3" % munitEffectVersion
lazy val collectioncompat =
  "es.weso" %% "collectioncompat" % collectioncompatVersion

def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }

val Java11 = JavaSpec.temurin("11")

ThisBuild / githubWorkflowJavaVersions := Seq(Java11)

lazy val checkerRoot =
  project
    .in(file("."))
    .settings(commonSettings)
    .aggregate(checker)
    .settings(
      ThisBuild / turbo := true,
      crossScalaVersions := Nil,
      publish / skip := true,
      ThisBuild / githubWorkflowBuild := Seq(
        WorkflowStep.Sbt(
          List(
            "clean",
            "test"
          ),
          id = None,
          name = Some("Test")
        )
      )
    )

lazy val checker =
  project
    .in(file("modules/checker"))
    .settings(commonSettings)
    .settings(
      crossScalaVersions := supportedScalaVersions,
      libraryDependencies ++= Seq(
        catsCore,
        catsKernel,
        catsEffect,
        collectioncompat
      )
    )

lazy val docs =
  project
    .in(file("checker-docs"))
    .settings(
      noPublishSettings,
      mdocSettings,
      ScalaUnidoc / unidoc / unidocProjectFilter := inAnyProject -- inProjects(
        noDocProjects: _*
      )
    )
    .dependsOn(checker)
    .enablePlugins(MdocPlugin, DocusaurusPlugin, ScalaUnidocPlugin)

lazy val mdocSettings = Seq(
  mdocVariables := Map(
    "VERSION" -> version.value
  ),
  ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(),
  ScalaUnidoc / unidoc / target := (LocalRootProject / baseDirectory).value / "website" / "static" / "api",
  cleanFiles += (ScalaUnidoc / unidoc / target).value,
  docusaurusCreateSite := docusaurusCreateSite
    .dependsOn(Compile / unidoc)
    .value,
  docusaurusPublishGhpages :=
    docusaurusPublishGhpages.dependsOn(Compile / unidoc).value,
  ScalaUnidoc / unidoc / scalacOptions ++= Seq(
    "-doc-source-url",
    s"https://github.com/weso/checker/tree/v${(ThisBuild / version).value}€{FILE_PATH}.scala",
    "-sourcepath",
    (LocalRootProject / baseDirectory).value.getAbsolutePath,
    "-doc-title",
    "checker",
    "-doc-version",
    s"v${(ThisBuild / version).value}"
  )
)

lazy val ghPagesSettings = Seq(
  git.remoteRepo := "git@github.com:weso/checker.git"
)

/* ********************************************************
 ******************** Grouped Settings ********************
 **********************************************************/

lazy val noDocProjects = Seq[ProjectReference](
  // validating
)

lazy val noPublishSettings = publish / skip := true

lazy val sharedDependencies = Seq(
  libraryDependencies ++= Seq(
    munit % Test,
    munitEffects % Test
  ),
  testFrameworks += new TestFramework("munit.Framework")
)

val compilerOptions = Seq()

lazy val compilationSettings = Seq(
  scalacOptions ++= Seq(
  )
  // format: on
)

lazy val commonSettings = compilationSettings ++ sharedDependencies ++ Seq(
  organization := "es.weso",
  sonatypeProfileName := "es.weso",
  homepage := Some(url("https://github.com/weso/checker")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/weso/checker"),
      "scm:git:git@github.com:weso/checker.git"
    )
  ),
  autoAPIMappings := true,
  apiURL := Some(url("http://weso.github.io/utils/latest/api/")),
  autoAPIMappings := true,
  developers := List(
    Developer(
      id = "labra",
      name = "Jose Emilio Labra Gayo",
      email = "jelabra@gmail.com",
      url = url("https://labra.weso.es")
    )
  )
)
