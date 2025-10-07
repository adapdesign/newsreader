package dev.androi.newsreader.data.data

import dev.androi.newsreader.data.local.ArticleEntity

fun ArticleEntity.toPreview(): ArticlePreview =
    ArticlePreview(
        id = id,
        title = title,
        source = source ?: "",
        summary = summary ?: "",
        image = image ?: "",
        isRead = isRead
    )

fun ArticleEntity.toDetail(): ArticleDetail =
    ArticleDetail(
        id = id,
        link = link ?: "",
        title = title,
        isRead = isRead,
        content  = content ?: ""
    )