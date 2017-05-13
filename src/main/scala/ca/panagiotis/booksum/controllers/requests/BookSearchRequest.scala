package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.RouteParam

/**
  * Created by panagiotis on 13/05/17.
  */
case class BookSearchRequest(
                            request: Request,
                            @RouteParam q: Option[String]
                            )
