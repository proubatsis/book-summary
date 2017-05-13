package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{BookGetRequest, ExternalBookGetRequest}
import ca.panagiotis.booksum.services.{BookDataService, BookService}
import ca.panagiotis.booksum.views.{BookDescriptionView, BookSummaryView, NotFoundView}
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 23/04/17.
  */
class BookController @Inject() (bookService: BookService, bookDataService: BookDataService) extends Controller {
  get("/books/:id") { request: BookGetRequest =>
    for {
      result <- bookService.findBookSummary(request.id)
    } yield {
      result match {
        case Some((b, s)) => BookSummaryView(b.title, b.author, b.description, s)
        case None => response.notFound(NotFoundView(s"Book: ${request.id}"))
      }
    }
  }

  get("/books/:externalId/description") { request: ExternalBookGetRequest =>
    for {
      result <- bookDataService.getBook(request.externalId)
    } yield {
      result match {
        case Some(bookData) => BookDescriptionView.fromBookData(bookData)
        case None => response.notFound(NotFoundView(s"Book: ${request.externalId}"))
      }
    }
  }
}
