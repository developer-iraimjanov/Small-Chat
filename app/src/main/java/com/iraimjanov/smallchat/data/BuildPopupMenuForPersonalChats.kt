package com.iraimjanov.smallchat.data

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.iraimjanov.smallchat.R
import com.iraimjanov.smallchat.data.PublicData.profile
import com.iraimjanov.smallchat.databinding.FragmentChatBinding
import com.iraimjanov.smallchat.models.Messages


class BuildPopupMenuForPersonalChats(
    val requireActivity: FragmentActivity,
    val binding: FragmentChatBinding,
) {
    private var booleanAntiBagPopupMenu = true
    private val realReference = Firebase.database.getReference("chats")

    @SuppressLint("RestrictedApi")
    fun forChats(messages: Messages, view: View) {
        if (booleanAntiBagPopupMenu) {
            val menuBuilder = MenuBuilder(requireActivity)
            val menuInflater = MenuInflater(requireActivity)
            menuInflater.inflate(R.menu.popup_menu, menuBuilder)
            val menuPopupHelper = MenuPopupHelper(requireActivity, menuBuilder, view)
            menuPopupHelper.setForceShowIcon(true)
            menuBuilder.setCallback(object : MenuBuilder.Callback {
                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.menu_edit -> {
                            if (messages.from == profile.uid) {
                                showEditingMode(messages)
                                binding.imageCancelEditing.setOnClickListener {
                                    showSentMode()
                                }
                            }
                        }

                        R.id.menu_delete -> {
                            realReference.child(messages.uid).removeValue()
                        }
                    }
                    return true
                }

                override fun onMenuModeChange(menu: MenuBuilder) {}
            })

            menuPopupHelper.setOnDismissListener {
                booleanAntiBagPopupMenu = true
            }

            menuPopupHelper.show()
        }
    }

    fun showSentMode() {
        binding.edtMessage.text.clear()
        binding.imageSend.setImageResource(R.drawable.ic_send)
        binding.lyTextEditing.visibility = View.GONE
    }

    private fun showEditingMode(messages: Messages) {
        binding.edtMessage.setText(messages.messages)
        binding.edtMessage.setSelection(messages.messages.length)
        binding.imageSend.setImageResource(R.drawable.ic_edit)
        binding.tvEditingText.text = messages.messages
        binding.lyTextEditing.visibility = View.VISIBLE
        showKeyboardFrom(requireActivity, binding.edtMessage)
    }

    private fun showKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }
}