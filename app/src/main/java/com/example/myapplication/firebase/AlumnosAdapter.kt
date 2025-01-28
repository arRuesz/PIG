import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.firebase.Alumno

class AlumnoAdapter(private val alumnosList: List<Alumno>) : RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {

    // ViewHolder para gestionar cada ítem
    class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvMediaExpediente: TextView = itemView.findViewById(R.id.tvMediaExpediente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alumno, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = alumnosList[position]
        holder.tvNombre.text = alumno.nombre
        holder.tvApellido.text = alumno.apellido
        holder.tvMediaExpediente.text = "Media: ${alumno.mediaexpediente}"
    }

    override fun getItemCount(): Int {
        return alumnosList.size
    }
}
