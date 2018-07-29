package br.ind.conceptu.tmdbupcoming.util

import java.text.SimpleDateFormat

class DateUtil {
    companion object {
        fun formatServerDate(serverDate: String):String{
            val formatter = SimpleDateFormat()
            formatter.applyPattern("yyyy-MM-dd")
            val serverDate = formatter.parse(serverDate)
            formatter.applyPattern("MMMM d, yyyy")
            return formatter.format(serverDate)
        }
    }
}