package cz.tryptafunk.skatehelp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.tryptafunk.skatehelp.common.enum.Difficulty
import cz.tryptafunk.skatehelp.screens.entity.Trick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrickTableScreen(
    tricks: List<Trick>,
    onTrickClick: (Trick) -> Unit,
    onMarkDone: (Trick) -> Unit,
    onFilterChange: (String?, Difficulty?, Boolean?) -> Unit
) {
    var filterName by remember { mutableStateOf("") }
    var filterDifficulty by remember { mutableStateOf<Difficulty?>(null) }
    var filterIsDone by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trick List", style = MaterialTheme.typography.headlineMedium) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                TextField(
                    value = filterName,
                    onValueChange = {
                        filterName = it
                        onFilterChange(filterName, filterDifficulty, filterIsDone)
                    },
                    label = { Text("Filter by Name") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                Box {
                    Button(onClick = { expanded = true }) {
                        Text("Difficulty: ${filterDifficulty?.name ?: "All"}")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            {
                                Text("All")
                            },
                            onClick = {
                            filterDifficulty = null
                            expanded = false
                            onFilterChange(filterName, filterDifficulty, filterIsDone)
                        })
                        Difficulty.entries.forEach { difficulty ->
                            DropdownMenuItem(
                                {
                                    Text(difficulty.name)
                                },
                                onClick = {
                                filterDifficulty = difficulty
                                expanded = false
                                onFilterChange(filterName, filterDifficulty, filterIsDone)
                            })
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Checkbox(
                        checked = filterIsDone == true,
                        onCheckedChange = {
                            filterIsDone = if (filterIsDone == true) null else true
                            onFilterChange(filterName, filterDifficulty, filterIsDone)
                        }
                    )
                    Text(text = "Done")
                }
            }

            LazyColumn {
                items(tricks) { trick ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTrickClick(trick) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = trick.name.orEmpty(), modifier = Modifier.weight(1f))
                        Text(text = trick.difficulty?.name.orEmpty(), modifier = Modifier.weight(1f))

                        Checkbox(
                            checked = trick.isDone,
                            onCheckedChange = { newValue ->
                                onMarkDone(trick.copy(isDone = newValue))
                            }
                        )
                    }
                }
            }
        }
    }
}

