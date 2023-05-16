/*
 * Copyright 2022 The Android Open Source Project
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

package androidx.glance.action

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositeKeyHash

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class LambdaAction(
    val key: String,
    val block: () -> Unit,
) : Action {
    override fun toString() = "LambdaAction($key, ${block.hashCode()})"
}

/**
 * Create an [Action] that runs [block] when triggered.
 *
 * @param key An optional key to be used as a key for the action. If not provided we use the key
 * that is automatically generated by the Compose runtime which is unique for every exact code
 * location in the composition tree.
 * @param block the function to be run when this action is triggered.
 */
@Composable
fun action(
    key: String? = null,
    block: () -> Unit,
): Action {
    val finalKey = if (!key.isNullOrEmpty()) key else currentCompositeKeyHash.toString()
    return LambdaAction(finalKey, block)
}