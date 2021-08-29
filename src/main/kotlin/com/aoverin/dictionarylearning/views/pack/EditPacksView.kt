package com.aoverin.dictionarylearning.views.pack

import com.aoverin.dictionarylearning.components.combobox.getComboBoxForItems
import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.models.WordsPack
import com.aoverin.dictionarylearning.services.WordsPackService
import com.aoverin.dictionarylearning.services.WordsService
import com.aoverin.dictionarylearning.views.MainLayout
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@PageTitle("Edit Packs")
@Route(value = "edit-packs", layout = MainLayout::class)
@RolesAllowed("admin")
class EditPacksView(
    private val wordsPackService: WordsPackService,
    private val wordsService: WordsService
) : VerticalLayout() {

    private val wordsList: List<HorizontalLayout> = mutableListOf()
    private val langLabel1 = Label("Lang one")
        .apply {
            isEnabled = false
            isVisible = false
        }
    private val langLabel2 = Label("Lang two")
        .apply {
            isEnabled = false
            isVisible = false
        }

    private val packBox = getComboBoxForItems("Packs", wordsPackService.getAllPack())
        .apply {
            setItemLabelGenerator { it.name }
            addValueChangeListener {
                initObjects()
                updateLabels(it.value) }
        }

    private val horizontalLayout: HorizontalLayout = HorizontalLayout()
        .apply {
            add(langLabel1)
            add(langLabel2)
        }

    private val addButton: Button = initAddWordsButton()

    init {
        initObjects()
    }

    private fun updateLabels(pack: WordsPack) {
        langLabel1.text = pack.langOne
        langLabel1.isVisible = true
        langLabel2.text = pack.langTwo
        langLabel2.isVisible = true
        addButton.isEnabled = true
        addButton.isVisible = true
        val wordsPack = wordsPackService.getWordsForPackId(pack.id)
        wordsPack.words.forEach {
            addComponentAtIndex(
                componentCount.minus(1),
                HorizontalLayout()
                    .apply {
                        add(Label(it.first.value), Label(it.second.value), getDeleteButton(pack, it.first, it.second))
                    }
            )
        }
    }

    private fun getDeleteButton(pack: WordsPack, word1: Word, word2: Word): Button {
        return Button("Delete")
            .apply {
                addClickListener { wordsPackService.removeWordsFromPack(pack.id, word1.id, word2.id) }
            }
    }

    private fun getAddButton(pack: WordsPack, word1: ComboBox<Word>, word2: ComboBox<Word>): Button {
        return Button("Add")
            .apply {
                addClickListener { wordsPackService.addWordsToPack(pack.id, word1.value.id, word2.value.id) }
            }
    }

    private fun initObjects() {
        removeAll()
        add(packBox)
        add(horizontalLayout)
        add(addButton
        )
    }

    private fun initAddWordsButton(): Button {
        return Button("Add")
            .apply {
                isDisableOnClick = true
                isEnabled = false
                isVisible = false
                addClickListener {
                    addComponentAtIndex(
                        componentCount.minus(1),
                        HorizontalLayout()
                            .apply {
                                val combo1 = getComboBoxForItems("Word", wordsService.getByLang(packBox.value.langOne))
                                    .apply {
                                        setItemLabelGenerator { it.value }
                                    }
                                val combo2 = getComboBoxForItems("Word", wordsService.getByLang(packBox.value.langTwo))
                                    .apply {
                                        setItemLabelGenerator { it.value }
                                    }
                                this.add(
                                    combo1,
                                    combo2,
                                    getAddButton(packBox.value, combo1, combo2)
                                )
                            }
                    )
                    this.isEnabled = true
                }
            }
    }
}