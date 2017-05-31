package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Account, Book, BookData}
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 15/05/17.
  */

@Mustache("new_summary")
case class NewSummaryView(title: String, author: String, formActionUrl: String, navbar: NavbarView) extends PageView

object NewSummaryView {
  def fromBookData(bookData: BookData, account: Option[Account]): NewSummaryView =
    NewSummaryView(bookData.title, bookData.author, s"/books/ext/${bookData.externalId}/summary/new", NavbarView.fromAccountOption(account))
  def fromBook(book: Book, account: Option[Account]): NewSummaryView =
    NewSummaryView(book.title, book.author, s"/books/${book.id}/summary/new", NavbarView.fromAccountOption(account))
}
