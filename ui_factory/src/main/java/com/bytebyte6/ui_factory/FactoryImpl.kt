package com.bytebyte6.ui_factory

import java.io.File
import java.nio.file.FileSystem
import javax.swing.filechooser.FileSystemView

class FactoryImpl(
    private val packageName: String,
    private val entryName: String
) : Factory2 {

    companion object {
        val rootDir = File(".")
        val uiFactoryDir = File(rootDir, File.separator.plus("ui_factory"))

        val temp = File(uiFactoryDir, File.separator.plus("temp"))
        val tempEntry = File(temp, File.separator.plus("entry"))
        val tempDao = File(temp, File.separator.plus("dao"))
        val tempFragment = File(temp, File.separator.plus("fragment"))
        val tempService = File(temp, File.separator.plus("service"))
        val tempUsecase = File(temp, File.separator.plus("usecase"))
        val tempViewModel = File(temp, File.separator.plus("viewmodel"))

        val generate = File(uiFactoryDir, File.separator.plus("generate")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }
        val entry = File(generate, File.separator.plus("entry")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }
        val dao = File(generate, File.separator.plus("dao")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }
        val fragment = File(generate, File.separator.plus("fragment")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }
        val service = File(generate, File.separator.plus("service")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }
        val usecase = File(generate, File.separator.plus("usecase")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }
        val viewmodel = File(generate, File.separator.plus("viewmodel")).run {
            if (!exists()) {
                mkdir()
            }
            this
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val factory = FactoryImpl("com.test.test", "Address")
            factory.provideDataClass(false, "")
            factory.provideDao()
            factory.provideAdapter()
            factory.provideFragment()
            factory.provideRetrofit()
            factory.provideUseCase()
            factory.provideViewModel()
        }
    }

    override fun provideDataClass(room: Boolean, input: String) {
        val content = File(tempEntry, File.separator.plus("User.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)
        createFile(replace, entryName, entry)
    }

    override fun provideDao() {
        val content = File(tempDao, File.separator.plus("UserDao.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)
        createFile(replace, entryName.plus("Dao"), dao)
    }

    override fun provideRetrofit() {
        val content = File(tempService, File.separator.plus("UserService.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)
        createFile(replace, entryName.plus("Service"), service)
    }

    override fun provideUseCase() {
        val content = File(tempUsecase, File.separator.plus("UserUseCase.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)
        createFile(replace, entryName.plus("UseCase"), usecase)
    }

    override fun provideViewModel() {
        val content = File(tempViewModel, File.separator.plus("UserViewModel.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)
        createFile(replace, entryName.plus("ViewModel"), viewmodel)
    }

    override fun provideFragment() {
        val content = File(tempFragment, File.separator.plus("UserDetailFragment.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)

        val content1 = File(tempFragment, File.separator.plus("UserInsertFragment.kt"))
            .readText()
        val replace1 = content1.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)

        val content2 = File(tempFragment, File.separator.plus("UserListFragment.kt"))
            .readText()
        val replace2 = content2.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)

        createFile(replace, entryName.plus("DetailFragment"), fragment)
        createFile(replace1, entryName.plus("InsertFragment"), fragment)
        createFile(replace2, entryName.plus("ListFragment"), fragment)
    }

    override fun provideAdapter() {
        val content = File(tempFragment, File.separator.plus("UserAdapter.kt"))
            .readText()
        val replace = content.replace("com.example.test_ui_factory", packageName)
            .replace("User", entryName)
        createFile(replace, entryName.plus("Adapter"), fragment)

    }

    private fun createFile(content: String, fileName: String, dir: File) {
        val file = File(dir, fileName.plus(".kt"))
        file.writeText(content)
    }
}