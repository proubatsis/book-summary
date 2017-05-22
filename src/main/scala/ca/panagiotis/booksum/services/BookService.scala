package ca.panagiotis.booksum.services

import ca.panagiotis.booksum.models.{Account, Book, BookSummary}
import com.twitter.util.Future

/**
  * Created by panagiotis on 22/04/17.
  */
trait BookService {
  def findBooks(): Future[List[Book]]
  def findBook(bookId: Int): Future[Option[Book]]
  def findBookSummaryAccount(bookId: Int): Future[Option[(Book, List[(BookSummary, Account)])]]
  def createBook(title: String, author: String, description: String, image: String): Future[Int]
  def createSummary(bookId: Int, accountId: Int, summary: String): Future[Int]
  def findBookFromExternalId(externalId: String): Future[Option[Book]]
  def registerExternalMapping(bookId: Int, externalId: String): Future[Int]
  def search(searchQuery: String, startIndex: Int, maxResults: Int): Future[List[Book]]
}
