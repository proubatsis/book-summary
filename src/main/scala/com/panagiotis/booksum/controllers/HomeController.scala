package com.panagiotis.booksum.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

/**
  * Created by panagiotis on 22/04/17.
  */
class HomeController extends Controller {
  get("/") { _:Request =>
    "Hello, world!"
  }
}
