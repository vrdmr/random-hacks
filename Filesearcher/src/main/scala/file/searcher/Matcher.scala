package file.searcher

import java.io.File
import scala.annotation.tailrec

class Matcher(filter: String,
              val rootLocation: String = new File(".").getCanonicalPath(), // default argument, also val makes it public. filter isn't public
              checkSubDirectories: Boolean = false) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation)) // runs in the constructor.

  def execute() = {
    // This is inside execute(). Helps in scoping.
    @tailrec // Guarantees tailrec.
    def recursiveMatch(files: List[IOObject], currentList: List[IOObject]) : List[IOObject] = {
      files match {
        case List() => currentList // empty list case. Returns hte currentList
        case iOObject :: rest => // fetching the head of the list.
          iOObject match { // matches with file/dir.
            case file: FileObject if FilterChecker(filter) matches file.name => {recursiveMatch(rest, file :: currentList)} // :: (cons operator) concatination operator.
            case directory: DirectoryObject =>              
              recursiveMatch(rest ::: directory.children(), currentList) // ::: similar to the cons-op - This appends two lists. :O
            case _ => recursiveMatch(rest, currentList)
          }
      }
    }
    
    val matchedFiles = rootIOObject match {
      case file: FileObject if FilterChecker(filter) matches file.name => List(file)
      case directory: DirectoryObject =>
        if (checkSubDirectories) recursiveMatch (directory.children(), List())
        else FilterChecker(filter) findMatchedFiles (directory.children()) // ??? means NotImplementedException is thrown.
      case _ => List() // Default case.
    }

    matchedFiles map (iOObject => iOObject.name) // fetched 
  }
}