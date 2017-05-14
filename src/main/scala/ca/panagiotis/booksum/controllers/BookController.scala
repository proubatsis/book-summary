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
  get("/books/:id/summary") { request: BookGetRequest =>
    for {
      result <- bookService.findBookSummary(request.id.head)
    } yield {
      result match {
        case Some((b, s)) => BookSummaryView(b.title, b.author, b.description, s)
        case None => response.notFound(NotFoundView(s"Book: ${request.id.head}"))
      }
    }
  }

  get("/books/:id/description") { request: BookGetRequest =>
    for {
      result <- bookService.findBook(request.id.head)
    } yield {
      result match {
        case Some(book) => BookDescriptionView.fromBook(book).head
        case None => response.notFound(NotFoundView(s"Book: ${request.id.head}"))
      }
    }
  }

  get("/books/ext/:external_id/description") { request: BookGetRequest =>
    for {
      result <- bookDataService.getBook(request.externalId.head)
    } yield {
      result match {
        case Some(bookData) => BookDescriptionView.fromBookData(bookData)
        case None => response.notFound(NotFoundView(s"Book: ${request.externalId.head}"))
      }
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
