package com.aoverin.dictionarylearning.views.addwords

import com.aoverin.dictionarylearning.components.combobox.getComboBoxForItems
import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.services.WordsService
import com.aoverin.dictionarylearning.views.MainLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@PageTitle("Add Words")
@Route(value = "add-words", layout = MainLayout::class)
@RolesAllowed("admin")
class AddWords(
    private val wordsService: WordsService
) : VerticalLayout() {

    init {
        add(Label("Add new word"))
        add(getLayoutToAddWord())
        add(Label("Connect two words"))
        add(getLayoutToConnectWords())
        add(Label("Add and connect two words"))
        add(getLayoutToAddAndConnectWords())
    }

    private fun getLayoutToAddAndConnectWords(): HorizontalLayout {
        val word1= TextField("Word")
        val lang1 = getComboBoxForItems("Language", wordsService.getLanguages())
        val word2 = TextField("Word")
        val lang2 = getComboBoxForItems("Language", wordsService.getLanguages())
        return HorizontalLayout()
            .apply {
                add(lang1)
                add(word1)
                add(Button("Add and Connect")
                    .apply {
                        addClickListener { addAndConnect(lang1.value, word1.value, lang2.value, word2.value) }
                        word1.clear()
                        word2.clear()
                    }
                )
                add(lang2)
                add(word2)
                for (i in 0 until componentCount) {
                    setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, getComponentAt(i))
                }
            }
    }

    private fun getLayoutToConnectWords(): HorizontalLayout {
        val word1 = getComboBoxForItems("Word", emptyList<Word>())
            .apply { setItemLabelGenerator { it.value } }
        val word2 = getComboBoxForItems("Word", emptyList<Word>())
            .apply { setItemLabelGenerator { it.value } }
        val langComboBox1 = getComboBoxForItems("Language", wordsService.getLanguages())
            .apply {
                addValueChangeListener {
                    word1.setItems(wordsService.getByLang(it.value))
                }
            }
        val langComboBox2 = getComboBoxForItems("Language", wordsService.getLanguages())
            .apply {
                addValueChangeListener {
                    word2.setItems(wordsService.getByLang(it.value))
                }
            }
        return HorizontalLayout()
            .apply {
                add(langComboBox1)
                add(word1)
                add(Button("Connect")
                    .apply {
                        addClickListener {
                            connectWords(word1.value, word2.value)
                            word1.clear()
                            word2.clear()
                        }
                    }
                )
                add(langComboBox2)
                add(word2)
                for (i in 0 until componentCount) {
                    setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, getComponentAt(i))
                }
            }
    }

    private fun getLayoutToAddWord(): HorizontalLayout {
        val wordsField = TextField("Word")
        val langComboBox = getComboBoxForItems("Language", wordsService.getLanguages())
        return HorizontalLayout()
            .apply {
                add(wordsField)
                add(langComboBox)
                add(Button("Add")
                    .apply {
                        addClickListener {
                            wordsService.add(wordsField.value, langComboBox.value)
                            wordsField.clear()
                        }
                    }
                )
                for (i in 0 until componentCount) {
                    setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, getComponentAt(i))
                }
            }
    }

    private fun addAndConnect(lang1: String, value1: String, lang2: String, value2: String) {
        if (lang1 == lang2) {
            Notification.show("Can't add and connect words")
        } else {
            wordsService.addAndConnectWords(lang1, value1, lang2, value2)
            Notification.show("Added and connected")
        }
    }

    private fun connectWords(wordsToConnect1: Word, wordsToConnect2: Word) {
        if (wordsToConnect1.lang == wordsToConnect2.lang) {
            Notification.show("Can't connect words")
        } else {
            wordsService.connectWords(wordsToConnect1.id, wordsToConnect2.id)
            Notification.show("Connected")
        }
    }
}