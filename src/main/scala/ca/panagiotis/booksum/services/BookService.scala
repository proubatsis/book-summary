package ca.panagiotis.booksum.services

import ca.panagiotis.booksum.models.{Book, BookSummary}
import com.twitter.util.Future

/**
  * Created by panagiotis on 22/04/17.
  */
trait BookService {
  def findBooks(): Future[List[Book]]
  def findBookSummary(bookId: Int): Future[Option[(Book, List[BookSummary])]]
}
