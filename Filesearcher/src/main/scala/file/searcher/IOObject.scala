package file.searcher

import java.io.File

trait IOObject {
  val file: File
  val name = file.getName()
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