package ca.panagiotis.booksum.controllers.requests

import com.twitter.finatra.request.RouteParam

/**
  * Created by panagiotis on 23/05/17.
  */
case class AccountRequest(
                         @RouteParam id: Int
                         )
