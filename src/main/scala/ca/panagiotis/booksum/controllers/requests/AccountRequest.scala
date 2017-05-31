package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.RouteParam

/**
  * Created by panagiotis on 23/05/17.
  */
case class AccountRequest(
                         request: Request,
                         @RouteParam id: Int
                         )
