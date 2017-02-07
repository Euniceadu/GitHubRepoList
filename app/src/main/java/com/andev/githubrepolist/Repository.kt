package com.andev.githubrepolist

import android.os.Parcel
import android.os.Parcelable


class Repository(): Parcelable {

  var name: String? = ""
   var full_name: String? = ""
   var description: String? = ""
   var html_url: String? = ""
   var owner: Owner? = null

  companion object {

    @JvmField @Suppress("unused")
    val CREATOR = createParcel { Repository() }

    inline fun <reified T : Parcelable> createParcel (
        crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
        object : Parcelable.Creator<T> {
          override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
          override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }
  }

  constructor(name: String?, full_name: String?, description: String?, html_url: String?,
              owner: Owner?): this() {
    this.name = name
    this.full_name = full_name
    this.description = description
    this.html_url = html_url
    this.owner = owner
  }

  constructor(parcel: Parcel) : this() {
    val data = arrayOfNulls<String>(4)
    parcel.readStringArray(data)
    this.name = data[0]
    this.owner = Owner(data[1], data[2], data[3])
  }

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeStringArray(arrayOf<String?>(this.name, this.owner?.login))
  }

  class Owner() {

    var login: String? = ""
    var avatar_url: String? = ""
    var html_url: String? = ""

    constructor(login: String?, avatar_url: String?, html_url: String?):this() {
      this.login = login
      this.avatar_url = avatar_url
      this.html_url = html_url
    }

  }
}