package com.example.base.allBase


import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.base.utils.inflate

/**
 * * Created by asadul haque on 11/14/22.
 * pran-rfl group
 */
abstract class BaseAdapter<T>(
    @LayoutRes private val view: Int
) : RecyclerView.Adapter<BaseAdapter.IViewHolder>() {
    protected val list: ArrayList<T> = arrayListOf()
    var itemClickCallback: RecycleItemViewCallback<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IViewHolder {
        return getViewHolder(parent, view, viewType)
    }

    protected open fun getViewHolder(parent: ViewGroup?, view: Int, viewType: Int): IViewHolder {
        val view = parent?.inflate(view)
        return IViewHolder(view!!)
    }

    override fun getItemCount() = list.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        onAttach(
            (recyclerView.context as? ContextWrapper)?.baseContext as? AppCompatActivity,
            getCurrentFragment(recyclerView)
        )
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun getCurrentFragment(recyclerView: RecyclerView): Fragment? {
        try {
            val supportFragmentManager =
                ((recyclerView.context as? ContextWrapper)?.baseContext as? AppCompatActivity)?.supportFragmentManager
            return if (supportFragmentManager?.backStackEntryCount != 0) {
                var frag =
                    supportFragmentManager?.fragments?.get(supportFragmentManager.backStackEntryCount - 1)
                if (frag != null && frag.childFragmentManager.backStackEntryCount != 0) {
                    frag =
                        frag.childFragmentManager.fragments[frag.childFragmentManager.backStackEntryCount - 1]
                }
                frag
            } else {
                null
            }
        }catch (e:Exception){
            e.printStackTrace()
            return null
        }
    }

    abstract override fun onBindViewHolder(holder: IViewHolder, position: Int)
    open fun onAttach(
        activity: Activity?,
        currentFragment: Fragment?
    ) {
    }

    fun addToList(list: List<T>?) {
        if (list != null) {
            val previousRange = list.size
            this.list.addAll(list)
            val newRange = list.size
            notifyItemChanged(previousRange, newRange)
        }
    }

    fun addNewList(list: ArrayList<T>?) {
        if (list != null) {
            this@BaseAdapter.list.clear()
            this@BaseAdapter.list.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addNewList(list: List<T>?) {
        if (list != null) {
            this@BaseAdapter.list.clear()
            this@BaseAdapter.list.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addToList(item: T): Int {
        list.add(item)
        val insertedAt = list.size - 1
        notifyItemInserted(insertedAt)
        return insertedAt
    }

    fun addNewList(list: Iterable<T>?) {
        if (list != null) {
            this@BaseAdapter.list.clear()
            this@BaseAdapter.list.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addMoreItems(items: List<T>?) {
        val previousRange = list.size
        items?.let { list.addAll(items) }
        val newRange = list.size
        notifyItemRangeInserted(previousRange, newRange)
    }

    fun addMoreItemsAtStart(items: List<T>) {
        val previousRange = list.size
        list.addAll(items)
        val newRange = list.size
        notifyItemRangeInserted(previousRange, newRange)
    }

    fun appendAtStart(item: T) {
        list.add(0, item)
        notifyItemInserted(0)
    }

    fun getOriginalList(): ArrayList<T> {
        return list
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeItem(item: T) {
        val pos = list.indexOf(item)
        if (pos != -1) {
            list.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    fun removeAll() {
        list.clear()
        notifyDataSetChanged()
    }

    class IViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}