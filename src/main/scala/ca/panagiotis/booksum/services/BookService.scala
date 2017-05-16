package ca.panagiotis.booksum.services

import ca.panagiotis.booksum.models.{Book, BookSummary}
import com.twitter.util.Future

/**
  * Created by panagiotis on 22/04/17.
  */
trait BookService {
  def findBooks(): Future[List[Book]]
  def findBook(bookId: Int): Future[Option[Book]]
  def findBookSummary(bookId: Int): Future[Option[(Book, List[BookSummary])]]
  def createBook(title: String, author: String, description: String, image: String): Future[Int]
  def createSummary(bookId: Int, summary: String): Future[Int]
}
