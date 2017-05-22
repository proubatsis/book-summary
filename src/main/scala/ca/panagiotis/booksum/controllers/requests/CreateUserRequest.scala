package ca.panagiotis.booksum.controllers.requests

import com.twitter.finatra.request.FormParam

/**
  * Created by panagiotis on 21/05/17.
  */
case class CreateUserRequest(
                            @FormParam email: String,
                            @FormParam username: String,
                            @FormParam password: String,
                            @FormParam confirmPassword: String
                            )
