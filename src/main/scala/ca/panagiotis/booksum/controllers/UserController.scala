package ca.panagiotis.booksum.controllers

import java.lang.Exception
import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{CreateUserRequest, LoginRequest}
import ca.panagiotis.booksum.models.BooksumUser
import ca.panagiotis.booksum.services.UserService
import ca.panagiotis.booksum.util.{Endpoint, Token}
import ca.panagiotis.booksum.views.{LoginView, SignupView, UserSummaryHistoryView}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future


/**
  * Created by panagiotis on 21/05/17.
  */
class UserController @Inject() (userService: UserService) extends Controller {
  get("/login") { req: Request =>
    LoginView(None, None, None)
  }

  get("/signup") { req: Request =>
    SignupView(None)
  }

  get("/accounts/:id") { req: Request =>
    UserSummaryHistoryView.fromUserAndSummaries("proubatsis", List())
  }

  post("/signup") { req: CreateUserRequest =>
    (for {
      _ <- userService.createUserAccount(req.email, req.username, req.password) if req.password.equals(req.confirmPassword)
    } yield LoginView(None, Some("Account created!"), Some(req.email))) rescue {
      case _: Throwable => Future.value(SignupView(Some("Error creating this user!")))
    }
  }

  post("/login") { req: LoginRequest =>
    for {
      userAccount <- userService.findUserAccountByEmail(req.email)
    } yield userAccount match {
      case Some((user, account)) => {
        if (BooksumUser.isValidPassword(user, req.password))
          response.temporaryRedirect.location("/").cookie("access", Token.encodeAccessToken(account))
        else LoginView(Some("Invalid email or password!"), None, Some(req.email))
      }
      case None => None
    }
  }
}
