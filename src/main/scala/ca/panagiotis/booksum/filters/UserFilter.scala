package ca.panagiotis.booksum.filters

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.RequestUtil
import ca.panagiotis.booksum.models.Account
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.inject.requestscope.FinagleRequestScope

/**
  * Created by panagiotis on 30/05/17.
  */
class UserFilter @Inject() (requestScope: FinagleRequestScope) extends SimpleFilter[Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]) = {
    val account = RequestUtil.getAuthorizedAccount(request)
    requestScope.seed[Option[Account]](account)
    service(request)
  }
}
