package ca.panagiotis.booksum.controllers

import ca.panagiotis.booksum.views.HomeView
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 22/04/17.
  */
class HomeController extends Controller {
  get("/") { req: Request =>
    HomeView("Welcome to Book Summary!")
  }
}
