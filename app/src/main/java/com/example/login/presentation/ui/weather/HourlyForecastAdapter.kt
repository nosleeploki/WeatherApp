import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.databinding.WaItemHourlyBinding
import com.example.login.data.model.ForecastItem
import java.text.SimpleDateFormat
import java.util.*

class HourlyForecastAdapter(forecastList: List<ForecastItem>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    private val hourlyList: List<ForecastItem>

    init {
        hourlyList = filterNext8Hours(forecastList)
    }

    inner class HourlyForecastViewHolder(val binding: WaItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val binding: WaItemHourlyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.wa_item_hourly,
            parent,
            false
        )
        return HourlyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.binding.hourlyWeather = hourlyList[position]
        holder.binding.executePendingBindings()
        if (position == hourlyList.size - 1) {
            holder.binding.separatorView.visibility = View.GONE
        } else {
            holder.binding.separatorView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = hourlyList.size

    private fun filterNext8Hours(forecastList: List<ForecastItem>): List<ForecastItem> {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        return forecastList.filter {
            val forecastTime = dateFormat.parse(it.dateTime)?.time ?: 0
            forecastTime > currentTime
        }.take(8)
    }
}
