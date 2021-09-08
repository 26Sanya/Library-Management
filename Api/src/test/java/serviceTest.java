import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymanagement.RESTApplication;
import com.librarymanagement.entity.Book;
import com.librarymanagement.exceptions.ResourceNotFoundException;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.service.BookServiceImplement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = { RESTApplication.class })
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
public class serviceTest {
    private ObjectMapper objectMapper;
    private MockMvc mvc;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImplement bookService;

    String db= "[{\"id\":1,\"name\":\"sample name1\",\"description\":\"Sample description1\",\"category\":\"sample category1\",\"publisher\":\"sample publisher1\",\"price\":2200,\"author\":\"sample author1\",\"createdAt\":\"2021-08-30T18:30:00.000+00:00\",\"updatedAt\":\"2021-08-30T18:30:00.000+00:00\"},{\"id\":2,\"name\":\"sample name2\",\"description\":\"Sample description2\",\"category\":\"sample category2\",\"publisher\":\"sample publisher2\",\"price\":220,\"author\":\"sample author2\",\"createdAt\":\"2021-08-30T18:30:00.000+00:00\",\"updatedAt\":\"2021-08-30T18:30:00.000+00:00\"},{\"id\":3,\"name\":\"sample name3\",\"description\":\"Sample description3\",\"category\":\"sample category2\",\"publisher\":\"sample publisher3\",\"price\":200,\"author\":\"sample author1\",\"createdAt\":\"2021-08-30T18:30:00.000+00:00\",\"updatedAt\":\"2021-08-30T18:30:00.000+00:00\"},{\"id\":4,\"name\":\"sample name4\",\"description\":\"Sample description4\",\"category\":\"sample category1\",\"publisher\":\"sample publisher1\",\"price\":2000,\"author\":\"sample author2\",\"createdAt\":\"2021-08-30T18:30:00.000+00:00\",\"updatedAt\":\"2021-08-30T18:30:00.000+00:00\"},{\"id\":6,\"name\":\"sample name updated\",\"description\":\"Sample description updated\",\"category\":\"caegory xyz updated\",\"publisher\":\"publisher xyz updated\",\"price\":2000,\"author\":\"author xyz updated\",\"createdAt\":\"2021-08-30T18:30:00.000+00:00\",\"updatedAt\":\"2021-08-30T18:30:00.000+00:00\"}]";
    ResourceNotFoundException exception= new ResourceNotFoundException("Book Not found with given id");

    List<Book> bookList = new ArrayList<>();

    @BeforeEach
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        Book[] books = objectMapper.readValue(db, Book[].class);
        bookList = Arrays.asList(books);
    }

    @Test
    public void validGetAllRequestReturnsBooks() throws Exception {
        Mockito.when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> getAllBooks = bookService.getAllBooks();
        assertEquals(5, getAllBooks.size() );
        assertEquals(1L, getAllBooks.get(0).getId());
        assertEquals(bookList, getAllBooks);
    }

    @Test
    public void validGetByIDRequestReturnsBook() throws Exception {
        Book mybook = bookList.get(0);
        Mockito.when(bookRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(mybook));
        Book getBookById = bookService.getBookById(1L);
        assertEquals(1L, getBookById.getId());
    }

    @Test
    public void InvalidGetByIDRequestThrowException() throws Exception {

        Exception xception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(6L);
        });
        String expectedMessage = "Record Not Found with id: 6";
        String actualMessage = xception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void ValidAddBookRequestReturnsBook() throws  Exception{
        Book book= bookList.get(0);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Book addBook = bookService.addBook(book);
        assertEquals(book, addBook);
    }

    @Test
    public void validGetByNameRequestReturnsBook() throws Exception {
        List<Book> mbooks =new ArrayList<>();
        mbooks.add(bookList.get(0));
        Mockito.when(bookRepository.findByName("sample name1")).thenReturn(mbooks);
        List<Book> getBookByName = bookService.getBooksByName("sample name1");
        assertEquals(mbooks, getBookByName);
    }

    @Test
    public void invalidGetByNameRequestThrows404() throws Exception {
        Exception xception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBooksByName("NON Existing Book");
        });
        String expectedMessage = "No Record Found with name: NON Existing Book";
        String actualMessage = xception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void validGetByPublisherRequestReturnsBook() throws Exception {
        List<Book> mbooks =new ArrayList<>();
        mbooks.add(bookList.get(0));
        mbooks.add(bookList.get(3));
        Mockito.when(bookRepository.findByPublisher("sample publisher2")).thenReturn(mbooks);
        List<Book> getBookByName = bookService.getBooksByPublisher("sample publisher2");
        assertEquals(mbooks, getBookByName);
    }
    @Test
    public void invalidGetByPublisherRequestThrows404() throws Exception {
        Exception xception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBooksByPublisher("NON Existing Publisher");
        });
        String expectedMessage = "No Record Found with publisher: NON Existing Publisher";
        String actualMessage = xception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
