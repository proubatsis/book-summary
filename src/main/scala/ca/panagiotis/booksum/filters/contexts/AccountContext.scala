package ca.panagiotis.booksum.filters.contexts

import ca.panagiotis.booksum.controllers.requests.RequestUtil
import ca.panagiotis.booksum.models.Account
import com.twitter.finagle.http.Request

/**
  * Created by panagiotis on 30/05/17.
  */
object AccountContext {
  private val accountField = Request.Schema.newField[Option[Account]]()

  implicit class AccountContextSyntax(val request: Request) extends AnyVal {
    def account: Option[Account] = request.ctx(accountField)
  }

  def setAccount(request: Request): Unit = {
    val account = RequestUtil.getAuthorizedAccount(request)
    request.ctx.update(accountField, account)
  }
}
