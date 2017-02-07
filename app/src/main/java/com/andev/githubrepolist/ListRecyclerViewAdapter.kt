package com.andev.githubrepolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * Created by Eunice A Obugyei on 29/01/2017.
 */
class ListRecyclerViewAdapter: RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

  private var repos: List<Repository> = ArrayList<Repository>()

  constructor(repositories: List<Repository>) {
    repos = repositories
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.mRepo = repos[position]
    holder.mRepoName.text = repos[position].full_name
    holder.mRepoDescription.text = repos[position].description

  }

  override fun getItemCount(): Int {
    return repos.size
  }

  inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
    var mRepo: Repository? = null
    val mRepoName: TextView
    val mRepoDescription: TextView

    init {
      mRepoName = mView.findViewById(R.id.repo_name) as TextView
      mRepoDescription = mView.findViewById(R.id.repo_description) as TextView
    }

    override fun toString(): String {
      return super.toString() + " '" + mRepoName.text + "'"
    }
  }
}