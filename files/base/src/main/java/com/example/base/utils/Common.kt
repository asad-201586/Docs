package com.example.base.utils

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.*
import android.text.format.DateFormat
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.math.RoundingMode
import java.net.URL
import java.net.URLDecoder
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


fun getTextWithNullCheckingAndCapitalization(value: String, fieldName: String): String {

    if (value.isNull()) {
        Log.d("text_error", "checkNullValueWithCapitalize: null filedName fieldName: $fieldName")
        return ""
    }
    return value.capitalizedFirstLetter()
//    return try {
//        value.capitalizedFirstLetter()
//    }catch (e: Exception){
//        Log.d("text_error", "checkNullValueWithCapitalize: fieldName: $fieldName, error: ${e.localizedMessage}")
//        ""
//    }
}

fun TextView.setTextWithNullChecking(value: String, fieldName: String) {
    try {
        this.text = value
    } catch (e: Exception) {
        this.text = ""
        Log.d(
            "text_error",
            "checkNullValueWithCapitalize: fieldName: $fieldName, error: ${e.localizedMessage}"
        )
    }
}

fun View.backPress() {
    this.setOnClickListener {
        (this.context as Activity).finish()
    }
}

inline fun <reified T> Activity.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
}


inline fun <reified T> Activity.openActivityWithFinish(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
    finish()
}

//forkan 31/07/22
inline fun <reified T> Activity.openActivityWithDataAndFinish(key: String, value: String, extras: Intent.() -> Unit ={}){
    val intent = Intent(this, T::class.java)
    intent.putExtra(key, value)
    startActivity(intent)
    finish()
}

inline fun <reified T> Fragment.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this.requireActivity(), T::class.java)
    intent.extras()
    startActivity(intent)
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun String.currencyFormat(): String {

    return if (!this.contains("৳"))
        "\u09F3 $this"
    else
        this
}

inline fun <reified T> Activity.openActivityForResult(
    requestCode: Int,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
    startActivityForResult(intent, requestCode, options.toBundle())
}

inline fun <reified T> Fragment.openActivityForResult(
    requestCode: Int,
    extras: Intent.() -> Unit = {}
) {
    val intent = Intent(this.requireActivity(), T::class.java)
    intent.extras()
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.requireActivity())
    startActivityForResult(intent, requestCode, options.toBundle())
}


@Throws(IllegalAccessException::class, InstantiationException::class)
inline fun <reified T> newFragmentInstance(extras: Bundle.() -> Unit = {}): T? {

    return (T::class.java.newInstance() as Fragment).apply {
        arguments = Bundle().apply { extras() }
    } as T

}

@Throws(IllegalAccessException::class, InstantiationException::class)
inline fun <reified T> AppCompatActivity.showDialogFragment(extras: Bundle.() -> Unit = {}): T? {

    val instance = newFragmentInstance<T>(extras)
    (instance as DialogFragment).show(
        supportFragmentManager,
        T::class.java.simpleName
    )
    return instance
}

@Throws(IllegalAccessException::class, InstantiationException::class)
inline fun <reified T> Fragment.showDialogFragment(
    fromActivity: Boolean = true,
    extras: Bundle.() -> Unit = {}
): T? {
    val instance = newFragmentInstance<T>(extras)
    (instance as DialogFragment).show(
        if (fromActivity) {
            activity?.supportFragmentManager!!
        } else {
            childFragmentManager
        },
        T::class.java.simpleName
    )
    return instance
}

fun randomNumber(): Int {
    val min = 10000
    val max = 99999
    return Random().nextInt(max - min + 1) + min
}

//fun Context.showAlertDialog(title: String, message: String) {
//    AlertDialog.Builder(this)
//        .setTitle(title)
//        .setMessage(message)
//        .setPositiveButton(R.string.ok) { dialog, _ ->
//            dialog.dismiss()
//        }
//        .setIcon(R.drawable.ic_baseline_warning_24)
//        .show()
//}

fun Activity.goToAppPermissionSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, 111)
}

fun Activity.checkPhoneCallPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
}

fun Bundle.putStrings(vararg values: Pair<String, String>) {
    values.forEach { value ->
        this.putString(value.first, value.second)
    }
}

fun permissionCode(): Int {
    val min = 100
    val max = 999
    return Random().nextInt(max - min + 1) + min
}


fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Activity.getColorCompat(@ColorRes color: Int): Int {
    return baseContext.getColorCompat(color)
}

