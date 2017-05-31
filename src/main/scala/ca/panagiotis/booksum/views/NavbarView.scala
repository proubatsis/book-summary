package ca.panagiotis.booksum.views

import ca.panagiotis.booksum.models.Account

/**
  * Created by panagiotis on 30/05/17.
  */

case class NavbarView(username: Option[String])

object NavbarView {
  def fromAccountOption(account: Option[Account]): NavbarView = {
    NavbarView(account map (_.username))
  }
}
