package com.example.codemanagementtest.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.codemanagementtest.data.paging.MoviePagingSource
import com.example.codemanagementtest.data.remote.movie.Movie
import com.example.codemanagementtest.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    val popular: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { MoviePagingSource(networkRepository, MoviePagingSource.Source.Popular) }
    ).flow.cachedIn(viewModelScope)


}