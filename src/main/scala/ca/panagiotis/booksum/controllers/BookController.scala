package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.BookGetRequest
import ca.panagiotis.booksum.services.BookService
import ca.panagiotis.booksum.views.BookSummaryView
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 23/04/17.
  */
class BookController @Inject() (bookService: BookService) extends Controller {
  get("/books/:id") { request: BookGetRequest =>
    for {
      (b, s) <- bookService.findBookSummary(request.id)
      view = BookSummaryView(b.title, b.author, b.description, s)
    } yield view
  }
}
