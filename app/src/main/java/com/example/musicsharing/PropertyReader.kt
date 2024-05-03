import android.content.Context
import java.util.*

private const val CONFIG = "secrets.properties"

object PropertiesReader {
    private val properties = Properties()

    fun init(context: Context) {
        try {
            val inputStream = context.assets.open(CONFIG)
            properties.load(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getProperty(key: String): String = properties.getProperty(key)
}
