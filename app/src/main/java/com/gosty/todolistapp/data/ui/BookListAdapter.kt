package com.gosty.todolistapp.data.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gosty.todolistapp.R
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.databinding.ItemBookCardBinding

class BookListAdapter : ListAdapter<Book, BookListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    /***
     * This method to set the onClick action from activity or fragment.
     * @param onItemClickCallback a callback for onClick action
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see OnItemClickCallback
     */
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBookCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
        holder.binding.root.setOnClickListener {
            onItemClickCallback.onItemClicked(book)
        }
    }

    /***
     * This is a interface to setting the onClick action.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    interface OnItemClickCallback {
        /***
         * This method is a contract that need to be implement for onClick action.
         * @param book variable that contain book model
         * @author Ghifari Octaverin
         * @since Sept 15th, 2023
         * @see Book
         */
        fun onItemClicked(book: Book)
    }

    /***
     * This class is to manage all view for this ItemBookCard layout.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    class MyViewHolder(val binding: ItemBookCardBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        /***
         * This method to get book based on the id from realtime database.
         * @param book variable that contain book model
         * @author Ghifari Octaverin
         * @since Sept 15th, 2023
         * @see Book
         */
        fun bind(book: Book) {
            Glide.with(itemView.context)
                .load(book.cover)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .centerCrop()
                .into(binding.ivCover)

            binding.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
            }
        }
    }

    companion object {
        /***
         * This variable is a callback to detect is all item still same.
         * @author Ghifari Octaverin
         * @since Sept 15th, 2023
         */
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Book> =
            object : DiffUtil.ItemCallback<Book>() {
                override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                    return oldItem == newItem
                }
            }
    }
}