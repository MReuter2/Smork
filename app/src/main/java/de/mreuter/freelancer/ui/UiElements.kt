package de.mreuter.freelancer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector

class ExposedDropMenuStateHolder <T> (val dropDownItems: List<T>) {
    var enabled by mutableStateOf(false)
    var value by mutableStateOf("")
    var selectedIndex by mutableStateOf(-1)
    val selectedItem: T?
        get() = if(selectedIndex == -1) null else dropDownItems[selectedIndex]
    var size by mutableStateOf(Size.Zero)

    val icon: ImageVector
    @Composable get() = if(!enabled){
        Icons.Outlined.KeyboardArrowDown
    }else{
        Icons.Outlined.KeyboardArrowUp
    }

    val items = dropDownItems.map{
        "$it"
    }

    fun onEnabled(newValue: Boolean){
        enabled = newValue
    }
    fun onSize(newValue: Size){
        size = newValue
    }
    fun onSelectedIndex(newValue: Int){
        selectedIndex = newValue
        value = items[selectedIndex]
    }
}

@Composable
fun rememberExposedMenuStateHolder(dropDownItems: List<Any>) = remember(){
    ExposedDropMenuStateHolder(dropDownItems)
}