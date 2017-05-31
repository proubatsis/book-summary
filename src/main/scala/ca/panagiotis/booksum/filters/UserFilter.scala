package ca.panagiotis.booksum.filters

import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.RequestUtil
import ca.panagiotis.booksum.filters.contexts.AccountContext
import ca.panagiotis.booksum.models.Account
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}

/**
  * Created by panagiotis on 30/05/17.
  */
class UserFilter extends SimpleFilter[Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]) = {
    AccountContext.setAccount(request)
    service(request)
  }
}
