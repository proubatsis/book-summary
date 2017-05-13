package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.BookData
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 13/05/17.
  */

@Mustache("book_description")
case class BookDescriptionView(title: String, author: String, description: String, image: String)

object BookDescriptionView {
  def fromBookData(bd: BookData): BookDescriptionView = {
    BookDescriptionView(bd.title, bd.author, bd.description, bd.imageUrl)
  }
}
