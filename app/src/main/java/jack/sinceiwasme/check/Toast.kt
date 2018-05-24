package jack.sinceiwasme.check

import android.content.Context
import android.widget.Toast

private var toast: Toast? = null

infix fun Context.toast(msg: String) {
    if (toast != null) {
        toast!!.cancel()
    }
    toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    toast!!.show()
}