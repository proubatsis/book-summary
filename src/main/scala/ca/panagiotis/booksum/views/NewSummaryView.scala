package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Book, BookData}
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 15/05/17.
  */

@Mustache("new_summary")
case class NewSummaryView(title: String, author: String, formActionUrl: String)

object NewSummaryView {
  def fromBookData(bookData: BookData): NewSummaryView = NewSummaryView(bookData.title, bookData.author, s"/books/ext/${bookData.externalId}/summary/new")
  def fromBook(book: Book): NewSummaryView = NewSummaryView(book.title, book.author, s"/books/${book.id}/summary/new")
}
