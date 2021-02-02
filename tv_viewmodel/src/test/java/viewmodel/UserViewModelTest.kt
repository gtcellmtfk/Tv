package viewmodel

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.bytebyte6.data.dataModule
import com.bytebyte6.data.roomModule
import com.bytebyte6.usecase.useCaseModule
import com.bytebyte6.viewmodel.UserViewModel
import com.bytebyte6.viewmodel.viewModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class UserViewModelTest : AutoCloseKoinTest(){

    private val userViewModel by inject<UserViewModel>()

    @Before
    fun setup(){
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(dataModule, useCaseModule, viewModule, roomModule)
        }
    }

    @Test
    fun test(){
        userViewModel.updateCapturePic(true)
    }

}