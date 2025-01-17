/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.material.icons.samples

import androidx.annotation.Sampled
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Sampled
@Composable
@Suppress("LocalVariableName")
fun AppIcons() {
    val MyAppIcons = Icons.Rounded
    SomeComposable(icon = MyAppIcons.Menu)
}

@Sampled
@Composable
fun DrawIcon() {
    Icon(Icons.Rounded.Menu, contentDescription = "Localized description")
}

@Sampled
@Composable
fun AutoMirroredIcon() {
    // This icon will be mirrored when the LayoutDirection is Rtl.
    Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = "Localized description")
}

@Composable
private fun SomeComposable(icon: ImageVector) {
    Box(Modifier.paint(rememberVectorPainter(icon)))
}
