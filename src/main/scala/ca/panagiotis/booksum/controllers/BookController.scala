package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{BookGetRequest, BookSearchRequest, CreateSummaryRequest, RequestUtil}
import ca.panagiotis.booksum.exceptions.{BookNotFoundException, CreateBookException, CreateSummaryException}
import ca.panagiotis.booksum.models.{Account, SearchPaginationModel}
import ca.panagiotis.booksum.services.{BookDataService, BookService}
import ca.panagiotis.booksum.util.{Endpoint, Token}
import ca.panagiotis.booksum.views._
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.response.ResponseBuilder
import com.twitter.util.Future

/**
  * Created by panagiotis on 23/04/17.
  */
class BookController @Inject() (bookService: BookService, bookDataService: BookDataService) extends Controller {
  private val BOOK_SEARCH_INDEX = 0
  private val BOOK_SEARCH_MAX_RESULTS = 10

  get("/books/:id/summary") { request: BookGetRequest =>
    for {
      result <- bookService.findBookSummaryAccount(request.id.head)
    } yield {
      result match {
        case Some((b, sa)) => BookSummaryView.create(b, sa)
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
    val seekToken = { (spm: SearchPaginationModel, f: (Int, Int) => Int) =>
      Token.encodeSearchPagination(SearchPaginationModel(spm.q, f(spm.startIndex, spm.maxResults), spm.maxResults))
    }

    (request.q, request.t) match {
      case (_, Some(t)) =>
        Token.decodeSearchPagination(t) match {
          case Some(pagination) =>
              for {
                result <- bookDataService.searchBook(pagination.q, pagination.startIndex, pagination.maxResults)
              } yield BookSearchView(pagination.q, result, Some(Endpoint.Book.searchToken(seekToken(pagination, (a, b) => a - b))), Some(Endpoint.Book.searchToken(seekToken(pagination, (a, b) => a + b))))
          case None => response.temporaryRedirect.location("/books/search")
        }
      case (Some(q), _) =>
        for {
          result <- bookDataService.searchBook(q, BOOK_SEARCH_INDEX, BOOK_SEARCH_MAX_RESULTS)
        } yield BookSearchView(q, result, None, Some(Endpoint.Book.searchToken(Token.encodeSearchPagination(SearchPaginationModel(q, BOOK_SEARCH_MAX_RESULTS, BOOK_SEARCH_MAX_RESULTS)))))
      case _ => BookSearchView("", List(), None, None)
    }
  }

  get("/books/ext/:external_id/summary/new") { request: BookGetRequest =>
    externalToInternalRedirectThenAuthorize(request.request, request.externalId.head, response, Endpoint.Book.newSummary, _ => {
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
    externalToInternalRedirectThenAuthorize(request.request, request.externalId.head, response, Endpoint.Book.newSummary, account => {
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
          _ <- bookService.registerExternalMapping(bookId, request.externalId.head)
          _ <- bookService.createSummary(bookId, account.id, request.summary)
        } yield response.found.location(Endpoint.Book.summary(bookId))
      }
      catch {
        case BookNotFoundException(m) => Future.value(response.notFound(m))
        case CreateBookException() => Future.value(response.internalServerError("Failed to create book"))
        case CreateSummaryException() => Future.value(response.internalServerError("Failed to create summary"))
      }
    })
  }

  get("/books/:id/summary/new") { request: BookGetRequest =>
    authorize(request.request, response, { _ =>
      for {
        result <- bookService.findBook(request.id.head)
      } yield {
        result match {
          case Some(book) => NewSummaryView.fromBook(book)
          case None => response.notFound(s"Book: ${request.id.head}")
        }
      }
    })
  }

  post("/books/:id/summary/new") { request: CreateSummaryRequest =>
    authorize(request.request, response, { account =>
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
          _ <- bookService.createSummary(book.id, account.id, request.summary)
        } yield response.found.location(Endpoint.Book.summary(book.id))
      }
      catch {
        case BookNotFoundException(m) => Future.value(response.notFound(m))
      }
    })
  }

  private def externalToInternalRedirect(externalId: String, response: ResponseBuilder, endpoint: Int => String, f: () => Future[Any]) = {
    (for {
      existingBook <- bookService.findBookFromExternalId(externalId)
    } yield existingBook match {
      case Some(eb) => Future.value(response.temporaryRedirect.location(endpoint(eb.id)))
      case None => f()
    }).flatten
  }

  private def authorize(req: Request, response: ResponseBuilder, f: Account => Future[Any]) = {
    RequestUtil.getAuthorizedAccount(req) match {
      case Some(account) => f(account)
      case None => Future.value(response.temporaryRedirect.location("/"))
    }
  }

  private def externalToInternalRedirectThenAuthorize(req: Request, externalId: String, response: ResponseBuilder, endpoint: Int => String, f: Account => Future[Any]) = {
    externalToInternalRedirect(externalId, response, endpoint, () => authorize(req, response, f))
  }
}