fun Fragment.getColorCompat(@ColorRes color: Int): Int {
    return activity!!.getColorCompat(color)
}

//fun Context.toast(msg: String) {
//    val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
//    val view = toast.view
//    toast.setGravity(Gravity.BOTTOM, 0, 120)
//    view?.background?.setColorFilter(ContextCompat.getColor(this, android.R.color.white))
//    val text = view?.findViewById<TextView>(android.R.id.message)
//    executeSafe {
//        text?.typeface = getMediumTypeFace(this)
//    }
//    text?.textSize = 12f
//    text?.setTextColor(ContextCompat.getColor(this, R.color.black))
//    toast.show()
//}

fun getMediumTypeFace(context: Context): Typeface? {
    return Typeface.createFromAsset(context.assets, "fonts/poppins_medium.ttf")
}

fun Drawable.setColorFilter(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}

fun ImageView.setColorFilter(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
    } else {
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
}


fun Context.parseResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard(view ?: View(activity))
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.openKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}


fun Fragment.openKeyboard() {
    activity?.openKeyboard()
}

fun Activity.openKeyboard() {
    applicationContext?.openKeyboard()
}

fun Context.showAlert(
    message: String?,
    cancelable: Boolean = true,
    showPositiveButton: Boolean = true,
    work: () -> Unit = { }
) {

    if (message.isNullOrEmpty()) return

    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
    } else {
        AlertDialog.Builder(this)
    }

    builder.setMessage(message)
        .setCancelable(cancelable)

    if (showPositiveButton) {
        builder.setPositiveButton("Ok") { dialog, id ->
            work.invoke()
            dialog.dismiss()
        }
    }

    val alert = builder.create()
    alert.getButton(Dialog.BUTTON_NEGATIVE).isAllCaps = false
    alert.getButton(Dialog.BUTTON_POSITIVE).isAllCaps = false
    alert.show()
}

/*fun Activity.showCancelConfirmationAlert(
    title: String = "",
    cancelable: Boolean = true,
    showPositiveButton: Boolean = true,
    work: () -> Unit = { }
) {
    val builder =
        androidx.appcompat.app.AlertDialog.Builder(this, R.style.AppBaseTheme2)
    //  AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
    // set the custom layout
    val layoutInflater = LayoutInflater.from(this)
    val view = layoutInflater.inflate(R.layout.cancel_confirmation_view, null)
    builder.setView(view)
    builder.setCancelable(cancelable)

    val yesTxt: TextView = view.findViewById(R.id.dialog_yes)
    val noTxt: TextView = view.findViewById(R.id.dialog_no)
    val cancel: ImageView = view.findViewById(R.id.dialog_close)
    val titleTxt: TextView = view.findViewById(R.id.title)
    if (title.isNotEmpty())
        if (titleTxt.isNotNull())
            titleTxt.text = title

    cancel.visible()
    val alert = builder.create()
    if (showPositiveButton) {
        yesTxt.setOnClickListener {
            try {
                work.invoke()
                alert.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        noTxt.setOnClickListener {
            try {
                alert.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        cancel.setOnClickListener {
            try {
                alert.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    alert.window?.addFlags(Window.FEATURE_NO_TITLE)
    alert.show()
}*/

fun Context.showConfirmAlert(
    message: String?,
    positiveText: String?,
    negativeText: String?,
    onConfirmed: () -> Unit = {},
    onCancel: () -> Unit = { }
) {

    if (message.isNullOrEmpty()) return

    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
    } else {
        AlertDialog.Builder(this)
    }


    builder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveText) { dialog, _ ->
            onConfirmed.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            onCancel.invoke()
            dialog.dismiss()
        }

    val alert = builder.create()
    alert.getButton(Dialog.BUTTON_NEGATIVE).isAllCaps = false
    alert.getButton(Dialog.BUTTON_POSITIVE).isAllCaps = false
    alert.show()
}

///////////////////////////////////////// VIEW ////////////////////////////////////

fun Activity.getDecorView(): View {
    return window.decorView
}

fun Fragment.getDecorView(): View {
    return activity?.window?.decorView!!
}

inline fun EditText.observeTextChange(crossinline body: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            body(p0.toString())
        }
    })
}

inline fun TextView.observeTextChange(crossinline body: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            body(p0.toString())
        }
    })
}

fun View.animateX(value: Float) {
    with(ObjectAnimator.ofFloat(this, View.TRANSLATION_X, value)) {
        duration = 3500
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        start()
    }
}

