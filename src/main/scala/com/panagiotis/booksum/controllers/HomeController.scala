package com.panagiotis.booksum.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.response.Mustache

@Mustache("home")
case class HomeView(A: String, B: String)

/**
  * Created by panagiotis on 22/04/17.
  */
class HomeController extends Controller {
  get("/") { req: Request =>
    HomeView("Test Title", "desc... lol :)")
  }
}
