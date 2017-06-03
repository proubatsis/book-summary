package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.BookGetRequest
import ca.panagiotis.booksum.services.{BookDataService, BookService}
import com.twitter.finagle.Http
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 02/06/17.
  */
class ContentController @Inject() (bookService: BookService, bookDataService: BookDataService) extends Controller {
  get("/content/images/book/:id") { req: BookGetRequest =>
    bookService.findBook(req.id.head) flatMap {
      case Some(book) => Http.fetchUrl(book.image)
      case None => response.notFound.toFuture
    }
  }

  get("/content/images/book/ext/:external_id") { req: BookGetRequest =>
    bookDataService.getBook(req.externalId.head) flatMap {
      case Some(bookData) => Http.fetchUrl(bookData.imageUrl)
      case None => response.notFound.toFuture
    }
  }
}
