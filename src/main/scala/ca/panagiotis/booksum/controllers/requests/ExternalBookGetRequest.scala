package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.RouteParam

/**
  * Created by panagiotis on 13/05/17.
  */
case class ExternalBookGetRequest(request: Request,
                                 @RouteParam externalId: String
                                 )
