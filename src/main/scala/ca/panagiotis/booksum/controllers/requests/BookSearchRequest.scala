package ca.panagiotis.booksum.controllers.requests

import com.twitter.finagle.http.Request
import com.twitter.finatra.request.QueryParam

/**
  * Created by panagiotis on 13/05/17.
  */
case class BookSearchRequest(
                            request: Request,
                            @QueryParam q: Option[String],
                            @QueryParam t: Option[String],
                            @QueryParam ajax: Option[Boolean]
                            )
