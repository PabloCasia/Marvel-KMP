package model.mappers

import dto.CharacterDto
import dto.ComicDto
import dto.MarvelImageDto
import io.ktor.http.Url
import isAndroid
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.Character
import model.Comic


fun ComicDto.toComic(): Comic {
    val date = buildDate()
    return Comic(
        id = id,
        title = title,
        date = date,
        thumbnail = thumbnail.toUrl(),
    )
}

private fun ComicDto.buildDate(): String? {
    val onSaleDate = dates?.find {
        it.type == "onsaleDate"
    }
    val date = onSaleDate?.date?.let {
        it.substring(0, it.length - 2)
    }
    return date?.let {
        val localDateTime = Instant.parse(it).toLocalDateTime(TimeZone.currentSystemDefault())
        "${localDateTime.dayOfMonth} ${localDateTime.month.name} ${localDateTime.year}"
    }
}

fun CharacterDto.toCharacter() = Character(
    id = id,
    name = name,
    description = description,
    thumbnail = thumbnail.toUrl(),
    comicsSize = comics?.items?.size ?: 0,
)

fun MarvelImageDto.toUrl(): Url {
    return if (isAndroid()) {
        Url("https:${path.split(':').last()}.$extension")
    } else {
        Url("${path}.${extension}")
    }
}
