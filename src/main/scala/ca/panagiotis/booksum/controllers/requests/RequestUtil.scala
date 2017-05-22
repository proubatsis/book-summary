package ca.panagiotis.booksum.controllers.requests

import ca.panagiotis.booksum.models.Account
import ca.panagiotis.booksum.util.Token
import com.twitter.finagle.http.Request

/**
  * Created by panagiotis on 22/05/17.
  */
object RequestUtil {
  def extractCookie(req: Request, cookieKey: String): Option[String] = {
    for {
      cookie <- req.cookies.get(cookieKey)
    } yield cookie.value
  }

  def getAuthorizedAccount(req: Request): Option[Account] = {
    for {
      token <- extractCookie(req, "access")
      account <- Token.decodeAccessToken(token)
    } yield account
  }
}
