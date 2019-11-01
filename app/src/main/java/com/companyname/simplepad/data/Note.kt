package com.companyname.simplepad.data

import java.util.Date

//data classes in kotlin allows the compiler to take care of identity, hashing, and copying automatically
data class Note(
    var id: Int = -1,
    var text: String? = null,
    var isPinned: Boolean = false,
    var createdAt: Date = Date(),
    var updatedAt: Date? = null
)
//field wise copy constructors come with this data class format b/c of kotlin's named arguments feature
//this allows easily copying the object and changing one or two fields in just one line
//ex: `noteCopy = orgNote.copy(updatedAt = Date())`
