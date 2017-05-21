package ca.panagiotis.booksum.views

import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 21/05/17.
  */
@Mustache("signup")
case class SignupView(error: Option[String])
