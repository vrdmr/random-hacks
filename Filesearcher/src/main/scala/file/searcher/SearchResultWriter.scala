package file.searcher

import java.io.FileWriter
import java.io.PrintWriter

object SearchResultWriter {
  def writeToFile(filePath: String, searchResults: List[(String, Option[Int])]) = {
    val fileWriter = new FileWriter(filePath)
    val printWriter = new PrintWriter(fileWriter)

    try
      for ((fileName, countOption) <- searchResults) // From searchResutls, fetch tuples.
        printWriter.println(countOption match {
          case Some(count) => s"$fileName -> $count" // If a contentFilter was specified.
          case None        => s"$fileName" // prefixing variables with $, with s before string.
        })
    finally {
      printWriter.close()
      fileWriter.close()
    }
  }
}