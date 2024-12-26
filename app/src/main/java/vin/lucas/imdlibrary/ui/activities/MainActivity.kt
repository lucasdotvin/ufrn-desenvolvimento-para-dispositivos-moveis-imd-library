package vin.lucas.imdlibrary.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vin.lucas.imdlibrary.R
import vin.lucas.imdlibrary.ui.activities.books.CreateBookActivity
import vin.lucas.imdlibrary.ui.theme.IMDLibraryTheme

class MainActivity : AuthenticatedActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IMDLibraryTheme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            ),
                            title = {
                                Text("IMDLibrary")
                            },
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButtons(this)
                    },
                    content = { paddingValues ->
                        Surface(modifier = Modifier.padding(paddingValues)) {
                            EmptyState(modifier = Modifier.padding(12.dp))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FloatingActionButtons(context: Context)
{
    Box() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SmallFloatingActionButton(onClick = {
                context.startActivity(Intent(context, CreateBookActivity::class.java))
            }) {
                Icon(
                    Icons.Filled.Search,
                    stringResource(R.string.search_content_description),
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            FloatingActionButton(onClick = {
                context.startActivity(Intent(context, CreateBookActivity::class.java))
            }) {
                Icon(
                    Icons.Filled.Add,
                    stringResource(R.string.add_content_description)
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier)
{
    val dashedStroke = Stroke(width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    val shapeColor = MaterialTheme.colorScheme.inverseSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRoundRect(
                    color = shapeColor,
                    style = dashedStroke,
                    cornerRadius = CornerRadius(16f)
                )
            },
    ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = stringResource(id = R.string.empty_content_description),
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Ainda não há livros cadastrados!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inverseSurface,
            )
        }
    }
}
