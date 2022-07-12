package de.mreuter.smork.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.ui.theme.FreelancerTheme
import de.mreuter.smork.ui.theme.Typography

@Composable
fun BasicListItem(topic: String? = null, description: String? = null) {
    Column {
        if (topic != null)
            Text(text = topic, style = MaterialTheme.typography.subtitle1)
        if (description != null)
            Text(
                text = description,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(start = 1.dp, top = 4.dp)
            )
    }
}

@Composable
fun ClickableListItem(topic: String? = null, subtitle: String? = null, action: () -> Unit) {
    ClickableRow(onClick = action){
        Column {
            if (topic != null)
                Text(text = topic, style = MaterialTheme.typography.subtitle1)
            if (subtitle != null)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier
                        .padding(start = 1.dp, top = 4.dp)
                )
        }
    }
}

@Preview
@Composable
fun ListPreview(){
    FreelancerTheme {

    }
}