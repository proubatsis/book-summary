package ca.panagiotis.booksum.controllers

import java.lang.Exception
import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{AccountRequest, CreateUserRequest, LoginRequest}
import ca.panagiotis.booksum.models.BooksumUser
import ca.panagiotis.booksum.services.{BookService, UserService}
import ca.panagiotis.booksum.util.{Endpoint, Token}
import ca.panagiotis.booksum.views.{LoginView, NotFoundView, SignupView, UserSummaryHistoryView}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future


/**
  * Created by panagiotis on 21/05/17.
  */
class UserController @Inject() (userService: UserService, bookService: BookService) extends Controller {
  get("/login") { req: Request =>
    val to = req.params.getOrElse("to", "/")
    LoginView(None, None, None, to)
  }

  get("/signup") { req: Request =>
    SignupView(None)
  }

  get("/accounts/:id") { req: AccountRequest =>
    for {
      result <- bookService.findAccountSummaryHistory(req.id)
    } yield result match {
      case Some((account, history)) => UserSummaryHistoryView.fromUserAndSummaries(account.username, history)
      case None => response.notFound(NotFoundView(s"Account: ${req.id} not found!"))
    }
  }

  post("/signup") { req: CreateUserRequest =>
    (for {
      _ <- userService.createUserAccount(req.email, req.username, req.password) if req.password.equals(req.confirmPassword)
    } yield LoginView(None, Some("Account created!"), Some(req.email), "/")) rescue {
      case _: Throwable => Future.value(SignupView(Some("Error creating this user!")))
    }
  }

  post("/login") { req: LoginRequest =>
    for {
      userAccount <- userService.findUserAccountByEmail(req.email)
    } yield userAccount match {
      case Some((user, account)) => {
        if (BooksumUser.isValidPassword(user, req.password))
          response.status(303).location(req.to).cookie("access", Token.encodeAccessToken(account))
        else LoginView(Some("Invalid email or password!"), None, Some(req.email), req.to)
      }
      case None => None
    }
  }
}
