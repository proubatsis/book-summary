package ca.panagiotis.booksum.views

import com.twitter.finagle.http.Request

/**
  * Created by panagiotis on 30/05/17.
  */

abstract class PageView(req: Request) {
  def navbar = NavbarView.fromRequest(req)
}
