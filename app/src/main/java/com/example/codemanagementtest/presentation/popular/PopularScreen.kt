package com.example.codemanagementtest.presentation.popular

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.example.codemanagementtest.common.Constants
import com.example.codemanagementtest.data.remote.movie.Movie
import com.example.codemanagementtest.presentation.navigation.Screen
import com.example.codemanagementtest.ui.theme.ratingStarColor


@Composable
fun PopularScreen(
    viewModel: PopularViewModel = hiltViewModel(),
    navController: NavController
) {
    val popularList = viewModel.popular.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Top Rated") },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
        modifier = Modifier.statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues = it)
        ) {
            items(popularList) { item ->
                item?.let { popular ->
                    PopularItem(popular = popular) { navigatedItem ->
                        navController.navigate(Screen.MovieDetail.route + "/${navigatedItem.id}")
                    }
                }
            }
            popularList.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        //when first time response page is loading
                        item { CircularProgressIndicator(color = Color.DarkGray) }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            LinearProgressIndicator(
                                color = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .statusBarsPadding(),
                            )
                        }
                    }
                    loadState.refresh is LoadState.Error -> {
                        item { Text(text = "Error: Bağlantınızı kontrol ediniz!") }
                    }
                }
            }
        }
    }

}

@Composable
fun PopularItem(popular: Movie, onClick: (Movie) -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        modifier = Modifier
            .height(220.dp)
            .padding(12.dp)
            .clickable {
                onClick(popular)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            CoilImage(
                imageModel = Constants.IMAGE_URL + popular.posterPath,
                contentScale = ContentScale.Crop,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.LightGray.copy(alpha = 0.6f),
                    durationMillis = 350, dropOff = 0.65f, tilt = 20f
                ),
                circularReveal = CircularReveal(duration = 350),
                failure = { Text(text = "Image request failed!") },
                modifier = Modifier
                    .height(200.dp)
                    .width(120.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)),
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .height(200.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append(popular.originalTitle)
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    ) {
                        append(" (${popular.title}) ")
                    }
                }
                Text(text = annotatedString, style = MaterialTheme.typography.h6)
                Row {
                    Icon(
                        imageVector = Icons.Default.Star, contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = ratingStarColor
                    )
                    Text(text = "${popular.voteAverage}/10", color = Color.LightGray)
                }
                Text(
                    text = popular.overview, maxLines = 5, overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}