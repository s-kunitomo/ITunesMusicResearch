package com.example.shoji.itunesmusicresearch

data class Article(
        val id: String,
        val title: String,
        val author: Author
)

data class Author(
        val id: String = "none",
        val name: String = ""
)

data class Articles(val articles: List<Article>) {
    companion object {
        fun of(vararg articles: Article) = Articles(listOf(*articles))
    }
}