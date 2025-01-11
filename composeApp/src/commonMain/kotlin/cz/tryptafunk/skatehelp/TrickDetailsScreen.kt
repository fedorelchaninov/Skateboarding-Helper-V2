package cz.tryptafunk.skatehelp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.tryptafunk.skatehelp.screens.entity.Trick

@Composable
fun TrickDetailsScreen(trick: Trick, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = trick.name.orEmpty(), style = MaterialTheme.typography.headlineMedium)
        Text(text = trick.description.orEmpty(), style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Open YouTube link */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Watch Guide")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text(text = "Back")
        }
    }
}
