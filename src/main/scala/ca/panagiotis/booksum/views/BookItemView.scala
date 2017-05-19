package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Book, BookData}
import ca.panagiotis.booksum.util.Endpoint

/**
  * Created by panagiotis on 19/05/17.
  */
case class BookItemView(
                         title: String,
                         author: String,
                         description: String,
                         image: String,
                         summary_url: Option[String],
                         description_url: Option[String],
                         new_summary_url: Option[String]
                       )

object BookItemView {
  def fromBook(book: Book): BookItemView = {
    BookItemView(
      book.title,
      book.author,
      book.description,
      book.image,
      Some(Endpoint.Book.summary(book.id)),
      Some(Endpoint.Book.description(book.id)),
      Some(Endpoint.Book.newSummary(book.id))
    )
  }

  def fromBookData(bookData: BookData): BookItemView = {
    BookItemView(
      bookData.title,
      bookData.author,
      bookData.description,
      bookData.imageUrl,
      None,
      Some(Endpoint.Book.externalDescription(bookData.externalId)),
      Some(Endpoint.Book.externalNewSummary(bookData.externalId))
    )
  }
}
