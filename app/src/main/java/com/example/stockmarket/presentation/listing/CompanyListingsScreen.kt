package com.example.stockmarket.presentation.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmarket.presentation.destinations.CompanyInfoScreenDestination
import com.example.stockmarket.presentation.listing.components.CompanyItem
import com.example.stockmarket.ui.theme.StockMarketTheme
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

    CompanyListingsRender(
        state = state,
        event = {
            viewModel.onEvent(it)
        },
        onClick = {
            navigator.navigate(
                CompanyInfoScreenDestination(symbol = it)
            )
        }
    )
}

@Composable
private fun CompanyListingsRender(
    state: CompanyListingsState,
    event: (CompanyListingsEvent) -> Unit,
    onClick: (String) -> Unit
) {
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
                event(CompanyListingsEvent.OnSearchQueryChange(query = it))
            },
            placeholder = {
                Text(
                    text = "Search...",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        SwipeRefresh(
            state = swipeRefresh,
            onRefresh = {
                event(CompanyListingsEvent.Refresh)
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
                                onClick(company.symbol)
                            }
                            .padding(16.dp),
                        company = company
                    )
                    if (index < state.companies.size) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun CompanyListingsRenderPreview() {
    StockMarketTheme {
        CompanyListingsRender(
            state = CompanyListingsState(
                companies = getMockCompanyListings(),
            ),
            event = {},
            onClick = {}
        )
    }
}