fun View.animateY(value: Float) {
    with(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, value)) {
        duration = 3500
        repeatMode = ValueAnimator.REVERSE
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        start()
    }
}

infix fun ViewGroup.inflate(@LayoutRes view: Int): View {
    return LayoutInflater.from(context).inflate(view, this, false)
}

fun Int.inflate(viewGroup: ViewGroup): View {
    return LayoutInflater.from(viewGroup.context).inflate(this, viewGroup, false)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.toggleVisibility() {
    when (this.visibility) {
        View.VISIBLE -> this.gone()
        View.INVISIBLE -> this.visible()
        View.GONE -> this.visible()
    }
}

fun Activity.snack(arrayList: String) {
    getDecorView().snack(arrayList)
}

fun Fragment.snack(arrayList: String) {
    getDecorView().snack(arrayList)
}

@SuppressLint("WrongConstant")
inline fun View.snack(
    arrayList: String,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(this, arrayList, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}


///////////////////////////////////////// COMMON ////////////////////////////////////

inline fun <T> T.executeSafe(body: () -> Unit) {
    try {
        body.invoke()
    } catch (e: Exception) {

    }
}

fun <T> T.isNull(): Boolean {
    return this == null
}

fun <T> T.isNotNull(): Boolean {
    return this != null
}

inline infix operator fun Int.times(action: (Int) -> Unit) {
    var i = 0
    while (i < this) {
        action(i)
        i++
    }
}

fun String.remove(vararg value: String): String {
    var removeString = this
    value.forEach {
        removeString = removeString.replace(it, "")
    }
    return removeString
}

//*
//  android:textColorHighlight="#f00" // background color when pressed
//    android:textColorLink="#0f0" link backgroudnd color**/
fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun TextView.makeMulticolourString(vararg links: Pair<String, View.OnClickListener>, colour: Int) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val foregroundColorSpan = object : ForegroundColorSpan(colour) {

        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            foregroundColorSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

fun spToPx(sp: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        Resources.getSystem().displayMetrics
    )
}

fun View.setTopMargin(top: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.topMargin = dpToPx(top)
        requestLayout()
    }
    /*(view.layoutParams as RelativeLayout.LayoutParams).setMargins(
        position.left.convertDpToPx(context),
        position.top.convertDpToPx(context),
        position.right.convertDpToPx(context),
        position.bottom.convertDpToPx(context)
    )*/

}

fun View.setLeftMargin(left: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.leftMargin = dpToPx(top)
        requestLayout()
    }
}

fun View.setRightMargin(right: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.rightMargin = dpToPx(top)
        requestLayout()
    }
}

fun View.setBottomMargin(bottom: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.bottomMargin = dpToPx(top)
        requestLayout()
    }
}

fun View.getMarginLayoutParams(): ViewGroup.MarginLayoutParams? {
    return if (layoutParams is ViewGroup.MarginLayoutParams) {
        layoutParams as ViewGroup.MarginLayoutParams
    } else {
        null
    }

}
/*

fun Activity.playSound(@RawRes sound: Int = R.raw.bell) {
    MediaPlayer.create(this, sound).start()
}
*/

fun String.underline(): SpannableString {
    val content = SpannableString(this)
    content.setSpan(UnderlineSpan(), 0, this.length, 0)
    return content
}

fun TextView.underline() {
    text = text.toString().underline()
}
/*

fun Context.openTab(url: String) {
    val builder = CustomTabsIntent.Builder()
// modify toolbar color
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
// add share button to overflow men
    builder.addDefaultShareMenuItem()
// add menu item to oveflow
    //builder.addMenuItem("MENU_ITEM_NAME", pendingIntent)
// show website title
    builder.setShowTitle(true)
// modify back button icon
    //builder.setCloseButtonIcon(bitmap)
// menu item icon
    //builder.setActionButton(bitmap, "Android", pendingIntent, true)
// animation for enter and exit of tab            builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
    builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
    val customTabsIntent = builder.build()
    val packageName = CustomTabHelper().getPackageNameToUse(this, url)

    if (packageName == null) {
        // if chrome not available open in web view
        openActivity<WebViewActivity> {
            putExtra(WebViewActivity.EXTRA_URL, url)
        }
    } else {
        customTabsIntent.intent.setPackage(packageName)
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}*/

//fun String.copyToClipboard(context: Context) {
//    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//    clipboard.setPrimaryClip(ClipData.newPlainText(this, this))
//}

