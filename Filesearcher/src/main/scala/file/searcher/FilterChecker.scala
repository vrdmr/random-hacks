package file.searcher

import java.io.File
import scala.util.control.NonFatal

class FilterChecker(filter: String) {

  val filterAsRegex = filter.r // Attempting a string to convert into a REGEX

  // infix Notation. Only for one parameter
  // OLD: def matches(content: String) = content contains filter // infix notation applied.
  def matches(content: String) = filterAsRegex findFirstMatchIn content match {
    case Some(_) => true // matches anything.
    case None    => false // Nothing matched.
  }

  def findMatchedFiles(iOObjects: List[IOObject]): List[IOObject] =
    for (
      iOObject <- iOObjects if (iOObject.isInstanceOf[FileObject]) if (matches(iOObject.name))
    ) // for ends here.
    yield iOObject

  def findMatchedContentCount(file: File) = {
    // Creating a new method which will take the content and run the RegEx and the get the count back from the iterator of matches.
    def getFilterMatchCount(content: String) = (filterAsRegex findAllIn content).length

    import scala.io.Source // importing within context. This will work only within the method.
    try {
      val fileSource = Source.fromFile(file)
      try
        fileSource.getLines().foldLeft(0)( // Fold Left - the 0 is the see value for the accumulator
          (accumulator, line) => accumulator + getFilterMatchCount(line))
      // OLD Code; exists (line => matches(line)) // fileSource.getLines returns 
      // a List of Strings. On which exists is applied with 
      // line => matches(line) applying the filter 
      catch {
        case NonFatal(_) => 0 // 
      } finally { fileSource.close() } // Finally for closing the file Source.
    } catch {
      case NonFatal(_) => 0 // TODO: handle error. NonFatal will catch all exceptions except for 
      // generally catastrophic - mainly Normal exceptions 
    }

  }
}

// Companion Object to the FilterChecker class, coz it created in the same file.
// Object is a way to create a singleton or static classes. 
object FilterChecker {
  def apply(filter: String) = new FilterChecker(filter)
}