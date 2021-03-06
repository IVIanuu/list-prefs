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

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog


/**
 * A dialog preference
 */
abstract class DialogPreferenceItem<T : Any> : AbstractPreferenceItem<T>() {

    var dialogTitle by property { title }
    var dialogMessage by optionalProperty<String>()
    var dialogIcon by optionalProperty<Drawable>()
    var positiveButtonText by property { context.getString(android.R.string.ok) }
    var negativeButtonText by property { context.getString(android.R.string.cancel) }

    override fun onClick() {
        super.onClick()
        showDialog()
    }

    protected abstract fun showDialog()

    protected open fun MaterialDialog.applyDialogSettings(
        applyTitle: Boolean = true,
        applyMessage: Boolean = true,
        applyIcon: Boolean = true,
        applyPositiveButtonText: Boolean = true,
        applyNegativeButtonText: Boolean = true
    ) = apply {
        if (applyTitle) dialogTitle?.let { title(text = it) }
        if (applyMessage) dialogMessage?.let { message(text = it) }
        if (applyIcon) dialogIcon?.let { icon(drawable = it) }
        if (applyPositiveButtonText) positiveButton(text = positiveButtonText)
        if (applyNegativeButtonText) negativeButton(text = negativeButtonText)
    }

}

fun DialogPreferenceItem<*>.dialogTitle(dialogTitleRes: Int) {
    dialogTitle = context.getString(dialogTitleRes)
}

fun DialogPreferenceItem<*>.dialogMessage(dialogMessageRes: Int) {
    dialogTitle = context.getString(dialogMessageRes)
}

fun DialogPreferenceItem<*>.dialogIcon(dialogIconRes: Int) {
    dialogIcon = ContextCompat.getDrawable(context, dialogIconRes)
}

fun DialogPreferenceItem<*>.positiveButtonText(positiveButtonTextRes: Int) {
    positiveButtonText = context.getString(positiveButtonTextRes)
}

fun DialogPreferenceItem<*>.negativeButtonText(negativeButtonTextRes: Int) {
    negativeButtonText = context.getString(negativeButtonTextRes)
}