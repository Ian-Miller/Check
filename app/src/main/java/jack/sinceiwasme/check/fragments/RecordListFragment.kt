package jack.sinceiwasme.check.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.transition.TransitionManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jack.sinceiwasme.check.R
import jack.sinceiwasme.check.adapter.RecordAdapter
import jack.sinceiwasme.check.data.DataFactory
import jack.sinceiwasme.check.data.Record
import jack.sinceiwasme.check.models.MyViewModel
import jack.sinceiwasme.check.toast
import kotlinx.android.synthetic.main.fragment_record_list.*


class RecordListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_record_list, container, false)
    }

    val adapter = RecordAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        records_list.layoutManager = LinearLayoutManager(context)
        records_list.adapter = adapter


        val dir = ItemTouchHelper.START or ItemTouchHelper.END
        val callback = object : ItemTouchHelper.SimpleCallback(0, dir) {
            val bound = Rect()
            override fun onMove(recyclerView: RecyclerView?,
                                viewHolder: RecyclerView.ViewHolder?,
                                target: RecyclerView.ViewHolder?) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                activity!!.toast("onSwiped: ${direction}")
                val record = adapter.currentList?.get(viewHolder.layoutPosition) ?: return
                val factory = DataFactory.instance(activity!!)
                factory.remove(record)
                Snackbar.make(getView()!!, "record ${record.label}:${record.record} was removed!", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO", View.OnClickListener {
                        dismiss()
                        factory.add(record)
                    })
                }.show();
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                c.apply {
                    save()
                    viewHolder.itemView.apply {
                        bound.set(left, top, right, bottom)
                        clipRect(bound)
                    }
                    drawColor(Color.RED)
                    restore()
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(callback).attachToRecyclerView(records_list)

        back.setOnClickListener {
            TransitionManager.beginDelayedTransition(getView()?.parent!! as ViewGroup)
            fragmentManager?.popBackStack()
        }

        ViewModelProviders.of(this).get(MyViewModel::class.java).records.observe(this, Observer {
            adapter.submitList(it)
            var _total = 0.0
            if (it != null) {
                for (r in it) {
                    _total += r.record;
                }
            }
            total.text = "total: ${Record.getFormattedNumber(_total)}"
        })
    }

    companion object {
        val tag = "RecordListFragment"
        fun newInstance(): RecordListFragment = RecordListFragment()
        fun open(manager: FragmentManager, root: Int) {
            if (manager.findFragmentByTag(tag) == null) {
                manager.beginTransaction()
                        .add(root, RecordListFragment(), tag)
                        .addToBackStack(null)
                        .commit()
            }
        }
    }
}
