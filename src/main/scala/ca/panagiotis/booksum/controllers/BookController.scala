package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{BookGetRequest, BookSearchRequest, CreateSummaryRequest}
import ca.panagiotis.booksum.exceptions.{BookNotFoundException, CreateBookException, CreateSummaryException}
import ca.panagiotis.booksum.services.{BookDataService, BookService}
import ca.panagiotis.booksum.util.Endpoint
import ca.panagiotis.booksum.views._
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.Future

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
        case Some(book) => BookDescriptionView.fromBook(book)
        case None => response.notFound(NotFoundView(s"Book: ${request.id.head}"))
      }
    }
  }

  get("/books/ext/:external_id/description") { request: BookGetRequest =>
    externalToInternalRedirect(request.externalId.head, response, Endpoint.Book.description, () => {
      for {
        result <- bookDataService.getBook(request.externalId.head)
      } yield {
        result match {
          case Some(bookData) => BookDescriptionView.fromBookData(bookData)
          case None => response.notFound(NotFoundView(s"Book: ${request.externalId.head}"))
        }
      }
    })
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

  get("/books/ext/:external_id/summary/new") { request: BookGetRequest =>
    externalToInternalRedirect(request.externalId.head, response, Endpoint.Book.newSummary, () => {
      for {
        result <- bookDataService.getBook(request.externalId.head)
      } yield {
        result match {
          case Some(bookData) => NewSummaryView.fromBookData(bookData)
          case None => response.notFound(NotFoundView(s"Book: ${request.externalId.head}"))
        }
      }
    })
  }

  post("/books/ext/:external_id/summary/new") { request: CreateSummaryRequest =>
    externalToInternalRedirect(request.externalId.head, response, Endpoint.Book.newSummary, () => {
      try {
        val bookData = for {
          data <- bookDataService.getBook(request.externalId.head)
        } yield {
          data match {
            case Some(d) => d
            case None => throw BookNotFoundException(s"Book ${request.externalId.head}")
          }
        }

        for {
          data <- bookData
          bookId <- bookService.createBook(data.title, data.author, data.description, data.imageUrl)
          _ <- bookService.createSummary(bookId, request.summary)
        } yield Future.value(response.found.location(s"/books/$bookId/summary"))
      }
      catch {
        case BookNotFoundException(m) => Future.value(response.notFound(m))
        case CreateBookException() => Future.value(response.internalServerError("Failed to create book"))
        case CreateSummaryException() => Future.value(response.internalServerError("Failed to create summary"))
      }
    })
  }

  get("/books/:id/summary/new") { request: BookGetRequest =>
    for {
      result <- bookService.findBook(request.id.head)
    } yield {
      result match {
        case Some(book) => NewSummaryView.fromBook(book)
        case None => response.notFound(s"Book: ${request.id.head}")
      }
    }
  }

  post("/books/:id/summary/new") { request: CreateSummaryRequest =>
    try {
      val bookFuture = for {
        book <- bookService.findBook(request.id.head)
      } yield {
        book match {
          case Some(b) => b
          case None => throw BookNotFoundException(s"Book: ${request.id.head}")
        }
      }

      for {
        book <- bookFuture
        _ <- bookService.createSummary(book.id, request.summary)
      } yield response.found.location(s"/books/${book.id}/summary")
    }
    catch {
      case BookNotFoundException(m) => response.notFound(m)
    }
  }

  private def externalToInternalRedirect(externalId: String, response: ResponseBuilder, endpoint: Int => String, f: () => Future[Any]) = {
    (for {
      existingBook <- bookService.findBookFromExternalId(externalId)
    } yield existingBook match {
      case Some(eb) => Future.value(response.temporaryRedirect.location(endpoint(eb.id)))
      case None => f()
    }).flatten
  }
}
