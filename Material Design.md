# 过渡动画

例子一

FragmentA

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //为了在片段替换时保持视图，不显示一片空白
    exitTransition = Hold()
}

//FragmentA也要加这不然返回没有动画效果，只有进入的时候有
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    //动画不显示请检查transitionName是否设置正确
    toolbar.transitionName = getString(R.string.search_share)
    
    postponeEnterTransition()
    
    view.doOnPreDraw {
        startPostponedEnterTransition()
    }
}
```

FragmentB

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    sharedElementEnterTransition = MaterialContainerTransform()
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    //动画不显示请检查transitionName是否设置正确
    view.transitionName = getString(R.string.search_share)
    
    //延迟转换，调用此方法后必须调用startPostponedEnterTransition()
    postponeEnterTransition()
    
    view.doOnPreDraw {
        //此方法在视图准备好后调用，比如要等一个ImageView图片加载出来后再调用startPostponedEnterTransition()
        startPostponedEnterTransition()
    }
}
```

例子二

Recyclerview使用同样要像例子一那样设置并执行如下操作

```kotlin
class ImageAdapter(private val favClickListener: ((pos: Int) -> Unit)? = null) :
    BaseListAdapter<Image, ImageViewHolder>(ImageDIFF) {
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        //重建后的recyclerview Item是没有transName的 所以在onbind要重新赋值一遍 动画效果才会有~~
        holder.itemView.transitionName = currentList[position].title
    }
}
```

```kotlin
//点击Item时跳转调用
fun Fragment.replaceWithShareElement(fragment: Fragment, tag: String?, share: View) {
    requireActivity().supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .addSharedElement(share, share.transitionName)
        .replace(R.id.main_container, fragment, tag)
        .addToBackStack(tag)
        .commit()
}
```

## 相关链接

[BaseShareFragment](https://github.com/bytebyte6/Tv/blob/master/lib_common/src/main/java/com/bytebyte6/common/BaseShareFragment.kt)

[TvAdapter](https://github.com/bytebyte6/Tv/blob/master/tv/src/main/java/com/bytebyte6/view/adapter/TvAdapter.kt)

[HomeFragment](https://github.com/bytebyte6/Tv/blob/master/tv/src/main/java/com/bytebyte6/view/home/HomeFragment.kt)

[CountryFragment](https://github.com/bytebyte6/Tv/blob/master/tv/src/main/java/com/bytebyte6/view/home/CountryFragment.kt)

[SearchFragment](https://github.com/bytebyte6/Tv/blob/master/tv/src/main/java/com/bytebyte6/view/search/SearchFragment2.kt)

[Material Design Motion](https://material.io/develop/android/theming/motion)

# 颜色相关

```xml
<item name="colorPrimary">@color/purple_500</item>
<item name="colorPrimaryVariant">@color/purple_700</item>
<item name="colorOnPrimary">@color/white</item>
<item name="colorSecondary">@color/teal_200</item>
<item name="colorSecondaryVariant">@color/teal_700</item>
<item name="colorOnSecondary">@color/white</item>
```

colorPrimary APP主色

colorOnPrimary 主色上显示的颜色

colorSecondary APP次色

colorOnSecondary 次色上显示的颜色



# MotionLayout

<img src="https://raw.githubusercontent.com/bytebyte6/Tv/master/pic/tv%20(7).jpg" style="zoom:50%;" />

[DrawerContent](https://github.com/bytebyte6/Tv/blob/f86bc9f576788bd2a2d0887ccab4dfe84c95212a/lib_common/src/main/java/com/bytebyte6/common/DrawerContent.kt)

继承MotionLayout实现 DrawerLayout.DrawerListener

```kotlin
class DrawerContent @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), DrawerLayout.DrawerListener {

    override fun onDrawerStateChanged(newState: Int) {

    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        //进度
        progress = slideOffset
    }

    override fun onDrawerClosed(drawerView: View) {

    }

    override fun onDrawerOpened(drawerView: View) {

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as? DrawerLayout)?.addDrawerListener(this)
    }
}
```

[布局](https://github.com/bytebyte6/Tv/blob/master/tv_view/src/main/res/layout/activity_main.xml)

```xml
<!--要注意设置约束布局属性 设置不对会导致片段显示不全-->
<com.bytebyte6.base_ui.DrawerContent
    android:id="@+id/drawContent"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layoutDescription="@xml/scene_home_content">

    <!--要显示的内容-->
    <FrameLayout
        android:id="@+id/main_container"
        android:layout_width="0dp"
        android:layout_height="0dp">

    </FrameLayout>

</com.bytebyte6.base_ui.DrawerContent>
```

[scene_home_content.xml](https://github.com/bytebyte6/Tv/blob/master/tv_view/src/main/res/xml/scene_home_content.xml)

```xml
<MotionScene xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:motion="http://schemas.android.com/apk/res-auto">

    <!--动画-->
    <Transition
        motion:constraintSetEnd="@+id/end"
        motion:constraintSetStart="@+id/start"
        motion:duration="250"
        motion:motionInterpolator="linear" />
    
	<!--开始状态-->
    <ConstraintSet android:id="@+id/start">
        <Constraint
            android:id="@id/main_container"
            android:layout_width="0dp"
            android:layout_height="0dp"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintRight_toRightOf="parent"
            motion:layout_constraintLeft_toLeftOf="parent"
            motion:layout_constraintTop_toTopOf="parent"
            motion:layout_constraintWidth_default="percent"
            motion:layout_constraintWidth_percent="1" />
    </ConstraintSet>

    <!--结束状态-->
    <ConstraintSet android:id="@+id/end">
        <Constraint
            android:id="@id/main_container"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:scaleX="0.8"
            android:scaleY="0.8"
            android:translationX="180dp"
            motion:layout_constraintBottom_toBottomOf="parent"
            motion:layout_constraintRight_toRightOf="parent"
            motion:layout_constraintLeft_toLeftOf="parent"
            motion:layout_constraintTop_toTopOf="parent"
            motion:layout_constraintWidth_default="percent"
            motion:layout_constraintWidth_percent="1" />
    </ConstraintSet>
</MotionScene>
```
