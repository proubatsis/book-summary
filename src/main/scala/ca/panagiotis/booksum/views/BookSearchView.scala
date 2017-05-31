package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.BookData
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 13/05/17.
  */

@Mustache("search")
case class BookSearchView(
                           q: String,
                           search_url: String,
                           books: List[BookItemView],
                           previous: Option[String],
                           next: Option[String],
                           navbar: NavbarView
                         ) extends PageView