fun String.maintainTwoLength(): String {
    executeSafe {
        if (this.length < 2) {
            return "0$this"
        }
    }
    return this
}

/*fun AutocompletePrediction.getAddress(): Address {
    val geocoder = Geocoder(App.getApp())
    var result: List<Address>
    result = geocoder.getFromLocationName(getFullText(null).toString(), 1)
    if (result.isNullOrEmpty()) {
        result = geocoder.getFromLocationName(getPrimaryText(null).toString(), 1)
    }

    if (result.isNullOrEmpty()) {
        return Address(Locale.getDefault())
    }

    return result[0]
}

fun LatLng.getAddress(): Address {
    val geocoder = Geocoder(App.getApp())
    val result: List<Address>
    result = geocoder.getFromLocation(latitude, longitude, 1)

    if (result.isNullOrEmpty()) {
        return Address(Locale.getDefault())
    }
    return result[0]
}*/

inline fun <reified T : Activity> RecyclerView.ViewHolder.getActivity(): T? {
    val contextWrapperBaseContext = ((itemView.context as ContextWrapper).baseContext)
    val fieldOuterContext = contextWrapperBaseContext.javaClass.getDeclaredField("mOuterContext")
    fieldOuterContext.isAccessible = true
    val activity = fieldOuterContext.get(contextWrapperBaseContext) as? T
    fieldOuterContext.isAccessible = false
    return activity
}

fun Long.getDateInformat(): String? {
    val sd = SimpleDateFormat("EEE,dd MMM-hh:mm")
    val dateformatdate = sd.format(this)
    return dateformatdate
}


fun getYearsTillEighteenYearOld(): ArrayList<String> {
    val years = ArrayList<String>()
    val thisYear = Calendar.getInstance().get(Calendar.YEAR)
    for (i in 1940..(thisYear - 18)) {
        years.add(i.toString())
    }
    return years
}

fun EditText.value(): String = this.text.toString()

fun TextView.value(): String = this.text.toString()

fun String.capitalizedFirstLetter(): String {
    return if (this.isEmpty())
        ""
    else this.substring(0, 1).uppercase(Locale.getDefault()) + this.substring(1)
}

/*fun EditText.observeTextChangeAsObservable(): Observable<String> {
    return Observable.create<String> { observer ->
        observeTextChange { text ->
            observer.onNext(text)
        }
    }.subscribeOn(AndroidSchedulers.mainThread())
}*/

fun Spinner.onItemSelectListener(listener: (View?, Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            listener.invoke(view, position)
        }
    }
}

inline fun <T> T.notNull(block: T.() -> Unit) {
    this?.block()
}

fun getCurrentDateAndTime(): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    return DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar).toString()
}

