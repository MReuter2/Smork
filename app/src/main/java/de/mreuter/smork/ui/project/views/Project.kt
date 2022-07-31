package de.mreuter.smork.ui.project.views

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Date
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.utils.*
import de.mreuter.smork.ui.theme.SmorkTheme


@Composable
fun Project(
    project: Project,
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {},
    navigateToEditView: (Project) -> Unit = {},
    onProjectUpdate: (Project) -> Unit = {}
) {
    val openStartDateDialog = remember { mutableStateOf(false) }
    val openFinishDateDialog = remember { mutableStateOf(false) }

    if (openStartDateDialog.value || openFinishDateDialog.value) {
        if (openStartDateDialog.value)
            DatePicker(
                { project.startDate = Date(it) },
                { openStartDateDialog.value = !openStartDateDialog.value })
        else
            DatePicker(
                { project.finishDate = Date(it) },
                { openFinishDateDialog.value = !openFinishDateDialog.value })
    }
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Project",
        backNavigation = {
            onProjectUpdate(project)
            backNavigation() },
        trailingAppBarIcons = {
            IconButton(
                onClick = { navigateToEditView(project) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_edit_24),
                    contentDescription = null
                )
            }
        }
    ) {
        BasicLazyColumn {
            BasicCard {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = project.client.toString(),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(1.dp)
                )
            }
            ExpandableCard(title = "Time period") {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Start date", style = MaterialTheme.typography.subtitle1)
                    ClickableText(
                        text = AnnotatedString(project.startDate?.toString() ?: "Select date"),
                        style = MaterialTheme.typography.subtitle1
                    ) {
                        openStartDateDialog.value = !openStartDateDialog.value
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "End date", style = MaterialTheme.typography.subtitle1)
                    ClickableText(
                        text = AnnotatedString(project.finishDate?.toString() ?: "Select date"),
                        style = MaterialTheme.typography.subtitle1
                    ) {
                        openFinishDateDialog.value = !openFinishDateDialog.value
                    }
                }
            }

            ExpandableCard(title = "Tasks") { TaskListWithCheckbox(tasks = project.tasks) }

            ExpandableCard(title = "Images") {
                val context = LocalContext.current
                val imageData: MutableState<Uri?> = remember { mutableStateOf(null) }
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { imageData.value = it }
                )
                /*TODO: ImageView*/
                imageData.let {
                    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                    val uri = it.value
                    if (uri != null) {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value =
                                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

                        bitmap.value?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))
        }
    }
}

@Preview
@Composable
fun PreviewProject() {
    SmorkTheme {
        Project(
            project = exampleProjects[0],
            bottomBar = { BottomNavigationBar() },
        )
    }
}