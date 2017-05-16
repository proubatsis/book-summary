package ca.panagiotis.booksum.controllers.requests

import com.twitter.finatra.request.{FormParam, RouteParam}

/**
  * Created by panagiotis on 15/05/17.
  */

case class CreateSummaryRequest(
                          @RouteParam id: Option[Int],
                          @RouteParam externalId: Option[String],
                          @FormParam summary: String
                          )
