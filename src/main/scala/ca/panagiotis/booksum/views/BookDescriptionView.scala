package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.{Account, Book, BookData}
import ca.panagiotis.booksum.util.Endpoint
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 13/05/17.
  */

@Mustache("book_description")
case class BookDescriptionView(title: String, author: String, description: String, image: String, new_summary_url: String, navbar: NavbarView) extends PageView

object BookDescriptionView {
  def fromBookData(bd: BookData, account: Option[Account]): BookDescriptionView = {
    BookDescriptionView(bd.title, bd.author, bd.description, bd.imageUrl, Endpoint.Book.externalNewSummary(bd.externalId), NavbarView.fromAccountOption(account))
  }

  def fromBook(b: Book, account: Option[Account]): BookDescriptionView = {
    BookDescriptionView(b.title, b.author, b.description, b.image, Endpoint.Book.newSummary(b.id), NavbarView.fromAccountOption(account))
  }
}
