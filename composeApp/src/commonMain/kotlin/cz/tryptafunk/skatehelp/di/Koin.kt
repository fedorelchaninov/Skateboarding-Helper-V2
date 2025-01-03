package cz.tryptafunk.skatehelp.di

import cz.tryptafunk.skatehelp.data.InMemoryMuseumStorage
import cz.tryptafunk.skatehelp.data.KtorMuseumApi
import cz.tryptafunk.skatehelp.data.MuseumApi
import cz.tryptafunk.skatehelp.data.MuseumRepository
import cz.tryptafunk.skatehelp.data.MuseumStorage
import cz.tryptafunk.skatehelp.screens.detail.DetailViewModel
import cz.tryptafunk.skatehelp.screens.list.ListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<MuseumApi> { KtorMuseumApi(get()) }
    single<MuseumStorage> { InMemoryMuseumStorage() }
    single {
        MuseumRepository(get(), get()).apply {
            initialize()
        }
    }
}

val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
