package ca.panagiotis.booksum.filters

import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response, Status}

/**
  * Created by panagiotis on 02/06/17.
  */
class ContentFilter extends SimpleFilter[Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]) = {
    service(request) map { response =>
      if (response.status.code >= 400) Response(Status.NotFound)
      else response
    }
  }
}
