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

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.ivianuu.list.Item
import com.ivianuu.list.common.KotlinHolder
import kotlinx.android.synthetic.main.item_preference.*

/**
 * Base preference
 */
abstract class AbstractPreferenceItem<T : Any> : Item<AbstractPreferenceItem.Holder>() {

    var context by requiredProperty<Context>(doHash = false)

    var key by idProperty<String>()

    var title by optionalProperty<String>()
    var summary by optionalProperty<String>()
    var icon by optionalProperty<Drawable>()
    var preserveIconSpace by property { false }
    var defaultValue by optionalProperty<T?>()
    var enabled by property { true }
    var clickable by property { true }

    var dependencies by property { mutableListOf<Dependency>() }
    val allowedByDependencies by property {
        dependencies.all { it.isOk(sharedPreferences) }
    }

    var sharedPreferences: SharedPreferences by property(doHash = false) {
        if (sharedPreferencesName != null) {
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        } else {
            ListPrefsPlugins.getDefaultSharedPreferences(context)
        }
    }
    var sharedPreferencesName by optionalProperty<String>("sharedPreferencesName")
    var useCommit by property("useCommit") { false }
    var persistent by property("persistent") { true }
    var widgetLayoutRes by property("widgetLayoutRes") { 0 }
    val value by property("value") {
        if (persistent) {
            sharedPreferences.all[key] as? T ?: defaultValue
        } else if (!persistent) {
            defaultValue
        } else {
            null
        }
    }

    val onClick = PrefClicks()
    val onChange = PrefChanges<T>()

    final override var layoutRes = R.layout.item_preference

    final override val viewType: Int
        get() = layoutRes + widgetLayoutRes

    protected val viewsShouldBeEnabled: Boolean get() = enabled && allowedByDependencies

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.title?.let {
            it.text = title
            it.visibility = if (title != null) View.VISIBLE else View.GONE
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.summary?.let {
            it.text = summary
            it.visibility = if (summary != null) View.VISIBLE else View.GONE
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.icon?.let {
            it.setImageDrawable(icon)
            it.isEnabled = viewsShouldBeEnabled
        }

        holder.icon_frame?.let {
            it.visibility = if (icon != null || preserveIconSpace) View.VISIBLE else View.GONE
        }

        holder.widget_frame?.let { widgetFrame ->
            widgetFrame.isEnabled = viewsShouldBeEnabled
            (0 until widgetFrame.childCount)
                .map { widgetFrame.getChildAt(it) }
                .forEach { it.isEnabled = viewsShouldBeEnabled }
        }

        holder.containerView.apply {
            isEnabled = viewsShouldBeEnabled
            isClickable = clickable

            if (clickable) {
                setOnClickListener {
                    val handled = onClick.callback?.invoke() ?: false
                    if (!handled) {
                        onClick()
                    }
                }
            } else {
                setOnClickListener(null)
            }
        }
    }

    override fun createHolder(): Holder = Holder()

    override fun buildView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutRes, parent, false)

        val widgetFrame = view.findViewById<ViewGroup>(android.R.id.widget_frame)

        if (widgetFrame != null) {
            if (widgetLayoutRes != 0) {
                inflater.inflate(widgetLayoutRes, widgetFrame)
            } else {
                widgetFrame.visibility = View.GONE
            }
        }

        return view
    }

    protected open fun onClick() {
    }

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("ApplySharedPref")
    protected fun persistValue(value: T) {
        if (onChange.callback?.invoke(value) != false && persistent) {
            sharedPreferences.edit().apply {
                when (value) {
                    is Boolean -> putBoolean(key, value)
                    is Float -> putFloat(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is String -> putString(key, value)
                    is Set<*> -> putStringSet(key, value as Set<String>)
                }

                if (useCommit) {
                    commit()
                } else {
                    apply()
                }
            }
        }
    }

    open class Holder : KotlinHolder()

    data class Dependency(
        val key: String,
        val value: Any?,
        val defaultValue: Any?
    ) {
        fun isOk(sharedPreferences: SharedPreferences): Boolean {
            return (sharedPreferences.all[key]
                ?: defaultValue
                ?: value.tryToResolveDefaultValue()) == value
        }
    }

}

fun AbstractPreferenceItem<*>.key(keyRes: Int) {
    key = context.getString(keyRes)
}

fun AbstractPreferenceItem<*>.title(titleRes: Int) {
    title = context.getString(titleRes)
}

fun AbstractPreferenceItem<*>.summary(summaryRes: Int) {
    summary = context.getString(summaryRes)
}

fun AbstractPreferenceItem<*>.icon(iconRes: Int) {
    icon = ContextCompat.getDrawable(context, iconRes)
}

fun AbstractPreferenceItem<*>.dependency(
    key: String,
    value: Any?,
    defaultValue: Any? = null
) {
    dependency(AbstractPreferenceItem.Dependency(key, value, defaultValue))
}

fun AbstractPreferenceItem<*>.dependency(dependency: AbstractPreferenceItem.Dependency) {
    dependencies.add(dependency)
}

fun AbstractPreferenceItem<*>.onClickIntent(intent: () -> Intent) {
    onClick {
        context.startActivity(intent())
        return@onClick true
    }
}

fun AbstractPreferenceItem<*>.onClickUrl(url: () -> String) {
    onClickIntent {
        Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url()) }
    }
}