package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.RouteParam

/**
  * Created by panagiotis on 02/06/17.
  */
case class TokenRequest(req: Request, @RouteParam token: String)

