package ca.panagiotis.booksum.models

import com.github.t3hnar.bcrypt._

/**
  * Created by panagiotis on 21/05/17.
  */
case class BooksumUser(id: Int, email: String, password: String)

object BooksumUser {
  def withHashedPassword(user: BooksumUser): BooksumUser = {
    BooksumUser(user.id, user.email, user.password.bcrypt)
  }

  def isValidPassword(user: BooksumUser, password: String): Boolean = {
    password.isBcrypted(user.password)
  }
}
