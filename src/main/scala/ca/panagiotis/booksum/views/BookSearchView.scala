package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.BookData
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 13/05/17.
  */

@Mustache("search")
case class BookSearchView(q: String, books: List[BookData], previous: Option[String], next: Option[String])
