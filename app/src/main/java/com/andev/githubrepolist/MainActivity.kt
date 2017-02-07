package com.andev.githubrepolist

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import okhttp3.OkHttpClient
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), TaskCompleteListener {

  internal var progressDialog: ProgressDialog? = null
  internal var repoListFragment: RepoListFragment? = null
  internal val GET_REPO_URL: String = "https://api.github.com/repositories"


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    if (findViewById(R.id.fragment_container) != null) {
      if (savedInstanceState != null) {
        return
      }
      if (isNetworkConnected()) {
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage("Please wait...")
        progressDialog?.setCancelable(false)
        progressDialog?.show()

        startDownload(GET_REPO_URL)
      } else {
        AlertDialog.Builder(this).setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again")
            .setPositiveButton(android.R.string.ok) { dialog, which -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
      }
    }

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    return super.onOptionsItemSelected(item)
  }

  override fun downloadComplete(repositories: ArrayList<Repository>) {
    showListFragment(repositories);
    if (progressDialog != null) {
      progressDialog?.hide();
    }
  }

  private fun showListFragment(repositories: ArrayList<Repository>) {

    repoListFragment = RepoListFragment()

    val args = Bundle()
    args.putParcelableArrayList(SARG_REPOS, repositories)
    repoListFragment?.arguments = args

    supportFragmentManager.beginTransaction().add(R.id.fragment_container, repoListFragment)
    .commit()
  }

  private fun startDownload(downloadUrl: String) {
//    DownloadRepositoryTask(this).execute(downloadUrl)
//    makeRequestWithOkHttp(downloadUrl)
//    makeRequestWithVolley(downloadUrl)
    makeRetrofitCalls()
  }

  private fun isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
  }

  private fun makeRequestWithOkHttp(url: String) {
    val client = OkHttpClient()
    val request = okhttp3.Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
      override fun onFailure(call: okhttp3.Call, e: IOException) {
        e.printStackTrace()
      }

      @Throws(IOException::class)
      override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
        val result = response.body().string()

        this@MainActivity.runOnUiThread {
          try {
            downloadComplete(retrieveRepositoriesFromResponse(result))
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        }
      }
    })
  }

  private fun makeRequestWithVolley(url: String) {

    val queue = Volley.newRequestQueue(this)

    val stringRequest = StringRequest(Request.Method.GET, url,
        Response.Listener<kotlin.String> { response ->
          try {
            downloadComplete(retrieveRepositoriesFromResponse(response))
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        }, Response.ErrorListener { })
    queue.add(stringRequest)

  }

  private fun makeRetrofitCalls() {
    val retrofit = Retrofit.Builder().baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI = retrofit.create<RetrofitAPI>(RetrofitAPI::class.java)

    val call = retrofitAPI.retrieveRepositories()

    call.enqueue(object : Callback<ArrayList<Repository>> {
      override fun onResponse(call: Call<ArrayList<Repository>>,
                              response: retrofit2.Response<ArrayList<Repository>>) {
        downloadComplete(response.body())
      }

      override fun onFailure(call: Call<ArrayList<Repository>>, t: Throwable) {
        Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
      }
    })
  }

}
