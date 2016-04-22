package file.searcher

import java.io.File
import scala.annotation.tailrec

class Matcher(filter: String,
              val rootLocation: String = new File(".").getCanonicalPath(), // default argument, also val makes it public. filter isn't public
              checkSubDirectories: Boolean = false,
              contentFilter: Option[String] = None) { // option is just a wrapper about a datatype. Null pointer exceptions is solved this.
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation)) // runs in the constructor.

  def execute() = {
    // This is inside execute(). Helps in scoping.
    @tailrec // Guarantees tailrec.
    def recursiveMatch(files: List[IOObject], currentList: List[IOObject]): List[IOObject] = {
      files match {
        case List() => currentList // empty list case. Returns hte currentList
        case iOObject :: rest => // fetching the head of the list.
          iOObject match { // matches with file/dir.
            case file: FileObject if FilterChecker(filter) matches file.name => { recursiveMatch(rest, file :: currentList) } // :: (cons operator) concatination operator.
            case directory: DirectoryObject =>
              recursiveMatch(rest ::: directory.children(), currentList) // ::: similar to the cons-op - This appends two lists. :O
            case _ => recursiveMatch(rest, currentList)
          }
      }
    }

    val matchedFiles = rootIOObject match {
      case file: FileObject if FilterChecker(filter) matches file.name => List(file)
      case directory: DirectoryObject =>
        if (checkSubDirectories) recursiveMatch(directory.children(), List())
        else FilterChecker(filter) findMatchedFiles (directory.children()) // ??? means NotImplementedException is thrown.
      case _ => List() // Default case.
    }

    val contentFilteredFiles = contentFilter match {
      // The dataFilter is captured directly.
      case Some(dataFilter) =>
        matchedFiles.map(iOObject => (
          iOObject, Some(FilterChecker(dataFilter).findMatchedContentCount(iOObject.file)))).
          filter(matchedTuple => matchedTuple._2.get > 0)

      // OLD Code.
      // matchedFiles filter (iOObject => // filter returns a list instead of a boolean
      // FilterChecker(dataFilter).findMatchedContentCount(iOObject.file) > 0) // Filter checks if the counts is greater than zero.
      case None => matchedFiles map (iOObject => (iOObject, None))
    }

    // From the tuples, fetching the Name and the count.
    contentFilteredFiles map { case (iOObject, count) => (iOObject.name, count) }
    // (iOObject => iOObject.name) // fetched 
  }
}