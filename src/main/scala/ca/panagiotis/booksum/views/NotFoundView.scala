package ca.panagiotis.booksum.views

import com.twitter.finagle.http.Request
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 24/04/17.
  */

@Mustache("not_found")
case class NotFoundView(item: String, req: Request) extends PageView(req)
