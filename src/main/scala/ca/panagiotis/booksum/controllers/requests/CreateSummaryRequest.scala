package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.{FormParam, Header, RouteParam}

/**
  * Created by panagiotis on 15/05/17.
  */

case class CreateSummaryRequest(
                          request: Request,
                          @RouteParam id: Option[Int],
                          @RouteParam externalId: Option[String],
                          @FormParam summary: String
                          )
