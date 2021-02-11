import java.text.SimpleDateFormat
import java.util.*

@Suppress("SimpleDateFormat")
fun date(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(Date())
}