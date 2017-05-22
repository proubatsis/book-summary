package ca.panagiotis.booksum.util

/**
  * Created by panagiotis on 17/05/17.
  */
object Endpoint {
  val host = "localhost"

  object Book {
    def summary(id: Int) = s"/books/$id/summary"
    def description(id: Int) = s"/books/$id/description"
    def externalDescription(eid: String) = s"/books/ext/$eid/description"
    def newSummary(id: Int) = s"/books/$id/summary/new"
    def externalNewSummary(eid: String) = s"/books/ext/$eid/summary/new"
    def searchQuery(q: String) = s"/books/search?q=$q"
    def searchToken(t: String) = s"/books/search?t=$t"
    def externalSearchQuery(q: String) = s"/books/ext/search?q=$q"
    def externalSearchToken(t: String) = s"/books/ext/search?t=$t"
  }

  def makeUrl(endpoint: String) = s"$host$endpoint"
}
