package hr.ferit.brunobasic.orwmalv8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), PersonRecyclerAdapter.ContentListener {
    private val db= Firebase.firestore
    private lateinit var recyclerAdapter: PersonRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val recyclerView=findViewById<RecyclerView>(R.id.personList)
        db.collection("persons")
            .get()
            .addOnSuccessListener {
                val list: ArrayList<Person> = ArrayList()
                for(data in it.documents){
                    val person = data.toObject(Person::class.java)
                    if(person!=null){
                        person.id=data.id
                        list.add(person)
                    }
                }
                recyclerAdapter= PersonRecyclerAdapter(list,this@MainActivity)
                recyclerView.apply{
                    layoutManager=LinearLayoutManager(this@MainActivity)
                    adapter=recyclerAdapter
                }
            }
            .addOnFailureListener(){
                Log.e("MainActivity",it.message.toString())
            }

        val addButton=findViewById<Button>(R.id.saveButton)
        val addImageUrl=findViewById<EditText>(R.id.addImageUrl)
        val addPersonName=findViewById<EditText>(R.id.addPersonName)
        val addPersonDescription=findViewById<EditText>(R.id.addPersonDescription)

        addButton.setOnClickListener(){
            val person=Person("",addImageUrl.text.toString(),addPersonName.text.toString(),addPersonDescription.text.toString())
            db.collection("persons")
                .add(person)
                .addOnSuccessListener { documentReference ->
                    person.id=documentReference.id
                    recyclerAdapter.addItem(person)
                    Log.d("MainActivity", "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("MainActivity", "Error adding document", e)
                }

        }
    }

    override fun onItemButtonClick(index: Int, person: Person, clickType: ItemClickType) {
        if(clickType==ItemClickType.EDIT){
            db.collection("persons").document(person.id).set(person)
        }
        else if(clickType==ItemClickType.DELETE){
            recyclerAdapter.removeItem(index)
            db.collection("persons").document(person.id).delete()
        }
    }
}