package sbtprotoc

import java.io.File

import sbt.librarymanagement.{CrossVersion, ModuleID}
import sbt.util.CacheImplicits
import sjsonnew.LList.:*:
import sjsonnew.{IsoLList, JsonFormat, LList, LNil}

private[sbtprotoc] trait Compat extends CacheImplicits { self: ProtocPlugin.type =>
  private val CrossDisabled = sbt.librarymanagement.Disabled()
  protected def makeArtifact(f: protocbridge.Artifact): ModuleID = {
    ModuleID(f.groupId, f.artifactId, f.version)
      .cross(if (f.crossVersion) CrossVersion.binary else CrossDisabled)
  }

  protected object CacheArguments {
    implicit val instance: IsoLList[Arguments] = LList.iso(
      (a: Arguments) => ("includePaths", a.includePaths) :*: ("protocOptions", a.protocOptions) :*: ("pythonExe", a.pythonExe) :*: ("deleteTargetDirectory", a.deleteTargetDirectory) :*: ("targets", a.targets) :*: LNil,
      (in: Seq[File] :*: Seq[String] :*: String :*: Boolean :*: Seq[(File, Seq[String])] :*: LNil) => Arguments(in.head, in.tail.head, in.tail.tail.head, in.tail.tail.tail.head, in.tail.tail.tail.tail.head)
    )
  }
}
