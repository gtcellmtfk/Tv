package com.bytebyte6.ui_factory

interface Factory {
    fun provideDataClass(room: Boolean, input: String)
    fun provideDao(entryName: String)
    fun provideRetrofit(entryName: String)
    fun provideUseCase(entryName: String)
    fun provideViewModel(entryName: String)
    fun provideFragment(entryName: String)
    fun provideAdapter(entryName: String)
}

interface Factory2 {
    fun provideDataClass(room: Boolean, input: String)
    fun provideDao()
    fun provideRetrofit()
    fun provideUseCase()
    fun provideViewModel( )
    fun provideFragment()
    fun provideAdapter()
}
