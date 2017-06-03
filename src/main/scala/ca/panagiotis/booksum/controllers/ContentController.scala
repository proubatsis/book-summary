package ca.panagiotis.booksum.controllers

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{BookGetRequest, TokenRequest}
import ca.panagiotis.booksum.services.{BookDataService, BookService}
import ca.panagiotis.booksum.util.Token
import com.twitter.finagle.Http
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 02/06/17.
  */
class ContentController @Inject() (bookService: BookService, bookDataService: BookDataService) extends Controller {
  get("/content/images/:token") { req: TokenRequest =>
    Token.decodeUrl(req.token) match {
      case Some(image) => Http.fetchUrl(image)
      case None => response.notFound.toFuture
    }
  }
}
