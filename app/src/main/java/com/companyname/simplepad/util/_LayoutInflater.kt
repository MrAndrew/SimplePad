package com.companyname.simplepad.util

import android.content.Context
import android.view.LayoutInflater

// extension function on Context class for LayoutInflater (b/c kotlin let's us and it's a little more convenient)
val Context.layoutInflater get() = LayoutInflater.from(this)