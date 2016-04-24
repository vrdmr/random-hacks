package file.searcher

import org.scalatest.FlatSpec
import java.io.File

class MatcherTest extends FlatSpec {
  "Matcher that is passed a file matching the filter" should
    "return a list with that file name" in {
      val matcher = new Matcher("fake", "fakePath")
      val results = matcher.execute()
      assert(results == List(("/Users/varadmeru/hack/Filesearcher/fakePath", None)))
    }

  "Matcher using a directory containing  one file matching the filter" should
    "return a list with that file name" in {
      val matcher = new Matcher("txt", new File("./testfiles/").getCanonicalPath())
      val results = matcher.execute()
      assert(results == List(("/Users/varadmeru/hack/Filesearcher/testfiles/readme.txt", None)))
    }

  "Matcher that is not using the root file location" should
    "use the current location" in {
      val matcher = new Matcher("filter")
      assert(matcher.rootLocation == new File(".").getCanonicalPath())
    }

  "Matcher with sub folder checking matching a root location with two subtree files matching" should
    "return a list with those file names" in {
      val searchSubDirectories = true
      val matcher = new Matcher("txt", new File("./testfiles/").getCanonicalPath(), searchSubDirectories)

      val results = matcher.execute()
      assert(results == List(("/Users/varadmeru/hack/Filesearcher/testfiles/SomeRandomSubFolder/notes.txt", None), // A list with 2 Tuples
        ("/Users/varadmeru/hack/Filesearcher/testfiles/readme.txt", None)))
    }

  "Matcher given path that has one file that matches file filter and content filter" should
    "return a list with that file name" in {
      // val searchSubDirectories = true
      val matcher = new Matcher("data", new File(".").getCanonicalPath(), true, Some("pluralsight"))

      val results = matcher.execute()
      assert(results == List(("/Users/varadmeru/hack/Filesearcher/testfiles/pluralsight.data", Some(3))))
    }

  "Matcher given path that has no file that matches file filter and content filter" should
    "return an empty list" in {
      // val searchSubDirectories = true
      val matcher = new Matcher("txt", new File(".").getCanonicalPath(), true, Some("pluralsight"))

      val results = matcher.execute()
      assert(results == List())
    }
}