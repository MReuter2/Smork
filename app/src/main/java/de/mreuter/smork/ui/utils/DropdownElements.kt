package de.mreuter.smork.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.theme.*

class DropDown <T : Any>(val dropDownItems: List<T>) {
    var exposedMenuStateHolder: ExposedDropMenuStateHolder<Any> =
        ExposedDropMenuStateHolder(dropDownItems)

    @Composable
    fun DropDownTextfield(label: String, preselectedItem: T? = null, isError: Boolean = false) {
        exposedMenuStateHolder = rememberExposedMenuStateHolder(dropDownItems)
        if(preselectedItem != null && dropDownItems.contains(preselectedItem) && exposedMenuStateHolder.selectedItem == null)
            exposedMenuStateHolder.onSelectedIndex(dropDownItems.indexOf(preselectedItem))

        Column{
            OutlinedTextField(
                value = exposedMenuStateHolder.value,
                onValueChange = {},
                label = { Column(verticalArrangement = Arrangement.Center, modifier = Modifier.height(24.dp)){ Text(text = label) } },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = exposedMenuStateHolder.icon,
                        contentDescription = null,
                        Modifier.clickable {
                            exposedMenuStateHolder.onEnabled(!(exposedMenuStateHolder.enabled))
                        },
                        tint = MaterialTheme.colors.onSurface
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .onGloballyPositioned { exposedMenuStateHolder.onSize(it.size.toSize()) },
                colors = defaultTextFieldColors(),
                isError = isError
            )
            DropdownMenu(
                expanded = exposedMenuStateHolder.enabled,
                onDismissRequest = { exposedMenuStateHolder.onEnabled(false) },
                modifier = Modifier
                    .width(with(LocalDensity.current) { exposedMenuStateHolder.size.width.toDp() }),
            ) {
                exposedMenuStateHolder.items.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        onClick = {
                            exposedMenuStateHolder.onSelectedIndex(index)
                            exposedMenuStateHolder.onEnabled(false)
                        }
                    ) {
                        Text(text = s, style = MaterialTheme.typography.subtitle1)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DropDownPreview(){
    SmorkTheme {
        DropDown(exampleProjects).DropDownTextfield(label = "Projects")
    }
}