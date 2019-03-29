/*
 * Copyright 2018 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.listprefs

import com.ivianuu.list.annotations.Model

/**
 * A list preference model
 */
@Model abstract class ListPreferenceModel : DialogPreferenceModel() {
    var entries by optionalProperty<List<String>>("entries")
    var entryValues by optionalProperty<List<String>>("entryValues")
}

fun ListPreferenceModel.entries(entriesRes: Int) {
    entries(context.resources.getStringArray(entriesRes).toList())
}

fun ListPreferenceModel.entries(vararg entries: String): ListPreferenceModel {
    entries(entries.toList())
    return this
}

fun ListPreferenceModel.entryValues(entryValuesRes: Int) {
    entryValues(context.resources.getStringArray(entryValuesRes).toList())
}

fun ListPreferenceModel.entryValues(vararg entryValues: String): ListPreferenceModel {
    entryValues(entryValues.toList())
    return this
}