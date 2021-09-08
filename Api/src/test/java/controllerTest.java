import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymanagement.RESTApplication;
import com.librarymanagement.controller.BookController;
import com.librarymanagement.entity.Book;
import com.librarymanagement.exceptions.ResourceNotFoundException;
import com.librarymanagement.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = { RESTApplication.class })
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
public class controllerTest {
    private ObjectMapper objectMapper;
    private MockMvc mvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }
    String bookJsonString = "[{\"id\":\"90876567890124\",\"name\":\"sample name \",\"description\":\"Sample description \",\"category\":\"category xyz\",\"publisher\":\"publisher xyz \",\"price\":2000,\"author\":\"author xyz\"},{\"id\":\"1234567890124\",\"name\":\"sample name updated\",\"description\":\"Sample description updated\",\"category\":\"category xyz updated\",\"publisher\":\"publisher xyz updated\",\"price\":2200,\"author\":\"author xyz updated\"}]";
    String singleBook = "{\"id\":\"123456789\",\"name\":\"sample name updated\",\"description\":\"Sample description updated\",\"category\":\"category xyz updated\",\"publisher\":\"publisher xyz updated\",\"price\":2200,\"author\":\"author xyz updated\"}";
    String postReq = "{\"id\":\"123456789\",\"name\":\"sample name updated\",\"description\":\"Sample description updated\",\"category\":\"category xyz updated\",\"publisher\":\"publisher xyz updated\",\"price\":2200,\"author\":\"author xyz updated\"}";
    ResourceNotFoundException exception1= new ResourceNotFoundException("Book Not found with id 9009");
    ResourceNotFoundException exception = new ResourceNotFoundException("Book Not Found");

    @Test
    public void validGetRequestReturnsBooks() throws Exception {

        URI url = UriComponentsBuilder.fromPath("/books/").build().toUri();
        assertEquals("/books/", url.toString());

        Book[] books = objectMapper.readValue(bookJsonString, Book[].class);
        List<Book> bookList = Arrays.asList(books);
        Mockito.when(bookService.getAllBooks()).thenReturn(bookList);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void getById() throws Exception {

        URI url = UriComponentsBuilder.fromPath("/books/123456789").build().toUri();
        assertEquals("/books/123456789", url.toString());

        Book book = objectMapper.readValue(singleBook, Book.class);
        Mockito.when(bookService.getBookById(123456789)).thenReturn(book);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void getByInvalidIdGives404() throws Exception {

        URI url = UriComponentsBuilder.fromPath("/books/9009").build().toUri();
        assertEquals("/books/9009", url.toString());
        Mockito.when(bookService.getBookById(9009)).thenThrow(exception);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void validPostRequestGivesBook() throws Exception {

        URI url = UriComponentsBuilder.fromPath("/books/").build().toUri();
        assertEquals("/books/", url.toString());
        Book Book= null;
        Mockito.when(bookService.addBook(Mockito.mock(Book.class))).thenReturn(Book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url.toString())
                .contentType(MediaType.APPLICATION_JSON).content(postReq);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }



    @Test
    public void validPutRequestGivesBook() throws Exception {

        URI url = UriComponentsBuilder.fromPath("/books/123456789").build().toUri();
        assertEquals("/books/123456789", url.toString());
        Book Book= null;
        Mockito.when(bookService.updateBook(Mockito.mock(Book.class))).thenReturn(Book);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(url.toString())
                .contentType(MediaType.APPLICATION_JSON).content(postReq);
        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void invalidDeleteRequest() throws Exception {

        URI url = UriComponentsBuilder.fromPath("/books/123456789").build().toUri();
        assertEquals("/books/123456789", url.toString());
        Mockito.when(bookService.getBookById(123456789)).thenThrow(exception);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void validGetByName() throws Exception {
        URI url = UriComponentsBuilder.fromPath("/books/name").queryParam("name","sample name updated").build().toUri();
        assertEquals("/books/name?name=sample%20name%20updated", url.toString());

        List<Book> book = new ArrayList<>();
        book.add(objectMapper.readValue(singleBook, Book.class));

        Mockito.when(bookService.getBooksByName("sample name updated")).thenReturn(book);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void validGetByAuthor() throws Exception {
        URI url = UriComponentsBuilder.fromPath("/books/author").queryParam("author","author xyz updated").build().toUri();
        assertEquals("/books/author?author=author%20xyz%20updated", url.toString());

        List<Book> book = new ArrayList<>();
        book.add(objectMapper.readValue(singleBook, Book.class));

        Mockito.when(bookService.getBooksByAuthor("author xyz updated")).thenReturn(book);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void validGetByPublisher() throws Exception {
        URI url = UriComponentsBuilder.fromPath("/books/publisher").queryParam("publisher","publisher xyz updated").build().toUri();
        assertEquals("/books/publisher?publisher=publisher%20xyz%20updated", url.toString());

        List<Book> book = new ArrayList<>();
        book.add(objectMapper.readValue(singleBook, Book.class));

        Mockito.when(bookService.getBooksByPublisher("publisher xyz updated")).thenReturn(book);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void validGetByCategory() throws Exception {
        URI url = UriComponentsBuilder.fromPath("/books/category").queryParam("category","category xyz updated").build().toUri();
        assertEquals("/books/category?category=category%20xyz%20updated", url.toString());

        List<Book> book = new ArrayList<>();
        book.add(objectMapper.readValue(singleBook, Book.class));

        Mockito.when(bookService.getBooksByCategory("category xyz updated")).thenReturn(book);
        MockHttpServletResponse response = mvc.perform(get(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void validAddCopies() throws Exception{
        URI url = UriComponentsBuilder.fromPath("/books/addcopies/123456789").build().toUri();
        assertEquals("/books/addcopies/123456789", url.toString());

        Book book = objectMapper.readValue(singleBook, Book.class);

        doNothing().when(bookService).addCopies(123456789);
        MockHttpServletResponse response = mvc.perform(patch(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void validRemoveCopies() throws Exception{
        URI url = UriComponentsBuilder.fromPath("/books/removecopies/123456789").build().toUri();
        assertEquals("/books/removecopies/123456789", url.toString());

        Book book = objectMapper.readValue(singleBook, Book.class);

        doNothing().when(bookService).addCopies(123456789);
        MockHttpServletResponse response = mvc.perform(patch(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @Test
    public void validDeleteRequest() throws Exception{
        URI url = UriComponentsBuilder.fromPath("/books/1").build().toUri();
        assertEquals("/books/1", url.toString());

        doNothing().when(bookService).deleteBook(1);
        MockHttpServletResponse response = mvc.perform(delete(url.toString())).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

}
