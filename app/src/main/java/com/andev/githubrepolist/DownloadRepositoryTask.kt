package com.andev.githubrepolist

import android.os.AsyncTask
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Eunice A Obugyei on 28/01/2017.
 */
class DownloadRepositoryTask(var taskCompleteListener: TaskCompleteListener?) : AsyncTask<String, Void, String>() {

  override fun doInBackground(vararg params: String): String? {
    try {
      return downloadData(params[0])
    } catch (e: IOException) {
      e.printStackTrace()
      return null
    }

  }

  override fun onPostExecute(result: String) {
    try {
      taskCompleteListener?.downloadComplete(retrieveRepositoriesFromResponse(result))
    } catch (e: JSONException) {
      e.printStackTrace()
    }
  }

  @Throws(IOException::class)
  private fun downloadData(urlString: String): String {
    var inputStream: InputStream? = null
    try {
      val url = URL(urlString)
      val conn = url.openConnection() as HttpURLConnection
      conn.requestMethod = "GET"
      conn.connect()

      inputStream = conn.inputStream
      return inputStream.bufferedReader().use { it.readText() }
    } finally {
      if (inputStream != null) {
        inputStream.close()
      }
    }
  }
}