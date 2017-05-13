package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.RouteParam

/**
  * Created by panagiotis on 23/04/17.
  */

case class BookGetRequest(
                           request: Request,
                           @RouteParam id: Option[Int],
                           @RouteParam externalId: Option[String]
                         )
