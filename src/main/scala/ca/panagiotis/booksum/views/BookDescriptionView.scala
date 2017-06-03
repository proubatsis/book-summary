package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Account, Book, BookData}
import ca.panagiotis.booksum.util.Endpoint
import com.twitter.finagle.http.Request
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 13/05/17.
  */

@Mustache("book_description")
case class BookDescriptionView(title: String, author: String, description: String, image: String, new_summary_url: String, req: Request) extends PageView(req)

object BookDescriptionView {
  def fromBookData(bd: BookData, req: Request): BookDescriptionView = {
    BookDescriptionView(bd.title, bd.author, bd.description, Endpoint.Content.image(bd.imageUrl), Endpoint.Book.externalNewSummary(bd.externalId), req)
  }

  def fromBook(b: Book, req: Request): BookDescriptionView = {
    BookDescriptionView(b.title, b.author, b.description, Endpoint.Content.image(b.image), Endpoint.Book.newSummary(b.id), req)
  }
}
