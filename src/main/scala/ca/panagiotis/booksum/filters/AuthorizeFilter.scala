package ca.panagiotis.booksum.filters

import java.net.URLEncoder

import ca.panagiotis.booksum.models.Account
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future
import contexts.AccountContext._

import scala.scalajs.niocharset.StandardCharsets

/**
  * Created by panagiotis on 30/05/17.
  */
class AuthorizeFilter extends SimpleFilter[Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]) = {
    request.account match {
      case Some(_) => service(request)
      case None =>
        val response = Response.apply(request)
        response.status = Status.TemporaryRedirect
        response.location = s"/login?to=${URLEncoder.encode(request.uri, StandardCharsets.UTF_8.toString)}"
        Future.value(response)
    }
  }
}
