package com.bytebyte6.ui_factory

import java.io.File
import java.util.*
import javax.swing.filechooser.FileSystemView

class Ui(
    private val entryPackageName: String = "com.bytebyte6.entry",
    private val servicePackageName: String = "com.bytebyte6.service",
    private val viewModelPackageName: String = "com.bytebyte6.app_tv_viewmodel.viewmodel",
    private val fragmentPackageName: String = "com.bytebyte6.fragment",
    private val useCasePackageName: String = "com.bytebyte6.usecase",
    private val daoPackageName: String = "com.bytebyte6.dao"
) : Factory {

    private val entryDir: File
    private val serviceDir: File
    private val viewModelDir: File
    private val fragmentDir: File
    private val usecaseDir: File
    private val daoDir: File
    private val layoutDir: File

    init {
        FileSystemView.getFileSystemView()
            .homeDirectory
            .absolutePath.plus(File.separator).plus("generate").plus(File.separator)
            .apply {
                val dir = File(this)

                val entry = entryPackageName.substring(entryPackageName.lastIndexOf(".").plus(1))
                val service =
                    servicePackageName.substring(servicePackageName.lastIndexOf(".").plus(1))
                val viewModel =
                    viewModelPackageName.substring(viewModelPackageName.lastIndexOf(".").plus(1))
                val fragment =
                    fragmentPackageName.substring(fragmentPackageName.lastIndexOf(".").plus(1))
                val usecase =
                    useCasePackageName.substring(useCasePackageName.lastIndexOf(".").plus(1))
                val dao = daoPackageName.substring(daoPackageName.lastIndexOf(".").plus(1))

                entryDir = File(this.plus(entry).plus(File.separator))
                serviceDir = File(this.plus(service).plus(File.separator))
                viewModelDir = File(this.plus(viewModel).plus(File.separator))
                fragmentDir = File(this.plus(fragment).plus(File.separator))
                usecaseDir = File(this.plus(usecase).plus(File.separator))
                daoDir = File(this.plus(dao).plus(File.separator))
                layoutDir = File(this.plus("layout").plus(File.separator))

                if (!dir.exists()) {
                    dir.mkdir()
                }

                if (!entryDir.exists()) {
                    entryDir.mkdir()
                }

                if (!serviceDir.exists()) {
                    serviceDir.mkdir()
                }

                if (!viewModelDir.exists()) {
                    viewModelDir.mkdir()
                }

                if (!fragmentDir.exists()) {
                    fragmentDir.mkdir()
                }

                if (!usecaseDir.exists()) {
                    usecaseDir.mkdir()
                }

                if (!daoDir.exists()) {
                    daoDir.mkdir()
                }

                if (!layoutDir.exists()) {
                    layoutDir.mkdir()
                }
            }
    }

    companion object {
        val typeMap = mutableMapOf<String, String>().apply {
            this["s"] = "String"
            this["i"] = "Int"
            this["f"] = "Float"
            this["d"] = "Double"
            this["b"] = "BigDecimal"
        }
        val typeMapDefaultValue = mutableMapOf<String, String>().apply {
            this["s"] = "\"\""
            this["i"] = "0"
            this["f"] = "0F"
            this["d"] = "0.0"
            this["b"] = "BigDecimal(0)"
            this["l"] = "emptyList()"
        }
        const val ENTER = "\n"
        const val SPACE = " "
        const val MAO = ":"
        const val DOU = ","
        const val SPACE_4 = "    "

        @JvmStatic
        fun main(args: Array<String>) {
            val string =
                "User\nname:s\nage:i\naddress:e->Address country:s,city:s\nbooks:l->Book name:s,size:i"
            Ui().apply {
                provideDataClass(true, string)
            }
        }
    }

    override fun provideDataClass(room: Boolean, input: String) {
        val split = input.split(ENTER)
        val builder = StringBuilder()

        if (room) {
            builder
                .append("package $entryPackageName").append(ENTER).append(ENTER)
                .append("import androidx.room.PrimaryKey").append(ENTER)
                .append("import androidx.room.ColumnInfo").append(ENTER)
                .append("import androidx.room.Entity").append(ENTER)
        }

        builder
            .append("import kotlinx.android.parcel.Parcelize").append(ENTER)
            .append("import com.google.gson.annotations.SerializedName").append(ENTER)
            .append("import android.os.Parcelable").append(ENTER)
            .append("@Parcelize").append(ENTER)

        if (room) {
            builder.append("@Entity").append(ENTER)
        }
        val size = split.size
        val size1 = size - 1
        if (split.size == 1) {
            provideDao(split[0])
            provideRetrofit(split[0])
            provideUseCase(split[0])
            provideViewModel(split[0])
            provideFragment(split[0])
            provideAdapter(split[0])
            builder.append("data class ${split[0]}(").append(ENTER)
            builder.append(ENTER).append(") : Parcelable").append(ENTER)
            this.println(builder.toString(), split[0].plus(".kt"), entryDir)
            return
        }
        split.forEachIndexed { index, str ->
            if (index == 0) {
                provideDao(str)
                provideRetrofit(str)
                provideUseCase(str)
                provideViewModel(str)
                provideFragment(str)
                provideAdapter(str)
                builder.append("data class $str(").append(ENTER)
            } else {
                val s = str.split(MAO, limit = 2)
                val name = s[0]
                val type = s[1]
                if (index == 1) {
                    if (room) {
                        builder.append(SPACE_4).append("@PrimaryKey").append(ENTER)
                    }
                }
                builder.append(SPACE_4).append("@SerializedName(\"$name\")").append(ENTER)
                if (room) {
                    builder
                        .append(SPACE_4)
                        .append("@ColumnInfo(name=\"$name\")")
                        .append(ENTER)
                }
                when (type[0]) {
                    'e' -> {
                        val realType = type.substring(3, type.indexOf(SPACE))
                        builder
                            .append(SPACE_4)
                            .append("var $name:$realType = ${realType.plus("()")}")
                        provideDataClass(
                            room,
                            type.substring(3)
                                .replace(SPACE, ENTER)
                                .replace(DOU, ENTER)
                        )
                    }
                    'l' -> {
                        val realType = type.substring(3, type.indexOf(SPACE))
                        builder
                            .append(SPACE_4)
                            .append("var $name:List<$realType> = ${typeMapDefaultValue["l"]}")
                        provideDataClass(
                            room,
                            type.substring(3)
                                .replace(SPACE, ENTER)
                                .replace(DOU, ENTER)
                        )
                    }
                    else -> {
                        builder
                            .append(SPACE_4)
                            .append("var $name:${typeMap[type]} = ${typeMapDefaultValue[type]}")
                    }
                }
                if (index != size1) {
                    builder.append(DOU).append(ENTER)
                } else {
                    builder.append(ENTER).append(") : Parcelable").append(ENTER)
                }
            }
        }
        this.println(builder.toString(), split[0].plus(".kt"), entryDir)
    }

    override fun provideDao(entryName: String) {
        val dao = "package $daoPackageName\n" +
                "\n" +
                "import androidx.lifecycle.LiveData\n" +
                "import androidx.room.*\n" +
                "import $entryPackageName.$entryName\n" +
                "\n" +
                "interface ${entryName}Dao {\n" +
                "    @Query(\"SELECT * FROM ${entryName}\")\n" +
                "    fun allAsLiveData(): LiveData<List<${entryName}>>\n" +
                "\n" +
                "    @Query(\"SELECT * FROM $entryName /*WHERE id=:id*/\")\n" +
                "    fun asLiveDataById(/*id:Long*/): LiveData<${entryName}>\n" +
                "\n" +
                "    @Query(\"SELECT * FROM $entryName LIMIT :pageSize OFFSET :offset\")\n" +
                "    fun queryByPaging(offset: Int,pageSize:Int= 20): List<${entryName}>\n" +
                "\n" +
                "    @Query(\"SELECT * FROM $entryName /*WHERE id=:id*/\")\n" +
                "    fun queryById(/*id:Long*/): ${entryName}\n" +
                "\n" +
                "    @Query(\"SELECT * FROM ${entryName}\")\n" +
                "    fun queryAll(): List<${entryName}>\n" +
                "\n" +
                "    @Query(\"SELECT COUNT(*) FROM ${entryName}\")\n" +
                "    fun queryCount(): Int\n" +
                "\n" +
                "    @Insert(onConflict = OnConflictStrategy.IGNORE)\n" +
                "    fun insert(data: ${entryName}): Long\n" +
                "\n" +
                "    @Insert(onConflict = OnConflictStrategy.IGNORE)\n" +
                "    fun insert(list: List<${entryName}>): List<Long>\n" +
                "\n" +
                "    @Delete\n" +
                "    fun delete(data: ${entryName})\n" +
                "\n" +
                "    @Delete\n" +
                "    fun delete(list: List<${entryName}>)\n" +
                "\n" +
                "    @Update\n" +
                "    fun update(data: ${entryName})\n" +
                "\n" +
                "    @Update\n" +
                "    fun update(list: List<${entryName}>)\n" +
                "}"
        this.println(dao, "${entryName}Dao.kt", daoDir)
    }

    override fun provideRetrofit(entryName: String) {
        val service = "package $servicePackageName\n" +
                "\n" +
                "import $entryPackageName.$entryName\n" +
                "import retrofit2.http.DELETE\n" +
                "import retrofit2.http.GET\n" +
                "import retrofit2.http.POST\n" +
                "interface ${entryName}Service {\n" +
                "    @GET\n" +
                "    fun query()\n" +
                "\n" +
                "    @POST\n" +
                "    fun update()\n" +
                "\n" +
                "    @DELETE\n" +
                "    fun delete()\n" +
                "\n" +
                "    @POST\n" +
                "    fun insert()\n" +
                "}"
        this.println(service, "${entryName}Service.kt", serviceDir)
    }

    override fun provideUseCase(entryName: String) {
        val useCase = "package $useCasePackageName\n" +
                "\n" +
                "import com.bytebyte6.base.RxUseCase\n" +
                "import $daoPackageName.${entryName}Dao\n" +
                "import $entryPackageName.$entryName\n" +
                "\n" +
                "class Insert${entryName}UseCase(\n" +
                "    private val dao: ${entryName}Dao\n" +
                ") : RxUseCase<${entryName}, ${entryName}>() {\n" +
                "    override fun run(param: ${entryName}): ${entryName} {\n" +
                "        dao.insert(param)\n" +
                "        return param\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class Delete${entryName}UseCase(\n" +
                "    private val dao: ${entryName}Dao\n" +
                ") : RxUseCase<${entryName}, ${entryName}>() {\n" +
                "    override fun run(param: ${entryName}): $entryName {\n" +
                "        dao.delete(param)\n" +
                "        return param\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class Update${entryName}UseCase(\n" +
                "    private val dao: ${entryName}Dao\n" +
                ") : RxUseCase<${entryName}, ${entryName}>() {\n" +
                "    override fun run(param: ${entryName}): $entryName {\n" +
                "        dao.update(param)\n" +
                "        return param\n" +
                "    }\n" +
                "}"
        this.println(useCase, "${entryName}UseCase.kt", usecaseDir)
    }

    override fun provideViewModel(entryName: String) {
        val viewModel = "package $viewModelPackageName\n" +
                "\n" +
                "import com.bytebyte6.base.BaseViewModel\n" +
                "import com.bytebyte6.base.onIo\n" +
                "import $daoPackageName.${entryName}Dao\n" +
                "import $entryPackageName.${entryName}\n" +
                "import $useCasePackageName.Insert${entryName}UseCase\n" +
                "import $useCasePackageName.Delete${entryName}UseCase\n" +
                "import $useCasePackageName.Update${entryName}UseCase\n" +
                "\n" +
                "class ${entryName}ViewModel(\n" +
                "    private val dao: ${entryName}Dao,\n" +
                "    private val insert${entryName}UseCase: Insert${entryName}UseCase,\n" +
                "    private val delete${entryName}UseCase: Delete${entryName}UseCase,\n" +
                "    private val update${entryName}UseCase: Update${entryName}UseCase\n" +
                ") : BaseViewModel() {\n" +
                "\n" +
                "    val all = dao.allAsLiveData()\n" +
                "\n" +
                "    fun ${entryName.toLowerCase(Locale.ROOT)}(/*id:Long*/) = dao.asLiveDataById(/*id*/)\n" +
                "\n" +
                "    val insertResult = insert${entryName}UseCase.result()\n" +
                "\n" +
                "    val deleteResult = delete${entryName}UseCase.result()\n" +
                "\n" +
                "    val updateResult = update${entryName}UseCase.result()\n" +
                "    \n" +
                "    fun insert(){\n" +
                "        addDisposable(\n" +
                "            insert${entryName}UseCase.execute(${entryName}()).onIo()\n" +
                "        )\n" +
                "    }\n" +
                "\n" +
                "    fun delete(){\n" +
                "        addDisposable(\n" +
                "            delete${entryName}UseCase.execute(${entryName}()).onIo()\n" +
                "        )\n" +
                "    }\n" +
                "\n" +
                "    fun update(){\n" +
                "        addDisposable(\n" +
                "            update${entryName}UseCase.execute(${entryName}()).onIo()\n" +
                "        )\n" +
                "    }\n" +
                "}"
        this.println(viewModel, "${entryName}ViewModel.kt", viewModelDir)
    }

    override fun provideFragment(entryName: String) {
        val list = "package $fragmentPackageName\n" +
                "\n" +
                "import android.os.Bundle\n" +
                "import android.view.View\n" +
                "import com.bytebyte6.library.ListFragment\n" +
                "import org.koin.android.viewmodel.ext.android.viewModel\n" +
                "\n" +
                "class ${entryName}ListFragment : ListFragment(){\n" +
                "\n" +
                "    companion object {\n" +
                "        const val TAG = \"${entryName}ListFragment\"\n" +
                "        fun newInstance(): ${entryName}ListFragment {\n" +
                "            return ${entryName}ListFragment().apply {\n" +
                "                arguments = Bundle()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private val viewModel by viewModel<${entryName}ViewModel>()\n" +
                "\n" +
                "    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {\n" +
                "        binding?.run {\n" +
                "\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    override fun onLoadMore() {\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    override fun onRefresh() {\n" +
                "\n" +
                "    }\n" +
                "}"
        this.println(list, "${entryName}ListFragment.kt", fragmentDir)

        val detailXmlName = "R.layout.fragment_${entryName.toLowerCase(Locale.ROOT)}_detail"
        val detail = "package $fragmentPackageName\n" +
                "\n" +
                "import android.os.Bundle\n" +
                "import android.view.View\n" +
                "import com.bytebyte6.base.BaseFragment\n" +
                "import org.koin.android.viewmodel.ext.android.viewModel\n" +
                "\n" +
                "class ${entryName}DetailFragment : BaseFragment<Fragment${entryName}DetailBinding>($detailXmlName){\n" +
                "\n" +
                "    companion object {\n" +
                "        const val TAG = \"${entryName}ListFragment\"\n" +
                "        fun newInstance(): ${entryName}DetailFragment {\n" +
                "            return ${entryName}DetailFragment().apply {\n" +
                "                arguments = Bundle()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private val viewModel by viewModel<${entryName}ViewModel>()\n" +
                "\n" +
                "    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {\n" +
                "        binding?.run {\n" +
                "\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    override fun initViewBinding(view: View): Fragment${entryName}DetailBinding? {\n" +
                "        return Fragment${entryName}DetailBinding.bind(view)\n" +
                "    }\n" +
                "}"

        val detailXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<FrameLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:id=\"@+id/frameLayout\">\n" +
                "\n" +
                "</FrameLayout>"
        this.println(
            detailXml,
            "fragment_${entryName.toLowerCase(Locale.ROOT)}_detail.xml",
            layoutDir
        )
        this.println(detail, "${entryName}DetailFragment.kt", fragmentDir)

        val insertXmlName = "R.layout.fragment_${entryName.toLowerCase(Locale.ROOT)}_insert"
        val insert = "package $fragmentPackageName\n" +
                "\n" +
                "import android.os.Bundle\n" +
                "import android.view.View\n" +
                "import com.bytebyte6.base.BaseFragment\n" +
                "import org.koin.android.viewmodel.ext.android.viewModel\n" +
                "\n" +
                "class ${entryName}InsertFragment : BaseFragment<Fragment${entryName}DetailBinding>($insertXmlName){\n" +
                "\n" +
                "    companion object {\n" +
                "        const val TAG = \"${entryName}ListFragment\"\n" +
                "        fun newInstance(): ${entryName}InsertFragment {\n" +
                "            return ${entryName}InsertFragment().apply {\n" +
                "                arguments = Bundle()\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private val viewModel by viewModel<${entryName}ViewModel>()\n" +
                "\n" +
                "    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {\n" +
                "        binding?.run {\n" +
                "\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    override fun initViewBinding(view: View): Fragment${entryName}DetailBinding? {\n" +
                "        return Fragment${entryName}DetailBinding.bind(view)\n" +
                "    }\n" +
                "}"
        val insertXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<FrameLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:id=\"@+id/frameLayout\">\n" +
                "\n" +
                "</FrameLayout>"
        this.println(insert, "${entryName}InsertFragment.kt", fragmentDir)
        this.println(
            insertXml,
            "fragment_${entryName.toLowerCase(Locale.ROOT)}_insert.xml",
            layoutDir
        )
    }

    override fun provideAdapter(entryName: String) {
        val adapter = "package $fragmentPackageName\n" +
                "\n" +
                "import android.view.LayoutInflater\n" +
                "import android.view.ViewGroup\n" +
                "import androidx.recyclerview.widget.DiffUtil\n" +
                "import androidx.recyclerview.widget.RecyclerView\n" +
                "import com.bytebyte6.library.BaseListAdapter\n" +
                "\n" +
                "class ${entryName}Adapter : BaseListAdapter<${entryName},${entryName}ViewHolder>(${entryName}Diff){\n" +
                "    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ${entryName}ViewHolder {\n" +
                "        return ${entryName}ViewHolder.create(parent)\n" +
                "    }\n" +
                "\n" +
                "    override fun onBindViewHolder(holder: ${entryName}ViewHolder, position: Int) {\n" +
                "        super.onBindViewHolder(holder, position)\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class ${entryName}ViewHolder(val binding:Item${entryName}Binding) : RecyclerView.ViewHolder(binding.root){\n" +
                "    companion object{\n" +
                "        fun create(parent: ViewGroup): ${entryName}ViewHolder {\n" +
                "            return ${entryName}ViewHolder(\n" +
                "                Item${entryName}Binding.inflate(\n" +
                "                    LayoutInflater.from(parent.context),\n" +
                "                    parent,\n" +
                "                    false\n" +
                "                )\n" +
                "            )\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "object ${entryName}Diff : DiffUtil.ItemCallback<${entryName}>{\n" +
                "    override fun areItemsTheSame(oldItem: ${entryName}, newItem: ${entryName}): Boolean {\n" +
                "        \n" +
                "    }\n" +
                "\n" +
                "    override fun areContentsTheSame(oldItem: ${entryName}, newItem: ${entryName}): Boolean {\n" +
                "\n" +
                "    }\n" +
                "}"

        val xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<FrameLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"match_parent\"\n" +
                "    android:id=\"@+id/frameLayout\">\n" +
                "\n" +
                "</FrameLayout>"
        this.println(adapter, "${entryName}Adapter.kt", fragmentDir)
        this.println(xml, "item_$entryName.xml", layoutDir)
    }

    private fun println(content: String, fileName: String, dir: File) {
        val file = File(dir, fileName)
        file.writeText(content)
    }
}