package de.mreuter.freelancer.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import de.mreuter.freelancer.ui.theme.Typography

@Composable
fun BasicListItem(topic: String? = null, description: String? = null) {
    Column {
        if(topic != null)
            Text(text = topic, style = Typography.subtitle1)
        Spacer(modifier = Modifier.padding(2.dp))
        if(description != null)
            Text(text = description, style = Typography.subtitle2)
        Spacer(modifier = Modifier.padding(0.dp))
    }
}

@Composable
fun ClickableListItem(topic: String? = null, clickableText: String? = null, action: (Int) -> Unit){
    Column {
        if(topic != null)
            Text(text = topic, style = Typography.subtitle1)
        Spacer(modifier = Modifier.padding(0.dp))
        if(clickableText != null)
            ClickableText(
                text = AnnotatedString(clickableText),
                style = Typography.subtitle2,
                modifier = Modifier
                    .padding(horizontal = 1.dp, vertical = 4.dp),
                onClick = action
            )
    }
}