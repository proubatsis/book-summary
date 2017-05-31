package ca.panagiotis.booksum.filters

import java.net.URLEncoder
import javax.inject.{Inject, Provider}

import ca.panagiotis.booksum.models.Account
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future

import scala.scalajs.niocharset.StandardCharsets

/**
  * Created by panagiotis on 30/05/17.
  */
class AuthorizeFilter @Inject() (rAccount: Provider[Option[Account]]) extends SimpleFilter[Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]) = {
    rAccount.get match {
      case Some(account) => service(request)
      case None =>
        val response = Response.apply(request)
        response.status = Status.TemporaryRedirect
        response.location = s"/login?to=${URLEncoder.encode(request.uri, StandardCharsets.UTF_8.toString)}"
        Future.value(response)
    }
  }
}
