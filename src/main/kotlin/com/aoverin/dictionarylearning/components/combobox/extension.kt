package com.aoverin.dictionarylearning.components.combobox

import com.vaadin.flow.component.combobox.ComboBox

fun <T> getComboBoxForItems(label: String, items: List<T>): ComboBox<T> {
    return ComboBox<T>(label)
        .apply{
            setItems(items)
        }
}