package ca.panagiotis.booksum.services.quill

import javax.inject.Inject

import ca.panagiotis.booksum.models.{Account, BooksumUser}
import ca.panagiotis.booksum.services.UserService
import com.twitter.util.Future
import io.getquill.{FinaglePostgresContext, SnakeCase}

/**
  * Created by panagiotis on 21/05/17.
  */
class QuillUserService @Inject() (ctx: FinaglePostgresContext[SnakeCase]) extends UserService {
  import ctx._

  override def findUserAccountByEmail(email: String): Future[Option[(BooksumUser, Account)]] = {
    val q = quote {
      for {
        user <- query[BooksumUser] filter (_.email == lift(email))
        account <- query[Account] filter (_.userId == user.id)
      } yield (user, account)
    }

    ctx.run(q) map (_.headOption)
  }

  override def createUserAccount(email: String, username: String, password: String): Future[(Int, Int)] = {
    val user = BooksumUser(0, email, password)

    for {
      userId <- ctx.run(query[BooksumUser].insert(lift(user)).returning(_.id))
      accountId <- ctx.run(query[Account].insert(lift(Account(0, userId, username))).returning(_.id))
    } yield (userId, accountId)
  }
}
