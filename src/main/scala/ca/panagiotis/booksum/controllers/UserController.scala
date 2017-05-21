package ca.panagiotis.booksum.controllers

import ca.panagiotis.booksum.views.{LoginView, SignupView, UserSummaryHistoryView}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 21/05/17.
  */
class UserController extends Controller {
  get("/login") { req: Request =>
    LoginView(None)
  }

  get("/signup") { req: Request =>
    SignupView(None)
  }

  get("/accounts/:id") { req: Request =>
    UserSummaryHistoryView.fromUserAndSummaries("proubatsis", List())
  }
}
