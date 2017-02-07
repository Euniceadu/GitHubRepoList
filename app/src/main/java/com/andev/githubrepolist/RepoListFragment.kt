package com.andev.githubrepolist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by Eunice A. Obugyei on 27/01/2017.
 */
class RepoListFragment: Fragment() {

  private var repos: ArrayList<Repository> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (arguments != null) {
      repos = arguments.getParcelableArrayList(SARG_REPOS)
    }
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val view = inflater!!.inflate(R.layout.fragment_list, container, false)



    if (view is RecyclerView) {
      val context = view.getContext()
      view.layoutManager = LinearLayoutManager(context)

      view.adapter = ListRecyclerViewAdapter(repos)
    }
    return view
  }

}