package com.example.librarymanagementsystem

// Кабинет оцифровки

class DigitizationCabinet {
    fun digitize(item: LibraryItem): Disc {
        if (item is Disc) {
            throw IllegalArgumentException("Оцифровка данного типа объекта не поддерживается!")
        }
        return Disc(8001, true, "Оцифрованный: ${item.getName()}", DiscType.CD)
    }
}
