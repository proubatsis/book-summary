package ca.panagiotis.booksum.views

import com.twitter.finatra.response.Mustache

/**
  * Created by panagiotis on 15/05/17.
  */

@Mustache("new_summary")
case class NewSummaryView(externalId: String, title: String, author: String)
