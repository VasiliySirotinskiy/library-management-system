import com.example.librarymanagementsystem.Book
import com.example.librarymanagementsystem.Disc
import com.example.librarymanagementsystem.DiscType
import com.example.librarymanagementsystem.LibraryItem
import com.example.librarymanagementsystem.Newspaper

// Интерфейс магазина и его реализации, а также сущность менеджера

interface Store<T : LibraryItem> {
    fun sell(): T
}

class BookStore : Store<Book> {
    override fun sell(): Book =
        Book(5001, true, "Новая книга", 350, "Неизвестный автор")
}

class DiscStore : Store<Disc> {
    override fun sell(): Disc =
        Disc(6001, true, "Новый диск", DiscType.DVD)
}

class NewspaperKiosk : Store<Newspaper> {
    override fun sell(): Newspaper =
        Newspaper(7001, true, "Новая газета", 101, 5)
}

class Manager {
    fun <T : LibraryItem> buy(store: Store<T>): T = store.sell()
}
