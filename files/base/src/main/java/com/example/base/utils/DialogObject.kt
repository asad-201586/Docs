package com.example.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.base.R
import com.example.base.databinding.DialogFailedBinding
import com.example.base.databinding.DialogSuccessBinding
import com.example.base.databinding.DialogWarningBinding

@SuppressLint("InflateParams")
fun Context.showWarningDialog(title: String = "", message: String = "", function: () -> Unit)  {

    val dialog = Dialog(this)
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_warning, null, false)
    val binding = DialogWarningBinding.bind(view)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    if (title.isNotEmpty()) binding.textTitle.text = title
    if (message.isNotEmpty()) binding.textMessage.text = message

    binding.btnCancel onClick { dialog.dismiss() }

    binding.btnOk onClick {
        dialog.dismiss()
        function.invoke()
    }

    dialog.show()
}


@SuppressLint("InflateParams")
fun Context.showSuccessDialog(title: String = "", message: String = "", function: () -> Unit)  {

    val dialog = Dialog(this)
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_success, null, false)
    val binding = DialogSuccessBinding.bind(view)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    if (title.isNotEmpty()) binding.textTitle.text = title
    if (message.isNotEmpty()) binding.textMessage.text = message

    binding.btnOk onClick {
        dialog.dismiss()
        function.invoke()
    }

    dialog.show()
}


@SuppressLint("InflateParams")
fun Context.showFailedDialog(title: String = "", message: String = "", function: () -> Unit)  {

    val dialog = Dialog(this)
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_failed, null, false)
    val binding = DialogFailedBinding.bind(view)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    if (title.isNotEmpty()) binding.textTitle.text = title
    if (message.isNotEmpty()) binding.textMessage.text = message

    binding.btnOk onClick {
        dialog.dismiss()
        function.invoke()
    }

    dialog.show()
}


fun Activity.showCallPermissionAlertDialog() {
    showWarningDialog("Permission Required","Phone call permission is required to make the call") {
        goToAppPermissionSetting()
    }
}
