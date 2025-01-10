package com.cengizhanerturan.kotlinnewsapplication.core.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.cengizhanerturan.kotlinnewsapplication.databinding.ErrorDialogBinding
import com.cengizhanerturan.kotlinnewsapplication.databinding.LogoutDialogBinding
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.LOGOUT_DIALOG_TITLE

object DialogUtils {
    fun showDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        message: String,
        onClick: (() -> Unit)? = null
    ) {
        val binding = ErrorDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                (context.resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = attributes?.apply {
                dimAmount = 0.1f
            }
        }

        binding.message.text = message

        binding.btnOK.setOnClickListener {
            onClick?.let {
                it()
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showLogoutDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        onClick: (() -> Unit)? = null
    ) {
        val binding = LogoutDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                (context.resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            attributes = attributes?.apply {
                dimAmount = 0.1f
            }
        }

        binding.message.text = LOGOUT_DIALOG_TITLE

        binding.btnOK.setOnClickListener {
            onClick?.let {
                it()
            }
            dialog.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}