fun getCustomDateTimeStamp(time: String?): Long {
    Log.d("token_db", "getCustomDateTimeStamp: time: $time")
    return try {
        val c = Calendar.getInstance(TimeZone.getDefault())
        when {
            time?.lowercase(Locale.getDefault())?.trim()?.indexOf("h") != -1 -> {
                val value: String? =
                    time?.lowercase(Locale.getDefault())?.trim()?.split("h")?.get(0).toString()
                value?.toInt()?.let { c.add(Calendar.HOUR, it) }
            }
            time.lowercase(Locale.getDefault()).trim().indexOf("m") != -1 -> {
                val value: String? = time.lowercase(Locale.getDefault()).trim().split("m")[0]
                value?.toInt()?.let { c.add(Calendar.MINUTE, it) }
            }
            time.lowercase(Locale.getDefault()).trim().indexOf("d") != -1 -> {
                val value: String? = time.lowercase(Locale.getDefault()).trim().split("d")[0]
                value?.toInt()?.let { c.add(Calendar.DAY_OF_MONTH, it) }
            }
        }
        c.add(Calendar.MINUTE, 0)
        return c.time.time
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

fun getDate(timestamp: Long): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    return DateFormat.format("dd-MMM-yy", calendar).toString()
}

fun decode(url: String): String? {
    var prevURL = ""
    var decodeURL = url
    decodeURL = decodeURL.replace("%(?![0-9a-fA-F]{2})", "%25")
    decodeURL = decodeURL.replace("\\+", "%2B")
    try {
        while (prevURL != decodeURL) {
            prevURL = decodeURL
            decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
        }
    } catch (e: Exception) {
    }
    /*  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          Html.fromHtml(decodeURL, Html.FROM_HTML_MODE_LEGACY)
      } else Html.fromHtml(decodeURL)*/
    return decodeURL
}

fun decodeString(url: String): String? {
    var prevURL = ""
    var decodeURL = url
    decodeURL = decodeURL.replace("%(?![0-9a-fA-F]{2})", "%25")
    decodeURL = decodeURL.replace("\\+", "%2B")
    try {
        while (prevURL != decodeURL) {
            prevURL = decodeURL
            decodeURL = URLDecoder.decode(decodeURL, "UTF-8")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return decodeURL
}

fun roundOffDecimal(number: Double?): Double? {
    return try {
        val symbols = DecimalFormatSymbols(Locale.US)
        val df = DecimalFormat("##.##", symbols)
        df.roundingMode = RoundingMode.FLOOR
        df.format(number).toDouble()
    } catch (e: Exception) {
        e.printStackTrace()
        number
    }
}

fun roundOffOneDecimal(number: Double?): Double? {
    return try {
        val symbols = DecimalFormatSymbols(Locale.US)
        val df = DecimalFormat("##.#", symbols)
        df.roundingMode = RoundingMode.HALF_UP
        df.format(number).toDouble()
    } catch (e: Exception) {
        e.printStackTrace()
        number
    }
}

fun Long.greaterThan20MB(): Boolean {
    val sizeInKB = this / 1024
    val sizeInMB = sizeInKB / 1024
    return sizeInMB > 20
}

fun Context.getAppVersionName(): String {
    val manager: PackageManager = packageManager
    val info: PackageInfo = manager.getPackageInfo(
        packageName, 0
    )
    return info.versionName
}

fun Activity.getAppVersionCode(): String {
    val info = packageManager?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
    return info?.versionCode.toString()
}

fun Activity.getAppCurrentVersion(): String {
    val info = packageManager?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
    return info?.versionName ?: ""
}

fun Activity.isAppUpdated(serverVersion: String): Boolean {
    val currentVersionArray = getAppCurrentVersion().split(".").toTypedArray()
    val serverVersionArray = serverVersion.split(".").toTypedArray()

    for (i in serverVersionArray.indices) {
        if (currentVersionArray[i].toInt() != serverVersionArray[i].toInt()
            && currentVersionArray[i].toInt() < serverVersionArray[i].toInt()
        ) {
            return false
        }
    }

    return true
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

@SuppressLint("ResourceType")
fun Context.convertXmlToDrawable(view: View): Drawable {
    view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    val bitmap: Bitmap = Bitmap.createBitmap(
        view.measuredWidth,
        view.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    view.draw(canvas)
    return BitmapDrawable(resources, bitmap)
}


//fun Activity.showToast(message: String) {
//    val toastLayout: View = LayoutInflater.from(this).inflate(R.layout.custom_toast,null,false)
//
//    val textView = toastLayout.findViewById(R.id.text_toast) as TextView
//    textView.text = message
//
//    val toast = Toast(this)
//    toast.duration = Toast.LENGTH_SHORT
//    toast.view = toastLayout
//    toast.setGravity(Gravity.CENTER,0,0)
//    toast.show()
//}


fun isInternetConnected(context: Context): Boolean {
    var connected: Boolean
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED).also {
        connected = it
    }
    return connected
}

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun backgroundTint(context: Context, colorRes: Int, view: View) {
    view.backgroundTintList = ContextCompat.getColorStateList(context, colorRes)
}

infix fun AutoCompleteTextView.itemClick(work: (Int) -> Unit) {
    setOnItemClickListener { parent, view, position, id ->
        work.invoke(position)
    }
}

infix fun View.onClick(function: (View) -> Unit) {
    setOnClickListener {
        function.invoke(it)
    }
}

fun drawableToBitmap(drawable: Drawable): Bitmap? {
    var bitmap: Bitmap? = null
    if (drawable is BitmapDrawable) {
        if (drawable.bitmap != null) {
            return drawable.bitmap
        }
    }
    bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun TextView.htmlText(html: String) {
    this.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(html)
    }

}

fun bitmapToString(bitmap: Bitmap?): String {
    val baos = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.PNG, 60, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun urlToBitmap(url: String): Bitmap? {
    var imageBitmap: Bitmap? = null
    try {
        val url = URL(url)
        imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
    } catch (e: IOException) {
        println(e)
    }
    return imageBitmap
}

val phoneNumberRegexBd = "^(?:\\+?88)?01[13-9]\\d{8}\$".toRegex()
val phoneNumberRegexInd = "^[789]\\d{9}\$".toRegex()

val emailValidationRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()



