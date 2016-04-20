package file.searcher

import java.io.File
import scala.util.control.NonFatal

class FilterChecker(filter: String) {

  // infix Notation. Only for one parameter
  def matches(content: String) = content contains filter // infix notation applied.

  def findMatchedFiles(iOObjects: List[IOObject]): List[IOObject] =
    for (
      iOObject <- iOObjects if (iOObject.isInstanceOf[FileObject]) if (matches(iOObject.name))
    ) // for ends here.
    yield iOObject

  def matchesFileContent(file: File) = {
    import scala.io.Source
    try {
      val fileSource = Source.fromFile(file)
      try
        fileSource.getLines() exists { line => matches(line) } // fileSource.getLines returns 
      // a List of Strings. On which exists is applied with 
      // line => matches(line) applying the filter 
      catch {
        case NonFatal(_) => false
      } finally { fileSource.close() } // Finally for closing the filesource.
    } catch {
      case NonFatal(_) => false // TODO: handle error. NonFatal will catch all exceptions except for generally catastrophic 
    }

  }
}

// Companion Object to the FilterChecker class, coz it created in the same file.
// Object is a way to create a singleton or static classes. 
object FilterChecker {
  def apply(filter: String) = new FilterChecker(filter)
}