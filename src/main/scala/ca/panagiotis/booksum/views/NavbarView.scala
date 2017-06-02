package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.filters.contexts.AccountContext._
import ca.panagiotis.booksum.models.Account
import com.twitter.finagle.http.Request

/**
  * Created by panagiotis on 30/05/17.
  */

case class NavbarView(account: Option[Account])

object NavbarView {
  def fromRequest(req: Request): NavbarView = {
    NavbarView(req.account)
  }
}
