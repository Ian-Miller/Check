package jack.sinceiwasme.check

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.transition.TransitionManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.FocusFinder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import jack.sinceiwasme.check.adapter.RecordAdapter
import jack.sinceiwasme.check.data.DataFactory
import jack.sinceiwasme.check.data.Record
import jack.sinceiwasme.check.fragments.RecordListFragment
import jack.sinceiwasme.check.models.MyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var consumeTextView: TextView
    private lateinit var receiveTextView: TextView
    private val adapter = RecordAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        consumeTextView = findViewById(R.id.consume)
        receiveTextView = findViewById(R.id.receive)

        consumeTextView.setOnClickListener { EntryInputDialog.show(this, false) }
        receiveTextView.setOnClickListener { EntryInputDialog.show(this, true) }

        findViewById<ImageView>(R.id.menu).setOnClickListener {
            showRecords()
        }

        findViewById<ImageView>(R.id.settings).setOnClickListener{
            Snackbar.make(it,"not implemented",Snackbar.LENGTH_LONG).show()
        }

        ViewModelProviders.of(this).get(MyViewModel::class.java).records.observe(this, Observer {
            var t = 0.0
            for (r in it!!) {
                t += r.record
            }
            val text = "total: ${Record.getFormattedNumber(t)}"
            findViewById<TextView>(R.id.entry_total).text = text
        })
    }

    override fun onBackPressed() {
        TransitionManager.beginDelayedTransition(findViewById(R.id.root))
        super.onBackPressed()
    }

    private fun showRecords() {
        TransitionManager.beginDelayedTransition(findViewById(R.id.root))
        RecordListFragment.open(supportFragmentManager, R.id.root)
    }

    object EntryInputDialog {
        fun show(context: Context, inOrOut: Boolean) {
            val dialog = AlertDialog.Builder(context);

            val view: ViewGroup = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_entry_input, null, false)
                    as ViewGroup
            dialog.setView(view)

            val title: String
            val label: String
            if (inOrOut) {
                title = context.resources.getString(R.string.receive)
                label = context.resources.getString(R.string.from)
            } else {
                title = context.resources.getString(R.string.consume)
                label = context.resources.getString(R.string._for)
            }

            view.findViewById<TextView>(R.id.title).text = title
            view.findViewById<TextView>(R.id.label).text = label

            val d = dialog.create()
            view.findViewById<TextView>(R.id.cancel).apply {
                setOnClickListener { d.dismiss() }
            }

            val inputField = view.findViewById<EditText>(R.id.input)
            val labelField = view.findViewById<EditText>(R.id.label_input)

            fun ok(textView: TextView): Boolean {
                val rec = try {
                    textView.text.toString().toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                    textView.error = "invalid input!"
                    return false
                }

                val record = Record(if (inOrOut) rec else -rec)
                val label = labelField.text.toString()
                if (!label.isEmpty()){
                    record.label = label
                }
                DataFactory.instance(context).add(record)
                d.dismiss()
                return true
            }

            labelField.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT){
                    inputField.requestFocus()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            inputField.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    return@OnEditorActionListener ok(v)
                }
                return@OnEditorActionListener false
            })

            view.findViewById<TextView>(R.id.ok).apply {
                setOnClickListener {
                    ok(inputField)
                }
            }

            inputField.post {
                val service = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                service?.showSoftInput(inputField, 0)
                labelField.requestFocus()
            }

            d.show()

        }
    }

}
