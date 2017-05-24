package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Book, BookData}
import ca.panagiotis.booksum.util.Endpoint
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 13/05/17.
  */

@Mustache("book_description")
case class BookDescriptionView(title: String, author: String, description: String, image: String, new_summary_url: String)

object BookDescriptionView {
  def fromBookData(bd: BookData): BookDescriptionView = {
    BookDescriptionView(bd.title, bd.author, bd.description, bd.imageUrl, Endpoint.Book.externalNewSummary(bd.externalId))
  }

  def fromBook(b: Book): BookDescriptionView = {
    BookDescriptionView(b.title, b.author, b.description, b.image, Endpoint.Book.newSummary(b.id))
  }
}
