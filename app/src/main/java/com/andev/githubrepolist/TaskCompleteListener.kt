package com.andev.githubrepolist

import java.util.*

/**
 * Created by Eunice A. Obugyei on 28/01/2017.
 */
interface TaskCompleteListener {

  fun downloadComplete(repositories: ArrayList<Repository>)
}