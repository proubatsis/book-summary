package ca.panagiotis.booksum.controllers.requests

import com.twitter.finatra.request.{FormParam, QueryParam}

/**
  * Created by panagiotis on 21/05/17.
  */
case class LoginRequest(
                       @FormParam email: String,
                       @FormParam password: String,
                       @QueryParam to: String
                       )
