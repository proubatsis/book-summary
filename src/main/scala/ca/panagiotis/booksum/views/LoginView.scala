package ca.panagiotis.booksum.views

import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 21/05/17.
  */

@Mustache("login")
case class LoginView(error: Option[String], success: Option[String], email: Option[String], to: String)
