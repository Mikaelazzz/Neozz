package id.vincent.neoz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class beranda : AppCompatActivity() {

    data class Hero(
        val name: String,
        val description: String,
        val imageRes: Int,
        val role: String
    )

    private val heroes = listOf(
        Hero("Akai", "Tank Hero", R.drawable.hero1, "Tank"),
        Hero("Chou", "Fighter Hero", R.drawable.hero1, "Fighter"),
        Hero("Gusion", "Assassin Hero", R.drawable.hero1, "Assassin"),
        Hero("Cyclops", "Mage Hero", R.drawable.hero1, "Mage"),
        Hero("Layla", "Marksman Hero", R.drawable.hero1, "Marksman"),
        Hero("Rafaela", "Support Hero", R.drawable.hero1, "Support")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beranda)

        // Urutkan heroes berdasarkan abjad secara default
        val sortedHeroes = heroes.sortedBy { it.name }

        // Setup RecyclerView untuk Buttons
        val recyclerButtons = findViewById<RecyclerView>(R.id.recycler_buttons)
        recyclerButtons.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val buttons = listOf("All", "Tank", "Fighter", "Assassin", "Mage", "Marksman", "Support")

        // Setup RecyclerView untuk Heroes
        val heroRecyclerView = findViewById<RecyclerView>(R.id.list_hero)
        val heroAdapter = HeroAdapter(sortedHeroes)
        heroRecyclerView.layoutManager = LinearLayoutManager(this)
        heroRecyclerView.adapter = heroAdapter

        // Atur Adapter untuk Buttons
        recyclerButtons.adapter = ButtonsAdapter(buttons) { role ->
            if (role == "All") {
                heroAdapter.updateData(sortedHeroes) // Tampilkan semua hero
            } else {
                val filteredHeroes = heroes.filter { it.role == role }
                heroAdapter.updateData(filteredHeroes) // Tampilkan hero berdasarkan role
            }
        }

        // Menyesuaikan padding dengan sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    class ButtonsAdapter(
        private val items: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<ButtonsAdapter.ButtonViewHolder>() {

        private var activeRole: String = "All" // Menyimpan role aktif

        inner class ButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val button: Button = view.findViewById(R.id.button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false)
            return ButtonViewHolder(view)
        }

        override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
            val role = items[position]
            holder.button.text = role

            // Update tampilan tombol berdasarkan apakah role aktif
            val isActive = role == activeRole
            holder.button.isActivated = isActive

            // Menangani klik tombol
            holder.button.setOnClickListener {
                activeRole = role // Set role aktif
                onClick(role) // Panggil callback untuk memfilter data
                notifyDataSetChanged() // Perbarui tampilan RecyclerView
            }
        }

        override fun getItemCount(): Int = items.size
    }


    class HeroAdapter(private var items: List<Hero>) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

        inner class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.title)
            val description: TextView = view.findViewById(R.id.desc)
            val image: ImageView = view.findViewById(R.id.images)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_heroes, parent, false)
            return HeroViewHolder(view)
        }

        override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
            val hero = items[position]
            holder.title.text = hero.name
            holder.description.text = hero.description
            holder.image.setImageResource(hero.imageRes)
        }

        override fun getItemCount(): Int = items.size

        fun updateData(newItems: List<Hero>) {
            items = newItems
            notifyDataSetChanged()
        }
    }
}
