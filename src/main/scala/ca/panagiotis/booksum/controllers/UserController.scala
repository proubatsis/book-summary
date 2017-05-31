package ca.panagiotis.booksum.controllers

import java.lang.Exception
import javax.inject.Inject

import ca.panagiotis.booksum.controllers.requests.{AccountRequest, CreateUserRequest, LoginRequest}
import ca.panagiotis.booksum.filters.AuthorizeFilter
import ca.panagiotis.booksum.models.BooksumUser
import ca.panagiotis.booksum.services.{BookService, UserService}
import ca.panagiotis.booksum.util.{Endpoint, Token}
import ca.panagiotis.booksum.views._
import com.twitter.finagle.http.{Request, Status}
import com.twitter.finatra.http.Controller
import com.twitter.util.Future
import ca.panagiotis.booksum.filters.contexts.AccountContext._


/**
  * Created by panagiotis on 21/05/17.
  */
class UserController @Inject() (userService: UserService, bookService: BookService) extends Controller {
  private val ACCESS_TOKEN_COOKIE = "access"

  get("/login") { req: Request =>
    val to = req.params.getOrElse("to", "/")
    LoginView(None, None, None, to)
  }

  get("/logout") { _: Request =>
    response.status(Status.SeeOther).location("/login").cookie(ACCESS_TOKEN_COOKIE, "")
  }

  get("/signup") { req: Request =>
    SignupView(None)
  }

  get("/accounts/:id") { req: AccountRequest =>
    for {
      result <- bookService.findAccountSummaryHistory(req.id)
    } yield result match {
      case Some((account, history)) => UserSummaryHistoryView.fromUserAndSummaries(account.username, history, req.request.account)
      case None => response.notFound(NotFoundView(s"Account: ${req.id} not found!", NavbarView.fromAccountOption(req.request.account)))
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
    val invalidLoginView = LoginView(Some("Invalid email or password"), None, Some(req.email), req.to)

    for {
      userAccount <- userService.findUserAccountByEmail(req.email)
    } yield userAccount match {
      case Some((user, account)) => {
        if (BooksumUser.isValidPassword(user, req.password))
          response.status(Status.SeeOther).location(req.to).cookie(ACCESS_TOKEN_COOKIE, Token.encodeAccessToken(account))
        else invalidLoginView
      }
      case None => invalidLoginView
    }
  }
}
