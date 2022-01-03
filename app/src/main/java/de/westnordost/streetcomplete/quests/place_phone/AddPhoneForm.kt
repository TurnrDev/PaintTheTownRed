package de.westnordost.streetcomplete.quests.place_phone

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.databinding.QuestPhoneBinding
import de.westnordost.streetcomplete.quests.AbstractQuestFormAnswerFragment
import de.westnordost.streetcomplete.quests.AnswerItem
import de.westnordost.streetcomplete.util.TextChangedWatcher


class AddPhoneForm : AbstractQuestFormAnswerFragment<PlacePhoneAnswer>() {

    override val contentLayoutResId = R.layout.quest_phone
    private val binding by contentViewBinding(QuestPhoneBinding::bind)

    override val otherAnswers = listOf(
        AnswerItem(R.string.quest_generic_answer_noSign) { confirmNoPhone() }
    )


    val phone get() = binding.phoneInput?.text?.toString().orEmpty().trim()

    // Convert phone number to international format
    private val phoneNumberWatcher = TextChangedWatcher {
        val phoneNumber = phone
        if (phoneNumber.length == 10) {
            binding.phoneInput.setText(phoneNumber.replaceRange(3, 6, " "))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phoneInput.addTextChangedListener(TextChangedWatcher { checkIsFormComplete() })
        binding.phoneInput.addTextChangedListener(phoneNumberWatcher)
    }

    override fun onClickOk() {
        applyAnswer(PlacePhone(phone))
    }

    private fun confirmNoPhone() {
        val ctx = context ?: return
        AlertDialog.Builder(ctx)
            .setTitle(R.string.quest_generic_confirmation_title)
            .setPositiveButton(R.string.quest_generic_confirmation_yes) { _, _ -> applyAnswer(NoPlacePhoneSign) }
            .setNegativeButton(R.string.quest_generic_confirmation_no, null)
            .show()
    }

    override fun isFormComplete() = phone.isNotEmpty()
}
