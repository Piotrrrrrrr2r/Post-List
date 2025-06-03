package com.example.postlist.screens.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postlist.ErrorContent
import com.example.postlist.FullScreenLoader
import com.example.postlist.data.api.ApiClient
import com.example.postlist.data.model.ToDo
import com.example.postlist.data.model.User
import com.example.postlist.data.repository.PostRepositoryImpl
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.draw.clip
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    userId: Int,
    onBackClick: () -> Unit,
    viewModel: UserScreenViewModel = viewModel(
        factory = UserScreenViewModelFactory.provideFactory(
            repository = PostRepositoryImpl(ApiClient.instance),
            userId = userId
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val scrollState = rememberScrollState()
    var isTasksExpanded by remember { mutableStateOf(false) }


    val context = LocalContext.current
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "User Details",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when {
                    uiState.isLoading -> FullScreenLoader()
                    uiState.error != null -> ErrorContent(
                        message = uiState.error,
                        onRetry = { viewModel.loadUserData() }
                    )
                    user != null -> {
                        UserDetailsCard(
                            user = user,
                            postCount = uiState.posts.size
                        )

                        val lat = user.address.geo.lat.toDoubleOrNull() ?: 0.0
                        val lng = user.address.geo.lng.toDoubleOrNull() ?: 0.0

                        if (lat != 0.0 || lng != 0.0) {
                            Text(
                                "Location",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            UserLocationMap(
                                lat = lat,
                                lng = lng,
                                userName = user.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                if (uiState.todos.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isTasksExpanded = !isTasksExpanded },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tasks (${uiState.todos.size})",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (isTasksExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isTasksExpanded) "Collapse Tasks" else "Expand Tasks",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    AnimatedVisibility(
                        visible = isTasksExpanded,
                        enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                        exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            uiState.todos.forEach { todo ->
                                TodoItem(todo = todo)
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserDetailsCard(user: User, postCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = user.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "@${user.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )


        DetailRow(
            label = "Posts",
            value = postCount.toString(),

        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        SectionTitle(title = "Contact")
        DetailRow(label = "Email", value = user.email)
        DetailRow(label = "Phone", value = user.phone)
        DetailRow(label = "Website", value = user.website)

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        SectionTitle(title = "Company")
        DetailRow(label = "Name", value = user.company.name)

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        SectionTitle(title = "Address")
        DetailRow(label = "Street", value = "${user.address.street}, ${user.address.suite}")
        DetailRow(label = "City", value = user.address.city)
        DetailRow(label = "Zipcode", value = user.address.zipcode)
    }
}

@Composable
fun UserLocationMap(
    lat: Double,
    lng: Double,
    userName: String,
    modifier: Modifier = Modifier,
    zoomLevel: Double = 4.0
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
    ) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    minZoomLevel = 3.0
                    maxZoomLevel = 19.0
                    controller.setZoom(zoomLevel)
                    controller.setCenter(GeoPoint(lat, lng))


                    val marker = Marker(this).apply {
                        position = GeoPoint(lat, lng)
                        title = userName
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }
                    overlays.add(marker)
                }
            },
            update = { mapView ->
                mapView.invalidate()
            }
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(90.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun TodoItem(todo: ToDo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (todo.completed) Icons.Default.CheckCircle else Icons.Default.Clear,
            contentDescription = if (todo.completed) "Completed" else "Not completed",
            tint = if (todo.completed) Color.Green else MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = todo.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

