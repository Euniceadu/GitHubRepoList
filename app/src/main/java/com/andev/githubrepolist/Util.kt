package com.andev.githubrepolist

import org.json.JSONArray
import org.json.JSONException
import java.util.*


 val SARG_REPOS = "repos"

  @Throws(JSONException::class)
  fun retrieveRepositoriesFromResponse(response: String?): ArrayList<Repository> {
    if (null == response) {
      return ArrayList<Repository>()
    }
    val jsonArray = JSONArray(response)
    val repositories = ArrayList<Repository>()

    for (i in 0..jsonArray.length() - 1) {
      val jsonObject = jsonArray.getJSONObject(i)
      if (null != jsonObject) {
        var repositoryOwner: Repository.Owner? = null;
        if (jsonObject.has("owner")) {
          val owner = jsonObject.getJSONObject("owner")
          val ownerName = if (owner.has("login")) owner.getString("login") else ""
          val avatarUrl = if (owner.has("avatar_url")) owner.getString("avatar_url") else ""
          val htmlUrl = if (owner.has("html_url")) owner.getString("html_url") else ""

          repositoryOwner = Repository.Owner(login = ownerName, avatar_url = avatarUrl, html_url
          = htmlUrl)
        }
        val repoName = if (jsonObject.has("name")) jsonObject.getString("name") else ""
        val fullName = if (jsonObject.has("full_name")) jsonObject.getString("full_name") else ""
        val description = if (jsonObject.has("description")) jsonObject.getString("description")
        else ""
        val url = if (jsonObject.has("html_url")) jsonObject.getString("html_url") else ""

        repositories.add(Repository(name = repoName, full_name = fullName, description =
        description, html_url = url, owner = repositoryOwner))
      }
    }
    return repositories
  }