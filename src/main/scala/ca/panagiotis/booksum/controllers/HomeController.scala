package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.models.Book
import ca.panagiotis.booksum.services.BookService
import ca.panagiotis.booksum.views.HomeView
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future

/**
  * Created by panagiotis on 22/04/17.
  */
class HomeController @Inject() (bookService: BookService) extends Controller {
  get("/") { req: Request =>
    for {
      books <- bookService.findBooks()
      view = HomeView("This is my title!", books)
    } yield view
  }
}
