package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService
import org.dbu.library.util.display

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "7" -> {
            listAllPatrons(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

fun addBook(service: LibraryService) {
    println("\n--- Add Book ---")
    print("ISBN: ")
    val isbn = readln().trim()
    print("Title: ")
    val title = readln().trim()
    print("Author: ")
    val author = readln().trim()
    print("Year: ")
    val year = readln().trim().toIntOrNull() ?: 0

    val book = Book(isbn, title, author, year)
    if (service.addBook(book)) {
        println("Book added successfully!")
    } else {
        println("Book already exists!")
    }
}

fun registerPatron(repository: LibraryRepository) {
    println("\n--- Register Patron ---")
    print("ID: ")
    val id = readln().trim()
    print("Name: ")
    val name = readln().trim()

    val patron = Patron(id, name)
    if (repository.addPatron(patron)) {
        println("Patron registered successfully!")
    } else {
        println("Patron ID already exists!")
    }
}

fun borrowBook(service: LibraryService) {
    println("\n--- Borrow Book ---")
    print("Patron ID: ")
    val patronId = readln().trim()
    print("ISBN: ")
    val isbn = readln().trim()

    val result = service.borrowBook(patronId, isbn)
    println("Result: $result")
}

fun returnBook(service: LibraryService) {
    println("\n--- Return Book ---")
    print("Patron ID: ")
    val patronId = readln().trim()
    print("ISBN: ")
    val isbn = readln().trim()

    if (service.returnBook(patronId, isbn)) {
        println("Book returned successfully!")
    } else {
        println("Return failed (check ID and ISBN)")
    }
}

fun search(service: LibraryService) {
    println("\n--- Search ---")
    print("Search query: ")
    val query = readln().trim()
    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found.")
    } else {
        results.forEach { println("${it.isbn}: ${it.display()} [${if (it.isAvailable) "Available" else "Borrowed"}]") }
    }
}

fun listAllBooks(repository: LibraryRepository) {
    println("\n--- All Books ---")
    val books = repository.getAllBooks()
    if (books.isEmpty()) {
        println("Library is empty.")
    } else {
        books.forEach { println("${it.isbn}: ${it.display()} [${if (it.isAvailable) "Available" else "Borrowed"}]") }
    }
}

fun listAllPatrons(repository: LibraryRepository) {
    println("\n--- All Patrons ---")
    val patrons = repository.getAllPatrons()
    if (patrons.isEmpty()) {
        println("No patrons registered.")
    } else {
        patrons.forEach { println("${it.id}: ${it.name} (Borrowed Books: ${it.borrowedBooks.joinToString()})") }
    }
}