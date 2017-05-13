package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{BookGetRequest, BookSearchRequest}
import ca.panagiotis.booksum.services.{BookDataService, BookService}
import ca.panagiotis.booksum.views.{BookDescriptionView, BookSearchView, BookSummaryView, NotFoundView}
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 23/04/17.
  */
class BookController @Inject() (bookService: BookService, bookDataService: BookDataService) extends Controller {
  get("/books/summary") { request: BookGetRequest =>
    request.id match {
      case Some(id) =>
        for {
          result <- bookService.findBookSummary(id)
        } yield {
          result match {
            case Some((b, s)) => BookSummaryView(b.title, b.author, b.description, s)
            case None => response.notFound(NotFoundView(s"Book: ${request.id}"))
          }
        }
      case None => response.badRequest()
    }
  }

  get("/books/description") { request: BookGetRequest =>
    request.externalId match {
      case Some(externalId) =>
        for {
          result <- bookDataService.getBook(externalId)
        } yield {
          result match {
            case Some(bookData) => BookDescriptionView.fromBookData(bookData)
            case None => response.notFound(NotFoundView(s"Book: ${request.externalId}"))
          }
        }
      case None => response.badRequest()
    }
  }

  get("/books/search") { request: BookSearchRequest =>
    request.q match {
      case Some(q) =>
        for {
          result <- bookDataService.searchBook(q)
        } yield BookSearchView(q, result)
      case None => BookSearchView("", List())
    }
  }
}
