package model.mappers

import dto.CharacterDto
import dto.ComicDto
import dto.MarvelImageDto
import io.ktor.http.Url
import isAndroid
import model.Character
import model.Comic

fun ComicDto.toComic() = Comic(
    id = id,
    title = title,
    description = description,
    isbn = isbn,
    thumbnail = thumbnail.toUrl(),
    images = images.map(MarvelImageDto::toUrl),
)

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
