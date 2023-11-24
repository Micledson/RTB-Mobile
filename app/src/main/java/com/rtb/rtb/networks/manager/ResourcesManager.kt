import android.content.Context
import android.util.Log
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.model.Resource
import com.rtb.rtb.model.fromResponse
import com.rtb.rtb.networks.BaseRepository
import com.rtb.rtb.networks.ResourceRepository

object ResourcesManager {
    private lateinit var resources: Resource

    fun initialize(context: Context) {
        val sharedPrefs = SharedPrefs(context)

        val resourceRepository = ResourceRepository()
        resourceRepository.getResources { result ->

            when(result) {
                is BaseRepository.Result.Success -> {
                    if (result.data != null) {
                        resources = fromResponse(result.data)
                        sharedPrefs.saveResources(resources)
                    }
                }

                is BaseRepository.Result.Error -> {
                    Log.d("Error getting resources", result.message)
                }
            }
        }
    }

    fun getResources(context: Context): Resource? {
        return SharedPrefs(context).getResources()
    }
}
