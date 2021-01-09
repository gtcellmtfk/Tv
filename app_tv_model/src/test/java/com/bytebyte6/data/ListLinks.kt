package com.bytebyte6.data

import org.jsoup.Jsoup
import org.junit.Test
import java.io.IOException

 class ListLinks {
    @Test
    @Throws(IOException::class)
    fun main() {
        val url =
            "https://cn.bing.com/images/search?q=%E8%8B%B1%E5%9B%BD%E5%9B%BD%E6%97%97&qs=n&form=QBILPG&sp=-1&pq=%E8%8B%B1%E5%9B%BD%E5%9B%BD%E6%97%97&sc=8-4&cvid=E5FCC010A76B42348791BEA81343C44C&first=1&tsc=ImageBasicHover"
        print("Fetching %s...", url)
        val doc = Jsoup.connect(url).get()
//        val links = doc.select("a[href]")
        val media = doc.select("[src]")
//        val imports = doc.select("link[href]")
        print("\nMedia: (%d)", media.size)
        for (src in media) {
            if (src.normalName() == "img") {
                print(
                    " * %s: <%s> %sx%s (%s)",
                    src.tagName(),
                    src.attr("abs:src"),
                    src.attr("width"),
                    src.attr("height"),
                    trim(src.attr("alt"), 20)
                )
            } else {
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"))
            }
        }
//        print("\nImports: (%d)", imports.size)
//        for (link in imports) {
//            print(
//                " * %s <%s> (%s)",
//                link.tagName(),
//                link.attr("abs:href"),
//                link.attr("rel")
//            )
//        }
//        print("\nLinks: (%d)", links.size)
//        for (link in links) {
//            print(
//                " * a: <%s>  (%s)",
//                link.attr("abs:href"),
//                trim(link.text(), 35)
//            )
//        }
    }

    companion object {
        private fun print(msg: String, vararg args: Any) {
            println(String.format(msg, *args))
        }

        private fun trim(s: String, width: Int): String {
            return if (s.length > width) s.substring(0, width - 1) + "." else s
        }
    }
}