package com.example.vinilos.data.cache

import android.util.LruCache
import com.example.vinilos.data.model.Album
import com.example.vinilos.data.model.Artist
import com.example.vinilos.data.model.CollectedAlbumAuction
import com.example.vinilos.data.model.Collector

object CacheManager {
    private const val DEFAULT_CACHE_MAX_SIZE = 15

    object CacheKeys {
        const val ALBUMS_LIST = "albums_list"
        fun albumDetailKey(albumId: Int) = "album_detail_$albumId"

        const val ARTISTS_LIST = "artists_list"
        fun artistDetailKey(artistId: Int) = "artist_detail_$artistId"

        const val COLLECTORS_LIST = "collectors_list"
        fun collectorDetailKey(collectorId: Int) = "collector_detail_$collectorId"
        fun collectorAlbumsKey(collectorId: Int) = "collector_albums_detail_$collectorId"
    }

    private val albumsListCache = LruCache<String, List<Album>>(DEFAULT_CACHE_MAX_SIZE)
    private val albumDetailCache = LruCache<String, Album>(DEFAULT_CACHE_MAX_SIZE)

    fun getAlbumsList(): List<Album>? = albumsListCache.get(CacheKeys.ALBUMS_LIST)
    fun putAlbumsList(albums: List<Album>) { albumsListCache.put(CacheKeys.ALBUMS_LIST, albums) }
    fun getAlbumDetail(albumId: Int): Album? = albumDetailCache.get(CacheKeys.albumDetailKey(albumId))
    fun putAlbumDetail(albumId: Int, album: Album) { albumDetailCache.put(CacheKeys.albumDetailKey(albumId), album) }

    fun invalidateAlbumsListCache() { albumsListCache.remove(CacheKeys.ALBUMS_LIST) }
    fun invalidateAlbumDetailCache(albumId: Int) { albumDetailCache.remove(CacheKeys.albumDetailKey(albumId)) }

    private val artistsListCache = LruCache<String, List<Artist>>(DEFAULT_CACHE_MAX_SIZE)
    private val artistDetailCache = LruCache<String, Artist>(DEFAULT_CACHE_MAX_SIZE)

    fun getArtistsList(): List<Artist>? = artistsListCache.get(CacheKeys.ARTISTS_LIST)
    fun putArtistsList(artists: List<Artist>) { artistsListCache.put(CacheKeys.ARTISTS_LIST, artists) }
    fun getArtistDetail(artistId: Int): Artist? = artistDetailCache.get(CacheKeys.artistDetailKey(artistId))
    fun putArtistDetail(artistId: Int, artist: Artist) { artistDetailCache.put(CacheKeys.artistDetailKey(artistId), artist) }

    fun invalidateArtistsListCache() { artistsListCache.remove(CacheKeys.ARTISTS_LIST) }
    fun invalidateArtistDetailCache(artistId: Int) { artistDetailCache.remove(CacheKeys.artistDetailKey(artistId)) }

    private val collectorsListCache = LruCache<String, List<Collector>>(DEFAULT_CACHE_MAX_SIZE)
    private val collectorDetailCache = LruCache<String, Collector>(DEFAULT_CACHE_MAX_SIZE)
    private val collectorAlbumsCache = LruCache<String, List<CollectedAlbumAuction>>(DEFAULT_CACHE_MAX_SIZE)

    fun getCollectorsList(): List<Collector>? = collectorsListCache.get(CacheKeys.COLLECTORS_LIST)
    fun putCollectorsList(collectors: List<Collector>) { collectorsListCache.put(CacheKeys.COLLECTORS_LIST, collectors) }
    fun getCollectorDetail(collectorId: Int): Collector? = collectorDetailCache.get(CacheKeys.collectorDetailKey(collectorId))
    fun putCollectorDetail(collectorId: Int, collector: Collector) { collectorDetailCache.put(CacheKeys.collectorDetailKey(collectorId), collector) }
    fun getCollectorAlbums(collectorId: Int): List<CollectedAlbumAuction>? = collectorAlbumsCache.get(CacheKeys.collectorAlbumsKey(collectorId))
    fun putCollectorAlbums(collectorId: Int, albums: List<CollectedAlbumAuction>) { collectorAlbumsCache.put(CacheKeys.collectorAlbumsKey(collectorId), albums) }

    fun invalidateCollectorsListCache() { collectorsListCache.remove(CacheKeys.COLLECTORS_LIST) }
    fun invalidateCollectorDetailCache(collectorId: Int) {
        collectorDetailCache.remove(CacheKeys.collectorDetailKey(collectorId))
        collectorAlbumsCache.remove(CacheKeys.collectorAlbumsKey(collectorId))
    }
    fun invalidateCollectorAlbumsCache(collectorId: Int) { collectorAlbumsCache.remove(CacheKeys.collectorAlbumsKey(collectorId)) }

    fun clearAllCaches() {
        albumsListCache.evictAll()
        albumDetailCache.evictAll()
        artistsListCache.evictAll()
        artistDetailCache.evictAll()
        collectorsListCache.evictAll()
        collectorDetailCache.evictAll()
        collectorAlbumsCache.evictAll()
    }
}