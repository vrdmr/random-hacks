package file.searcher

import java.io.File
import scala.util.control.NonFatal

trait IOObject {
  val file: File
  val name = file.getName()
  val fullName = try file.getAbsolutePath() catch {case NonFatal(_) => name} //.getName()
}

// removed the empty '{', '}'.
// Case comes with its own companions.
case class FileObject(file: File) extends IOObject
case class DirectoryObject(file: File) extends IOObject {
  def children() =
    try
      file.listFiles().toList map (file => FileConverter convertToIOObject file)
    catch {
      // Catch all with type - catch the specific exceptions 
      case _: NullPointerException => List()
    }
}