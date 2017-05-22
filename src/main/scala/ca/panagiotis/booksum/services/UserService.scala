package ca.panagiotis.booksum.services

import ca.panagiotis.booksum.models.{Account, BooksumUser}
import com.twitter.util.Future

/**
  * Created by panagiotis on 21/05/17.
  */
trait UserService {
  def findUserAccountByEmail(email: String): Future[Option[(BooksumUser, Account)]]
  def createUserAccount(email: String, username: String, password: String): Future[(Int, Int)]
}
