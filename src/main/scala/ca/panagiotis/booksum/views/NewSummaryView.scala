package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Account, Book, BookData}
import com.twitter.finagle.http.Request
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 15/05/17.
  */

@Mustache("new_summary")
case class NewSummaryView(title: String, author: String, formActionUrl: String, req: Request) extends PageView(req)

object NewSummaryView {
  def fromBookData(bookData: BookData, req: Request): NewSummaryView =
    NewSummaryView(bookData.title, bookData.author, s"/books/ext/${bookData.externalId}/summary/new", req)
  def fromBook(book: Book, req: Request): NewSummaryView =
    NewSummaryView(book.title, book.author, s"/books/${book.id}/summary/new", req)
}
