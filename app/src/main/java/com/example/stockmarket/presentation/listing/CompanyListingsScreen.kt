package com.example.stockmarket.presentation.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmarket.presentation.listing.components.CompanyItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val swipeRefresh = rememberSwipeRefreshState(
        isRefreshing = state.isRefreshing
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(event = CompanyListingsEvent.OnSearchQueryChange(query = it))
            },
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
        )
        SwipeRefresh(
            state = swipeRefresh,
            onRefresh = {
                viewModel.onEvent(event = CompanyListingsEvent.Refresh)
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.companies.size) { index ->
                    val company = state.companies[index]
                    CompanyItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            }
                            .padding(16.dp),
                        company = company
                    )
                    if (index < state.companies.size) {
                        Divider(
                            modifier = Modifier
                                .padding(
                                    horizontal = 16.dp
                                )
                        )
                    }
                }
            }
        }
    }
}