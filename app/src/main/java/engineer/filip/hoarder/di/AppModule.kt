package engineer.filip.hoarder.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import engineer.filip.hoarder.data.repository.BookmarkRepository
import engineer.filip.hoarder.data.repository.BookmarkRepositoryImpl
import javax.inject.Singleton

/**
 * Hilt module for binding interfaces to implementations.
 *
 * @Binds tells Hilt: "When someone asks for BookmarkRepository, give them BookmarkRepositoryImpl"
 * @Singleton ensures only one instance exists app-wide.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindBookmarkRepository(
        impl: BookmarkRepositoryImpl
    ): BookmarkRepository
}
