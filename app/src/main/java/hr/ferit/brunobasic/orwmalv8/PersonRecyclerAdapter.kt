package hr.ferit.brunobasic.orwmalv8

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

enum class ItemClickType{
    EDIT,
    DELETE,
    ADD,
}

class PersonRecyclerAdapter (
    val items: ArrayList<Person>,
    val listener: ContentListener
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PersonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PersonViewHolder->{
                holder.bind(position,items[position],listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int){
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index,items.size)
    }

    fun addItem(person:Person){
        items.add(person)
        notifyItemInserted(items.size)
    }

    class PersonViewHolder(val view: View):RecyclerView.ViewHolder(view){
        private val personImage=view.findViewById<ImageView>(R.id.personImage)
        private val personName=view.findViewById<EditText>(R.id.personName)
        private val personDescription=view.findViewById<EditText>(R.id.personDescription)
        private val deleteButton=view.findViewById<ImageButton>(R.id.deleteButton)
        private val editButton=view.findViewById<ImageButton>(R.id.editButton)


        fun bind(index: Int, person: Person, listener: ContentListener){
            Glide.with(view.context).load(person.imageUrl).into(personImage)
            personName.setText(person.name)
            personDescription.setText(person.description)

            editButton.setOnClickListener(){
                person.name=personName.text.toString()
                person.description=personDescription.text.toString()

                listener.onItemButtonClick(index,person,ItemClickType.EDIT)
            }

            deleteButton.setOnClickListener(){
                listener.onItemButtonClick(index,person,ItemClickType.DELETE)
            }

        }
    }

    interface ContentListener{
        fun onItemButtonClick(index: Int, person: Person, clickType: ItemClickType)
    }
}