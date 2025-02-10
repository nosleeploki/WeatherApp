import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.login.R
import com.example.login.databinding.WaItemDailyBinding
import com.example.login.data.model.ForecastItem
import kotlin.collections.LinkedHashMap

class DailyForecastAdapter(forecastList: List<ForecastItem>) :
    RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {

    private val dailyAverages: List<ForecastItem>

    init {
        dailyAverages = calculateDailyAverages(forecastList)
    }

    inner class DailyForecastViewHolder(val binding: WaItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val binding: WaItemDailyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.wa_item_daily,
            parent,
            false
        )
        return DailyForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.binding.dailyWeather = dailyAverages[position]
        holder.binding.executePendingBindings()
        if (position == 0) {
            holder.binding.separatorView.visibility = View.GONE
        } else {
            holder.binding.separatorView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = dailyAverages.size

    private fun calculateDailyAverages(forecastList: List<ForecastItem>): List<ForecastItem> {
        val groupedData = LinkedHashMap<String, MutableList<ForecastItem>>()

        for (forecast in forecastList) {
            val date = forecast.dateTime.split(" ")[0]
            if (!groupedData.containsKey(date)) {
                groupedData[date] = mutableListOf()
            }
            groupedData[date]?.add(forecast)
        }

        val averagedList = mutableListOf<ForecastItem>()
        for ((date, forecasts) in groupedData) {
            val avgTemp = forecasts.map { it.main.temperature }.average()
            val mainWeather = forecasts[0].main.copy(temperature = avgTemp)
            val icon = forecasts[0].weather[0].icon // Dùng icon đầu tiên

            averagedList.add(
                ForecastItem(
                    dateTime = date,
                    main = mainWeather,
                    weather = listOf(forecasts[0].weather[0].copy(icon = icon))
                )
            )
        }

        return averagedList.take(5)
    }
}
