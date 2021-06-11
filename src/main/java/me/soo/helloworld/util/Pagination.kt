package me.soo.helloworld.util

class Pagination(val cursor: Int?, val pageSize: Int) {

    companion object {
        @JvmStatic
        fun create(cursor: Int?, pageSize: Int) = Pagination(cursor, pageSize)
    }
}
