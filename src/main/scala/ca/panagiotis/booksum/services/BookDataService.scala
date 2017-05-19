package ca.panagiotis.booksum.services

import ca.panagiotis.booksum.models.BookData
import com.twitter.util.Future

/**
  * Created by panagiotis on 11/05/17.
  */
trait BookDataService {
  def getBook(id: String): Future[Option[BookData]]
  def searchBook(query: String, startIndex: Int, maxResults: Int): Future[List[BookData]]
}
