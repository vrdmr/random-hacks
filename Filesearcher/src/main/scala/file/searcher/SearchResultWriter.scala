package file.searcher

import java.io.FileWriter
import java.io.PrintWriter

object SearchResultWriter {
  def writeToFile(filePath: String, searchResults: List[(String, Option[Int])]) = {
    val fileWriter = new FileWriter(filePath)
    val printWriter = new PrintWriter(fileWriter)

    try
      for ((fileName, countOption) <- searchResults) // From searchResutls, fetch tuples.
    	  printWriter.println(getString(fileName, countOption)) // Writing to file.
    finally {
      printWriter.close()
      fileWriter.close()
    }
  }

  def writeToConsole(searchResults: List[(String, Option[Int])]) =
    for ((fileName, countOption) <- searchResults)
      println(getString(fileName, countOption))

  private def getString(fileName: String, countOption: Option[Int]) = { // Private method.
    countOption match {
      case Some(count) => s"\t$fileName -> $count" // If a contentFilter was specified.
      case None        => s"\t$fileName" // prefixing variables with $, with s before string.
    }
  }
}